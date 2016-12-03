package de.hannesstruss.windfish;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;

import rx.Observable;
import rx.functions.Func1;

class WindFishState {
  private enum Mode {
    ALWAYS_OFF, /*ON_WHEN_ON_POWER,*/ ALWAYS_ON
  }

  private static final String KEY_MODE = "mode";

  private static final Func1<String, Mode> TO_MODE = Mode::valueOf;

  private final Preference<String> mode;

  WindFishState(Application context) {
    SharedPreferences sharedPreferences =
        context.getSharedPreferences("windfish_state.prefs", Context.MODE_PRIVATE);
    RxSharedPreferences rxPrefs = RxSharedPreferences.create(sharedPreferences);
    mode = rxPrefs.getString(KEY_MODE, Mode.ALWAYS_OFF.name());
  }

  Observable<Boolean> isEnabled() {
    return mode.asObservable()
        .map(Mode::valueOf)
        .map(mode -> mode == Mode.ALWAYS_ON);
  }

  void toggle() {
    mode.asObservable()
        .first()
        .map(TO_MODE)
        .subscribe(mode -> {
          Mode nextMode;
          if (mode == Mode.ALWAYS_ON) {
            nextMode = Mode.ALWAYS_OFF;
          } else {
            nextMode = Mode.ALWAYS_ON;
          }
          setMode(nextMode);
        });
  }

  private void setMode(Mode nextMode) {
    mode.set(nextMode.name());
  }
}
