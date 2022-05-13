import 'dart:core';

import 'package:applovinmax_mediation/shared/max_error.dart';
import 'package:flutter/cupertino.dart';

enum BannerAdSize {
  banner,
  adaptive,
  leader,
  mrec,
  crossPromo,
}

enum ConsentDialogState {
  applies,
  doesNotApply,
  unknown,
}

enum AppLovinAdListener {
  adReceived,
}

abstract class ApplovinMaxCallback {
  void onAdLoaded();
  void onAdDisplayed();
  void onAdHidden();
  void onAdClicked();
  void onAdExpanded();
  void onAdCollapsed();
  void onAdLoadFailed(MaxError? error);
  void onAdDisplayFailed(MaxError? error);
}

class ApplovinListener extends ApplovinMaxCallback {
  @override
  onAdClicked() {
    print('flutter applovin : - plugin on ad clicked');
  }

  @override
  onAdCollapsed() {
    // TODO: implement onAdCollapsed
    throw UnimplementedError();
  }

  @override
  onAdDisplayFailed(MaxError? error) {
    // TODO: implement onAdDisplayFailed
    throw UnimplementedError();
  }

  @override
  void onAdDisplayed() {
    // TODO: implement onAdDisplayed
    print('flutter applovin : - plugin on ad displayed');
  }

  @override
  onAdExpanded() {
    // TODO: implement onAdExpanded
    throw UnimplementedError();
  }

  @override
  onAdHidden() {
    // TODO: implement onAdHidden
    throw UnimplementedError();
  }

  @override
  onAdLoadFailed(MaxError? error) {
    // TODO: implement onAdLoadFailed
    throw UnimplementedError();
  }

  @override
  void onAdLoaded() {
    print('flutter applovin : - plugin on ad loaded');
  }
}
