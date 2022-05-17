package lk.dew.ads.applovinmax_mediation;


import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinAdSize;

import java.util.HashMap;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.platform.PlatformView;

public class BannerAdView extends FlutterActivity implements PlatformView {
    private static final String TAG = "Applovin:Banner- ";
    final int viewId;
    final MaxAdView bannerView;
    AppLovinAdSize size;
    int height;
    int width;
    String adUnitId;
    final ApplovinMaxMediationPlugin instance;

    public BannerAdView(Context context, HashMap args, ApplovinMaxMediationPlugin instance, int viewId) {
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
                Log.d(TAG, "onAdExpanded: ");
            }

            @Override
            public void onAdCollapsed(MaxAd ad) {
                Log.d(TAG, "onAdCollapsed: ");

            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                Log.d(TAG, "onAdLoaded: ");
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                Log.d(TAG, "onAdDisplayed: ");
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                Log.d(TAG, "onAdHidden: ");
            }

            @Override
            public void onAdClicked(MaxAd ad) {
                Log.d(TAG, "onAdClicked: ");
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                Log.d(TAG, "onAdLoadFailed: "+error.getMessage());
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                Log.d(TAG, "onAdDisplayFailed: "+error.getMessage());
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
    }
}
