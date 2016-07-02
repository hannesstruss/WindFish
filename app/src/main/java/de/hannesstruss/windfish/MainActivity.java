package de.hannesstruss.windfish;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    String hello = "\uD83D\uDC33";
    ((TextView) findViewById(R.id.textview)).setText(hello);
  }
}
