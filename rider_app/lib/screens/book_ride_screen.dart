import 'package:flutter/material.dart';
import 'package:latlong2/latlong.dart';
import '../models/location.dart';
import '../models/ride_request.dart';
import '../services/ride_service.dart';
import '../services/location_service.dart';
import '../models/ride_response.dart';
import '../providers/auth_provider.dart';
import 'package:provider/provider.dart';
import 'map_screen.dart';

class BookRideScreen extends StatefulWidget {
  const BookRideScreen({super.key});

  @override
  State<BookRideScreen> createState() => _BookRideScreenState();
}

class _BookRideScreenState extends State<BookRideScreen> {
  final TextEditingController _pickupController = TextEditingController();
  final TextEditingController _dropoffController = TextEditingController();
  final RideService _rideService = RideService();
  final LocationService _locationService = LocationService();
  
  bool _isLoading = false;
  bool _isGettingLocation = false;
  String? _errorMessage;
  
  // Location coordinates
  double? _pickupLat;
  double? _pickupLon;
  String _pickupAddress = 'Current Location';
  
  double? _dropoffLat;
  double? _dropoffLon;
  String _dropoffAddress = '';

  @override
  void initState() {
    super.initState();
    _getCurrentLocation();
  }

  Future<void> _getCurrentLocation() async {
    if (!mounted) return;
    
    setState(() {
      _isGettingLocation = true;
    });

    try {
      final position = await _locationService.getCurrentLocation(useFallback: true);
      if (mounted) {
        setState(() {
          _pickupLat = position.latitude;
          _pickupLon = position.longitude;
          _pickupAddress = 'Current Location (${position.latitude.toStringAsFixed(4)}, ${position.longitude.toStringAsFixed(4)})';
          _pickupController.text = _pickupAddress;
          _isGettingLocation = false;
        });
      }
    } catch (e) {
      if (mounted) {
        setState(() {
          _isGettingLocation = false;
        // Use fallback location (Bangalore, India)
        _pickupLat = 12.9716;
        _pickupLon = 77.5946;
        _pickupAddress = 'Default Location (Bangalore)';
          _pickupController.text = _pickupAddress;
        });
      }
    }
  }

  Future<void> _selectLocationOnMap(bool isPickup) async {
    if (!mounted) return;
    
    try {
      final currentLocation = _pickupLat != null && _pickupLon != null
          ? LatLng(_pickupLat!, _pickupLon!)
          : null;

      final selectedLocation = await Navigator.of(context).push<LatLng>(
        MaterialPageRoute(
          builder: (context) => MapScreen(
            initialCenter: currentLocation,
            showCurrentLocation: true,
            onMapTap: (location) {
              if (Navigator.of(context).canPop()) {
                Navigator.of(context).pop(location);
              }
            },
          ),
        ),
      );

      if (selectedLocation != null && mounted) {
        setState(() {
          if (isPickup) {
            _pickupLat = selectedLocation.latitude;
            _pickupLon = selectedLocation.longitude;
            _pickupController.text = '${selectedLocation.latitude.toStringAsFixed(4)}, ${selectedLocation.longitude.toStringAsFixed(4)}';
            _pickupAddress = _pickupController.text;
          } else {
            _dropoffLat = selectedLocation.latitude;
            _dropoffLon = selectedLocation.longitude;
            _dropoffController.text = '${selectedLocation.latitude.toStringAsFixed(4)}, ${selectedLocation.longitude.toStringAsFixed(4)}';
            _dropoffAddress = _dropoffController.text;
          }
        });
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Failed to select location: $e'),
            backgroundColor: Colors.red,
          ),
        );
      }
    }
  }

  @override
  void dispose() {
    _pickupController.dispose();
    _dropoffController.dispose();
    super.dispose();
  }

  Future<void> _bookRide() async {
    if (_pickupLat == null || _pickupLon == null) {
      setState(() {
        _errorMessage = 'Please set pickup location';
      });
      return;
    }

    if (_dropoffLat == null || _dropoffLon == null || _dropoffController.text.isEmpty) {
      setState(() {
        _errorMessage = 'Please set dropoff location';
      });
      return;
    }

    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    try {
      final rideRequest = RideRequest(
        pickupLocation: Location(
          latitude: _pickupLat!,
          longitude: _pickupLon!,
          address: _pickupAddress,
        ),
        dropoffLocation: Location(
          latitude: _dropoffLat!,
          longitude: _dropoffLon!,
          address: _dropoffController.text.isNotEmpty 
              ? _dropoffController.text 
              : '${_dropoffLat!.toStringAsFixed(4)}, ${_dropoffLon!.toStringAsFixed(4)}',
        ),
      );

      final authProvider = Provider.of<AuthProvider>(context, listen: false);
      final accessToken = authProvider.accessToken;
      
      if (accessToken == null) {
        throw Exception('Not authenticated. Please login again.');
      }

      final rideResponse = await _rideService.createRide(accessToken, rideRequest);

      if (mounted) {
        // Show success dialog
        showDialog(
          context: context,
          builder: (context) => AlertDialog(
            title: const Text('Ride Booked!'),
            content: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text('Ride ID: ${rideResponse.rideId}'),
                const SizedBox(height: 8),
                Text('Status: ${rideResponse.status}'),
                const SizedBox(height: 8),
                Text('Estimated Fare: \$${rideResponse.estimatedFare.toStringAsFixed(2)}'),
                const SizedBox(height: 8),
                Text('Estimated Arrival: ${rideResponse.estimatedArrivalTime} minutes'),
              ],
            ),
            actions: [
              TextButton(
                onPressed: () {
                  Navigator.of(context).pop(); // Close dialog
                  Navigator.of(context).pop(); // Go back to home
                },
                child: const Text('OK'),
              ),
            ],
          ),
        );
      }
    } catch (e) {
      setState(() {
        _errorMessage = e.toString().replaceAll('Exception: ', '');
        _isLoading = false;
      });
    } finally {
      if (mounted) {
        setState(() {
          _isLoading = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Book a Ride'),
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // Pickup Location
            Card(
              elevation: 2,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        Icon(
                          Icons.location_on,
                          color: Colors.green,
                          size: 28,
                        ),
                        const SizedBox(width: 8),
                        const Text(
                          'Pickup Location',
                          style: TextStyle(
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 12),
                    if (_isGettingLocation)
                      const Row(
                        children: [
                          SizedBox(
                            width: 20,
                            height: 20,
                            child: CircularProgressIndicator(strokeWidth: 2),
                          ),
                          SizedBox(width: 12),
                          Text('Getting your location...'),
                        ],
                      )
                    else
                      Row(
                        children: [
                          Expanded(
                            child: TextField(
                              controller: _pickupController,
                              enabled: false,
                              decoration: InputDecoration(
                                hintText: 'Current location',
                                border: OutlineInputBorder(
                                  borderRadius: BorderRadius.circular(8),
                                ),
                                prefixIcon: const Icon(Icons.my_location),
                                filled: true,
                                fillColor: Colors.grey.shade100,
                              ),
                            ),
                          ),
                          const SizedBox(width: 8),
                          IconButton(
                            icon: const Icon(Icons.refresh),
                            onPressed: _getCurrentLocation,
                            tooltip: 'Refresh location',
                          ),
                          IconButton(
                            icon: const Icon(Icons.map),
                            onPressed: () => _selectLocationOnMap(true),
                            tooltip: 'Select on map',
                          ),
                        ],
                      ),
                    const SizedBox(height: 8),
                    if (_pickupLat != null && _pickupLon != null)
                      Text(
                        'Coordinates: ${_pickupLat!.toStringAsFixed(6)}, ${_pickupLon!.toStringAsFixed(6)}',
                        style: const TextStyle(
                          fontSize: 12,
                          color: Colors.grey,
                        ),
                      ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 16),
            
            // Dropoff Location
            Card(
              elevation: 2,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        Icon(
                          Icons.location_on,
                          color: Colors.red,
                          size: 28,
                        ),
                        const SizedBox(width: 8),
                        const Text(
                          'Dropoff Location',
                          style: TextStyle(
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 12),
                    Row(
                      children: [
                        Expanded(
                          child: TextField(
                            controller: _dropoffController,
                            decoration: InputDecoration(
                              hintText: 'Enter dropoff address or tap map to select',
                              border: OutlineInputBorder(
                                borderRadius: BorderRadius.circular(8),
                              ),
                              prefixIcon: const Icon(Icons.search),
                            ),
                            onChanged: (value) {
                              // You can add address search/autocomplete here
                            },
                          ),
                        ),
                        const SizedBox(width: 8),
                        IconButton(
                          icon: const Icon(Icons.map),
                          onPressed: () => _selectLocationOnMap(false),
                          tooltip: 'Select on map',
                        ),
                      ],
                    ),
                    const SizedBox(height: 8),
                    if (_dropoffLat != null && _dropoffLon != null)
                      Text(
                        'Coordinates: ${_dropoffLat!.toStringAsFixed(6)}, ${_dropoffLon!.toStringAsFixed(6)}',
                        style: const TextStyle(
                          fontSize: 12,
                          color: Colors.grey,
                        ),
                      ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 24),
            
            // Error Message
            if (_errorMessage != null)
              Container(
                padding: const EdgeInsets.all(12),
                margin: const EdgeInsets.only(bottom: 16),
                decoration: BoxDecoration(
                  color: Colors.red.shade50,
                  borderRadius: BorderRadius.circular(8),
                  border: Border.all(color: Colors.red.shade200),
                ),
                child: Row(
                  children: [
                    Icon(Icons.error_outline, color: Colors.red.shade700),
                    const SizedBox(width: 8),
                    Expanded(
                      child: Text(
                        _errorMessage!,
                        style: TextStyle(color: Colors.red.shade700),
                      ),
                    ),
                  ],
                ),
              ),
            
            // Book Ride Button
            ElevatedButton(
              onPressed: (_isLoading || _pickupLat == null || _dropoffLat == null) 
                  ? null 
                  : _bookRide,
              style: ElevatedButton.styleFrom(
                padding: const EdgeInsets.symmetric(vertical: 16),
                backgroundColor: Theme.of(context).colorScheme.primary,
                foregroundColor: Colors.white,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8),
                ),
              ),
              child: _isLoading
                  ? const SizedBox(
                      height: 20,
                      width: 20,
                      child: CircularProgressIndicator(
                        strokeWidth: 2,
                        valueColor: AlwaysStoppedAnimation<Color>(Colors.white),
                      ),
                    )
                  : const Text(
                      'Book Ride',
                      style: TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
            ),
          ],
        ),
      ),
    );
  }
}
