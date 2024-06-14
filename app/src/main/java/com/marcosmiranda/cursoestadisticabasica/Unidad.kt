package com.marcosmiranda.cursoestadisticabasica

import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class Unidad : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var temas: Cursor

    private lateinit var adViewContainer : FrameLayout
    private lateinit var adView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidad)

        // Ads
        startAds()

        db = DBHelper(this, "CursoEstadisticaBasica.db", 1)
        val idUnidad = intent.getIntExtra("idUnidad", 0)
        val title = intent.getStringExtra("title")
        temas = db.getTemasByUnidad(idUnidad)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = title

        val layout: LinearLayout = findViewById(R.id.linLayout)
        var index: Int
        if (temas.count > 0) {
            do {
                val button = Button(this)
                index = temas.getColumnIndexOrThrow("idTema")
                val idTema = temas.getInt(index)
                index = temas.getColumnIndexOrThrow("tema")
                val tema = temas.getString(index)
                index = temas.getColumnIndexOrThrow("html")
                val html = temas.getString(index)
                index = temas.getColumnIndexOrThrow("show")
                val show = temas.getInt(index) == 1
                button.text = tema//.toUpperCase()
                // Log.e("idTema", idTema.toString())

                if (!show) continue

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 0, 40)
                button.layoutParams = params
                // button.setBackgroundColor(ContextCompat.getColor(this, R.color.button))
                button.setBackgroundColor(Color.WHITE)
                button.setTextColor(Color.BLACK)
                button.textSize = 14f

                button.setOnClickListener {
                    val subtemas = db.getSubTemasByTema(idTema)

                    if (subtemas.count == 0) {
                        intent = if (html.isEmpty()) {
                            when (idTema) {
                                9       -> Intent(this, MatrizDatos::class.java)
                                10      -> Intent(this, MatrizDatos::class.java)
                                11      -> Intent(this, MatrizDatos2Variables::class.java)
                                12      -> Intent(this, MatrizDatos2Variables::class.java)
                                13      -> Intent(this, MatrizDatos2Variables::class.java)
                                19      -> Intent(this, CalcTStudent::class.java)
                                20      -> Intent(this, CalcFFisher::class.java)
                                21      -> Intent(this, CalcChiCuadrado::class.java)
                                else    -> Intent(this, Formula::class.java)
                            }
                        } else {
                            Intent(this, Formula::class.java)
                        }
                    } else {
                        intent = when (idTema) {
                            9, 10   -> Intent(this, MatrizDatos::class.java)
                            11, 12  -> Intent(this, MatrizDatos2Variables::class.java)
                            else    -> Intent(this, Tema::class.java)
                        }
                    }

                    intent.putExtra("idTema", idTema)
                    intent.putExtra("title", tema)
                    this.startActivity(intent)
                }

                layout.addView(button)
            } while (temas.moveToNext())
        }

        temas.close()
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
    }

    private fun startAds()
    {
        // Initialize the Google Mobile Ads SDK on a background thread.
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            MobileAds.initialize(this@Unidad)
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