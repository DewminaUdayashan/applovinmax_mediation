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
