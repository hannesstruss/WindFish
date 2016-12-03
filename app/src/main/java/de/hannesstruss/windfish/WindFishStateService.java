package de.hannesstruss.windfish;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.hannesstruss.windfish.common.Constants;
import rx.Observable;
import rx.Subscription;

public class WindFishStateService extends Service {
  private static final String TAG = WindFishStateService.class.getSimpleName();

  private final Messenger messenger = new Messenger(new IncomingHandler());
  private final List<Messenger> clients = new ArrayList<>();

  private Observable<Boolean> state;
  private Subscription stateSubscription;

  @Override public void onCreate() {
    super.onCreate();
    state = ((WindFishApp) getApplicationContext()).state().isEnabled()
        .startWith(false).replay(1).refCount();
    stateSubscription = state.subscribe(this::onStateChanged);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    if (stateSubscription != null) {
      stateSubscription.unsubscribe();
    }
  }

  private void onStateChanged(boolean isEnabled) {
    notifyAllClients(isEnabled);
  }

  @Override public IBinder onBind(Intent intent) {
    return messenger.getBinder();
  }

  private void notifyAllClients(boolean windFishIsEnabled) {
    for (int n = clients.size() - 1; n >= 0; n--) {
      try {
        notifyClient(windFishIsEnabled, clients.get(n));
      } catch (RemoteException e) {
        Log.e(TAG, "Couldn't notify client", e);
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

  class IncomingHandler extends Handler {
    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case Constants.MSG_REGISTER_CLIENT:
          final Messenger replyTo = msg.replyTo;
          state.subscribe(isEnabled -> {
            try {
              notifyClient(isEnabled, replyTo);
            } catch (RemoteException e) {
              Log.e(TAG, "Couldn't notify client", e);
            }
          });
          if (!clients.contains(msg.replyTo)) {
            clients.add(msg.replyTo);
          }
          break;

        case Constants.MSG_UNREGISTER_CLIENT:
          clients.remove(msg.replyTo);
          break;
      }
    }
  }
}
