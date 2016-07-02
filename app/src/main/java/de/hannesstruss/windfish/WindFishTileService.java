package de.hannesstruss.windfish;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class WindFishTileService extends TileService {
  private WindFishState state;

  @Override public void onCreate() {
    super.onCreate();

    state = ((WindFishApp) getApplicationContext()).state();
  }

  @Override public void onTileAdded() {
    super.onTileAdded();
    updateTile();
  }

  @Override public void onStartListening() {
    super.onStartListening();
    updateTile();
  }

  @Override public void onClick() {
    super.onClick();
    state.toggle();
    updateTile();
  }

  private void updateTile() {
    Tile tile = getQsTile();

    tile.setState(state.isEnabled() ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);

    tile.updateTile();
  }
}
