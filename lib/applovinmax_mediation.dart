import 'dart:async';

import 'package:flutter/foundation.dart';

import 'shared/enums.dart';
import 'package:flutter/services.dart';

class ApplovinMaxMediation {
  static const MethodChannel _channel = MethodChannel('applovinmax_mediation');

  @protected
  static MethodChannel get getChannel => _channel;

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  /// pass bool to set applovin sdk verbose logging
  /// to turn on or off
  static Future<void> setVerboseLogging(bool isTurnOn) async {
    await _channel.invokeMethod('setVerboseLogging', isTurnOn);
  }

  /// initialize applovin sdk
  /// return value will be consent dialog state
  /// you should show consent dialog and get user consent if required
  /// [ConsentDialogState] is an enum indicating the relevant state
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
