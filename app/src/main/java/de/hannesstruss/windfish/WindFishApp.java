package de.hannesstruss.windfish;

import android.app.Application;

public class WindFishApp extends Application {
  private RxPowerStatus powerStatus;
  private WindFishState state;

  @Override public void onCreate() {
    super.onCreate();

    powerStatus = new RxPowerStatus(this);
    state = new WindFishState(this, powerStatus);
  }

  public WindFishState state() {
    return state;
  }
}
