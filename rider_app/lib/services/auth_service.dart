import 'dart:convert';
import 'dart:io';
import 'package:http/http.dart' as http;
import '../config/api_config.dart';
import '../models/login_request.dart';
import '../models/register_request.dart';
import '../models/auth_response.dart';
import '../models/refresh_token_request.dart';

class AuthService {
  // Login
  Future<AuthResponse> login(LoginRequest request) async {
    try {
      final response = await http.post(
        Uri.parse(ApiConfig.loginEndpoint),
        headers: {'Content-Type': 'application/json'},
        body: json.encode(request.toJson()),
      ).timeout(
        const Duration(seconds: 10),
        onTimeout: () {
          throw Exception('Connection timeout. Please check:\n1. Backend is running\n2. ADB forwarding: adb reverse tcp:8080 tcp:8080\n3. Internet connection');
        },
      );

      if (response.statusCode == 200) {
        return AuthResponse.fromJson(json.decode(response.body) as Map<String, dynamic>);
      } else {
        String errorMessage;
        try {
          final errorBody = json.decode(response.body) as Map<String, dynamic>?;
          errorMessage = errorBody?['message'] ?? 'Login failed: ${response.statusCode}';
        } catch (e) {
          errorMessage = 'Login failed: ${response.statusCode}\nResponse: ${response.body}';
        }
        throw Exception(errorMessage);
      }
    } on http.ClientException catch (e) {
      throw Exception('Connection error: ${e.message}\n\nPlease check:\n1. Backend is running on port 8080\n2. Run: adb reverse tcp:8080 tcp:8080\n3. Device and computer are connected');
    } on SocketException catch (e) {
      throw Exception('Network error: ${e.message}\n\nPlease check:\n1. Backend is running\n2. ADB forwarding is set up\n3. Internet connection');
    } catch (e) {
      if (e.toString().contains('timeout') || e.toString().contains('Connection')) {
        rethrow;
      }
      throw Exception('Login failed: $e');
    }
  }

  // Register as Rider
  Future<Map<String, dynamic>> registerRider(RegisterRequest request) async {
    final response = await http.post(
      Uri.parse(ApiConfig.registerRiderEndpoint),
      headers: {'Content-Type': 'application/json'},
      body: json.encode(request.toJson()),
    );

    if (response.statusCode == 201) {
      return json.decode(response.body) as Map<String, dynamic>;
    } else {
      final errorBody = json.decode(response.body) as Map<String, dynamic>?;
      throw Exception(errorBody?['message'] ?? 'Registration failed: ${response.statusCode}');
    }
  }

  // Register as Driver
  Future<Map<String, dynamic>> registerDriver(RegisterRequest request) async {
    final response = await http.post(
      Uri.parse(ApiConfig.registerDriverEndpoint),
      headers: {'Content-Type': 'application/json'},
      body: json.encode(request.toJson()),
    );

    if (response.statusCode == 201) {
      return json.decode(response.body) as Map<String, dynamic>;
    } else {
      final errorBody = json.decode(response.body) as Map<String, dynamic>?;
      throw Exception(errorBody?['message'] ?? 'Registration failed: ${response.statusCode}');
    }
  }

  // Refresh access token
  Future<AuthResponse> refreshToken(String refreshToken) async {
    final request = RefreshTokenRequest(refreshToken: refreshToken);
    final response = await http.post(
      Uri.parse(ApiConfig.refreshTokenEndpoint),
      headers: {'Content-Type': 'application/json'},
      body: json.encode(request.toJson()),
    );

    if (response.statusCode == 200) {
      return AuthResponse.fromJson(json.decode(response.body) as Map<String, dynamic>);
    } else {
      final errorBody = json.decode(response.body) as Map<String, dynamic>?;
      throw Exception(errorBody?['message'] ?? 'Token refresh failed: ${response.statusCode}');
    }
  }

  // Logout
  Future<void> logout(String accessToken) async {
    final response = await http.post(
      Uri.parse(ApiConfig.logoutEndpoint),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $accessToken',
      },
    );

    if (response.statusCode != 200) {
      // Even if logout fails on server, we'll clear local tokens
      throw Exception('Logout failed: ${response.statusCode}');
    }
  }
}

