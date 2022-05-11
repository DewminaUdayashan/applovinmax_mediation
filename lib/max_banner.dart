import 'package:applovinmax_mediation/shared/enums.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

typedef ApplovinAdListener = void Function();

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

  ApplovinMaxBanner({required this.size, required this.adUnitId, Key? key})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return AndroidView(
      viewType: '/Banner',
      key: UniqueKey(),
      creationParams: {'size': sizes[size], 'adUnitId': adUnitId},
      creationParamsCodec: const StandardMessageCodec(),
    );
  }
}
