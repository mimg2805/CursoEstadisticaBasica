package com.marcosmiranda.cursoestadisticabasica

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
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
import java.io.File

class MainMenu : AppCompatActivity() {

    private lateinit var dbPath: String
    private val dbName = "CursoEstadisticaBasica.db"

    private lateinit var adViewContainer : FrameLayout
    private lateinit var adView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // Language
        val lang = Resources.getSystem().configuration.locales.get(0).language

        // Ads
        startAds()

        val pkgName = applicationContext.packageName
        dbPath = "data/data/$pkgName/databases/$dbName"
        val db = DBHelper(this, dbName, 1)
        val unidades = db.getUnidades()

        val prefs = getSharedPreferences(pkgName, MODE_PRIVATE)
        val firstTime = prefs.getBoolean("1stTime", true)
        if (firstTime) {
            prefs.edit().putBoolean("1stTime", false).apply()
        } else {
            val outputFile = File(dbPath)
            if (outputFile.exists()) outputFile.delete()
            /*try {
                copyDB()
            } catch (e: IOException) {
                e.printStackTrace()
            }*/
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val layout: LinearLayout = findViewById(R.id.linLayout)
        var index: Int
        if (unidades.count > 0) {
            do {
                index = unidades.getColumnIndexOrThrow("id")
                val unidadId = unidades.getInt(index)
                index = unidades.getColumnIndexOrThrow("nombre")
                val unidadNombreEs = unidades.getString(index)
                index = unidades.getColumnIndexOrThrow("nombre_en")
                val unidadNombreEn = unidades.getString(index)
                index = unidades.getColumnIndexOrThrow("mostrar")

                val unidadMostrar = unidades.getInt(index) == 1
                if (!unidadMostrar) continue

                val unidadNombre = if (lang == "en") unidadNombreEn
                else unidadNombreEs

                val unitBtn = Button(this)
                unitBtn.text = unidadNombre

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 0, 40)
                unitBtn.layoutParams = params
                //button.setBackgroundColor(ContextCompat.getColor(this, R.color.button))
                unitBtn.setBackgroundColor(Color.WHITE)
                unitBtn.setTextColor(Color.BLACK)
                unitBtn.textSize = 14f

                unitBtn.setOnClickListener {
                    intent = Intent(this, Unidad::class.java)
                    intent.putExtra("unidadId", unidadId)
                    intent.putExtra("unidadNombre", unidadNombre)
                    this.startActivity(intent)
                }
                layout.addView(unitBtn)
            } while (unidades.moveToNext())
        }

        unidades.close()
        db.close()

        // Botón de Más apps
        val masAppsBtn = Button(this)
        masAppsBtn.text = getString(R.string.more_apps)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 40)
        masAppsBtn.layoutParams = params
        //button.setBackgroundColor(ContextCompat.getColor(this, R.color.button))
        masAppsBtn.setBackgroundColor(Color.WHITE)
        masAppsBtn.setTextColor(Color.BLACK)
        masAppsBtn.textSize = 14f

        masAppsBtn.setOnClickListener {
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.developer_url)))
            this.startActivity(intent)
        }
        layout.addView(masAppsBtn)
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
            MobileAds.initialize(this@MainMenu)
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