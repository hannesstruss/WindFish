package de.hannesstruss.windfish;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import rx.Observable;
import rx.android.MainThreadSubscription;

class RxPowerStatus {
  private final Application application;

  RxPowerStatus(Application application) {
    this.application = application;
  }

  Observable<Boolean> isCharging() {
    return Observable.create(subscriber -> {
      IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
      BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent batteryStatus) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(isCharging(batteryStatus));
          }
        }
      };
      Intent batteryStatus = application.registerReceiver(receiver, filter);
      subscriber.onNext(isCharging(batteryStatus));

      subscriber.add(new MainThreadSubscription() {
        @Override protected void onUnsubscribe() {
          application.unregisterReceiver(receiver);
        }
      });
    });
  }

  private static boolean isCharging(Intent batteryStatus) {
    int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
    return status == BatteryManager.BATTERY_STATUS_CHARGING ||
        status == BatteryManager.BATTERY_STATUS_FULL;
  }
}
