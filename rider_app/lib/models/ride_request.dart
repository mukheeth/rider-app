import 'location.dart';

class RideRequest {
  final Location pickupLocation;
  final Location dropoffLocation;

  RideRequest({
    required this.pickupLocation,
    required this.dropoffLocation,
  });

  Map<String, dynamic> toJson() {
    return {
      'pickupLocation': pickupLocation.toJson(),
      'dropoffLocation': dropoffLocation.toJson(),
    };
  }
}

