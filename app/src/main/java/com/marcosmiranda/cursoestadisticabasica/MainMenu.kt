package com.marcosmiranda.cursoestadisticabasica

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.io.File

class MainMenu : AppCompatActivity() {

    private lateinit var dbPath: String
    private val dbName = "CursoEstadisticaBasica.db"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

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
                index = unidades.getColumnIndexOrThrow("idUnidad")
                val idUnidad = unidades.getInt(index)
                index = unidades.getColumnIndexOrThrow("titulo")
                val titulo = unidades.getString(index)
                index = unidades.getColumnIndexOrThrow("show")
                val show = unidades.getInt(index) == 1

                // Log.e("idUnidad", idUnidad.toString())
                if (!show) continue

                val unitBtn = Button(this)
                unitBtn.text = titulo//.toUpperCase()

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
                    intent.putExtra("idUnidad", idUnidad)
                    intent.putExtra("title", titulo)
                    this.startActivity(intent)
                }
                layout.addView(unitBtn)
            } while (unidades.moveToNext())
        }

        unidades.close()
        db.close()

        // Botón de Más apps
        val masAppsBtn = Button(this)
        masAppsBtn.text = getString(R.string.mas_apps)

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
            intent = Intent(this, MasApps::class.java)
            this.startActivity(intent)
        }
        layout.addView(masAppsBtn)
    }
}