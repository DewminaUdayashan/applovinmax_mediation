import 'max_error.dart';

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

abstract class ApplovinMaxRewardedCallback {
  void onAdLoaded();
  void onAdDisplayed();
  void onAdHidden();
  void onAdClicked();
  void onAdExpanded();
  void onAdCollapsed();
  void onRewardedVideoCompleted();
  void onUserRewarded();
  void onRewardedVideoStarted();
  void onAdLoadFailed(MaxError? error);
  void onAdDisplayFailed(MaxError? error);
}
