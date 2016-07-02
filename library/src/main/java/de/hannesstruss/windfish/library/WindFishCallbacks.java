package de.hannesstruss.windfish.library;

/**
 * Optional interface. If your Activity implements this, it will be notified about
 * WindFish state changes.
 */
public interface WindFishCallbacks {
  void onWindFishStateChanged(boolean keepScreenOn);
}
