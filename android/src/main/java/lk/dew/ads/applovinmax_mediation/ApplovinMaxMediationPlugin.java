package lk.dew.ads.applovinmax_mediation;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;

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
    public MethodChannel channel;
    public Activity activity;
    public FlutterPluginBinding bindingInstance;

   public ApplovinMaxMediationPlugin(){
       Log.d(TAG, "ApplovinMaxMediationPlugin: ================ Applovin Mediation Plugin Initialized ================");
   }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        context = flutterPluginBinding.getApplicationContext();
        bindingInstance = flutterPluginBinding;
        this.channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "applovinmax_mediation", StandardMethodCodec.INSTANCE);
        this.channel.setMethodCallHandler(this);
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
            default:
                result.notImplemented();
                break;
        }
    }

    private void initApplovinSDK(@NonNull Result result) {
        AppLovinSdk.getInstance(context).setMediationProvider("max");
        AppLovinSdk.initializeSdk(context, config -> {
            if (config.getConsentDialogState() == AppLovinSdkConfiguration.ConsentDialogState.APPLIES) {
                // Show user consent dialog
                result.success("APPLIES");
            } else if (config.getConsentDialogState() == AppLovinSdkConfiguration.ConsentDialogState.DOES_NOT_APPLY) {
                // No need to show consent dialog, proceed with initialization
                result.success("DOES_NOT_APPLY");
            } else {
                // Consent dialog state is unknown. Proceed with initialization, but check if the consent
                // dialog should be shown on the next application initialization
                result.success("UNKNOWN");
            }
        });
    }


    public void callback(String adUnitId, String callback, HashMap<String, String> error, ApplovinMaxMediationPlugin ins) {
//        Log.d(TAG, "callback: CALLBACK METHOD CALLED.... unit id : " + (adUnitId) + ", callback : " + (callback) + "," +
//                " error is null : " + (error == null) + ", is channel null ? :- " + (instance.channel == null));
//        final HashMap<String, Object> data = new HashMap<>();
//        data.put("callback", callback);
//        if (error != null) {
//            data.put("error", error);
//        }
        ins.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ins.activity.runOnUiThread(() -> ins.channel.invokeMethod(adUnitId, "data", new Result() {
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

    public void registerBannerFactory(PlatformViewRegistry registry) {
        Log.d(TAG, "registerBannerFactory: - channel is null when registerBannerFactoryCalled ? :- " + (instance.channel == null));
        registry.registerViewFactory("/Banner", new BannerFactory(instance));
//        callback("0001", "testing gasa", null, instance);
    }


    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        Log.d(TAG, "onDetachedFromEngine: ");
//        channel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
        Log.d(TAG, "onAttachedToActivity: ======================================================");
        channel.setMethodCallHandler(this);
//        if (bindingInstance != null)
//            registerBannerFactory(bindingInstance.getPlatformViewRegistry());
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        Log.d(TAG, "onDetachedFromActivityForConfigChanges: ============================================");
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        Log.d(TAG, "onReattachedToActivityForConfigChanges: ============================================");
        activity = binding.getActivity();

    }

    @Override
    public void onDetachedFromActivity() {
        Log.d(TAG, "onDetachedFromActivity: ========================================================");
    }
}
