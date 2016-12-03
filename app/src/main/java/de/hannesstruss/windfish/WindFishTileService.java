package de.hannesstruss.windfish;

import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import rx.Subscription;

public class WindFishTileService extends TileService {
  private WindFishState state;
  private Subscription stateSubscription;

  @Override public void onCreate() {
    super.onCreate();

    state = ((WindFishApp) getApplicationContext()).state();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    if (stateSubscription != null) {
      stateSubscription.unsubscribe();
    }
  }

  @Override public void onStartListening() {
    super.onStartListening();
    stateSubscription = state.mode().subscribe(this::updateTile);
  }

  @Override public void onClick() {
    super.onClick();
    state.toggle();
  }

  private void updateTile(WindFishState.Mode mode) {
    Tile tile = getQsTile();

    tile.setState(mode != WindFishState.Mode.ALWAYS_OFF
        ? Tile.STATE_ACTIVE
        : Tile.STATE_INACTIVE);

    int iconRes = mode == WindFishState.Mode.ON_WHEN_CHARGING
        ? R.drawable.tile_power
        : R.drawable.tile;
    tile.setIcon(Icon.createWithResource(getApplicationContext(), iconRes));

    tile.updateTile();
  }
}
