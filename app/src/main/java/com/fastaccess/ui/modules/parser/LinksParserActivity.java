package com.fastaccess.ui.modules.parser;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.fastaccess.App;
import com.fastaccess.R;
import com.fastaccess.data.dao.model.Login;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.provider.scheme.SchemeParser;
import com.fastaccess.ui.modules.login.chooser.LoginChooserActivity;

/**
 * Created by Kosh on 09 Dec 2016, 12:31 PM
 */

public class LinksParserActivity extends Activity {

  @Override
  protected void onCreate(final @Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (Login.getUser() == null) {
      Toast
          .makeText(App.getInstance(), R.string.please_login,
                    Toast.LENGTH_SHORT)
          .show();
      startActivity(new Intent(this, LoginChooserActivity.class));
      finish();
      return;
    }
    onCreate(getIntent());
  }

  @Override
  protected void onStart() {
    super.onStart();
    setVisible(true);
  }

  private void onCreate(final @Nullable Intent intent) {
    if (intent == null || intent.getAction() == null) {
      finish();
      return;
    }
    if (Intent.ACTION_SEND.equals(intent.getAction())) {
      try {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (!InputHelper.isEmpty(sharedText)) {
          Uri uri = Uri.parse(sharedText);
          onUriReceived(uri);
        }
      } catch (Exception ignored) {
      }
    } else if ((intent.getAction().equals(Intent.ACTION_VIEW)) && (intent.getData() != null)) {
      onUriReceived(intent.getData());
    }
    finish();
  }

  private void onUriReceived(final @NonNull Uri uri) {
    SchemeParser.launchUri(this, uri, false, true);
  }
}
