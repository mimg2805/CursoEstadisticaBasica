package com.marcosmiranda.cursoestadisticabasica

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Formula : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var data: Cursor

    private lateinit var adViewContainer : FrameLayout
    private lateinit var adView : AdView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formula)

        // Language
        val lang = Resources.getSystem().configuration.locales.get(0).language

        // Ads
        startAds()

        db = DBHelper(this, "CursoEstadisticaBasica.db", 1)
        val subsubtemaId = intent.getIntExtra("subsubtemaId", 0)
        val subtemaId = intent.getIntExtra("subtemaId", 0)
        val temaId = intent.getIntExtra("temaId", 0)
        data = if (subsubtemaId > 0) {
            db.getHtmlOfSubsubtema(subsubtemaId)
        } else if (subtemaId > 0) {
            db.getHtmlOfSubtema(subtemaId)
        } else {
            db.getHtmlOfTema(temaId)
        }

        //supportActionBar?.title = "Test"//text
        val webView: WebView = findViewById(R.id.webView)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.visibility = View.INVISIBLE
        val button = findViewById<Button>(R.id.button)
        var index: Int

        if (data.count > 0) {
            index = data.getColumnIndexOrThrow("html")
            val htmlFileNameEs = data.getString(index)

            index = data.getColumnIndexOrThrow("html_en")
            val htmlFileNameEn = data.getString(index)

            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    // button.visibility = View.VISIBLE
                    view?.visibility = View.VISIBLE
                }
            }

            var htmlFilePath = "file:///android_asset/html/"
            htmlFilePath += if (lang == "en") "html_en/${htmlFileNameEn}"
            else "html_es/${htmlFileNameEs}"
            webView.loadUrl(htmlFilePath)

            if (subsubtemaId > 0) {
                intent = when (subsubtemaId) {
                    1       -> Intent(this, MuestreoAleatorioSimple::class.java)
                    2       -> Intent(this, MuestreoAleatorioSistematico::class.java)
                    3       -> Intent(this, MuestreoAleatorioEstratificado::class.java)
                    4       -> Intent(this, MuestreoAleatorioConglomerados::class.java)
                    else    -> null
                }
            } else if (subtemaId > 0) {
                when (subtemaId) {
                    1       -> intent = Intent(this, CalcTamanioMuestraProporcion::class.java)
                    2       -> intent = Intent(this, CalcTamanioMuestraMedia::class.java)
                    3       -> intent = Intent(this, CalcTamanioMuestra2ProporcionesPoblaciones::class.java)
                    4       -> intent = Intent(this, CalcTamanioMuestra2MediasPoblaciones::class.java)
                    5       -> intent = Intent(this, CalcProbTeorica::class.java)
                    6       -> intent = Intent(this, CalcProbEmpirica::class.java)
                    7       -> intent = Intent(this, CalcReglaComplemento::class.java)
                    8       -> intent = Intent(this, CalcReglaAdicion::class.java)
                    9       -> intent = Intent(this, CalcReglaMultiplicacion::class.java)
                    10      -> intent = Intent(this, CalcTeoremaBayesSimple::class.java)
                    11      -> intent = Intent(this, CalcDistNormalEstandarizacion::class.java)
                    12      -> intent = Intent(this, CalcDistNormalProbabilidades::class.java)
                    13      -> intent = Intent(this, CalcIntervaloConfianzaProporcionPoblacionDesconocida::class.java)
                    14      -> intent = Intent(this, CalcIntervaloConfianzaProporcionPoblacionConocida::class.java)
                    15      -> intent = Intent(this, CalcIntervaloConfianzaMediaPoblacionDesconocida::class.java)
                    16      -> intent = Intent(this, CalcIntervaloConfianzaMediaPoblacionConocida::class.java)
                    17, 18  -> {
                        val subtemaNombre = intent.getStringExtra("subtemaNombre")
                        intent = Intent(this, Subtema::class.java)
                        intent.putExtra("subtemaId", subtemaId)
                        intent.putExtra("subtemaNombre", subtemaNombre)
                    }
                    else    -> intent = null
                }
            } else {
                intent = when (temaId) {
                    8       -> Intent(this, RNG::class.java)
                    9       -> Intent(this, CalcDistMuestra::class.java)
                    16      -> Intent(this, CalcDistBinomial::class.java)
                    17      -> Intent(this, CalcDistPoisson::class.java)
                    18      -> Intent(this, CalcDistHipergeometrica::class.java)
                    23      -> Intent(this, CalcIntervaloConfianzaProporcionPoblacionDesconocida::class.java)
                    24      -> Intent(this, CalcIntervaloConfianzaMediaPoblacionDesconocida::class.java)
                    25      -> Intent(this, CalcPruebaHipotesisProporcionPoblacional::class.java)
                    26      -> Intent(this, CalcPruebaHipotesisMediaPoblacional::class.java)
                    27      -> Intent(this, CalcPruebaHipotesis2ProporcionesPoblacionales::class.java)
                    28      -> Intent(this, CalcPruebaHipotesis2MediasPoblacionales::class.java)
                    else    -> null
                }
            }

            if (intent != null) {
                button.visibility = View.VISIBLE
                if (subtemaId == 18) {
                    button.text = getString(R.string.view)
                }
                button.setOnClickListener {
                    this.startActivity(intent)
                }
            }
        }

        data.close()
        db.close()
    }

    public override fun onPause() {
        adView.pause()
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        adView.resume()
    }

    public override fun onDestroy() {
        adView.destroy()
        super.onDestroy()
        val button: Button = findViewById(R.id.button)
        button.visibility = View.INVISIBLE
    }

    private fun startAds()
    {
        // Initialize the Google Mobile Ads SDK on a background thread.
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            val conf = RequestConfiguration.Builder().setTestDeviceIds(listOf("BE89C404157C24CCDB17A860A9B5B878")).build()
            MobileAds.setRequestConfiguration(conf)
            MobileAds.initialize(this@Formula)
        }

        // Create a new ad view.
        adView = AdView(this)
        adView.setAdSize(adSize)
        adView.adUnitId = getString(R.string.banner_ad_id)

        adViewContainer = findViewById(R.id.flAdsContainer)
        adViewContainer.addView(adView)

        // Create an ad request.
        val adRequest = AdRequest.Builder().build()

        // Start loading the ad in the background.
        adView.loadAd(adRequest)
    }

    private val adSize: AdSize
    get() {
        // Determine the screen width (less decorations) to use for the ad width.
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = outMetrics.density

        // If the ad hasn't been laid out, default to the full screen width.
        var adWidthPixels = adView.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }

        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
    }
}