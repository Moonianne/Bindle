package org.pursuit.usolo;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.pursuit.usolo.map.MapFragment;

public final class HostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        inflateFragment(MapFragment.newInstance());
    }

    private void inflateFragment(Fragment fragment) {
        getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.main_container, fragment)
          .commit();
    }
}
