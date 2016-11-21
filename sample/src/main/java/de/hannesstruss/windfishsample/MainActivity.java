package de.hannesstruss.windfishsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import de.hannesstruss.windfish.library.WindFishCallbacks;

public class MainActivity extends AppCompatActivity implements WindFishCallbacks {
  private static final String ZZZ = "\uD83D\uDCA4";
  private static final String COFFEE = "☕";
  private static final String WHALE = "\uD83D\uDC33";

  private TextView txtStatus;
  private TextView txtDescription;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    txtStatus = (TextView) findViewById(R.id.txt_status);
    txtDescription = (TextView) findViewById(R.id.txt_description);

    txtStatus.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, OtherActivity.class);
        startActivity(intent);
      }
    });
  }

  @Override public void onWindFishStateChanged(boolean keepScreenOn) {
    String text = WHALE + (keepScreenOn
        ? COFFEE
        : ZZZ);
    txtStatus.setText(text);

    txtDescription.setText(keepScreenOn
        ? "Screen will stay on!"
        : "Screen will turn off.");
  }
}
