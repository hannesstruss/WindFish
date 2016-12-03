package de.hannesstruss.windfish;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;

import rx.Observable;

class WindFishState {
  private static final String KEY_ENABLED = "enabled";

  private final Preference<Boolean> enabled;

  WindFishState(Application context) {
    SharedPreferences sharedPreferences =
        context.getSharedPreferences("windfish_state.prefs", Context.MODE_PRIVATE);
    RxSharedPreferences rxPrefs = RxSharedPreferences.create(sharedPreferences);
    enabled = rxPrefs.getBoolean(KEY_ENABLED, false);
  }

  Observable<Boolean> isEnabled() {
    return enabled.asObservable();
  }

  void toggle() {
    //noinspection ConstantConditions
    enabled.set(!enabled.get());
  }
}
