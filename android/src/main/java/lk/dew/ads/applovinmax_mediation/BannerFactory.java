package lk.dew.ads.applovinmax_mediation;


import android.content.Context;

import java.util.HashMap;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class BannerFactory extends PlatformViewFactory {
ApplovinMaxMediationPlugin instance;
MethodChannel channel;
    public BannerFactory(ApplovinMaxMediationPlugin instance, MethodChannel channel) {
        super(StandardMessageCodec.INSTANCE);
        this.instance = instance;
        this.channel = channel;
    }

    @Override
    public PlatformView create(Context context, int viewId, Object args) {
        return new BannerAdView(context, (HashMap) args,instance, viewId,channel);
    }
}
