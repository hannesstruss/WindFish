package de.hannesstruss.windfishsample;

import android.app.Application;

import de.hannesstruss.windfish.library.WindFish;

public class SampleApp extends Application {
  @Override public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
      WindFish.install(this);
    }
  }
}
