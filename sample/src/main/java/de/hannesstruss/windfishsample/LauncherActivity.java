package de.hannesstruss.windfishsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/** For testing Memory Leaks */
public class LauncherActivity extends Activity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    finish();
  }
}
