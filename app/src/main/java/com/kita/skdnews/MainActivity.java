package com.kita.skdnews;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.kita.skdnews.adapter.NewsAdapter;
import com.kita.skdnews.alarm.AlarmActivity;
import com.kita.skdnews.db.DBNews;
import com.kita.skdnews.db.DSSettings;
import com.kita.skdnews.helper.BottomDialog;
import com.kita.skdnews.helper.Extra;
import com.kita.skdnews.models.Article;
import com.kita.skdnews.ui.SettingFragment;
import com.kita.skdnews.ui.about.AboutFragment;
import com.kita.skdnews.ui.archive.ArchiveFragment;
import com.kita.skdnews.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, BottomDialog.ItemClickListener{

    private static String TAG = MainActivity.class.getSimpleName();
    public static final String TIME_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String API_KEY;
    public static Locale local;
    public static String Country, Language, Category;

    public static ArrayList<Article> articles = new ArrayList<>();
    public static NewsAdapter newsAdapter;

    public static DBNews db;
    public static FloatingActionButton btnSearch;

    private int RC_SPLASH = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomDialog bottomSheet = BottomDialog.newInstance();
                bottomSheet.show(getSupportFragmentManager(), BottomDialog.TAG);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        db = DBNews.getInstance(MainActivity.this);

        Intent splash = new Intent(MainActivity.this, SplashActivity.class);
        startActivityForResult(splash, RC_SPLASH);

        Country = getString(R.string.country);
        Language = getString(R.string.language);
        Category = getString(R.string.category);
        API_KEY = getString(R.string.apikey);

        DSSettings st = db.getDAOSetting().getSettingByID(1);
        if (st != null){
            Country = st.country;
            Language = st.language;
            Category = st.category;
            API_KEY = st.apikey;
        }

        Extra.updateSetting();
        local = new Locale(Language,Country);

        showView(R.id.navigation_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_settings:
                Fragment frag = new SettingFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, frag, frag.getClass().getSimpleName());
                fragmentTransaction.addToBackStack(TAG);
                fragmentTransaction.commit();
                return true;
            case R.id.action_alarm:
                Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                String mOrderMessage = null;
                intent.putExtra(EXTRA_MESSAGE, mOrderMessage);
                startActivity(intent);
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        showView(item.getItemId());
        return true;
    }

    public void showView(int viewId) {
        String title = "";
        Fragment frag = null;
        btnSearch.setVisibility(View.GONE);
        switch (viewId) {
            case R.id.navigation_home:
                frag = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case R.id.navigation_archive:
                frag = new ArchiveFragment();
                title = getString(R.string.title_archive);
                break;
            case R.id.navigation_about:
                frag = new AboutFragment();
                title = getString(R.string.title_about);
                break;
        }

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, frag, frag.getClass().getSimpleName());
            ft.commit();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setSubtitle("");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            String title = "";
            if (getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getSimpleName()) != null)
                title = getString(R.string.title_home);
            else if (getSupportFragmentManager().findFragmentByTag(ArchiveFragment.class.getSimpleName()) != null)
                title = getString(R.string.title_archive);
            else if (getSupportFragmentManager().findFragmentByTag(AboutFragment.class.getSimpleName()) != null)
                title = getString(R.string.title_about);

            getSupportActionBar().setTitle(title);
            getSupportActionBar().setSubtitle("");

            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(String str) {
        Fragment frag = null;
        frag = getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getSimpleName());
        if (frag != null) {
            ((HomeFragment) frag).errorLayout.setVisibility(View.GONE);
            ((HomeFragment) frag).onLoadingSwipeRefresh(str);
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            return;
        }

        frag = getSupportFragmentManager().findFragmentByTag(ArchiveFragment.class.getSimpleName());
        if (frag != null) {
            ((ArchiveFragment) frag).reInitData(str);
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            return;
        }
    }
}