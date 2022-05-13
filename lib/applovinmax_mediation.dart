import 'dart:async';

import 'package:applovinmax_mediation/shared/enums.dart';
import 'package:applovinmax_mediation/shared/max_error.dart';
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

  static void setBannerAdCallbacks({
    required String adUnitId,
    // Function? onAdLoaded,
    // Function? onAdDisplayed,
    // Function? onAdHidden,
    // Function? onAdClicked,
    // Function? onAdExpanded,
    // Function? onAdCollapsed,
    // Function(MaxError? error)? onAdLoadFailed,
    // Function(MaxError? error)? onAdDisplayFailed,
    ApplovinMaxCallback? callbacks,
  }) {
    print("FLUTTER APPLOVIN : - DART SIDE - Callback listner registered");
    _channel.setMethodCallHandler((MethodCall call) async {
      print("FLUTTER APPLOVIN : - DART SIDE - " + call.method.toString());
      print("FLUTTER APPLOVIN : - DART SIDE - " + call.arguments.toString());
      print("FLUTTER APPLOVIN : - DART SIDE ad unit id : - " + adUnitId);
      print("FLUTTER APPLOVIN : - DART SIDE ad unit id equals ? : - " +
          (adUnitId == call.method).toString());

      //
      if (call.method == adUnitId) {
        switch (call.arguments.get('callback')) {
          case 'onAdLoaded':
            callbacks?.onAdLoaded.call();
            break;
          case 'onAdDisplayed':
            callbacks?.onAdDisplayed.call();
            break;
          case 'onAdHidden':
            callbacks?.onAdHidden.call();
            break;
          case 'onAdClicked':
            callbacks?.onAdClicked.call();
            break;
          case 'onAdExpanded':
            callbacks?.onAdExpanded.call();
            break;
          case 'onAdCollapsed':
            callbacks?.onAdCollapsed.call();
            break;
          case 'onAdLoadFailed':
            callbacks?.onAdLoadFailed
                .call(MaxError.fromMap(call.arguments.get('error')));
            break;
          case 'onAdDisplayFailed':
            callbacks?.onAdDisplayFailed
                .call(MaxError.fromMap(call.arguments.get('error')));
            break;
          default:
            break;
        }
      } else {
        print("FLUTTER APPLOVIN : - DART SIDE - Ad unit id dosent match");
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
