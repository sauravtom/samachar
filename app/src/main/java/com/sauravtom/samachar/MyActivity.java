package com.sauravtom.samachar;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class MyActivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mTitle;
    private String current_url = "";
    boolean doubleBackToExitPressedOnce;
    public static String baseurl = "http://tosc.in:8081/";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (!isNetworkAvailable()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setMessage("Cannot find the internet !!");

            alert.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    finish();
                    startActivity(getIntent());
                }
            });


            alert.show();
        }

        //No need for full screen, your app is not that important saurav, huh
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_main);

        mTitle = getResources().getString(R.string.app_name);
        getActionBar().setTitle(mTitle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer_2, R.string.drawer_open,
                R.string.drawer_close) {

            /** Called when drawer is closed */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

        };

        // Setting DrawerToggle on DrawerLayout
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Creating an ArrayAdapter to add items to the listview mDrawerList
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                R.layout.drawer_list_item, getResources().getStringArray(R.array.menus));

        mDrawerList.setAdapter(adapter);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting item click listener for the listview mDrawerList
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                String[] menuItems = getResources().getStringArray(R.array.menus);
                mTitle =getResources().getString(R.string.app_name)+ " / " +menuItems[position];

                WebViewFragment rFragment = new WebViewFragment();

                Bundle data = new Bundle();
                data.putInt("position", position);
                data.putString("url", getUrl(position));
                rFragment.setArguments(data);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_frame, rFragment);
                ft.commit();

                getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                mDrawerLayout.closeDrawer(mDrawerList);

            }
        });

        InitFragment("0");

        /*Hide ads as of now
        // Look up the AdView as a resource and load a request.
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        */

    }


    public void InitFragment(String url){
        if (url.length() < 2 ) url = baseurl;
        WebViewFragment rFragment = new WebViewFragment();
        Bundle data = new Bundle();
        data.putInt("position", 1);
        data.putString("url", url);
        rFragment.setArguments(data);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, rFragment);
        ft.commit();
    }

    protected String getUrl(int position) {

        switch (position) {
            case 0:return baseurl;
            case 1:return baseurl+"w";
            case 2:return baseurl+"n";
            case 3:return baseurl+"b";
            case 4:return baseurl+"e";
            case 5:return baseurl+"s";
            case 6:return baseurl+"h";
            case 7:return baseurl+"sources";
            case 8:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return "settings";
            case 9:return baseurl+"about";
            default:
                return "";
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

        menu.findItem(R.id.action_reload).setVisible(!drawerOpen);
        menu.findItem(R.id.action_share).setVisible(!drawerOpen);
        menu.findItem(R.id.action_openBrowser).setVisible(!drawerOpen);

        if(drawerOpen) {
            getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        }
        //menu.findItem(R.id.action_share).setShowAsAction(1);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        current_url = WebViewFragment.webView.getUrl();
        MenuItem icon_share = menu.findItem(R.id.action_share);
        MenuItem icon_reload = menu.findItem(R.id.action_reload);
        MenuItem icon_openBrowser = menu.findItem(R.id.action_openBrowser);
        MenuItem icon_copy = menu.findItem(R.id.action_copy);
        MenuItem icon_exit = menu.findItem(R.id.action_exit);
        MenuItem icon_settings = menu.findItem(R.id.action_settings);

        //Toast.makeText(getBaseContext(),WebViewFragment.webView.getTitle(), Toast.LENGTH_SHORT).show();

        icon_reload.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                WebViewFragment.webView.reload();
                return false;
            }
        });

        icon_openBrowser.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WebViewFragment.webView.getUrl()));
                startActivity(browserIntent);
                return false;
            }
        });

        icon_copy.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("url", current_url);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Link Copied", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        icon_settings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return false;
            }
        });

        icon_exit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                finish();
                return false;
            }
        });

        ShareActionProvider mShareActionProvider = (ShareActionProvider) icon_share.getActionProvider();
        Intent myIntent = new Intent();
        myIntent.setAction(Intent.ACTION_SEND);
        myIntent.putExtra(Intent.EXTRA_TEXT, current_url);
        myIntent.setType("text/plain");
        mShareActionProvider.setShareIntent(myIntent);

        return true;
    }

    /*Class that prevents closing tha app on back button press, instead go to the previous page. Double press closes the app.*/
    @Override
    public void onBackPressed() {

        if(doubleBackToExitPressedOnce){
            finish();
        }

        String url = WebViewFragment.webView.getUrl();

        if (url.startsWith(baseurl)) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        }

        invalidateOptionsMenu();
        if(WebViewFragment.webView.canGoBack()){
            WebViewFragment.webView.goBack();
        }else{
            InitFragment("0");
            //finish();
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}