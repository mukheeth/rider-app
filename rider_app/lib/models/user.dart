class User {
  final String userId;
  final String email;
  final String role; // RIDER, DRIVER, ADMIN

  User({
    required this.userId,
    required this.email,
    required this.role,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      userId: json['userId'] as String,
      email: json['email'] as String,
      role: json['role'] as String,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'userId': userId,
      'email': email,
      'role': role,
    };
  }
}

