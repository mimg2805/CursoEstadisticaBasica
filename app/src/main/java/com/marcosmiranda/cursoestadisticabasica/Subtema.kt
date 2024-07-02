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

class Subtema : AppCompatActivity() {

    lateinit var db: DBHelper
    private lateinit var subsubtemas: Cursor

    private lateinit var adViewContainer : FrameLayout
    private lateinit var adView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subtema)

        // Language
        val lang = Resources.getSystem().configuration.locales.get(0).language

        // Ads
        startAds()

        db = DBHelper(this, "CursoEstadisticaBasica.db", 1)
        val subtemaId = intent.getIntExtra("subtemaId", 0)
        val subtemaNombre = intent.getStringExtra("subtemaNombre")
        subsubtemas = db.getSubsubtemasBySubtema(subtemaId)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = subtemaNombre

        val layout: LinearLayout = findViewById(R.id.linLayout)
        var index: Int
        if (subsubtemas.count > 0) {
            do {
                index = subsubtemas.getColumnIndexOrThrow("id")
                val subsubtemaId = subsubtemas.getInt(index)
                index = subsubtemas.getColumnIndexOrThrow("nombre")
                val subsubtemaNombreEs = subsubtemas.getString(index)
                index = subsubtemas.getColumnIndexOrThrow("nombre_en")
                val subsubtemaNombreEn = subsubtemas.getString(index)

                val subsubtemaNombre = if (lang == "en") subsubtemaNombreEn
                else subsubtemaNombreEs

                val subsubtemasBtn = Button(this)
                subsubtemasBtn.text = subsubtemaNombre

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 0, 40)
                subsubtemasBtn.layoutParams = params
                subsubtemasBtn.setBackgroundColor(Color.WHITE)
                subsubtemasBtn.setTextColor(Color.BLACK)
                subsubtemasBtn.textSize = 14f

                subsubtemasBtn.setOnClickListener {
                    intent = Intent(this, Formula::class.java)
                    intent.putExtra("subsubtemaId", subsubtemaId)
                    intent.putExtra("subsubtemaNombre", subsubtemaNombre)
                    this.startActivity(intent)
                }
                layout.addView(subsubtemasBtn)
            } while (subsubtemas.moveToNext())
        }

        subsubtemas.close()
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
            val conf = RequestConfiguration.Builder().setTestDeviceIds(listOf("BE89C404157C24CCDB17A860A9B5B878")).build()
            MobileAds.setRequestConfiguration(conf)
            MobileAds.initialize(this@Subtema)
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