import 'dart:convert';
import 'package:flutter/foundation.dart';
import '../models/user.dart';
import '../models/auth_response.dart';
import '../models/login_request.dart';
import '../models/register_request.dart';
import '../services/auth_service.dart';
import '../services/token_storage_service.dart';

class AuthProvider with ChangeNotifier {
  final AuthService _authService = AuthService();
  final TokenStorageService _tokenStorage = TokenStorageService();

  User? _user;
  String? _accessToken;
  bool _isLoading = false;
  String? _error;

  User? get user => _user;
  String? get accessToken => _accessToken;
  bool get isLoading => _isLoading;
  String? get error => _error;
  bool get isAuthenticated => _user != null && _accessToken != null;

  // Check if user is already logged in on app start
  Future<void> checkAuthStatus() async {
    _isLoading = true;
    notifyListeners();

    try {
      final token = await _tokenStorage.getAccessToken();
      if (token != null && token.isNotEmpty) {
        _accessToken = token;
        
        // Restore user data from storage
        final userDataJson = await _tokenStorage.getUserData();
        if (userDataJson != null && userDataJson.isNotEmpty) {
          try {
            final userMap = json.decode(userDataJson) as Map<String, dynamic>;
            _user = User.fromJson(userMap);
          } catch (e) {
            debugPrint('Error parsing user data: $e');
            // If user data is corrupted, clear tokens and require re-login
            await _tokenStorage.clearTokens();
            _accessToken = null;
            _user = null;
          }
        }
        
        // In a real app, you might want to validate the token by calling a /me endpoint
        // For now, we'll assume the token is valid if it exists and user data is restored
        _isLoading = false;
        notifyListeners();
      } else {
        _isLoading = false;
        notifyListeners();
      }
    } catch (e) {
      _error = e.toString();
      _isLoading = false;
      notifyListeners();
    }
  }

  // Login
  Future<bool> login(String email, String password) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      final authResponse = await _authService.login(
        LoginRequest(email: email, password: password),
      );

      await _saveAuthData(authResponse);
      return true;
    } catch (e) {
      _error = e.toString();
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  // Register
  Future<bool> register({
    required String email,
    required String password,
    required String phoneNumber,
    required String firstName,
    required String lastName,
    required bool isDriver,
    String? licenseNumber,
    String? vehicleModel,
    String? vehiclePlate,
  }) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      final registerRequest = RegisterRequest(
        email: email,
        password: password,
        phoneNumber: phoneNumber,
        firstName: firstName,
        lastName: lastName,
        licenseNumber: licenseNumber,
        vehicleModel: vehicleModel,
        vehiclePlate: vehiclePlate,
      );

      // Register user
      if (isDriver) {
        await _authService.registerDriver(registerRequest);
      } else {
        await _authService.registerRider(registerRequest);
      }

      // Auto-login after registration
      return await login(email, password);
    } catch (e) {
      _error = e.toString();
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }

  // Refresh access token
  Future<bool> refreshAccessToken() async {
    try {
      final refreshToken = await _tokenStorage.getRefreshToken();
      if (refreshToken == null || refreshToken.isEmpty) {
        await logout();
        return false;
      }

      final authResponse = await _authService.refreshToken(refreshToken);
      await _saveAuthData(authResponse);
      return true;
    } catch (e) {
      // Refresh failed, logout user
      await logout();
      return false;
    }
  }

  // Save authentication data
  Future<void> _saveAuthData(AuthResponse authResponse) async {
    _user = authResponse.user;
    _accessToken = authResponse.accessToken;
    
    // Save tokens
    await _tokenStorage.saveTokens(
      authResponse.accessToken,
      authResponse.refreshToken,
    );
    
    // Save user data for persistence
    final userJson = json.encode(authResponse.user.toJson());
    await _tokenStorage.saveUserData(userJson);
    
    _isLoading = false;
    _error = null;
    notifyListeners();
  }

  // Logout
  Future<void> logout() async {
    try {
      if (_accessToken != null) {
        await _authService.logout(_accessToken!);
      }
    } catch (e) {
      // Continue with logout even if server call fails
      debugPrint('Logout error: $e');
    } finally {
      _user = null;
      _accessToken = null;
      await _tokenStorage.clearTokens(); // This clears tokens and user data
      _error = null;
      notifyListeners();
    }
  }

  // Clear error
  void clearError() {
    _error = null;
    notifyListeners();
  }
}

