package com.example.qingxiaodong.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;
import android.support.v7.widget.ShareActionProvider;


public class DetailActivity extends ActionBarActivity {

    private ShareActionProvider mShareActionProvider;
    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(getShareIntent());
        } else {
            Log.d(LOG_TAG, "ShareActionProvider is null?");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        if (id == R.id.action_view_map) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            String zipCode = sharedPrefs.getString(getString(R.string.pref_location_key),
                    getString(R.string.pref_location_default));
            Intent viewMapIntent = new Intent(Intent.ACTION_VIEW);
            Uri locationUri = Uri.parse("geo:0,0?q=" + zipCode);
            viewMapIntent.setData(locationUri);
            startActivity(viewMapIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent getShareIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        TextView view = (TextView) findViewById(R.id.detail_text);
        String weatherDetail = view.getText().toString();
        shareIntent.putExtra(Intent.EXTRA_TEXT, weatherDetail + " #SunshineApp");
        shareIntent.setType("text/plain");
        return shareIntent;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                TextView textView = (TextView) rootView.findViewById(R.id.detail_text);
                String weatherDetail = intent.getStringExtra(Intent.EXTRA_TEXT);
                textView.setText(weatherDetail);
            }
            return rootView;
        }
    }
}
