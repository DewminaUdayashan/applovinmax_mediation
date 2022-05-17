package lk.dew.ads.applovinmax_mediation;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class InterAd
        implements MaxAdListener {
    private static final String TAG = "Applovin:inter : ";
    private MaxInterstitialAd interstitialAd;
    private int retryAttempt;
    private ApplovinMaxMediationPlugin instance;

    public InterAd(@NonNull ApplovinMaxMediationPlugin instance) {
        this.instance = instance;
    }

    public void createInterstitialAd(@NonNull Activity activity, @NonNull String adUnitId) {
        interstitialAd = new MaxInterstitialAd(adUnitId, activity);
        interstitialAd.setListener(this);

        // Load the first ad
        interstitialAd.loadAd();
    }

    public boolean isReady() {
        return interstitialAd.isReady();
    }

    public void showAd(@Nullable Object adUnitId) {
        if (adUnitId == null) interstitialAd.showAd();
        else
            interstitialAd.showAd(adUnitId.toString());
    }


    // MAX Ad Listener
    @Override
    public void onAdLoaded(final MaxAd maxAd) {
        // Interstitial ad is ready to be shown. interstitialAd.isReady() will now return 'true'

        // Reset retry attempt
        retryAttempt = 0;
        Log.d(TAG, "onAdLoaded: ");
    }

    @Override
    public void onAdLoadFailed(final String adUnitId, final MaxError error) {
        // Interstitial ad failed to load
        // AppLovin recommends that you retry with exponentially higher delays up to a maximum delay (in this case 64 seconds)
        Log.d(TAG, "onAdLoadFailed: ");
        retryAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));

        new Handler().postDelayed(() -> interstitialAd.loadAd(), delayMillis);
    }

    @Override
    public void onAdDisplayFailed(final MaxAd maxAd, final MaxError error) {
        // Interstitial ad failed to display. AppLovin recommends that you load the next ad.
        Log.d(TAG, "onAdDisplayFailed: ");
    }

    @Override
    public void onAdDisplayed(final MaxAd maxAd) {
        Log.d(TAG, "onAdDisplayed: ");
    }

    @Override
    public void onAdClicked(final MaxAd maxAd) {
        Log.d(TAG, "onAdClicked: ");
    }

    @Override
    public void onAdHidden(final MaxAd maxAd) {
        Log.d(TAG, "onAdHidden: ");
    }
}