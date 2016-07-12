package de.hannesstruss.windfish.library;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.WindowManager;

import de.hannesstruss.windfish.common.Constants;

class ActivityCompanion {
  private static final String TAG = "WindFish";
  private static final String WINDFISH_PKG = "de.hannesstruss.windfish";
  private static final String WINDFISH_STATE_SERVICE = "de.hannesstruss.windfish.WindFishStateService";

  private Activity activity;
  private Messenger service;

  private final IncomingHandler incomingHandler = new IncomingHandler(this);
  private final Messenger messenger = new Messenger(incomingHandler);

  private ServiceConnection serviceConnection = new ServiceConnection() {
    @Override public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
      service = new Messenger(iBinder);

      try {
        Message msg = Message.obtain(null, Constants.MSG_REGISTER_CLIENT);
        msg.replyTo = messenger;
        service.send(msg);
      } catch (Exception e) {
        Log.e(TAG, "Couldn't register client", e);
      }
    }

    @Override public void onServiceDisconnected(ComponentName componentName) {
      keepScreenOn(false);
    }
  };

  public void attach(Activity activity) {
    this.activity = activity;

    Intent intent = new Intent();
    intent.setComponent(new ComponentName(WINDFISH_PKG, WINDFISH_STATE_SERVICE));
    boolean couldConnect = activity.bindService(intent, serviceConnection,
        Service.BIND_AUTO_CREATE);

    if (couldConnect) {
      Log.d(TAG, "Connected to WindFish service");
    } else {
      Log.w(TAG, "Couldn't connect to WindFish service. Did you install the companion app?");
    }
  }

  public void destroy() {
    keepScreenOn(false);
    if (service != null) {
      service = null;
      activity.unbindService(serviceConnection);
    }
    incomingHandler.dispose();
    activity = null;
  }

  void onWindFishDisabled() {
    Log.i(TAG, "Disabled WindFish");
    keepScreenOn(false);
  }

  void onWindFishEnabled() {
    Log.i(TAG, "Enabled WindFish");
    keepScreenOn(true);
  }

  private void keepScreenOn(boolean keepOn) {
    if (activity != null) {
      if (keepOn) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
      } else {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
      }

      if (activity instanceof WindFishCallbacks) {
        ((WindFishCallbacks) activity).onWindFishStateChanged(keepOn);
      }
    }
  }

  private static class IncomingHandler extends Handler {
    private ActivityCompanion activityCompanion;

    public IncomingHandler(ActivityCompanion activityCompanion) {
      this.activityCompanion = activityCompanion;
    }

    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
      if (activityCompanion == null) {
        return;
      }

      switch (msg.what) {
        case Constants.MSG_WINDFISH_DISABLED:
          activityCompanion.onWindFishDisabled();
          break;

        case Constants.MSG_WINDFISH_ENABLED:
          activityCompanion.onWindFishEnabled();
          break;
      }
    }

    void dispose() {
      activityCompanion = null;
    }
  }
}
