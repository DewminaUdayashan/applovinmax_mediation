package lk.dew.ads.applovinmax_mediation;


import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinAdSize;

import java.util.HashMap;
import java.util.Objects;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

public class BannerAdView extends FlutterActivity implements PlatformView {
    final static String TAG = "FLUTTER APPLOVIN : - ";
    final int viewId;
    final MaxAdView bannerView;
    AppLovinAdSize size;
    int height;
    int width;
    String adUnitId;
    final ApplovinMaxMediationPlugin instance;

    public BannerAdView(Context context, HashMap args, ApplovinMaxMediationPlugin instance, int viewId) {
        Log.d(TAG, "BannerAdView: The instance came from the params is null ? :- " + (instance == null));
        this.instance = instance;
        this.viewId = viewId;
        this.width = ViewGroup.LayoutParams.MATCH_PARENT;
        try {
            this.adUnitId = args.get("adUnitId").toString();
        } catch (Exception e) {
            this.adUnitId = "UNIT_ID";
        }

        this.bannerView = new MaxAdView(adUnitId, context);
        try {
            if (args.get("size").equals("ADAPTIVE")) {
                Log.d(TAG, "BannerAdView: ADAPTIVE BANNER CALLED");
                height = MaxAdFormat.BANNER.getAdaptiveSize(instance.activity).getHeight();
                bannerView.setExtraParameter("adaptive_banner", "true");
            } else {
                this.height = getResources().getDimensionPixelSize(isTablet(context) ? 90 : 50);
            }
            this.size = AppLovinAdSize.fromString(args.get("size").toString());
        } catch (Exception e) {
            this.size = AppLovinAdSize.BANNER;
        }
        bannerView.setLayoutParams(new FrameLayout.LayoutParams(size.getWidth(), size.getWidth()));
        bannerView.setGravity(Gravity.CENTER);
        this.bannerView.loadAd();
        bannerView.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd ad) {
                instance.callback(adUnitId, "onAdExpanded", null, instance);
                Log.d(TAG, "onAdExpanded: ");
            }

            @Override
            public void onAdCollapsed(MaxAd ad) {
                callback(adUnitId, "onAdCollapsed", null, instance);
                Log.d(TAG, "onAdCollapsed: ");
            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                Log.d(TAG, "onAdLoaded: ");
                callback(adUnitId, "onAdLoaded", null, instance);
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                Log.d(TAG, "onAdDisplayed: instance state " + (instance == null));
                callback(adUnitId, "onAdDisplayed", null, instance);
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                callback(adUnitId, "onAdHidden", null, instance);
                Log.d(TAG, "onAdHidden: ");
            }

            @Override
            public void onAdClicked(MaxAd ad) {
                callback(adUnitId, "onAdClicked", null, instance);
                Log.d(TAG, "onAdClicked: ");
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                Log.d(TAG, "onAdLoadFailed: ");
                HashMap<String, String> err = new HashMap<>();
                err.put("code", String.valueOf(error.getCode()));
                err.put("message", error.getMessage());
               callback(adUnitId, "onAdLoadFailed", err, instance);
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                Log.d(TAG, "onAdDisplayFailed: ");
                HashMap<String, String> err = new HashMap<>();
                err.put("code", String.valueOf(error.getCode()));
                err.put("message", error.getMessage());
                callback(adUnitId, "onAdDisplayFailed", err, instance);
            }
        });
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    @Override
    public View getView() {
        return bannerView;
    }

    @Override
    public void dispose() {
        bannerView.destroy();
        instance.callback(adUnitId, "onAdViewDisposed", null, instance);
    }


    public void callback(String adUnitId, String callback, HashMap<String, String> error, ApplovinMaxMediationPlugin ins) {
        Log.d(TAG, "callback: CALLBACK METHOD CALLED.... unit id : " + (adUnitId) + ", callback : " + (callback) + "," +
                " error is null : " + (error == null) + ", is channel null ? :- " + (ins.channel == null));
        final HashMap<String, Object> data = new HashMap<>();
        data.put("callback", callback);
        if (error != null) {
            data.put("error", error);
//        }
            ins.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ins.activity.runOnUiThread(() -> ins.channel.invokeMethod(adUnitId, "data", new MethodChannel.Result() {
                        @Override
                        public void success(@Nullable Object result) {
                            Log.d(TAG, "success: callback result");
                        }

                        @Override
                        public void error(String errorCode, @Nullable String errorMessage, @Nullable Object errorDetails) {
                            Log.d(TAG, "error: callback result");
                        }

                        @Override
                        public void notImplemented() {
                            Log.d(TAG, "notImplemented: callback result");
                        }
                    }));
                }
            });
        }
    }
//        if (channel != null)
//            new Handler(Looper.getMainLooper()).post(() -> channel.invokeMethod(adUnitId, data, new Result() {
//                @Override
//                public void success(@Nullable Object result) {
//                    Log.d(TAG, "success: callback result");
//                }
//
//                @Override
//                public void error(String errorCode, @Nullable String errorMessage, @Nullable Object errorDetails) {
//                    Log.d(TAG, "error: callback result");
//                }
//
//                @Override
//                public void notImplemented() {
//                    Log.d(TAG, "notImplemented: callback result");
//                }
//            }));
//        else Log.d(TAG, "callback: CHANNEL WAS NULL WHEN TRYING TO INVOKE METHOD");


}
