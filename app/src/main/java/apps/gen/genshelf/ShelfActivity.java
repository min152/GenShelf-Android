package apps.gen.genshelf;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.transition.ActionBarTransition;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import apps.gen.genshelf.fragments.BaseFragment;
import apps.gen.genshelf.fragments.ShelfFragment;
import cz.msebera.android.httpclient.Header;

public class ShelfActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseFragment.OnFragmentInteractionListener {

    Fragment mCurrentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        pushFragment(ShelfFragment.newInstance("1", "2"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final View finalView = view;
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get("http://www.baidu.com", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Document doc = Jsoup.parse(new String(responseBody));
                            Log.i("test", doc.title());
                            Snackbar.make(finalView, String.format("Success: %s", doc.select("input#su").val()), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Snackbar.make(finalView, String.format("Failed %s", error.toString()), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });
                }
            });
        }

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                if (getSupportFragmentManager().popBackStackImmediate()) {
                    checkCurrentFragment();
                }
            }else {
                finish();
            }
        }
    }

    void resetTitle() {
        if (mCurrentFragment instanceof BaseFragment) {
//            getSupportActionBar().setTitle(((BaseFragment)mCurrentFragment).getTitle());
        }
    }

    void checkCurrentFragment() {
        for (Fragment fragment:getSupportFragmentManager().getFragments()) {
            if (fragment != null && fragment.isVisible()) {
                mCurrentFragment = fragment;
                resetTitle();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shelf, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.i("Open drawer", "shelf debug");

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onPushFragment(BaseFragment sender, BaseFragment other) {
        if (sender == mCurrentFragment)
            pushFragment(other);
    }

    @Override
    public BaseFragment onPopFragment(BaseFragment sender) {
        if (sender == mCurrentFragment)
            return popFragment();
        return null;
    }

    @Override
    public void titleChange(BaseFragment sender, String title) {
        if (sender == mCurrentFragment)
            return;
    }

    public void setFragments() {

    }

    public void pushFragment(BaseFragment other) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.fragment_push_in,
                R.anim.fragment_push_out,
                R.anim.fragment_push_in,
                R.anim.fragment_push_out);
        if (mCurrentFragment != null)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container, other)
                .commit();
        mCurrentFragment = other;
        resetTitle();
    }

    public BaseFragment popFragment() {
        return null;
    }
}
