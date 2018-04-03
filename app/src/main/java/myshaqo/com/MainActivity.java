package myshaqo.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import im.delight.android.webview.AdvancedWebView;

public class MainActivity extends Activity implements AdvancedWebView.Listener {


    private static final String TEST_PAGE_URL = "http://www.myshaqo.com";
    private AdvancedWebView mWebView;
    ImageView imageView;
   // public ProgressBar progressBar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        mWebView =  findViewById(R.id.webview);
        imageView = findViewById(R.id.imageLoading1);
       // progressBar = findViewById(R.id.progressBar1);



        if(isConnected()){



            if(!getSharedPreferences("APP_PREFERENCE", Activity.MODE_PRIVATE).getBoolean("IS_ICON_CREATED", false)){
                createShortcut();
                getSharedPreferences("APP_PREFERENCE", Activity.MODE_PRIVATE).edit().putBoolean("IS_ICON_CREATED", true).commit();
            }

            mWebView.setListener(this, this);
            mWebView.setGeolocationEnabled(false);
            mWebView.setMixedContentAllowed(true);
            mWebView.setCookiesEnabled(true);
            mWebView.setThirdPartyCookiesEnabled(true);
            mWebView.setWebViewClient(new WebViewClient() {



                @Override
                public void onPageFinished(WebView view, String url) {

                    //Toast.makeText(MainActivity.this, "Finished loading", Toast.LENGTH_SHORT).show();

                    //hide loading image
                    imageView.setVisibility(View.INVISIBLE);

                    //show webview
                    // mWebView.setVisibility(View.VISIBLE);


                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    imageView.setVisibility(View.VISIBLE);


                }
            });
            mWebView.setWebChromeClient(new WebChromeClient() {

                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    //
                    // Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProgressChanged(WebView view, int newProgress) {

//                    {
//                        if (newProgress < 100 && progressBar.getVisibility() == ProgressBar.GONE) {
//                            progressBar.setVisibility(ProgressBar.VISIBLE);
//                        }
//                        progressBar.setProgress(newProgress);
//                        if (newProgress == 100) {
//                            progressBar.setVisibility(ProgressBar.GONE);
//                        }
//
//                    }

                }
            });

            mWebView.addHttpHeader("X-Requested-With", "");

            mWebView.loadUrl(TEST_PAGE_URL);


        }
        else {

            DialogInternet dialogInternet = new DialogInternet(context);

            dialogInternet.show();

        }


    }

    public boolean isConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    private void createShortcut() {
        Intent shortcutIntent = new Intent(getApplicationContext(), MainActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "MyShaqo");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.appiconshaqo));

        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        addIntent.putExtra("duplicate", false);  //may it's already there so don't duplicate
        getApplicationContext().sendBroadcast(addIntent);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (!mWebView.onBackPressed()) { return; }
        // ...
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
       // progressBar.setVisibility(View.VISIBLE);
        mWebView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPageFinished(String url) {
      //  progressBar.setVisibility(View.INVISIBLE);
        mWebView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
       // Toast.makeText(MainActivity.this, "onPageError(errorCode = "+errorCode+",  description = "+description+",  failingUrl = "+failingUrl+")", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
       // Toast.makeText(MainActivity.this, "onDownloadRequested(url = "+url+",  suggestedFilename = "+suggestedFilename+",  mimeType = "+mimeType+",  contentLength = "+contentLength+",  contentDisposition = "+contentDisposition+",  userAgent = "+userAgent+")", Toast.LENGTH_LONG).show();

		/*if (AdvancedWebView.handleDownload(this, url, suggestedFilename)) {
			// download successfully handled
		}
		else {
			// download couldn't be handled because user has disabled download manager app on the device
		}*/
    }

    @Override
    public void onExternalPageRequest(String url) {
      //  Toast.makeText(MainActivity.this, "onExternalPageRequest(url = "+url+")", Toast.LENGTH_SHORT).show();
    }

}

