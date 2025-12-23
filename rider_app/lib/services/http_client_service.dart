import 'dart:convert';
import 'package:http/http.dart' as http;
import 'token_storage_service.dart';

class HttpClientService {
  final TokenStorageService _tokenStorage = TokenStorageService();

  // Get authenticated headers with JWT token
  Future<Map<String, String>> _getHeaders({bool includeAuth = true}) async {
    final headers = <String, String>{
      'Content-Type': 'application/json',
    };

    if (includeAuth) {
      final token = await _tokenStorage.getAccessToken();
      if (token != null) {
        headers['Authorization'] = 'Bearer $token';
      }
    }

    return headers;
  }

  // GET request
  Future<http.Response> get(String url, {bool includeAuth = true}) async {
    final headers = await _getHeaders(includeAuth: includeAuth);
    return await http.get(Uri.parse(url), headers: headers);
  }

  // POST request
  Future<http.Response> post(
    String url, {
    Object? body,
    bool includeAuth = true,
  }) async {
    final headers = await _getHeaders(includeAuth: includeAuth);
    return await http.post(
      Uri.parse(url),
      headers: headers,
      body: body != null ? json.encode(body) : null,
    );
  }

  // PUT request
  Future<http.Response> put(
    String url, {
    Object? body,
    bool includeAuth = true,
  }) async {
    final headers = await _getHeaders(includeAuth: includeAuth);
    return await http.put(
      Uri.parse(url),
      headers: headers,
      body: body != null ? json.encode(body) : null,
    );
  }

  // DELETE request
  Future<http.Response> delete(String url, {bool includeAuth = true}) async {
    final headers = await _getHeaders(includeAuth: includeAuth);
    return await http.delete(Uri.parse(url), headers: headers);
  }
}

