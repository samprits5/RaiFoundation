package raifoundation.rai;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    WebView webView;
    String ur;
    public ProgressBar progressBar, pbar;
    DrawerLayout drawerLayout;
    ImageView imageView;
    int x;
    final String ver = "3";
    public static long enq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        x = 1;

        webView = (WebView) findViewById(R.id.wv);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        pbar = (ProgressBar) findViewById(R.id.progressBar2);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ConstraintLayout child = (ConstraintLayout) drawerLayout.findViewById(R.id.con);
        imageView = (ImageView) findViewById(R.id.img);
        imageView.setVisibility(View.VISIBLE);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        if (!isInterentConnection()) {
            webView.setVisibility(View.GONE);
            pbar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "No Internet!", Toast.LENGTH_SHORT).show();
        } else {

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, int errorCode,
                                            String description, String failingUrl) {
                    view.loadUrl("about:blank");
                    webView.stopLoading();
                    if (!isInterentConnection()) {
                        Toast.makeText(MainActivity.this, "Please Check Internet Connection!", Toast.LENGTH_SHORT).show();
                    }
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (request.getUrl().toString().contains("http://raifoundation") || request.getUrl().toString().contains("www.raifoundation")) {

                            return super.shouldOverrideUrlLoading(view, request);
                        } else if (request.getUrl().toString().contains("facebook.com/raifoundationkrishnagar")) {

                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/114414639254093"));
                                startActivity(intent);
                            } catch (Exception e) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/raifoundationkrishnagar")));
                            }


                        } else {
                            try {
                                ur = (String) request.getUrl().toString();
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ur));
                                startActivity(browserIntent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    return true;
                }

            });
            webView.loadUrl("http://raifoundation.in/h6bca5628x/sampukumar");
            webView.setWebChromeClient(new WebChromeClient() {

                @Override
                public void onReceivedTitle(WebView view, String title) {
                    if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                        super.onReceivedTitle(view, title);
                        ur = view.getUrl();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ur));
                        if (view.getUrl().contains("http://raifoundation") || view.getUrl().contains("www.raifoundation")) {

                            //make something
                        } else {
                            try {
                                startActivity(browserIntent);
                                webView.stopLoading();
                                webView.goBack();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }


                public void onProgressChanged(WebView view, int progress) {
                    progressBar.setProgress(progress);
                    progressBar.setClickable(false);
                    progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#2196f3"),
                            PorterDuff.Mode.MULTIPLY);
                    if (progress == 100) {

                        if (x == 1) {

                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            drawerLayout.removeView(imageView);
                            drawerLayout.removeView(child);
                            x = 2;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("Storage Permission Needed!");
                                    builder.setMessage("You need to allow the write permission to External Storage for future updates!")
                                            .setCancelable(false)
                                            .setPositiveButton("Okay, Go on!", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    ActivityCompat.requestPermissions(MainActivity.this,
                                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                            1);


                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                } else {
                                    try {
                                        String PATH = Environment.getExternalStorageDirectory() + "/Download/" + "rai.apk";
                                        File apkFile = new File(PATH);
                                        if (apkFile.exists()) {
                                            apkFile.delete();
                                            //                                Toast.makeText(MainActivity.this, "Updated to the new version!", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Exception e) {
                                    }
                                    new check().execute();
                                }
                            } else {
                                try {
                                    String PATH = Environment.getExternalStorageDirectory() + "/Download/" + "rai.apk";
                                    File apkFile = new File(PATH);
                                    if (apkFile.exists()) {
                                        apkFile.delete();
                                        //                                Toast.makeText(MainActivity.this, "Updated to the new version!", Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                }
                                new check().execute();
                            }


                        } else {
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }

                    } else {
                        if (x == 1) {
                            pbar.setVisibility(View.VISIBLE);
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        } else {
                            progressBar.setVisibility(View.VISIBLE);
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }

                    }
                }

            });


            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", "raifoundationkrishnanagar@gmail.com", null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Rai Foundation : Query");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(Intent.createChooser(emailIntent, "Send Mail to Rai..."));
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else if (webView.canGoBack()){
            webView.goBack();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Sure to exit ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.alert, null);
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setView(alertLayout);
                final AlertDialog dialog = alert.create();
                dialog.show();
                break;
            case R.id.action_reload:
                webView.loadUrl(webView.getUrl());
                if (webView.getUrl() == ""){
                    webView.loadUrl("http://raifoundation.in/h6bca5628x/sampukumar");
                }
                break;
            case R.id.action_exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Sure to exit ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert2 = builder.create();
                alert2.show();


                break;
            default:
                break;
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        webView = (WebView) findViewById(R.id.wv);



        int id = item.getItemId();

        if (id == R.id.nav_home) {
            webView.loadUrl("http://raifoundation.in/h6bca5628x/sampukumar");
        }else if (id == R.id.nav_about) {
            webView.loadUrl("http://raifoundation.in/h6bca5628x/about");
        } else if (id == R.id.nav_support) {
            webView.loadUrl("http://raifoundation.in/h6bca5628x/support");
        } else if (id == R.id.nav_contact){
            webView.loadUrl("http://raifoundation.in/h6bca5628x/contact");
        } else if (id == R.id.nav_work){
            webView.loadUrl("http://raifoundation.in/h6bca5628x/ourworks.html");
        } else if (id == R.id.nav_pen){
            webView.loadUrl("http://raifoundation.in/h6bca5628x/kolom.html");
        } else if (id ==  R.id.nav_col) {
            webView.loadUrl("http://raifoundation.in/h6bca5628x/collaboration");
        } else if (id == R.id.nav_update){
            new longOperation().execute();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public boolean isInterentConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;

        } else {
            return false;
        }
    }

    public void logo(View view){
        webView.loadUrl("http://raifoundation.in/h6bca5628x/logo");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public class longOperation extends AsyncTask<Void, Void, Void>{
        String content = "";
        @Override
        protected Void doInBackground(Void... voids) {
            if (isInterentConnection()) {
                try {
                    URL url = new URL("http://www.raifoundation.in/h6bca5628x/Update_switch.txt");

                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String str;
                    while ((str = in.readLine()) != null) {
                        content += str;
                    }
                    in.close();
                } catch (MalformedURLException e) {
                    Toast.makeText(MainActivity.this, "Connection Failed!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, "Error in Reading from Server!", Toast.LENGTH_SHORT).show();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            if (isInterentConnection()) {
                if (!content.equals(ver)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Update Available!");
                    builder.setMessage("Do you want to download the latest version of this app ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    download();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert2 = builder.create();
                    alert2.show();
                } else {
                    Toast.makeText(MainActivity.this, "Your App is Up to Date!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Check Internet Connection!", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(aVoid);
        }
    }

    public void download(){
        if (isInterentConnection()) {
            try {
                String PATH = Environment.getExternalStorageDirectory() + "/Download/" + "rai.apk";
                File apkFile = new File(PATH);

                if (apkFile.exists()) apkFile.delete();

                Uri Download_Uri = Uri.parse("http://raifoundation.in/rai.apk");
                DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

                //Restrict the types of networks over which this download may proceed.
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                //Set whether this download may proceed over a roaming connection.
                request.setAllowedOverRoaming(true);
                //Set the title of this download, to be displayed in notifications (if enabled).
                request.setTitle("New Package");
                //Set a description of this download, to be displayed in notifications (if enabled)
                request.setDescription("Downloading the update...");
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                //Set the local destination for the downloaded file to a path within the application's external files directory
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/rai.apk");

                //Enqueue a new download and same the referenceId
                final DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                enq = downloadManager.enqueue(request);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error in Downloading!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class check extends AsyncTask<Void, Void, Void>{
        String con = "";
        String err;
        @Override
        protected Void doInBackground(Void... voids) {
            if (isInterentConnection()) {
                try {
                    URL url = new URL("http://www.raifoundation.in/h6bca5628x/Update_switch.txt");

                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String str;
                    while ((str = in.readLine()) != null) {
                        con += str;
                    }
                    in.close();
                } catch (Exception e) {
                    err = e.getMessage();
                }

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (isInterentConnection()) {
                if (err == null) {
                    if (!con.equals(ver)) {
                        pbar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Update Available!");
                        builder.setMessage("Do you want to download the latest version of this app ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        download();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert2 = builder.create();
                        alert2.show();
                    } else {
                        super.onPostExecute(aVoid);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error in checking the server", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Check Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        }
    }


                                                @Override
                                                public void onRequestPermissionsResult(int requestCode,
                                                String permissions[], int[] grantResults) {
                                                    switch (requestCode) {
                                                        case 1: {

                                                            // If request is cancelled, the result arrays are empty.
                                                            if (grantResults.length > 0
                                                                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                                                                try {
                                                                    String PATH = Environment.getExternalStorageDirectory() + "/Download/" + "rai.apk";
                                                                    File apkFile = new File(PATH);
                                                                    if (apkFile.exists()) {
                                                                        apkFile.delete();
                                                                    }
                                                                } catch (Exception e){
                                                                }
                                                                new check().execute();
                                                            } else {
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                                                builder.setMessage("App needs storage permission to work properly!")
                                                                        .setCancelable(false)
                                                                        .setPositiveButton("Exit!", new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int id) {
                                                                                finish();
                                                                                System.exit(0);
                                                                            }
                                                                        });
                                                                AlertDialog alert = builder.create();
                                                                alert.show();
                                                            }
                                                            return;
                                                        }

                                                        // other 'case' lines to check for other
                                                        // permissions this app might request
                                                    }
                                                }

}
