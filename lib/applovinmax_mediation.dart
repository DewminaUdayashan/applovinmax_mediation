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
