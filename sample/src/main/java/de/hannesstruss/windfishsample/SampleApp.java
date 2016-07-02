package de.hannesstruss.windfishsample;

import android.app.Application;

import de.hannesstruss.schmutils.timberrr.TimberChristmasTree;
import de.hannesstruss.windfish.library.WindFish;
import timber.log.Timber;

public class SampleApp extends Application {
  @Override public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
      Timber.plant(new TimberChristmasTree("WindFish Sample"));
      WindFish.install(this);
    }

    Timber.d("Launching Sample");
  }
}
