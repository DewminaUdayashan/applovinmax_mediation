package lk.dew.ads.applovinmax_mediation;

import android.app.Activity;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RewardedAd
        implements MaxRewardedAdListener {
    private MaxRewardedAd rewardedAd;
    private int retryAttempt;
    private ApplovinMaxMediationPlugin instance;

    public RewardedAd( @NonNull ApplovinMaxMediationPlugin instance) {
        this.instance = instance;
    }

    public void createRewardedAd(@NonNull String adUnitId,@NonNull Activity activity) {
        rewardedAd = MaxRewardedAd.getInstance(adUnitId, activity);
        rewardedAd.setListener(this);
        rewardedAd.loadAd();
    }

    public boolean isReady() {
        return rewardedAd.isReady();
    }

    public void showRewardedAd() {
        rewardedAd.showAd();
    }

    // MAX Ad Listener
    @Override
    public void onAdLoaded(final MaxAd maxAd) {
        // Rewarded ad is ready to be shown. rewardedAd.isReady() will now return 'true'
        // Reset retry attempt
        retryAttempt = 0;
        instance.rewardedCallback(maxAd.getAdUnitId(), "onAdLoaded", null);
    }

    @Override
    public void onAdLoadFailed(final String adUnitId, final MaxError error) {
        // Rewarded ad failed to load
        // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)
        HashMap<String, String> err = new HashMap<>();
        err.put("code", String.valueOf(error.getCode()));
        err.put("message", error.getMessage().replace(":", ""));
        instance.rewardedCallback(adUnitId, "onAdLoadFailed", err);

        retryAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rewardedAd.loadAd();
            }
        }, delayMillis);
    }

    @Override
    public void onAdDisplayFailed(final MaxAd maxAd, final MaxError error) {
        // Rewarded ad failed to display. We recommend loading the next ad
        HashMap<String, String> err = new HashMap<>();
        err.put("code", String.valueOf(error.getCode()));
        err.put("message", error.getMessage().replace(":", ""));
        instance.rewardedCallback(maxAd.getAdUnitId(), "onAdDisplayFailed", err);
        rewardedAd.loadAd();
    }

    @Override
    public void onAdDisplayed(final MaxAd maxAd) {
        instance.rewardedCallback(maxAd.getAdUnitId(), "onAdDisplayed", null);
    }

    @Override
    public void onAdClicked(final MaxAd maxAd) {
        instance.rewardedCallback(maxAd.getAdUnitId(), "onAdClicked", null);
    }


    @Override
    public void onAdHidden(final MaxAd maxAd) {
        instance.rewardedCallback(maxAd.getAdUnitId(), "onAdHidden", null);
        // rewarded ad is hidden. Pre-load the next ad
        rewardedAd.loadAd();
    }


    @Override
    public void onRewardedVideoCompleted(final MaxAd maxAd) {
        instance.rewardedCallback(maxAd.getAdUnitId(), "onRewardedVideoCompleted", null);
    }


    @Override
    public void onUserRewarded(final MaxAd maxAd, final MaxReward maxReward) {
        instance.rewardedCallback(maxAd.getAdUnitId(), "onUserRewarded", null);
        // Rewarded ad was displayed and user should receive the reward
    }

    @Override
    public void onRewardedVideoStarted(MaxAd ad) {
        instance.rewardedCallback(ad.getAdUnitId(), "onRewardedVideoStarted", null);
    }
}