class RegisterRequest {
  final String email;
  final String password;
  final String phoneNumber;
  final String firstName;
  final String lastName;
  final String? licenseNumber;
  final String? vehicleModel;
  final String? vehiclePlate;

  RegisterRequest({
    required this.email,
    required this.password,
    required this.phoneNumber,
    required this.firstName,
    required this.lastName,
    this.licenseNumber,
    this.vehicleModel,
    this.vehiclePlate,
  });

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> json = {
      'email': email,
      'password': password,
      'phoneNumber': phoneNumber,
      'firstName': firstName,
      'lastName': lastName,
    };
    
    // Only include driver fields if they are provided
    if (licenseNumber != null) {
      json['licenseNumber'] = licenseNumber;
    }
    if (vehicleModel != null) {
      json['vehicleModel'] = vehicleModel;
    }
    if (vehiclePlate != null) {
      json['vehiclePlate'] = vehiclePlate;
    }
    
    return json;
  }
}

