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
