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
  /// can also set from manifest :- [https://dash.applovin.com/documentation/mediation/android/getting-started/advanced-settings]
  static Future<void> setVerboseLogging(bool isTurnOn) async {
    await _channel.invokeMethod('setVerboseLogging', isTurnOn);
  }

  /// If the user consents, set the user consent flag to [true]
  /// If the user does not consent, set the user consent flag to [false]
  /// Once you set the consent value,
  /// AppLovin will respect that value for the lifetime of your application or until the user consents.
  /// You do not have to set this flag for users who are outside of the European Countries.
  /// If you do set the flag for such users, this will not impact how ads are served to them.
  static Future<void> setHasUserConsent(bool isHasConsent) async {
    await _channel.invokeMethod('setHasUserConsent', isHasConsent);
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
