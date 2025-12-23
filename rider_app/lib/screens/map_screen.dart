import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:latlong2/latlong.dart';
import 'package:geolocator/geolocator.dart';
import '../services/location_service.dart';
import '../models/location_model.dart';

class MapScreen extends StatefulWidget {
  final LatLng? initialCenter;
  final List<Marker>? customMarkers;
  final bool showCurrentLocation;
  final Function(LatLng)? onMapTap;
  final Function(LatLng)? onLocationChanged;

  const MapScreen({
    super.key,
    this.initialCenter,
    this.customMarkers,
    this.showCurrentLocation = true,
    this.onMapTap,
    this.onLocationChanged,
  });

  @override
  State<MapScreen> createState() => _MapScreenState();
}

class _MapScreenState extends State<MapScreen> {
  final MapController _mapController = MapController();
  final LocationService _locationService = LocationService();
  StreamSubscription<Position>? _locationSubscription;
  LatLng? _currentLocation;
  bool _isLoadingLocation = true;
  String? _locationError;
  bool _hasTileErrors = false;
  int _tileErrorCount = 0;

  @override
  void initState() {
    super.initState();
    if (widget.showCurrentLocation) {
      _initializeLocation();
    } else if (widget.initialCenter != null) {
      _currentLocation = widget.initialCenter;
      _isLoadingLocation = false;
    } else {
      _isLoadingLocation = false;
    }
  }

  Future<void> _initializeLocation() async {
    try {
      // Check if location services are enabled
      final isEnabled = await _locationService.isLocationServiceEnabled();
      if (!isEnabled) {
        setState(() {
          _isLoadingLocation = false;
          _locationError = 'Location services are disabled. Please enable GPS.';
        });
        return;
      }

      // Request permission and get initial location (with fallback for emulators)
      final position = await _locationService.getCurrentLocation(useFallback: true);
      
      if (!mounted) return;
      
      setState(() {
        _currentLocation = LatLng(position.latitude, position.longitude);
        _isLoadingLocation = false;
        _locationError = null;
      });

      // Move map to current location
      _mapController.move(_currentLocation!, 15.0);

      // Start listening to location updates (no timeout - continuous stream)
      _locationSubscription = _locationService.getLocationStream(
        accuracy: LocationAccuracy.medium, // Better for emulators
      ).listen(
        (position) {
          if (!mounted) return;
          final newLocation = LatLng(position.latitude, position.longitude);
          setState(() {
            _currentLocation = newLocation;
            _locationError = null; // Clear any previous errors
          });

          // Notify parent widget
          widget.onLocationChanged?.call(newLocation);

          // Optionally move map to follow location
          // _mapController.move(newLocation, _mapController.zoom);
        },
        onError: (error) {
          // Only show error if it's not a timeout (timeouts are normal)
          if (mounted && !error.toString().contains('TimeoutException')) {
            setState(() {
              _locationError = 'Location update: ${error.toString()}';
            });
          }
        },
        cancelOnError: false, // Keep stream alive even on errors
      );
    } catch (e) {
      if (mounted) {
        setState(() {
          _isLoadingLocation = false;
          _locationError = e.toString();
        });
      }
    }
  }

  void _moveToCurrentLocation() async {
    try {
      // Force fresh location (clear cache and get new location)
      _locationService.clearCache();
      final position = await _locationService.getCurrentLocation(useFallback: false, forceRefresh: true);
      final location = LatLng(position.latitude, position.longitude);
      _mapController.move(location, 15.0);
      if (mounted) {
        setState(() {
          _currentLocation = location;
          _locationError = null;
        });
      }
    } catch (e) {
      // If real location fails, try with fallback
      try {
        final position = await _locationService.getCurrentLocation(useFallback: true);
        final location = LatLng(position.latitude, position.longitude);
        _mapController.move(location, 15.0);
        if (mounted) {
          setState(() {
            _currentLocation = location;
            _locationError = null;
          });
        }
      } catch (e2) {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('Failed to get location. Please check GPS settings.')),
          );
        }
      }
    }
  }

  @override
  void dispose() {
    _locationSubscription?.cancel();
    _mapController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final center = _currentLocation ?? widget.initialCenter ?? const LatLng(0, 0);
    final markers = <Marker>[];

    // Add current location marker
    if (_currentLocation != null && widget.showCurrentLocation) {
      markers.add(
        Marker(
          point: _currentLocation!,
          width: 40,
          height: 40,
          child: const Icon(
            Icons.location_on,
            color: Colors.blue,
            size: 40,
          ),
        ),
      );
    }

    // Add custom markers
    if (widget.customMarkers != null) {
      markers.addAll(widget.customMarkers!);
    }

    return Stack(
      children: [
        FlutterMap(
          mapController: _mapController,
          options: MapOptions(
            initialCenter: center,
            initialZoom: 13.0,
            minZoom: 5.0,
            maxZoom: 18.0,
            onTap: (tapPosition, point) {
              try {
                widget.onMapTap?.call(point);
              } catch (e) {
                debugPrint('Error in map tap handler: $e');
              }
            },
          ),
          children: [
            // OpenStreetMap tiles with error handling
            TileLayer(
              urlTemplate: 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
              userAgentPackageName: 'com.ridehailing.rider_app',
              maxZoom: 19,
              maxNativeZoom: 19,
              errorTileCallback: (tile, error, stackTrace) {
                // Track tile errors to show user message
                _tileErrorCount++;
                if (_tileErrorCount > 5 && mounted) {
                  setState(() {
                    _hasTileErrors = true;
                  });
                }
                debugPrint('Tile load error for ${tile.coordinates}: $error');
              },
            ),
            // Markers
            MarkerLayer(markers: markers),
          ],
        ),
        // Loading indicator
        if (_isLoadingLocation)
          const Center(
            child: CircularProgressIndicator(),
          ),
        // Error message for location
        if (_locationError != null && !_isLoadingLocation)
          Positioned(
            top: 16,
            left: 16,
            right: 16,
            child: Card(
              color: Colors.orange.shade100,
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: Row(
                  children: [
                    const Icon(Icons.info_outline, color: Colors.orange),
                    const SizedBox(width: 8),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          Text(
                            _locationError!.contains('timeout') 
                                ? 'Using default location (emulator)'
                                : _locationError!,
                            style: const TextStyle(color: Colors.orange, fontSize: 12),
                          ),
                          if (_locationError!.contains('timeout'))
                            TextButton(
                              onPressed: _initializeLocation,
                              child: const Text('Retry', style: TextStyle(fontSize: 10)),
                            ),
                        ],
                      ),
                    ),
                    IconButton(
                      icon: const Icon(Icons.close, size: 18),
                      onPressed: () {
                        setState(() {
                          _locationError = null;
                        });
                      },
                      padding: EdgeInsets.zero,
                      constraints: const BoxConstraints(),
                    ),
                  ],
                ),
              ),
            ),
          ),
        // Error message for map tiles
        if (_hasTileErrors)
          Positioned(
            top: _locationError != null ? 80 : 16,
            left: 16,
            right: 16,
            child: Card(
              color: Colors.red.shade100,
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: Row(
                  children: [
                    const Icon(Icons.wifi_off, color: Colors.red),
                    const SizedBox(width: 8),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          const Text(
                            'Map tiles not loading. Please check your internet connection.',
                            style: TextStyle(color: Colors.red, fontSize: 12),
                          ),
                          const SizedBox(height: 4),
                          TextButton(
                            onPressed: () {
                              setState(() {
                                _hasTileErrors = false;
                                _tileErrorCount = 0;
                              });
                            },
                            child: const Text('Dismiss', style: TextStyle(fontSize: 10)),
                          ),
                        ],
                      ),
                    ),
                    IconButton(
                      icon: const Icon(Icons.close, size: 18),
                      onPressed: () {
                        setState(() {
                          _hasTileErrors = false;
                          _tileErrorCount = 0;
                        });
                      },
                      padding: EdgeInsets.zero,
                      constraints: const BoxConstraints(),
                    ),
                  ],
                ),
              ),
            ),
          ),
        // My location button
        if (widget.showCurrentLocation && !_isLoadingLocation)
          Positioned(
            bottom: 16,
            right: 16,
            child: FloatingActionButton(
              mini: true,
              onPressed: _moveToCurrentLocation,
              child: const Icon(Icons.my_location),
            ),
          ),
      ],
    );
  }
}

