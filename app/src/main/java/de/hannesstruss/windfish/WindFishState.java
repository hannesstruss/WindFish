package de.hannesstruss.windfish;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedHashSet;
import java.util.Set;

public class WindFishState {
  private static final String KEY_ENABLED = "enabled";

  private final SharedPreferences sharedPreferences;
  private final Set<Listener> listeners;

  public WindFishState(Application context) {
    this.sharedPreferences = context.getSharedPreferences("windfish_state.prefs", Context.MODE_PRIVATE);
    listeners = new LinkedHashSet<>();
  }

  public boolean isEnabled() {
    return sharedPreferences.getBoolean(KEY_ENABLED, false);
  }

  public void setEnabled(boolean enabled) {
    sharedPreferences.edit().putBoolean(KEY_ENABLED, enabled).apply();
    
    for (Listener listener : listeners) {
      listener.onStateChanged(enabled);
    }
  }
  
  public void addListener(Listener listener) {
    listeners.add(listener);
  }

  public void toggle() {
    setEnabled(!isEnabled());
  }

  public void removeListener(Listener listener) {
    listeners.remove(listener);
  }

  interface Listener {
    void onStateChanged(boolean isEnabled);
  }
}
