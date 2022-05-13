import 'dart:async';

import 'shared/enums.dart';
import 'package:flutter/services.dart';

class ApplovinMaxMediation {
  static const MethodChannel _channel = MethodChannel('applovinmax_mediation');

  static MethodChannel get getChannel => _channel;

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<ConsentDialogState> initSDK() async {
    final consentDialogState = await _channel.invokeMethod('InitSdk');
    switch (consentDialogState) {
      case 'APPLIES':
        return ConsentDialogState.applies;
      case 'DOES_NOT_APPLY':
        return ConsentDialogState.doesNotApply;
      default:
        return ConsentDialogState.unknown;
    }
  }
}
