import 'package:flutter/material.dart';

class DriverDetailsScreen extends StatefulWidget {
  final String email;
  final String password;
  final String phoneNumber;
  final String firstName;
  final String lastName;

  const DriverDetailsScreen({
    super.key,
    required this.email,
    required this.password,
    required this.phoneNumber,
    required this.firstName,
    required this.lastName,
  });

  @override
  State<DriverDetailsScreen> createState() => _DriverDetailsScreenState();
}

class _DriverDetailsScreenState extends State<DriverDetailsScreen> {
  final _formKey = GlobalKey<FormState>();
  final _licenseNumberController = TextEditingController();
  final _vehicleModelController = TextEditingController();
  final _vehiclePlateController = TextEditingController();

  @override
  void dispose() {
    _licenseNumberController.dispose();
    _vehicleModelController.dispose();
    _vehiclePlateController.dispose();
    super.dispose();
  }

  void _handleContinue() {
    if (_formKey.currentState!.validate()) {
      // Return to previous screen with driver details
      Navigator.of(context).pop({
        'licenseNumber': _licenseNumberController.text.trim(),
        'vehicleModel': _vehicleModelController.text.trim(),
        'vehiclePlate': _vehiclePlateController.text.trim(),
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Driver Information'),
      ),
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(24.0),
          child: Form(
            key: _formKey,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                // Header
                Text(
                  'Please provide your driver and vehicle information',
                  style: Theme.of(context).textTheme.titleMedium,
                ),
                const SizedBox(height: 8),
                Text(
                  'This information is required to register as a driver.',
                  style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                        color: Colors.grey[600],
                      ),
                ),
                const SizedBox(height: 32),

                // License Number
                TextFormField(
                  controller: _licenseNumberController,
                  decoration: const InputDecoration(
                    labelText: 'License Number',
                    hintText: 'e.g., DL123456789',
                    prefixIcon: Icon(Icons.badge_outlined),
                    border: OutlineInputBorder(),
                    helperText: 'Enter your driving license number',
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return 'Please enter your license number';
                    }
                    if (value.length < 5) {
                      return 'License number must be at least 5 characters';
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 16),

                // Vehicle Model
                TextFormField(
                  controller: _vehicleModelController,
                  decoration: const InputDecoration(
                    labelText: 'Vehicle Model',
                    hintText: 'e.g., Toyota Camry',
                    prefixIcon: Icon(Icons.directions_car_outlined),
                    border: OutlineInputBorder(),
                    helperText: 'Enter your vehicle make and model',
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return 'Please enter your vehicle model';
                    }
                    if (value.length < 2) {
                      return 'Vehicle model must be at least 2 characters';
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 16),

                // License Plate
                TextFormField(
                  controller: _vehiclePlateController,
                  decoration: const InputDecoration(
                    labelText: 'License Plate',
                    hintText: 'e.g., ABC1234',
                    prefixIcon: Icon(Icons.confirmation_number_outlined),
                    border: OutlineInputBorder(),
                    helperText: 'Enter your vehicle license plate number',
                  ),
                  textCapitalization: TextCapitalization.characters,
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return 'Please enter your license plate';
                    }
                    if (value.length < 3) {
                      return 'License plate must be at least 3 characters';
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 32),

                // Continue button
                ElevatedButton(
                  onPressed: _handleContinue,
                  style: ElevatedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(vertical: 16),
                  ),
                  child: const Text(
                    'Continue',
                    style: TextStyle(fontSize: 16),
                  ),
                ),
                const SizedBox(height: 16),

                // Back button
                TextButton(
                  onPressed: () {
                    Navigator.of(context).pop();
                  },
                  child: const Text('Go Back'),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

