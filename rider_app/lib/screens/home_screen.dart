import 'dart:async';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:latlong2/latlong.dart';
import 'package:geolocator/geolocator.dart';
import '../providers/auth_provider.dart';
import '../services/location_service.dart';
import '../services/websocket_service.dart';
import '../models/location_model.dart';
import 'login_screen.dart';
import 'book_ride_screen.dart';
import 'map_screen.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final LocationService _locationService = LocationService();
  final WebSocketService _webSocketService = WebSocketService();
  StreamSubscription<Map<String, dynamic>>? _websocketSubscription;
  StreamSubscription<Position>? _locationStreamSubscription;
  Timer? _locationUpdateTimer;
  bool _isLocationTracking = false;
  LocationModel? _currentLocation;

  @override
  void initState() {
    super.initState();
    // Delay initialization to ensure widget is fully built
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _initializeLocationTracking();
    });
  }

  Future<void> _initializeLocationTracking() async {
    if (!mounted) return;
    
    final authProvider = Provider.of<AuthProvider>(context, listen: false);
    final accessToken = authProvider.accessToken;

    if (accessToken == null) {
      // User not logged in, skip location tracking
      return;
    }

    try {
      // Connect to WebSocket (non-blocking, handle errors gracefully)
      try {
        await _webSocketService.connect(accessToken).timeout(
          const Duration(seconds: 5),
          onTimeout: () {
            throw TimeoutException('WebSocket connection timeout');
          },
        );

        // Listen to WebSocket messages
        _websocketSubscription = _webSocketService.messageStream.listen(
          (message) {
            if (mounted) {
              _handleWebSocketMessage(message);
            }
          },
          onError: (error) {
            // Silently handle WebSocket errors
            debugPrint('WebSocket error: $error');
          },
        );
      } catch (e) {
        debugPrint('WebSocket connection failed: $e');
        // Continue without WebSocket - location tracking will still work
      }

      // Start location tracking (non-blocking)
      _startLocationTracking();
    } catch (e) {
      debugPrint('Failed to initialize location tracking: $e');
      // Don't show error to user on startup - just log it
    }
  }

  Future<void> _startLocationTracking() async {
    if (!mounted) return;
    
    try {
      // Check if location services are enabled
      final isEnabled = await _locationService.isLocationServiceEnabled();
      if (!isEnabled) {
        debugPrint('Location services are disabled');
        return;
      }

      // Get initial location (try real location first, then fallback)
      // Clear cache to ensure fresh location from emulator
      _locationService.clearCache();
      Position position;
      try {
        position = await _locationService.getCurrentLocation(useFallback: false, forceRefresh: true).timeout(
          const Duration(seconds: 5),
        );
      } catch (e) {
        // If timeout or error, use fallback (Bangalore)
        position = await _locationService.getCurrentLocation(useFallback: true, forceRefresh: true);
      }
      
      if (!mounted) return;
      
      setState(() {
        _currentLocation = LocationModel.fromPosition(position);
        _isLocationTracking = true;
      });

      // Send initial location
      _sendLocationUpdate(position.latitude, position.longitude);

      // Cancel existing stream if any
      await _locationStreamSubscription?.cancel();
      
      // Listen to location stream for real-time updates (primary method - no timeout)
      _locationStreamSubscription = _locationService.getLocationStream(
        accuracy: LocationAccuracy.medium, // Better for emulators
      ).listen(
        (position) {
          debugPrint('Location update: ${position.latitude}, ${position.longitude}');
          if (mounted) {
            setState(() {
              _currentLocation = LocationModel.fromPosition(position);
              _isLocationTracking = true;
            });
            _sendLocationUpdate(position.latitude, position.longitude);
          }
        },
        onError: (error) {
          debugPrint('Location stream error: $error');
          // Don't show error - stream will keep trying
        },
        cancelOnError: false, // Keep stream alive even on errors
      );

      // Backup: Periodic manual updates every 20 seconds (less frequent)
      _locationUpdateTimer = Timer.periodic(
        const Duration(seconds: 20),
        (timer) async {
          if (!mounted) {
            timer.cancel();
            return;
          }
          try {
            // Quick location check with short timeout
            final position = await _locationService.getCurrentLocation(useFallback: false).timeout(
              const Duration(seconds: 3),
            );
            if (mounted) {
              setState(() {
                _currentLocation = LocationModel.fromPosition(position);
              });
              _sendLocationUpdate(position.latitude, position.longitude);
            }
          } catch (e) {
            // Silent fail - stream is primary method, timeout is expected
            debugPrint('Periodic location update skipped (timeout expected): $e');
          }
        },
      );
    } catch (e) {
      debugPrint('Location tracking error: $e');
      // Don't show error on startup - user can retry later
      if (mounted) {
        // Only show error if it's a permission issue after user interaction
        setState(() {
          _isLocationTracking = false;
        });
      }
    }
  }

  void _sendLocationUpdate(double latitude, double longitude) {
    if (_webSocketService.isConnected) {
      _webSocketService.sendLocationUpdate(latitude, longitude);
    }
  }

  void _handleWebSocketMessage(Map<String, dynamic> message) {
    final type = message['type'] as String?;

    switch (type) {
      case 'connected':
        // Silently handle connection - don't show snackbar
        debugPrint('WebSocket connected');
        break;
      case 'error':
        // Only show critical errors, not echo messages
        final errorMsg = message['message']?.toString() ?? '';
        if (!errorMsg.startsWith('Echo:') && !errorMsg.startsWith('echo:')) {
          if (mounted) {
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(
                content: Text(errorMsg),
                backgroundColor: Colors.red,
                duration: const Duration(seconds: 3),
              ),
            );
          }
        }
        break;
      case 'disconnected':
        // Handle disconnection silently
        debugPrint('WebSocket disconnected');
        break;
      // Handle driver location updates, ride status updates, etc.
      case 'ride:driver:location':
        // Handle driver location update
        debugPrint('Driver location update received');
        break;
      case 'ride:status:updated':
        // Handle ride status update
        debugPrint('Ride status updated');
        break;
      default:
        // Ignore unknown message types (likely echoes)
        debugPrint('WebSocket message: $type');
        break;
    }
  }

  Future<void> _handleLogout() async {
    // Stop location tracking
    _locationUpdateTimer?.cancel();
    await _websocketSubscription?.cancel();
    await _webSocketService.disconnect();

    final authProvider = Provider.of<AuthProvider>(context, listen: false);
    await authProvider.logout();
    if (mounted) {
      Navigator.of(context).pushReplacement(
        MaterialPageRoute(builder: (_) => const LoginScreen()),
      );
    }
  }

  @override
  void dispose() {
    _locationUpdateTimer?.cancel();
    _locationStreamSubscription?.cancel();
    _websocketSubscription?.cancel();
    _webSocketService.dispose();
    super.dispose();
  }

  /// Force refresh location by restarting the location stream
  Future<void> _refreshLocation() async {
    if (!mounted) return;
    
    try {
      // Clear cache
      _locationService.clearCache();
      
      // Cancel existing stream
      await _locationStreamSubscription?.cancel();
      
      // Get fresh location
      final position = await _locationService.getCurrentLocation(useFallback: false, forceRefresh: true);
      
      if (mounted) {
        setState(() {
          _currentLocation = LocationModel.fromPosition(position);
        });
        _sendLocationUpdate(position.latitude, position.longitude);
      }
      
      // Restart location stream
      _locationStreamSubscription = _locationService.getLocationStream(
        accuracy: LocationAccuracy.medium,
      ).listen(
        (position) {
          debugPrint('Location update: ${position.latitude}, ${position.longitude}');
          if (mounted) {
            setState(() {
              _currentLocation = LocationModel.fromPosition(position);
            });
            _sendLocationUpdate(position.latitude, position.longitude);
          }
        },
        onError: (error) {
          debugPrint('Location stream error: $error');
        },
        cancelOnError: false,
      );
    } catch (e) {
      debugPrint('Error refreshing location: $e');
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Location refresh failed. Using emulator Extended Controls to set location.')),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Ride Hailing'),
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        actions: [
          // Refresh location button
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: _refreshLocation,
            tooltip: 'Refresh Location',
          ),
          // Location status indicator
          if (_isLocationTracking)
            const Padding(
              padding: EdgeInsets.all(8.0),
              child: Icon(
                Icons.location_on,
                color: Colors.green,
              ),
            )
          else
            const Padding(
              padding: EdgeInsets.all(8.0),
              child: Icon(
                Icons.location_off,
                color: Colors.grey,
              ),
            ),
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: _handleLogout,
            tooltip: 'Logout',
          ),
        ],
      ),
      body: Column(
        children: [
          // Map view - takes most of the screen
          Expanded(
            flex: 3,
            child: MapScreen(
              showCurrentLocation: true,
              onLocationChanged: (location) {
                // Location updated from map screen
                setState(() {
                  _currentLocation = LocationModel(
                    latitude: location.latitude,
                    longitude: location.longitude,
                    timestamp: DateTime.now(),
                  );
                });
                _sendLocationUpdate(location.latitude, location.longitude);
              },
            ),
          ),
          // Bottom section with user info and actions
          Container(
            padding: const EdgeInsets.all(16),
            decoration: BoxDecoration(
              color: Theme.of(context).scaffoldBackgroundColor,
              boxShadow: [
                BoxShadow(
                  color: Colors.black.withOpacity(0.1),
                  blurRadius: 4,
                  offset: const Offset(0, -2),
                ),
              ],
            ),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                // User info
                Consumer<AuthProvider>(
                  builder: (context, authProvider, _) {
                    final user = authProvider.user;
                    return Row(
                      children: [
                        CircleAvatar(
                          child: Text(
                            user?.email[0].toUpperCase() ?? 'U',
                          ),
                        ),
                        const SizedBox(width: 12),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                user?.email ?? 'Guest',
                                style: const TextStyle(
                                  fontSize: 16,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                              if (user != null)
                                Chip(
                                  label: Text(
                                    user.role,
                                    style: const TextStyle(fontSize: 10),
                                  ),
                                  padding: EdgeInsets.zero,
                                ),
                            ],
                          ),
                        ),
                      ],
                    );
                  },
                ),
                const SizedBox(height: 16),
                // Book Ride Button
                SizedBox(
                  width: double.infinity,
                  child: ElevatedButton.icon(
                    onPressed: () {
                      Navigator.of(context).push(
                        MaterialPageRoute(
                          builder: (_) => const BookRideScreen(),
                        ),
                      );
                    },
                    icon: const Icon(Icons.directions_car),
                    label: const Text(
                      'Book a Ride',
                      style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                    ),
                    style: ElevatedButton.styleFrom(
                      padding: const EdgeInsets.symmetric(vertical: 16),
                      backgroundColor: Theme.of(context).colorScheme.primary,
                      foregroundColor: Colors.white,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                    ),
                  ),
                ),
                // Location status
                if (_currentLocation != null)
                  Padding(
                    padding: const EdgeInsets.only(top: 8),
                    child: Text(
                      'Location: ${_currentLocation!.latitude.toStringAsFixed(6)}, ${_currentLocation!.longitude.toStringAsFixed(6)}',
                      style: TextStyle(
                        fontSize: 10,
                        color: Colors.grey[600],
                      ),
                    ),
                  ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
