import 'dart:async';

import 'package:applovinmax_mediation/shared/enums.dart';
import 'package:applovinmax_mediation/shared/max_error.dart';
import 'package:flutter/services.dart';

class ApplovinMaxMediation {
  static const MethodChannel _channel = MethodChannel('applovinmax_mediation');

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

  static void setBannerAdCallbacks({
    required String adUnitId,
    Function? onAdLoaded,
    Function? onAdClicked,
    Function(MaxError? error)? onAdLoadFailed,
    Function(MaxError? error)? onAdDisplayFailed,
    Function? onDispose,
  }) {
    _channel.setMethodCallHandler((MethodCall call) async {
      print("APPLOVINMAXLISTNER" + call.method);
      if (call.method == adUnitId) {
        switch (call.arguments['callback']) {
          case 'dispose':
            onDispose?.call();
            break;
          case 'onAdLoaded':
            onAdLoaded?.call();
            break;
          case 'onAdClicked':
            onAdClicked?.call();
            break;
          case 'onAdLoadFailed':
            onAdLoadFailed?.call(MaxError.fromMap(call.arguments['error']));
            break;
          case 'onAdDisplayFailed':
            onAdDisplayFailed?.call(MaxError.fromMap(call.arguments['error']));
            break;
          default:
            break;
        }
      }
    });
  }
}

// typedef AdCallback = void Function({
//   Function? onAdReceived,
//   Function? onDailedToReceiveAd,
//   Function? onAdOpenedFullscreen,
//   Function? onAdClosedFullscreen,
//   Function? onAdLeftApplication,
//   Function? onAdFailedToDisplay,
//   Function? onAdClicked,
//   Function? onAdDisplayed,
//   Function? onAdHidden,
//   Function? onDispose,
// });
