package com.prettifier.pretty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fastaccess.R;
import com.fastaccess.helper.AppHelper;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.Logger;
import com.fastaccess.helper.ViewHelper;
import com.fastaccess.provider.markdown.MarkDownProvider;
import com.fastaccess.provider.scheme.SchemeParser;
import com.fastaccess.ui.modules.code.CodeViewerActivity;
import com.prettifier.pretty.callback.MarkDownInterceptorInterface;
import com.prettifier.pretty.helper.GithubHelper;
import com.prettifier.pretty.helper.PrettifyHelper;


public class PrettifyWebView extends NestedWebView {
    private OnContentChangedListener onContentChangedListener;
    private boolean interceptTouch;
    private boolean enableNestedScrolling;

    public interface OnContentChangedListener {
        void onContentChanged(int progress);

        void onScrollChanged(boolean reachedTop, int scroll);
    }

    public PrettifyWebView(final Context context) {
        super(context);
        if (isInEditMode()) return;
        initView(null);
    }

    public PrettifyWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public PrettifyWebView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    @Override public boolean onInterceptTouchEvent(final MotionEvent p) {
        return true;
    }

    @SuppressLint("ClickableViewAccessibility") @Override public boolean onTouchEvent(final MotionEvent event) {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(interceptTouch);
        }
        return super.onTouchEvent(event);
    }

    @SuppressLint("SetJavaScriptEnabled") private void initView(final @Nullable AttributeSet attrs) {
        if (isInEditMode()) return;
        if (attrs != null) {
            TypedArray tp = getContext().obtainStyledAttributes(attrs, R.styleable.PrettifyWebView);
            try {
                int color = tp.getColor(R.styleable.PrettifyWebView_webview_background, ViewHelper.getWindowBackground(getContext()));
                setBackgroundColor(color);
            } finally {
                tp.recycle();
            }
        }
        setWebChromeClient(new ChromeClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setWebViewClient(new WebClient());
        } else {
            setWebViewClient(new WebClientCompat());
        }
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCachePath(getContext().getCacheDir().getPath());
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setLoadsImagesAutomatically(true);
        settings.setBlockNetworkImage(false);
        setOnLongClickListener((view) -> {
            WebView.HitTestResult result = getHitTestResult();
            if (hitLinkResult(result) && !InputHelper.isEmpty(result.getExtra())) {
                AppHelper.copyToClipboard(getContext(), result.getExtra());
                return true;
            }
            return false;
        });
    }

    @Override protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onContentChangedListener != null) {
            onContentChangedListener.onScrollChanged(t == 0, t);
        }
    }

    @Override protected void onDetachedFromWindow() {
        onContentChangedListener = null;
        super.onDetachedFromWindow();
    }

    private boolean hitLinkResult(final WebView.HitTestResult result) {
        return result.getType() == WebView.HitTestResult.SRC_ANCHOR_TYPE || result.getType() == HitTestResult.IMAGE_TYPE
               || result.getType() == HitTestResult.SRC_IMAGE_ANCHOR_TYPE;
    }

    public void setOnContentChangedListener(final @NonNull OnContentChangedListener onContentChangedListener) {
        this.onContentChangedListener = onContentChangedListener;
    }

    public void setThemeSource(final @NonNull String source, final @Nullable String theme) {
        if (!InputHelper.isEmpty(source)) {
            WebSettings settings = getSettings();
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
            setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            settings.setSupportZoom(true);
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);
            String page = PrettifyHelper.generateContent(source, theme);
            loadCode(page);
        }
    }

    public void setSource(final @NonNull String source, final boolean wrap) {
        if (!InputHelper.isEmpty(source)) {
            WebSettings settings = getSettings();
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
            setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            settings.setSupportZoom(!wrap);
            settings.setBuiltInZoomControls(!wrap);
            if (!wrap) settings.setDisplayZoomControls(false);
            String page = PrettifyHelper.generateContent(source, AppHelper.isNightMode(getResources()), wrap);
            loadCode(page);
        }
    }

    private void loadCode(final String page) {
        post(() -> loadDataWithBaseURL("file:///android_asset/highlight/", page, "text/html", "utf-8", null));
    }

    public void scrollToLine(final @NonNull String url) {
        String[] lineNo = getLineNo(url);
        if (lineNo != null && lineNo.length > 1) {
            loadUrl("javascript:scrollToLineNumber('" + lineNo[0] + "', '" + lineNo[1] + "')");
        } else if (lineNo != null) {
            loadUrl("javascript:scrollToLineNumber('" + lineNo[0] + "', '0')");
        }
    }

    public static String[] getLineNo(final @Nullable String url) {
        String lineNo[] = null;
        if (url != null) {
            try {
                Uri uri = Uri.parse(url);
                String lineNumber = uri.getEncodedFragment();
                if (lineNumber != null) {
                    lineNo = lineNumber.replaceAll("L", "").split("-");
                }
            } catch (Exception ignored) { }
        }
        return lineNo;
    }

    public void setGithubContentWithReplace(final @NonNull String source, final @Nullable String baseUrl, final boolean replace) {
        setGithubContent(source, baseUrl, false);
        addJavascriptInterface(new MarkDownInterceptorInterface(this, false), "Android");
        String page = GithubHelper.generateContent(getContext(), source, baseUrl, AppHelper.isNightMode(getResources()), false, replace);
        post(() -> loadDataWithBaseURL("file:///android_asset/md/", page, "text/html", "utf-8", null));
    }

    public void setGithubContent(final @NonNull String source, final @Nullable String baseUrl, final boolean toggleNestScrolling) {
        setGithubContent(source, baseUrl, toggleNestScrolling, true);
    }

    public void setWikiContent(final @NonNull String source, final @Nullable String baseUrl) {
        addJavascriptInterface(new MarkDownInterceptorInterface(this, true), "Android");
        String page = GithubHelper.generateContent(getContext(), source, baseUrl, AppHelper.isNightMode(getResources()), AppHelper.isNightMode
                      (getResources()), true);
        post(() -> loadDataWithBaseURL("file:///android_asset/md/", page, "text/html", "utf-8", null));
    }

    public void setGithubContent(final @NonNull String source, final @Nullable String baseUrl, final boolean toggleNestScrolling, final boolean enableBridge) {
        if (enableBridge) addJavascriptInterface(new MarkDownInterceptorInterface(this, toggleNestScrolling), "Android");
        String page = GithubHelper.generateContent(getContext(), source, baseUrl, AppHelper.isNightMode(getResources()),
                      AppHelper.isNightMode(getResources()), false);
        post(() -> loadDataWithBaseURL("file:///android_asset/md/", page, "text/html", "utf-8", null));
    }

    public void loadImage(final @NonNull String url, final boolean isSvg) {
        WebSettings settings = getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        String html;
        if (isSvg) {
            html = url;
        } else {
            html = "<html><head><style>img{display: inline; height: auto; max-width: 100%;}</style></head><body>"
                   + "<img src=\"" + url + "\"/></body></html>";
        }
        Logger.e(html);
        loadData(html, "text/html", null);
    }

    public void setInterceptTouch(final boolean interceptTouch) {
        this.interceptTouch = interceptTouch;
    }

    public void setEnableNestedScrolling(final boolean enableNestedScrolling) {
        if (this.enableNestedScrolling != enableNestedScrolling) {
            setNestedScrollingEnabled(enableNestedScrolling);
            this.enableNestedScrolling = enableNestedScrolling;
        }
    }

    private void startActivity(final @Nullable Uri url) {
        if (url == null) return;
        if (MarkDownProvider.isImage(url.toString())) {
            CodeViewerActivity.startActivity(getContext(), url.toString(), url.toString());
        } else {
            String lastSegment = url.getEncodedFragment();
            if (lastSegment != null || url.toString().startsWith("#") || url.toString().indexOf('#') != -1) {
                return;
            }
            SchemeParser.launchUri(getContext(), url, true);
        }
    }

    private class ChromeClient extends WebChromeClient {
        @Override public void onProgressChanged(final WebView view, final int progress) {
            super.onProgressChanged(view, progress);
            if (onContentChangedListener != null) {
                onContentChangedListener.onContentChanged(progress);
            }
        }
    }

    private class WebClient extends WebViewClient {
        @Override public boolean shouldOverrideUrlLoading(final WebView view, final WebResourceRequest request) {
            startActivity(request.getUrl());
            return true;
        }
    }

    private class WebClientCompat extends WebViewClient {
        @SuppressWarnings("deprecation") @Override public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            startActivity(Uri.parse(url));
            return true;
        }

    }
}
