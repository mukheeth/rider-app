import 'dart:async';
import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:web_socket_channel/web_socket_channel.dart';
import '../config/api_config.dart';

class WebSocketService {
  WebSocketChannel? _channel;
  StreamSubscription? _subscription;
  bool _isConnected = false;
  final StreamController<Map<String, dynamic>> _messageController =
      StreamController<Map<String, dynamic>>.broadcast();

  Stream<Map<String, dynamic>> get messageStream => _messageController.stream;
  bool get isConnected => _isConnected;

  /// Connect to WebSocket server
  Future<void> connect(String accessToken) async {
    try {
      if (_isConnected) {
        await disconnect();
      }

      final url = ApiConfig.getWebSocketUrl(accessToken);
      _channel = WebSocketChannel.connect(Uri.parse(url));

      _subscription = _channel!.stream.listen(
        (message) {
          try {
            if (message is String) {
              // Filter out echo messages from backend (case insensitive)
              final messageLower = message.toLowerCase().trim();
              if (messageLower.startsWith('echo:') || 
                  messageLower.contains('"echo"') ||
                  messageLower.contains("'echo'")) {
                // Ignore echo messages - these are just confirmations from backend
                debugPrint('Ignored echo message: ${message.substring(0, message.length > 50 ? 50 : message.length)}');
                return;
              }

              // Try to parse as JSON
              try {
                final data = json.decode(message) as Map<String, dynamic>;
                
                // Check if it's an echo message in JSON format
                if (data.containsKey('echo') || 
                    data['message']?.toString().toLowerCase().startsWith('echo:') == true) {
                  debugPrint('Ignored echo message in JSON format');
                  return;
                }
                
                // Only process messages that have a 'type' field or are actual server messages
                // Ignore messages that look like our own location updates (echoed back)
                if (data.containsKey('type') || 
                    data.containsKey('rideId') || 
                    data.containsKey('event') ||
                    data.containsKey('driverLocation')) {
                  _messageController.add(data);
                } else {
                  // Likely an echo of our location update - ignore it silently
                  debugPrint('Ignored location echo: ${data.keys.join(", ")}');
                }
              } catch (jsonError) {
                // Not valid JSON, might be a plain text message
                // Only process if it's not an echo
                if (!messageLower.startsWith('echo:')) {
                  _messageController.add({
                    'type': 'message',
                    'content': message,
                  });
                }
              }
            }
          } catch (e) {
            // Only show actual errors, not echo messages
            final messageStr = message.toString();
            final messageLower = messageStr.toLowerCase();
            if (!messageLower.startsWith('echo:') && 
                !messageLower.contains('echo') &&
                !messageLower.contains('{"latitude"')) {
              debugPrint('WebSocket error: $e');
              _messageController.add({
                'type': 'error',
                'message': messageStr.length > 100 ? '${messageStr.substring(0, 100)}...' : messageStr,
              });
            }
          }
        },
        onError: (error) {
          _messageController.add({
            'type': 'error',
            'message': error.toString(),
          });
          _isConnected = false;
        },
        onDone: () {
          _isConnected = false;
          _messageController.add({
            'type': 'disconnected',
            'message': 'WebSocket connection closed',
          });
        },
      );

      _isConnected = true;
      _messageController.add({
        'type': 'connected',
        'message': 'WebSocket connected successfully',
      });
    } catch (e) {
      _isConnected = false;
      _messageController.add({
        'type': 'error',
        'message': 'Failed to connect: $e',
      });
      rethrow;
    }
  }

  /// Send location update to server
  void sendLocationUpdate(double latitude, double longitude) {
    if (!_isConnected || _channel == null) {
      return;
    }

    try {
      final message = json.encode({
        'latitude': latitude,
        'longitude': longitude,
        'timestamp': DateTime.now().toIso8601String(),
      });

      _channel!.sink.add(message);
    } catch (e) {
      _messageController.add({
        'type': 'error',
        'message': 'Failed to send location: $e',
      });
    }
  }

  /// Send heartbeat/ping
  void sendHeartbeat() {
    if (!_isConnected || _channel == null) {
      return;
    }

    try {
      final message = json.encode({
        'type': 'ping',
        'timestamp': DateTime.now().toIso8601String(),
      });

      _channel!.sink.add(message);
    } catch (e) {
      // Silent fail for heartbeat
    }
  }

  /// Disconnect from WebSocket server
  Future<void> disconnect() async {
    try {
      await _subscription?.cancel();
      await _channel?.sink.close();
    } catch (e) {
      // Ignore errors during disconnect
    } finally {
      _channel = null;
      _subscription = null;
      _isConnected = false;
    }
  }

  /// Dispose resources
  void dispose() {
    disconnect();
    _messageController.close();
  }
}

