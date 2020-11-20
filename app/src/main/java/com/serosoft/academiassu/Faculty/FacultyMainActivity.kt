package com.serosoft.academiassu

import android.annotation.SuppressLint
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import com.serosoft.academiassu.Helpers.Consts
import com.serosoft.academiassu.Helpers.Permissions
import com.serosoft.academiassu.Modules.Login.LoginActivity
import com.serosoft.academiassu.Networking.KEYS
import com.serosoft.academiassu.Utils.BaseActivity
import com.serosoft.academiassu.Utils.ProjectUtils
import kotlinx.android.synthetic.main.faculty_activity_webview.*


class FacultyMainActivity : BaseActivity() {

    private val TAG = FacultyMainActivity::class.java.simpleName
    private val mWebViewClient = HelloWebViewClient()
    var handler = Handler()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.faculty_activity_webview)
        supportActionBar?.hide()

        ProjectUtils.changeStatusBarColorNew(this@FacultyMainActivity, R.color.colorFaculty);

        val mWebView = findViewById<WebView>(R.id.webview)

        showProgressDialog(this@FacultyMainActivity)

        val webSettings = mWebView.settings
        webSettings.javaScriptEnabled = true
        webSettings.setAppCacheEnabled(true)
        webSettings.databaseEnabled = true
        webSettings.domStorageEnabled = true
        WebView.setWebContentsDebuggingEnabled(true)
        mWebView.webViewClient = mWebViewClient
        var url = KEYS.FACULTY_WEBSITE_URL+"?token="+sharedPrefrenceManager.accessTokenFromKey;
        mWebView.loadUrl(url)

        handler.postDelayed(runnable, Consts.SPLASH_TIME_OUT.toLong())
    }

    var runnable = Runnable {
        hideProgressDialog()
    }

    private inner class HelloWebViewClient : WebViewClient() {

        override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {

            val originalURL = view!!.originalUrl

            if (originalURL.contains("login") && !originalURL.contains("?token")){
                sharedPrefrenceManager.setUserLoginStatusInSP(false)
                sharedPrefrenceManager.setIsFacultyStatusInSP(false)
                sharedPrefrenceManager.setUserGLoginStatusInSP(false)
                sharedPrefrenceManager.clearPreferences(Permissions.MODULE_PERMISSION)
                sharedPrefrenceManager.clearPreferences(Consts.TRANSLATION_KEYS)
                sharedPrefrenceManager.clearPreferences(Consts.REQUEST_ID_LIST)
                dbHelper.deleteCurrencyDB()

                val intent = Intent(this@FacultyMainActivity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            super.doUpdateVisitedHistory(view, url, isReload)
        }

        override fun onPageFinished(view: WebView, url: String) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url)
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            val builder = AlertDialog.Builder(this@FacultyMainActivity)
            var message = "SSL Certificate error."
            when (error.primaryError) {
                SslError.SSL_UNTRUSTED -> message = "The certificate authority is not trusted."
                SslError.SSL_EXPIRED -> message = "The certificate has expired."
                SslError.SSL_IDMISMATCH -> message = "The certificate Hostname mismatch."
                SslError.SSL_NOTYETVALID -> message = "The certificate is not yet valid."
            }
            message += " Do you want to continue anyway?"

            builder.setTitle("SSL Certificate Error")
            builder.setMessage(message)
            builder.setPositiveButton("Continue") { dialog, which -> handler.proceed() }
            builder.setNegativeButton("Cancel") { dialog, which -> handler.cancel() }
            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
