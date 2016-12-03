package de.hannesstruss.windfish;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedHashSet;
import java.util.Set;

class WindFishState {
  private static final String KEY_ENABLED = "enabled";

  private final SharedPreferences sharedPreferences;
  private final Set<Listener> listeners;

  WindFishState(Application context) {
    this.sharedPreferences = context.getSharedPreferences("windfish_state.prefs", Context.MODE_PRIVATE);
    listeners = new LinkedHashSet<>();
  }

  boolean isEnabled() {
    return sharedPreferences.getBoolean(KEY_ENABLED, false);
  }

  private void setEnabled(boolean enabled) {
    sharedPreferences.edit().putBoolean(KEY_ENABLED, enabled).apply();
    
    for (Listener listener : listeners) {
      listener.onStateChanged(enabled);
    }
  }
  
  void addListener(Listener listener) {
    listeners.add(listener);
  }

  void toggle() {
    setEnabled(!isEnabled());
  }

  void removeListener(Listener listener) {
    listeners.remove(listener);
  }

  interface Listener {
    void onStateChanged(boolean isEnabled);
  }
}
