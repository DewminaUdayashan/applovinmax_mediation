import 'dart:async';

import 'package:applovinmax_mediation/shared/enums.dart';
import 'package:applovinmax_mediation/shared/max_error.dart';
import 'package:flutter/services.dart';

class ApplovinMaxMediation {
  static const MethodChannel _channel = MethodChannel('applovinmax_mediation');
  static const MethodChannel bannerChannel = MethodChannel('banner');

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
    Function? onAdDisplayed,
    Function? onAdHidden,
    Function? onAdClicked,
    Function? onAdReceived,
    Function? onAdLeftApplication,
    Function(MaxError? error)? onAdFailedToReceiveAd,
    Function(MaxError? error)? onAdFailedToDisplay,
  }) {
    _channel.setMethodCallHandler((MethodCall call) async {
      print("APPLOVINMAXLISTNER" + " setBannerAdCalbacks Called...");
      print("APPLOVINMAXISTNER " + call.method.toString());
      print("APPLOVINMAXISTNER " + call.arguments.toString());
      //
      if (call.method == adUnitId) {
        switch (call.arguments.get('callback')) {
          case 'onAdLoaded':
            onAdLoaded?.call();
            break;
          case 'onAdDisplayed':
            onAdDisplayed?.call();
            break;
          case 'onAdHidden':
            onAdHidden?.call();
            break;
          case 'onAdClicked':
            onAdClicked?.call();
            break;
          case 'onAdReceived':
            onAdReceived?.call();
            break;
          case 'onAdLeftApplication':
            onAdLeftApplication?.call();
            break;
          case 'onAdLoadFailed':
            onAdFailedToReceiveAd
                ?.call(MaxError.fromMap(call.arguments.get('error')));
            break;
          case 'onAdDisplayFailed':
            onAdFailedToDisplay
                ?.call(MaxError.fromMap(call.arguments.get('error')));
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
