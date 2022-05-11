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

abstract class AdListener {
  void onLoad();
  void onDispose();
}

class MyAdListner extends AdListener {
  @override
  void onDispose() {
    // TODO: implement onDispose
  }

  @override
  void onLoad() {
    // TODO: implement onLoad
  }
}
