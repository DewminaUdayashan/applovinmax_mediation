package lk.dew.ads.applovinmax_mediation;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.platform.PlatformViewRegistry;

/** ApplovinMaxMediationPlugin */
public class ApplovinMaxMediationPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  private ApplovinMaxMediationPlugin instance;
  public  Context context;
  private MethodChannel channel;
  public Activity activity;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    context = flutterPluginBinding.getApplicationContext();

    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "applovinmax_mediation");
    channel.setMethodCallHandler(this);
    if (instance == null) {
      instance = new ApplovinMaxMediationPlugin();
    }
    registerBannerFactory(flutterPluginBinding.getPlatformViewRegistry());
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
    AppLovinSdk.getInstance(context).setMediationProvider( "max" );
    AppLovinSdk.initializeSdk(context, config -> {
      if ( config.getConsentDialogState() == AppLovinSdkConfiguration.ConsentDialogState.APPLIES )
      {
        // Show user consent dialog
        result.success("APPLIES");
      }
      else if ( config.getConsentDialogState() == AppLovinSdkConfiguration.ConsentDialogState.DOES_NOT_APPLY )
      {
        // No need to show consent dialog, proceed with initialization
        result.success("DOES_NOT_APPLY");
      }
      else
      {
        // Consent dialog state is unknown. Proceed with initialization, but check if the consent
        // dialog should be shown on the next application initialization
        result.success("UNKNOWN");
      }
    });
  }


  public void registerBannerFactory(PlatformViewRegistry registry) {
    registry.registerViewFactory("/Banner", new BannerFactory(instance));
  }




  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    activity = binding.getActivity();
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

  }
}
