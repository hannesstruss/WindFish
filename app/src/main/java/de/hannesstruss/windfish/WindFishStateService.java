package de.hannesstruss.windfish;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

import de.hannesstruss.windfish.common.Constants;
import timber.log.Timber;

public class WindFishStateService extends Service implements WindFishState.Listener {
  private final Messenger messenger = new Messenger(new IncomingHandler());
  private final List<Messenger> clients = new ArrayList<>();

  private WindFishState state;

  @Override public void onCreate() {
    super.onCreate();
    state = ((WindFishApp) getApplicationContext()).state();
    state.addListener(this);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    state.removeListener(this);
  }

  @Override public void onStateChanged(boolean isEnabled) {
    notifyAllClients(isEnabled);
  }

  class IncomingHandler extends Handler {
    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
      Timber.d("%s got message: %s", WindFishStateService.class.getSimpleName(), msg.what);

      switch (msg.what) {
        case Constants.MSG_REGISTER_CLIENT:
          try {
            notifyClient(state.isEnabled(), msg.replyTo);
            if (!clients.contains(msg.replyTo)) {
              clients.add(msg.replyTo);
            }
          } catch (RemoteException e) {
            Timber.e(e, "Couldn't notify client");
          }
          break;

        case Constants.MSG_UNREGISTER_CLIENT:
          clients.remove(msg.replyTo);
          break;
      }
    }
  }

  @Override public IBinder onBind(Intent intent) {
    Timber.d("OnBind service");
    return messenger.getBinder();
  }

  private void notifyAllClients(boolean windFishIsEnabled) {
    for (int n = clients.size() - 1; n >= 0; n--) {
      try {
        notifyClient(windFishIsEnabled, clients.get(n));
      } catch (RemoteException e) {
        Timber.e(e, "Couldn't notify client");
        clients.remove(n);
      }
    }
  }

  private void notifyClient(boolean windfishIsEnabled, Messenger client) throws RemoteException {
    Message msg = Message.obtain(null, windfishIsEnabled
        ? Constants.MSG_WINDFISH_ENABLED
        : Constants.MSG_WINDFISH_DISABLED);
    client.send(msg);
  }
}
