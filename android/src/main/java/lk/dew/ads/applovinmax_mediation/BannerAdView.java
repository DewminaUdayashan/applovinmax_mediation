package lk.dew.ads.applovinmax_mediation;


import android.content.Context;
import android.content.res.Configuration;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.applovin.adview.AppLovinAdView;
import com.applovin.adview.AppLovinAdViewDisplayErrorCode;
import com.applovin.adview.AppLovinAdViewEventListener;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdClickListener;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinSdkUtils;

import java.util.HashMap;

import io.flutter.Log;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.platform.PlatformView;

public class BannerAdView extends FlutterActivity implements PlatformView, AppLovinAdClickListener, AppLovinAdDisplayListener,
        AppLovinAdViewEventListener, AppLovinAdLoadListener {
    final static String TAG = "FLUTTER APPLOVIN : BANNER - ";
    final MaxAdView bannerView;
    AppLovinAdSize size;
    int height;
    int width;
    String adUnitId;
    ApplovinMaxMediationPlugin instance;


    public BannerAdView(Context context, HashMap args, ApplovinMaxMediationPlugin instance) {
        this.instance = instance;
        this.width = ViewGroup.LayoutParams.MATCH_PARENT;
        try {
            this.adUnitId = args.get("UnitId").toString();
        } catch (Exception e) {
            this.adUnitId = "UNIT_ID";
        }

        this.bannerView = new MaxAdView(adUnitId, context);
        try {
            if(args.get("size")=="ADAPTIVE"){
                height= MaxAdFormat.BANNER.getAdaptiveSize(instance.activity).getHeight();
                bannerView.setExtraParameter( "adaptive_banner", "true" );
            }else{
                this.height = getResources().getDimensionPixelSize(isTablet(context)?90:50);
            }
            this.size = AppLovinAdSize.fromString(args.get("size").toString());
        } catch (Exception e) {
            this.size = AppLovinAdSize.BANNER;
        }
        bannerView.setLayoutParams( new FrameLayout.LayoutParams(size.getWidth(),size.getWidth()));
        bannerView.setGravity(Gravity.CENTER);
        this.bannerView.loadAd();
    }

    public boolean isTablet(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.DONUT) {
            boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
            boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
            return (xlarge || large);
        }
        return false;
    }

    @Override
    public View getView() {
        return bannerView;
    }

    @Override
    public void dispose() {
        bannerView.destroy();
    }

    @Override
    public void adReceived(AppLovinAd ad) {

    }

    @Override
    public void failedToReceiveAd(int errorCode) {
        Log.e(TAG, "Failed to receive Ad with code : " + errorCode);
    }

    @Override
    public void adOpenedFullscreen(AppLovinAd ad, AppLovinAdView adView) {

    }

    @Override
    public void adClosedFullscreen(AppLovinAd ad, AppLovinAdView adView) {

    }

    @Override
    public void adLeftApplication(AppLovinAd ad, AppLovinAdView adView) {

    }

    @Override
    public void adFailedToDisplay(AppLovinAd ad, AppLovinAdView adView, AppLovinAdViewDisplayErrorCode code) {

    }

    @Override
    public void adClicked(AppLovinAd ad) {

    }

    @Override
    public void adDisplayed(AppLovinAd ad) {

    }

    @Override
    public void adHidden(AppLovinAd ad) {

    }
}
