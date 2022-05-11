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
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdClickListener;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;

import java.util.HashMap;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.platform.PlatformView;

public class BannerAdView extends FlutterActivity implements PlatformView, MaxAdListener {
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
        instance.callback(adUnitId,"dispose",null);
        
    }

    @Override
    public void onAdLoaded(MaxAd ad) {
        instance.callback(ad.getAdUnitId(),"onAdLoaded",null);
        Log.d(TAG, "onAdLoaded: APPLOVINMAXLISTNER");
    }

    @Override
    public void onAdDisplayed(MaxAd ad) {
        //don't use
    }

    @Override
    public void onAdHidden(MaxAd ad) {
        //don't use
    }

    @Override
    public void onAdClicked(MaxAd ad) {
        instance.callback(ad.getAdUnitId(),"onAdClicked",null);
        Log.d(TAG, "onAdClicked: APPLOVINMAXLISTNER");
    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {
        final HashMap other = new HashMap<String,String>();
        other.put("code",error.getCode());
        other.put("message",error.getMessage());
        instance.callback(adUnitId,"onAdLoadFailed", other);
        Log.d(TAG, "onAdLoadFailed: APPLOVINMAXLISTNER");
    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
        final HashMap other = new HashMap<String,String>();
        other.put("code",error.getCode());
        other.put("message",error.getMessage());
        instance.callback(ad.getAdUnitId(),"onAdDisplayFailed",other);
        Log.d(TAG, "onAdDisplayFailed: APPLOVINMAXLISTNER");
    }
}
