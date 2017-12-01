package com.gnuarmap.mixare.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import java.util.List;

/**
 * Created by nazunamoe on 2017-12-01.
 */

public class WebContent {
    /**
     * This class is repsonsible for Web Content
     */
    public static interface WebContentManager {

        /**
         * Shows a webpage with the given url if a markerobject is selected
         * (mixlistview, mixoverlay).
         */
        void loadWebPage(String url, Context context) throws Exception;

        /**
         * Checks if the url can be opened by another intent activity, instead of
         * the webview This method searches for possible intents that can be used
         * instead. I.E. a mp3 file can be forwarded to a mediaplayer.
         *
         * @param url
         *            the url to process
         * @return
         */
        boolean processUrl(String url, Context ctx);

    }

    /**
     * Factory Of  WebContentManager
     */
    public static class WebContentManagerFactory {
        /**
         * Hide implementation Of WebContentManager
         * @param mixContext
         * @return WebContentManager
         */
        public static WebContentManager makeWebContentManager(MixContext mixContext){
            return new WebPageMgrImpl(mixContext);
        }

    }

    static class WebPageMgrImpl implements WebContentManager {

        protected MixContext mixContext;

        /**
         * Shows a webpage with the given url when clicked on a marker.
         */
        public void loadMixViewWebPage(String url) throws Exception {
            loadWebPage(url, mixContext.getActualMixView());
        }

        public WebPageMgrImpl(MixContext mixContext) {
           this.mixContext=mixContext;
        }

        /* (non-Javadoc)
         * @see WebContentManager#loadWebPage(java.lang.String, android.content.Context)
         */
        public void loadWebPage(String url, Context context) throws Exception {
            WebView webview = new WebView(context);
            webview.getSettings().setJavaScriptEnabled(true);

            final Dialog d = new Dialog(context) {
                public boolean onKeyDown(int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                        this.dismiss();
                    return true;
                }
            };

            webview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (url.endsWith("return")) {
                        d.dismiss();
                        mixContext.getActualMixView().repaint();
                    } else {
                        super.onPageFinished(view, url);
                    }
                }

            });

            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.getWindow().setGravity(Gravity.BOTTOM);
            d.addContentView(webview, new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM));

            if (!processUrl(url, mixContext.getActualMixView())) { // if the url could not be processed by
                                             // another intent
                d.show();
                webview.loadUrl(url);
            }
        }

        /* (non-Javadoc)
         * @see WebContentManager#processUrl(java.lang.String, android.content.Context)
         */
        public boolean processUrl(String url, Context ctx) {
            // get available packages from the given url
            List<ResolveInfo> resolveInfos = getAvailablePackagesForUrl(url, ctx);
            // filter the webbrowser > because the webview will replace it, using
            // google as simple url
            List<ResolveInfo> webBrowsers = getAvailablePackagesForUrl(
                    "http://www.google.com", ctx);
            for (ResolveInfo resolveInfo : resolveInfos) {
                for (ResolveInfo webBrowser : webBrowsers) { // check if the found
                                                                // intent is not a
                                                                // webbrowser
                    if (!resolveInfo.activityInfo.packageName
                            .equals(webBrowser.activityInfo.packageName)) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        intent.setClassName(resolveInfo.activityInfo.packageName,
                                resolveInfo.activityInfo.name);
                        ctx.startActivity(intent);
                        return true;
                    }
                }
            }
            return false;
        }

        private List<ResolveInfo> getAvailablePackagesForUrl(String url, Context ctx) {
            PackageManager packageManager = ctx.getPackageManager();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            return packageManager.queryIntentActivities(intent,
                    PackageManager.GET_RESOLVED_FILTER);
        }

    }
}
