package com.marcosmiranda.cursoestadisticabasica

import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
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

class Tema : AppCompatActivity() {

    lateinit var db: DBHelper
    private lateinit var subtemas: Cursor

    private lateinit var adViewContainer : FrameLayout
    private lateinit var adView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tema)

        // Language
        val lang = Resources.getSystem().configuration.locales.get(0).language

        // Ads
        startAds()

        db = DBHelper(this, "CursoEstadisticaBasica.db", 1)
        val temaId = intent.getIntExtra("temaId", 0)
        val temaNombre = intent.getStringExtra("temaNombre")
        val tabla2x2 = intent.getBooleanExtra("tabla2x2", false)
        Log.e("temaId", temaId.toString())
        Log.e("temaNombre", temaNombre.toString())
        subtemas = db.getSubtemasByTema(temaId)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = temaNombre

        val layout: LinearLayout = findViewById(R.id.linLayout)
        var index: Int
        if (subtemas.count > 0) {
            do {
                index = subtemas.getColumnIndexOrThrow("id")
                val subtemaId = subtemas.getInt(index)
                index = subtemas.getColumnIndexOrThrow("nombre")
                val subtemaNombreEs = subtemas.getString(index)
                index = subtemas.getColumnIndexOrThrow("nombre_en")
                val subtemaNombreEn = subtemas.getString(index)

                val hasSubsubtemas = db.getSubsubtemasBySubtema(subtemaId).count > 0

                val subtemaNombre = if (lang == "en") subtemaNombreEn
                else subtemaNombreEs
                Log.e("subtemaNombre", subtemaNombre)

                val subtemaBtn = Button(this)
                subtemaBtn.text = subtemaNombre

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 0, 40)
                subtemaBtn.layoutParams = params
                // button.setBackgroundColor(ContextCompat.getColor(this, R.color.button))
                subtemaBtn.setBackgroundColor(Color.WHITE)
                subtemaBtn.setTextColor(Color.BLACK)
                subtemaBtn.textSize = 14f

                subtemaBtn.setOnClickListener {
                    intent = if (subtemaId == 25) Intent(this, Tabla2x2::class.java)
                    else if (hasSubsubtemas) Intent(this, Subtema::class.java)
                    else Intent(this, Formula::class.java)
                    // intent = Intent(this, Formula::class.java)
                    intent.putExtra("subtemaId", subtemaId)
                    intent.putExtra("subtemaNombre", subtemaNombre)
                    this.startActivity(intent)
                }
                layout.addView(subtemaBtn)
            } while (subtemas.moveToNext())
        }

        subtemas.close()
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
            MobileAds.initialize(this@Tema)
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