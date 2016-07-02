package de.hannesstruss.windfish.library;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedHashMap;
import java.util.Map;

public class WindFish {
  public static void install(Application application) {
    WindFish windFish = new WindFish();
    application.registerActivityLifecycleCallbacks(windFish.callbacks());
  }

  private final Map<Activity, ActivityCompanion> companions;

  WindFish() {
    companions = new LinkedHashMap<>();
  }

  Application.ActivityLifecycleCallbacks callbacks() {
    return new EmptyActivityLifecycleCallbacks() {
      @Override public void onActivityResumed(Activity activity) {
        ActivityCompanion companion = new ActivityCompanion();
        companion.attach(activity);
        companions.put(activity, companion);
      }

      @Override public void onActivityPaused(Activity activity) {
        ActivityCompanion companion = companions.get(activity);
        if (companion != null) {
          companions.remove(activity);
          companion.destroy();
        }
      }
    };
  }
}
