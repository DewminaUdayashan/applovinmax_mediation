package lk.dew.ads.applovinmax_mediation;


import android.content.Context;

import java.util.HashMap;

import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class BannerFactory extends PlatformViewFactory {
ApplovinMaxMediationPlugin instance;
    public BannerFactory(ApplovinMaxMediationPlugin instance) {
        super(StandardMessageCodec.INSTANCE);
        this.instance = instance;
    }

    @Override
    public PlatformView create(Context context, int viewId, Object args) {
        return new BannerAdView(context, (HashMap) args,instance);
    }
}
