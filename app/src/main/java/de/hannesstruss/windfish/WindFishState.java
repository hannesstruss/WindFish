package de.hannesstruss.windfish;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;

import rx.Observable;
import rx.functions.Func1;

class WindFishState {
  private final RxPowerStatus powerStatus;

  private enum Mode {
    ALWAYS_OFF, ON_WHEN_CHARGING, ALWAYS_ON
  }

  private static final String KEY_MODE = "mode";

  private static final Func1<String, Mode> TO_MODE = (name) -> {
    try {
      return Mode.valueOf(name);
    } catch (IllegalArgumentException e) {
      return Mode.ALWAYS_OFF;
    }
  };

  private final Preference<String> mode;

  WindFishState(Application context, RxPowerStatus powerStatus) {
    this.powerStatus = powerStatus;

    SharedPreferences sharedPreferences =
        context.getSharedPreferences("windfish_state.prefs", Context.MODE_PRIVATE);
    RxSharedPreferences rxPrefs = RxSharedPreferences.create(sharedPreferences);
    mode = rxPrefs.getString(KEY_MODE, Mode.ALWAYS_OFF.name());
  }

  Observable<Boolean> isEnabled() {
    Observable<Mode> modes = mode.asObservable().map(Mode::valueOf);
    Observable<Boolean> powerConnected = powerStatus.isCharging();

    return Observable.combineLatest(modes, powerConnected, (mode, isCharging) -> {
      if (mode == Mode.ALWAYS_ON) {
        return true;
      } else if (mode == Mode.ALWAYS_OFF) {
        return false;
      } else {
        return isCharging;
      }
    });
  }

  void toggle() {
    mode.asObservable()
        .first()
        .map(TO_MODE)
        .subscribe(mode -> {
          Mode nextMode;
          if (mode == Mode.ALWAYS_OFF) {
            nextMode = Mode.ON_WHEN_CHARGING;
          } else if (mode == Mode.ON_WHEN_CHARGING) {
            nextMode = Mode.ALWAYS_ON;
          } else if (mode == Mode.ALWAYS_ON) {
            nextMode = Mode.ALWAYS_OFF;
          } else {
            throw new AssertionError();
          }
          setMode(nextMode);
        });
  }

  private void setMode(Mode nextMode) {
    mode.set(nextMode.name());
  }
}
