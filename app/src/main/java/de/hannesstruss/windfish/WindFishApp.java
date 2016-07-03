package de.hannesstruss.windfish;

import android.app.Application;

public class WindFishApp extends Application {
  private WindFishState state;

  @Override public void onCreate() {
    super.onCreate();

    state = new WindFishState(this);
  }

  public WindFishState state() {
    return state;
  }
}
