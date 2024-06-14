package com.marcosmiranda.cursoestadisticabasica

import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class Tema : AppCompatActivity() {

    lateinit var db: DBHelper
    private lateinit var subtemas: Cursor

    private lateinit var adViewContainer : FrameLayout
    private lateinit var adView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tema)

        // Ads
        startAds()

        db = DBHelper(this, "CursoEstadisticaBasica.db", 1)
        val idTema = intent.getIntExtra("idTema", 0)
        val title = intent.getStringExtra("title")
        val values = intent.getStringExtra("values")
        subtemas = db.getSubTemasByTema(idTema)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = title

        val layout: LinearLayout = findViewById(R.id.linLayout)
        var index: Int
        if (subtemas.count > 0) {
            do {
                val button = Button(this)
                index = subtemas.getColumnIndexOrThrow("idSubTema")
                val idSubTema = subtemas.getInt(index)
                index = subtemas.getColumnIndexOrThrow("subtema")
                val subtema = subtemas.getString(index)
                button.text = subtema//.toUpperCase()

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
                    intent = Intent(this, Formula::class.java)
                    intent.putExtra("sub", true)
                    intent.putExtra("idSubTema", idSubTema)
                    intent.putExtra("title", subtema)
                    this.startActivity(intent)
                }
                layout.addView(button)
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