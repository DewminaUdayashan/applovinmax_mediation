import 'package:applovinmax_mediation/applovinmax_mediation.dart';
import 'package:applovinmax_mediation/shared/enums.dart';
import 'package:applovinmax_mediation/shared/max_error.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class ApplovinMaxBanner extends StatelessWidget {
  ApplovinMaxBanner({
    required this.size,
    required this.adUnitId,
    this.listner,
    Key? key,
  }) : super(key: key);
  final Map<BannerAdSize, String> sizes = {
    BannerAdSize.banner: 'BANNER',
    BannerAdSize.leader: 'LEADER',
    BannerAdSize.crossPromo: 'XPROMO',
    BannerAdSize.mrec: 'MREC',
    BannerAdSize.adaptive: 'ADAPTIVE',
  };
  final BannerAdSize size;
  final String adUnitId;
  final ApplovinMaxCallback? listner;

  @override
  Widget build(BuildContext context) {
    return AndroidView(
      viewType: '/Banner',
      key: UniqueKey(),
      creationParams: {'size': sizes[size], 'adUnitId': adUnitId},
      creationParamsCodec: const StandardMessageCodec(),
      onPlatformViewCreated: (int vi) {
        _setBannerAdCallbacks(
          adUnitId: adUnitId,
          callbacks: listner,
        );
      },
    );
  }

  static void _setBannerAdCallbacks({
    required String adUnitId,
    ApplovinMaxCallback? callbacks,
  }) {
    print("FLUTTER APPLOVIN : - DART SIDE - Callback listner registered");
    ApplovinMaxMediation.getChannel
        .setMethodCallHandler((MethodCall call) async {
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
