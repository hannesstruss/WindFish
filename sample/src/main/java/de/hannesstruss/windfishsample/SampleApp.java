package de.hannesstruss.windfishsample;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import de.hannesstruss.windfish.library.WindFish;

public class SampleApp extends Application {
  @Override public void onCreate() {
    super.onCreate();

    if (LeakCanary.isInAnalyzerProcess(this)) {
      return;
    }
    LeakCanary.install(this);

    if (BuildConfig.DEBUG) {
      WindFish.install(this);
    }
  }
}
