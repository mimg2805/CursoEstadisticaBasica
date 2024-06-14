package com.marcosmiranda.cursoestadisticabasica

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class Formula : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var data: Cursor

    private lateinit var adViewContainer : FrameLayout
    private lateinit var adView : AdView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formula)

        // Ads
        startAds()

        db = DBHelper(this, "CursoEstadisticaBasica.db", 1)
        // val text = intent.getStringExtra("text")
        // Log.e("text", text)
        val sub = intent.getBooleanExtra("sub", false)
        var idTema = 0
        var idSubTema = 0
        if (sub) {
            idSubTema = intent.getIntExtra("idSubTema", 0)
            data = db.getTxtOfSubTema(idSubTema)
        } else {
            idTema = intent.getIntExtra("idTema", 0)
            data = db.getTxtOfTema(idTema)
        }

        //supportActionBar?.title = "Test"//text
        val webView: WebView = findViewById(R.id.webView)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.visibility = View.INVISIBLE
        val button: Button = findViewById(R.id.button)
        val index: Int
        // Log.e("idTema", idTema.toString())
        // Log.e("idSubTema", idSubTema.toString())
        // Log.e("data", data.toString())
        if (data.count > 0) {
            index = data.getColumnIndexOrThrow("html")
            // Log.e("index", index.toString())
            val htmlFileName = data.getString(index)
            // Log.e("file", htmlFileName)

            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    button.visibility = View.VISIBLE
                    view?.visibility = View.VISIBLE
                }
            }

            val htmlFilePath = "file:///android_asset/html/$htmlFileName"
            // val htmlFile = File(htmlFilePath)
            // Log.e("Exists?", htmlFile.exists().toString())
            webView.loadUrl(htmlFilePath)

            if (sub) {
                intent = when (idSubTema) {
                    1       -> Intent(this, CalcTamanioMuestraProporcion::class.java)
                    2       -> Intent(this, CalcTamanioMuestraMedia::class.java)
                    3       -> Intent(this, CalcProbTeorica::class.java)
                    4       -> Intent(this, CalcProbEmpirica::class.java)
                    5       -> Intent(this, CalcReglaComplemento::class.java)
                    6       -> Intent(this, CalcReglaAdicion::class.java)
                    7       -> Intent(this, CalcReglaMultiplicacion::class.java)
                    8       -> Intent(this, CalcDistNormalEstandarizacion::class.java)
                    9       -> Intent(this, CalcDistNormalProbabilidades::class.java)
                    10      -> Intent(this, CalcIntervaloConfianzaProporcionPoblacionDesconocida::class.java)
                    11      -> Intent(this, CalcIntervaloConfianzaProporcionPoblacionConocida::class.java)
                    12      -> Intent(this, CalcIntervaloConfianzaMediaPoblacionDesconocida::class.java)
                    13      -> Intent(this, CalcIntervaloConfianzaMediaPoblacionConocida::class.java)
                    14      -> Intent(this, CalcTeoremaBayesSimple::class.java)
                    15      -> Intent(this, CalcTamanioMuestra2ProporcionesPoblaciones::class.java)
                    16      -> Intent(this, CalcTamanioMuestra2MediasPoblaciones::class.java)
                    else    -> null
                }
            } else {
                intent = when (idTema) {
                    1       -> null
                    2       -> null
                    3       -> null
                    4       -> null
                    5       -> null
                    6       -> null
                    7       -> Intent(this, RNG::class.java)
                    8       -> Intent(this, CalcDistMuestra::class.java)
                    9       -> null
                    10      -> null
                    11      -> null
                    12      -> null
                    13      -> null
                    14      -> null
                    15      -> Intent(this, CalcDistBinomial::class.java)
                    16      -> Intent(this, CalcDistPoisson::class.java)
                    17      -> Intent(this, CalcDistHipergeometrica::class.java)
                    18      -> null
                    19      -> null // intent = Intent(this, CalcIntervaloConfianzaProporcion::class.java)
                    20      -> null // intent = Intent(this, CalcIntervaloConfianzaMedia::class.java)
                    21      -> null
                    22      -> Intent(this, CalcIntervaloConfianzaProporcionPoblacionDesconocida::class.java)
                    23      -> Intent(this, CalcIntervaloConfianzaMediaPoblacionDesconocida::class.java)
                    24      -> Intent(this, CalcPruebaHipotesisProporcionPoblacional::class.java)
                    25      -> Intent(this, CalcPruebaHipotesisMediaPoblacional::class.java)
                    26      -> Intent(this, CalcPruebaHipotesis2ProporcionesPoblacionales::class.java)
                    27      -> Intent(this, CalcPruebaHipotesis2MediasPoblacionales::class.java)
                    else    -> null
                }
            }

            if (intent == null) {
                button.isEnabled = false
            } else {
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