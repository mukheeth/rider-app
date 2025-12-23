import 'dart:convert';
import 'package:http/http.dart' as http;
import '../config/api_config.dart';
import '../models/ride_request.dart';
import '../models/ride_response.dart';
import '../services/token_storage_service.dart';

class RideService {
  final TokenStorageService _tokenStorage = TokenStorageService();

  Future<RideResponse> createRide(String accessToken, RideRequest request) async {
    final response = await http.post(
      Uri.parse('${ApiConfig.baseUrl}/api/v1/rides'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $accessToken',
      },
      body: json.encode(request.toJson()),
    );

    if (response.statusCode == 201) {
      return RideResponse.fromJson(json.decode(response.body) as Map<String, dynamic>);
    } else {
      final errorBody = json.decode(response.body) as Map<String, dynamic>?;
      throw Exception(errorBody?['message'] ?? 'Failed to create ride: ${response.statusCode}');
    }
  }

  Future<Map<String, dynamic>> getRideDetails(String rideId) async {
    final token = await _tokenStorage.getAccessToken();
    if (token == null || token.isEmpty) {
      throw Exception('User not authenticated');
    }

    final response = await http.get(
      Uri.parse('${ApiConfig.baseUrl}/api/v1/rides/$rideId'),
      headers: {
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode == 200) {
      return json.decode(response.body) as Map<String, dynamic>;
    } else {
      throw Exception('Failed to get ride details: ${response.statusCode}');
    }
  }
}

