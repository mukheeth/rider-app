/// API Configuration
/// 
/// For Android Emulator: Use 'http://10.0.2.2:8080'
/// For Physical Device via USB: Use 'http://localhost:8080' (after running: adb reverse tcp:8080 tcp:8080)
/// For Physical Device via Network: Use your computer's IP address (e.g., 'http://192.168.0.125:8080')
class ApiConfig {
  // Change this based on your connection method:
  // - Emulator: 'http://10.0.2.2:8080'
  // - USB Device (ADB Forwarding): 'http://localhost:8080' (RECOMMENDED - run: adb reverse tcp:8080 tcp:8080)
  // - USB Device (Network): 'http://192.168.0.125:8080' (your computer's IP - found via ipconfig)
  // 
  // AUTO-DETECT: Uses localhost if ADB forwarding is set up, otherwise uses emulator address
  static String get baseUrl {
    // Try to detect if we're on emulator or physical device
    // For now, use localhost (works with ADB forwarding for physical devices)
    // If ADB forwarding is set up, localhost will work on physical device
    // If not, user should run: adb reverse tcp:8080 tcp:8080
    return 'http://localhost:8080'; // Works with ADB forwarding for physical devices
  }
  
  static String get healthEndpoint => '$baseUrl/health';
  
  // Authentication endpoints
  static String get loginEndpoint => '$baseUrl/api/v1/auth/login';
  static String get registerRiderEndpoint => '$baseUrl/api/v1/auth/register/rider';
  static String get registerDriverEndpoint => '$baseUrl/api/v1/auth/register/driver';
  static String get refreshTokenEndpoint => '$baseUrl/api/v1/auth/refresh';
  static String get logoutEndpoint => '$baseUrl/api/v1/auth/logout';
  
  // Ride endpoints
  static String get createRideEndpoint => '$baseUrl/api/v1/rides';
  static String getRideDetailsEndpoint(String rideId) => '$baseUrl/api/v1/rides/$rideId';
  
  // WebSocket endpoint
  static String getWebSocketUrl(String token) {
    // Convert http to ws for WebSocket
    final wsBaseUrl = baseUrl.replaceFirst('http://', 'ws://');
    return '$wsBaseUrl/ws?token=$token';
  }
}

