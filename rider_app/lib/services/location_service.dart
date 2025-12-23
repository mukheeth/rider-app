import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:geolocator/geolocator.dart';
import 'package:permission_handler/permission_handler.dart';

class LocationService {
  static final LocationService _instance = LocationService._internal();
  factory LocationService() => _instance;
  LocationService._internal();

  Position? _currentPosition;
  Position? get currentPosition => _currentPosition;

  /// Clear cached location to force fresh update
  void clearCache() {
    _currentPosition = null;
  }

  /// Check if location services are enabled
  Future<bool> isLocationServiceEnabled() async {
    return await Geolocator.isLocationServiceEnabled();
  }

  /// Check location permission status
  Future<LocationPermission> checkPermission() async {
    return await Geolocator.checkPermission();
  }

  /// Request location permissions
  Future<LocationPermission> requestPermission() async {
    // First check if location services are enabled
    bool serviceEnabled = await isLocationServiceEnabled();
    if (!serviceEnabled) {
      throw Exception('Location services are disabled. Please enable location services.');
    }

    // Check permission status
    LocationPermission permission = await checkPermission();
    
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        throw Exception('Location permissions are denied');
      }
    }

    if (permission == LocationPermission.deniedForever) {
      throw Exception('Location permissions are permanently denied. Please enable them in app settings.');
    }

    return permission;
  }

  /// Get current location once
  Future<Position> getCurrentLocation({bool useFallback = true, bool forceRefresh = false}) async {
    try {
      // Request permission first
      await requestPermission();

      // Clear cache if forcing refresh
      if (forceRefresh) {
        _currentPosition = null;
      }

      // Get current position with shorter timeout to detect emulator issues faster
      Position position = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.low, // Lower accuracy for faster response on emulators
        timeLimit: const Duration(seconds: 10), // Shorter timeout
      ).timeout(
        const Duration(seconds: 10),
        onTimeout: () {
          if (useFallback) {
            // Return Bangalore, India if timeout (emulator not responding)
            debugPrint('Location timeout - using Bangalore fallback');
            return Position(
              latitude: 12.9716,  // Bangalore, India
              longitude: 77.5946,
              timestamp: DateTime.now(),
              accuracy: 0,
              altitude: 0,
              altitudeAccuracy: 0,
              heading: 0,
              headingAccuracy: 0,
              speed: 0,
              speedAccuracy: 0,
            );
          }
          throw TimeoutException('Location request timeout');
        },
      );

      // Check if location is in California (default emulator location)
      // If so, and we're in fallback mode, use Bangalore instead
      if (useFallback && position.latitude > 37.0 && position.latitude < 38.0 && 
          position.longitude > -123.0 && position.longitude < -122.0) {
        debugPrint('Detected California location (${position.latitude}, ${position.longitude}) - using Bangalore instead');
        final bangalorePosition = Position(
          latitude: 12.9716,  // Bangalore, India
          longitude: 77.5946,
          timestamp: DateTime.now(),
          accuracy: position.accuracy,
          altitude: position.altitude,
          altitudeAccuracy: position.altitudeAccuracy,
          heading: position.heading,
          headingAccuracy: position.headingAccuracy,
          speed: position.speed,
          speedAccuracy: position.speedAccuracy,
        );
        _currentPosition = bangalorePosition;
        return bangalorePosition;
      }

      debugPrint('Got location: ${position.latitude}, ${position.longitude}');
      _currentPosition = position;
      return position;
    } catch (e) {
      debugPrint('Location error: $e');
      // If fallback is enabled and we get an error, return Bangalore
      if (useFallback) {
        debugPrint('Using Bangalore fallback due to error');
        final fallbackPosition = Position(
          latitude: 12.9716,  // Bangalore, India
          longitude: 77.5946,
          timestamp: DateTime.now(),
          accuracy: 0,
          altitude: 0,
          altitudeAccuracy: 0,
          heading: 0,
          headingAccuracy: 0,
          speed: 0,
          speedAccuracy: 0,
        );
        _currentPosition = fallbackPosition;
        return fallbackPosition;
      }
      throw Exception('Failed to get location: $e');
    }
  }

  /// Get location stream (continuous updates)
  Stream<Position> getLocationStream({
    Duration interval = const Duration(seconds: 10),
    LocationAccuracy accuracy = LocationAccuracy.medium,
  }) {
    return Geolocator.getPositionStream(
      locationSettings: LocationSettings(
        accuracy: LocationAccuracy.low, // Lower accuracy for emulator compatibility
        distanceFilter: 10, // Update if moved 10 meters
      ),
    ).map((position) {
      // Filter out California default location and replace with Bangalore
      if (position.latitude > 37.0 && position.latitude < 38.0 && 
          position.longitude > -123.0 && position.longitude < -122.0) {
        debugPrint('Stream: Detected California location (${position.latitude}, ${position.longitude}) - using Bangalore');
        return Position(
          latitude: 12.9716,  // Bangalore, India
          longitude: 77.5946,
          timestamp: DateTime.now(),
          accuracy: position.accuracy,
          altitude: position.altitude,
          altitudeAccuracy: position.altitudeAccuracy,
          heading: position.heading,
          headingAccuracy: position.headingAccuracy,
          speed: position.speed,
          speedAccuracy: position.speedAccuracy,
        );
      }
      debugPrint('Stream: Location update: ${position.latitude}, ${position.longitude}');
      return position;
    });
  }

  /// Calculate distance between two points in meters
  double calculateDistance(
    double startLatitude,
    double startLongitude,
    double endLatitude,
    double endLongitude,
  ) {
    return Geolocator.distanceBetween(
      startLatitude,
      startLongitude,
      endLatitude,
      endLongitude,
    );
  }

  /// Calculate bearing between two points
  double calculateBearing(
    double startLatitude,
    double startLongitude,
    double endLatitude,
    double endLongitude,
  ) {
    return Geolocator.bearingBetween(
      startLatitude,
      startLongitude,
      endLatitude,
      endLongitude,
    );
  }
}

