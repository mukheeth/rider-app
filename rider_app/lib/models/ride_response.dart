class RideResponse {
  final String rideId;
  final String status;
  final double estimatedFare;
  final int estimatedArrivalTime;

  RideResponse({
    required this.rideId,
    required this.status,
    required this.estimatedFare,
    required this.estimatedArrivalTime,
  });

  factory RideResponse.fromJson(Map<String, dynamic> json) {
    return RideResponse(
      rideId: json['rideId'] as String,
      status: json['status'] as String,
      estimatedFare: (json['estimatedFare'] as num).toDouble(),
      estimatedArrivalTime: json['estimatedArrivalTime'] as int,
    );
  }
}

