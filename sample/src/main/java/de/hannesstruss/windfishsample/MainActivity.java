package de.hannesstruss.windfishsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import de.hannesstruss.windfish.library.WindFishCallbacks;

public class MainActivity extends AppCompatActivity implements WindFishCallbacks {
  private static final String ZZZ = "\uD83D\uDCA4";
  private static final String WHALE = "\uD83D\uDC33";

  private TextView txtStatus;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    txtStatus = (TextView) findViewById(R.id.txt_status);
  }

  @Override public void onWindFishStateChanged(boolean keepScreenOn) {
    String text = WHALE + (keepScreenOn ? "" : ZZZ);
    txtStatus.setText(text);
  }
}
