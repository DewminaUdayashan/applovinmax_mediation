import 'dart:async';

import 'package:applovinmax_mediation/shared/enums.dart';
import 'package:applovinmax_mediation/shared/max_error.dart';
import 'package:flutter/services.dart';

class ApplovinMaxMediation {
  static const MethodChannel _channel = MethodChannel('applovinmax_mediation');
  static const MethodChannel bannerChannel = MethodChannel('banner_channel');
  static const EventChannel _eventChannel = EventChannel('banner_channel');

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
    Function? onAdExpanded,
    Function? onAdCollapsed,
    Function(MaxError? error)? onAdLoadFailed,
    Function(MaxError? error)? onAdDisplayFailed,
  }) {
    print("FLUTTER APPLOVIN : - DART SIDE - Callback listner registered");
    bannerChannel.setMethodCallHandler((MethodCall call) async {
      print("FLUTTER APPLOVIN : - DART SIDE - " + call.method.toString());
      print("FLUTTER APPLOVIN : - DART SIDE - " + call.arguments.toString());
      print("FLUTTER APPLOVIN : - DART SIDE ad unit id : - " + adUnitId);
      print("FLUTTER APPLOVIN : - DART SIDE ad unit id equals ? : - " +
          (adUnitId == call.method).toString());

      _eventChannel.receiveBroadcastStream().listen((event) {
        print('FLUTTER APPLOVIN : - DART SIDE - EVENT  $event');
      });
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
          case 'onAdExpanded':
            onAdExpanded?.call();
            break;
          case 'onAdCollapsed':
            onAdCollapsed?.call();
            break;
          case 'onAdLoadFailed':
            onAdLoadFailed?.call(MaxError.fromMap(call.arguments.get('error')));
            break;
          case 'onAdDisplayFailed':
            onAdDisplayFailed
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
