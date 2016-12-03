package de.hannesstruss.windfish;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import rx.Observable;
import rx.Subscription;

public class WindFishTileService extends TileService {
  private WindFishState state;
  private Observable<Boolean> isEnabled;
  private Subscription stateSubscription;

  @Override public void onCreate() {
    super.onCreate();

    state = ((WindFishApp) getApplicationContext()).state();
    isEnabled = state
        .isEnabled()
        .startWith(false)
        .replay(1)
        .refCount();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    if (stateSubscription != null) {
      stateSubscription.unsubscribe();
    }
  }

  @Override public void onTileAdded() {
    super.onTileAdded();
  }

  @Override public void onStartListening() {
    super.onStartListening();
    stateSubscription = isEnabled.subscribe(this::updateTile);
  }

  @Override public void onClick() {
    super.onClick();
    state.toggle();
  }

  private void updateTile(boolean isEnabled) {
    Tile tile = getQsTile();

    tile.setState(isEnabled ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);

    tile.updateTile();
  }
}
