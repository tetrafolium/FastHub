package com.fastaccess.github.editor

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebView

/**
 * Created by Kosh on 2019-04-11.
 */
class PrettifierWebView : WebView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun loadEditor(value: String? = null) {
        enableDefaultSettings()
        post { loadDataWithBaseURL("file:///android_asset/codeflask/", gethtmlContent(value ?: ""), "text/html", "utf-8", null) }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun enableDefaultSettings() {
        val settings = settings
        settings.javaScriptEnabled = true
        settings.setAppCachePath(context.cacheDir.path)
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.defaultTextEncodingName = "utf-8"
        settings.loadsImagesAutomatically = true
        settings.blockNetworkImage = false
    }

    companion object {
        private fun gethtmlContent(value: String): String {
            return "<html>\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;\"/></head>\n" +
                "\n" +
                "<body>\n" +
                "<div id=\"codeArea\">$value</div>\n" +
                "${getJsTags()}\n" +
                "</body>\n" +
                "\n" +
                "</html>"
        }

        private fun getJsTags(): String {
            return "<script src=\"./codeflask.js\"></script>\n" + "<script src=\"./index.js\"></script>\n"
        }
    }
}