package lk.dew.ads.applovinmax_mediation;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.applovin.sdk.AppLovinPrivacySettings;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinUserService;

import java.util.HashMap;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.StandardMethodCodec;
import io.flutter.plugin.platform.PlatformViewRegistry;

/**
 * ApplovinMaxMediationPlugin
 */
public class ApplovinMaxMediationPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
    final static String TAG = "FLUTTER APPLOVIN : - ";
    private ApplovinMaxMediationPlugin instance;
    public Context context;
    static public MethodChannel channel;
    public Activity activity;
    public FlutterPluginBinding bindingInstance;
    private InterAd interstitialAd;

    public ApplovinMaxMediationPlugin() {
        Log.d(TAG, "================ Applovin Mediation Plugin Initialized ================");
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        context = flutterPluginBinding.getApplicationContext();
        bindingInstance = flutterPluginBinding;
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "applovinmax_mediation", StandardMethodCodec.INSTANCE);
        channel.setMethodCallHandler(this);
        instance = new ApplovinMaxMediationPlugin();
        registerBannerFactory(flutterPluginBinding.getPlatformViewRegistry());
        Log.d(TAG, "onAttachedToEngine: is channel null on initially : " + (channel == null));
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case "getPlatformVersion":
                result.success("Android " + android.os.Build.VERSION.RELEASE);
                break;
            case "InitSdk":
                initApplovinSDK(result);
                break;
            case "setVerboseLogging":
                setVerboseLogging((boolean) call.arguments);
            case "setHasUserConsent":
                setHaseUserConsent((boolean) call.arguments);
            case "createInterstitialAd":
                createInter(call.arguments.toString());
            case "isInterstitialAdReady":
                result.success(isInterReady());
            case "showInterstitialAd":
                showInter(call.arguments);
            default:
                result.notImplemented();
                break;
        }
    }


    private boolean isInterReady() {
        return interstitialAd.isReady();
    }

    private void showInter(@Nullable Object arguments) {
        interstitialAd.showAd(arguments);
    }

    private void createInter(String adUnitId) {
        interstitialAd.createInterstitialAd(activity, adUnitId);
    }

    private void setHaseUserConsent(boolean arguments) {
        AppLovinPrivacySettings.setHasUserConsent(arguments, context);
    }

    private void setVerboseLogging(boolean arguments) {
        AppLovinSdk.getInstance(context).getSettings().setVerboseLogging(arguments);
    }

    private void initApplovinSDK(@NonNull Result result) {
        AppLovinSdk.getInstance(context).setMediationProvider("max");
        AppLovinSdk.initializeSdk(context, config -> {
            switch (config.getConsentDialogState()) {
                case APPLIES:
                    // Show user consent dialog
                    if (activity != null)
                        result.success("APPLIES");
                    break;
                case DOES_NOT_APPLY:
                    // No need to show consent dialog, proceed with initialization
                    result.success("DOES_NOT_APPLY");
                    break;
                default:
                    // Consent dialog state is unknown. Proceed with initialization, but check if the consent
                    // dialog should be shown on the next application initialization
                    result.success("UNKNOWN");
                    break;
            }
        });
    }


    public void callback(String adUnitId, String callback, HashMap<String, String> error) {
    }

    public void rewardedCallback(String adUnitId, String callback, HashMap<String, String> error) {
        Log.d(TAG, "callback: " + adUnitId + ",");
        final HashMap<String, Object> data = new HashMap<>();
        data.put("callback", callback);
        if (error != null)
            data.put("error", error);
        channel.invokeMethod(adUnitId, data, new Result() {
            @Override
            public void success(@Nullable Object result) {
                Log.d(TAG, "success: callback result");
            }

            @Override
            public void error(String errorCode, @Nullable String errorMessage, @Nullable Object errorDetails) {
                Log.d(TAG, "error: callback result" + errorMessage + "\n " + errorDetails);
            }

            @Override
            public void notImplemented() {
                Log.d(TAG, "notImplemented: callback result");
            }
        });
    }

    public void registerBannerFactory(PlatformViewRegistry registry) {
        registry.registerViewFactory("/Banner", new BannerFactory(instance, channel));
    }


    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
        channel.setMethodCallHandler(this);
        interstitialAd = new InterAd(instance);
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {
        channel.setMethodCallHandler(null);
    }


}
