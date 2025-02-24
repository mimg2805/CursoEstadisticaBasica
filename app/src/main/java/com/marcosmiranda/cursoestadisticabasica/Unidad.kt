package com.marcosmiranda.cursoestadisticabasica

import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Unidad : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var temas: Cursor

    private lateinit var adViewContainer : FrameLayout
    private lateinit var adView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidad)

        // Language
        val lang = Resources.getSystem().configuration.locales.get(0).language

        // Ads
        startAds()

        db = DBHelper(this, "CursoEstadisticaBasica.db", 1)
        val unidadId = intent.getIntExtra("unidadId", 0)
        val unidadNombre = intent.getStringExtra("unidadNombre")
        temas = db.getTemasByUnidad(unidadId)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = unidadNombre

        val layout: LinearLayout = findViewById(R.id.linLayout)
        var index: Int
        if (temas.count > 0) {
            do {
                index = temas.getColumnIndexOrThrow("id")
                val temaId = temas.getInt(index)
                index = temas.getColumnIndexOrThrow("nombre")
                val temaNombreEs = temas.getString(index)
                index = temas.getColumnIndexOrThrow("nombre_en")
                val temaNombreEn = temas.getString(index)
                index = temas.getColumnIndexOrThrow("html")
                val temaHtml = temas.getString(index)
                index = temas.getColumnIndexOrThrow("mostrar")

                val temaMostrar = temas.getInt(index) == 1
                if (!temaMostrar) continue

                val temaNombre = if (lang == "en") temaNombreEn
                else temaNombreEs

                val temaBtn = Button(this)
                temaBtn.text = temaNombre

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 0, 40)
                temaBtn.layoutParams = params
                temaBtn.setBackgroundColor(Color.WHITE)
                temaBtn.setTextColor(Color.BLACK)
                temaBtn.textSize = 14f

                temaBtn.setOnClickListener {
                    val subtemas = db.getSubtemasByTema(temaId)

                    if (subtemas.count == 0) {
                        intent = if (temaHtml.isEmpty()) {
                            when (temaId) {
                                10, 11      -> Intent(this, MatrizDatos::class.java)
                                12, 13, 14  -> Intent(this, MatrizDatos2Variables::class.java)
                                20          -> Intent(this, CalcTStudent::class.java)
                                21          -> Intent(this, CalcFFisher::class.java)
                                22          -> Intent(this, CalcChiCuadrado::class.java)
                                25          -> Intent(this, MatrizDatos::class.java)
                                else        -> Intent(this, Formula::class.java)
                            }
                        } else {
                            Intent(this, Formula::class.java)
                        }
                    } else {
                        intent = when (temaId) {
                            9, 10   -> Intent(this, MatrizDatos::class.java)
                            11, 12  -> Intent(this, MatrizDatos2Variables::class.java)
                            else    -> Intent(this, Tema::class.java)
                        }
                    }

                    intent.putExtra("temaId", temaId)
                    intent.putExtra("temaNombre", temaNombre)
                    this.startActivity(intent)
                }

                layout.addView(temaBtn)
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
            val conf = RequestConfiguration.Builder().setTestDeviceIds(listOf(getString(R.string.test_device_id))).build()
            MobileAds.setRequestConfiguration(conf)
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