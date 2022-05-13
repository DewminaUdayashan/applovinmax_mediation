package lk.dew.ads.applovinmax_mediation;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class BannerFactory extends PlatformViewFactory {
    final static String TAG = "FLUTTER APPLOVIN : - ";
    ApplovinMaxMediationPlugin instance;
MethodChannel channel;
    public BannerFactory(ApplovinMaxMediationPlugin instance,MethodChannel channel) {
        super(StandardMessageCodec.INSTANCE);
        this.instance = instance;
        this.channel = channel;
        Log.d(TAG, "BannerFactory: channel was null ? "+ (channel==null));
    }

    @Override
    public PlatformView create(Context context, int viewId, Object args) {
        Log.d(TAG, "create: BannerFactory channel was null in create method ? "+(this.channel==null));
        return new BannerAdView(context, (HashMap) args,instance, viewId,this.channel);
    }
}
