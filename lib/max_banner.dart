import 'package:applovinmax_mediation/shared/enums.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'shared/max_error.dart';

class ApplovinMaxBanner extends StatelessWidget {
  final Map<BannerAdSize, String> sizes = {
    BannerAdSize.banner: 'BANNER',
    BannerAdSize.leader: 'LEADER',
    BannerAdSize.crossPromo: 'XPROMO',
    BannerAdSize.mrec: 'MREC',
    BannerAdSize.adaptive: 'ADAPTIVE',
  };
  final BannerAdSize size;
  final String adUnitId;

  ApplovinMaxBanner({
    required this.size,
    required this.adUnitId,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return AndroidView(
      viewType: '/Banner',
      key: UniqueKey(),
      creationParams: {'size': sizes[size], 'adUnitId': adUnitId},
      creationParamsCodec: const StandardMessageCodec(),
      onPlatformViewCreated: (int vi) {
        final MethodChannel _channel = MethodChannel('/banner$vi');
        setBannerAdCallbacks(
          adUnitId: adUnitId,
          channel: _channel,
        );
      },
    );
  }

  static void setBannerAdCallbacks({
    required String adUnitId,
    required MethodChannel channel,
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
    channel.setMethodCallHandler((MethodCall call) async {
      print("FLUTTER APPLOVIN : - DART SIDE - " + call.method.toString());
      print("FLUTTER APPLOVIN : - DART SIDE - " + call.arguments.toString());
      print("FLUTTER APPLOVIN : - DART SIDE ad unit id : - " + adUnitId);
      print("FLUTTER APPLOVIN : - DART SIDE ad unit id equals ? : - " +
          (adUnitId == call.method).toString());
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
