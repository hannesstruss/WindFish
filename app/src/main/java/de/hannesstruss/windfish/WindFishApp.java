package de.hannesstruss.windfish;

import android.app.Application;

import de.hannesstruss.schmutils.timberrr.TimberChristmasTree;
import timber.log.Timber;

public class WindFishApp extends Application {
  private WindFishState state;

  @Override public void onCreate() {
    super.onCreate();

    state = new WindFishState(this);

    if (BuildConfig.DEBUG) {
      Timber.plant(new TimberChristmasTree("WindFish Tile"));
    }

    Timber.d("Launching WindFish");
  }

  public WindFishState state() {
    return state;
  }
}
