package de.hannesstruss.windfish;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
  private static final String HELP_URL = "https://youtu.be/AuuIB4cT2SA";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    String hello = "\uD83D\uDC33";
    ((TextView) findViewById(R.id.textview)).setText(hello);

    findViewById(R.id.btn_help_me).setOnClickListener(view -> {
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.setData(Uri.parse(HELP_URL));
      startActivity(intent);
    });
  }
}
