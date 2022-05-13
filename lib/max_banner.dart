import 'dart:convert';

import 'applovinmax_mediation.dart';
import 'shared/applovin_max_callback.dart';
import 'shared/enums.dart';
import 'shared/max_error.dart';
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

  /// banner ad type [BannerAdSize enum]
  final BannerAdSize size;

  /// ad unit id from the applovin ad unit
  final String adUnitId;

  /// if need to trigger function for ad callback
  /// send instance of a concrete class of [ApplovinMaxCallback] class.
  /// [ApplovinMaxCallback] is an abstract class
  /// create new class and extends it from [ApplovinMaxCallback] to
  /// override its callback methods
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
    print('Flutter Applovin : - Dart side ad unit id from dart $adUnitId');

    ApplovinMaxMediation.getChannel
        .setMethodCallHandler((MethodCall call) async {
      print('Flutter Applovin : - Dart side ' + call.method.toString());
      print('Flutter Applovin : - Dart side ' + call.arguments.toString());
      print('Flutter Applovin : - Dart side ' + adUnitId.toString());
      print(
          'Flutter Applovin : - Dart side ad unit id from java ${call.method}');
      if (call.method == adUnitId) {
        switch (call.arguments['callback']) {
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
            final error = jsonEncode(call.arguments['error']);
            callbacks?.onAdLoadFailed.call(MaxError.fromMap(jsonDecode(error)));
            break;
          case 'onAdDisplayFailed':
            final error = jsonEncode(call.arguments['error']);
            callbacks?.onAdDisplayFailed
                .call(MaxError.fromMap(jsonDecode(error)));
            break;
          default:
            break;
        }
      } else {
        debugPrint("FLUTTER APPLOVIN :- Ad unit id dosent match");
      }
    });
  }
}
