package lk.dew.ads.applovinmax_mediation;


import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.applovin.adview.AppLovinAdView;
import com.applovin.adview.AppLovinAdViewDisplayErrorCode;
import com.applovin.adview.AppLovinAdViewEventListener;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdClickListener;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;

import java.util.HashMap;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;

public class BannerAdView extends FlutterActivity implements PlatformView, MaxAdViewAdListener {
    final static String TAG = "FLUTTER APPLOVIN : - ";
    final int viewId;
    final MaxAdView bannerView;
    AppLovinAdSize size;
    int height;
    int width;
    String adUnitId;
    final ApplovinMaxMediationPlugin instance;

    public BannerAdView(Context context, HashMap args, ApplovinMaxMediationPlugin instance, int viewId) {
        android.util.Log.d(TAG, "BannerAdView: "+viewId);
        this.instance = instance;
        this.viewId = viewId;
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
        Log.d(TAG, "BannerAdView: SETTING UP TEST CALLBACK");
        instance.callback("02242","Banner load called",null);
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
    public void onAdExpanded(MaxAd ad) {
        Log.d(TAG, "onAdExpanded: APPLOVINMAXISTNER");
    }

    @Override
    public void onAdCollapsed(MaxAd ad) {
        Log.d(TAG, "onAdCollapsed: APPLOVINMAXISTNER");

    }

    @Override
    public void onAdLoaded(MaxAd ad) {
        Log.d(TAG, "onAdLoaded: APPLOVINMAXISTNER");
        instance.callback(ad.getAdUnitId(),"onAdLoaded",null);
    }

    @Override
    public void onAdDisplayed(MaxAd ad) {
        Log.d(TAG, "onAdDisplayed: APPLOVINMAXISTNER");
        instance.callback(ad.getAdUnitId(),"onAdDisplayed",null);
    }

    @Override
    public void onAdHidden(MaxAd ad) {
        Log.d(TAG, "onAdHidden: APPLOVINMAXISTNER");
        instance.callback(ad.getAdUnitId(),"onAdHidden",null);
    }

    @Override
    public void onAdClicked(MaxAd ad) {
        Log.d(TAG, "onAdClicked: APPLOVINMAXISTNER");
        instance.callback(ad.getAdUnitId(),"onAdClicked",null);
    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {
        Log.d(TAG, "onAdLoadFailed: APPLOVINMAXISTNER");
        HashMap err = new HashMap<String,String>();
        err.put("code",error.getCode());
        err.put("message",error.getMessage());
        instance.callback(adUnitId,"onAdLoadFailed",err);
    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
        Log.d(TAG, "onAdDisplayFailed: APPLOVINMAXISTNER");
        HashMap err = new HashMap<String,String>();
        err.put("code",error.getCode());
        err.put("message",error.getMessage());
        instance.callback(adUnitId,"onAdLoadFailed",err);
    }

//    @Override
//    public void adOpenedFullscreen(AppLovinAd ad, AppLovinAdView adView) {
//
//    }
//
//    @Override
//    public void adClosedFullscreen(AppLovinAd ad, AppLovinAdView adView) {
//
//    }
//
//    @Override
//    public void adLeftApplication(AppLovinAd ad, AppLovinAdView adView) {
//
//    }
//
//    @Override
//    public void adFailedToDisplay(AppLovinAd ad, AppLovinAdView adView, AppLovinAdViewDisplayErrorCode code) {
//        Log.d(TAG, "adFailedToDisplay: APPLOVINMAXISTNER 2");
//    }
//
//    @Override
//    public void adClicked(AppLovinAd ad) {
//        Log.d(TAG, "adClicked: APPLOVINMAXISTNER 2");
//    }
//
//    @Override
//    public void adDisplayed(AppLovinAd ad) {
//        Log.d(TAG, "adDisplayed: APPLOVINMAXISTNER 2");
//    }
//
//    @Override
//    public void adHidden(AppLovinAd ad) {
//        Log.d(TAG, "adHidden: APPLOVINMAXISTNER 2");
//    }
//
//    @Override
//    public void adReceived(AppLovinAd ad) {
//        Log.d(TAG, "adReceived: APPLOVINMAXISTNER 2");
//    }
//
//    @Override
//    public void failedToReceiveAd(int errorCode) {
//        Log.d(TAG, "failedToReceiveAd: APPLOVINMAXISTNER 2"); //
//    }
}
