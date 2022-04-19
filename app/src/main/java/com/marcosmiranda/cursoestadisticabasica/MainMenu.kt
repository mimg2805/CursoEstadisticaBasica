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
        val showUnits = arrayOf(2, 4, 5)
        if (unidades.count > 0) {
            do {
                index = unidades.getColumnIndexOrThrow("idUnidad")
                val idUnidad = unidades.getInt(index)
                index = unidades.getColumnIndexOrThrow("titulo")
                val titulo = unidades.getString(index)

                // Log.e("idUnidad", idUnidad.toString())
                if (showUnits.contains(idUnidad)) {
                    val button = Button(this)
                    button.text = titulo//.toUpperCase()

                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(0, 0, 0, 40)
                    button.layoutParams = params
                    //button.setBackgroundColor(ContextCompat.getColor(this, R.color.button))
                    button.setBackgroundColor(Color.WHITE)
                    button.setTextColor(Color.BLACK)
                    button.textSize = 14f

                    button.setOnClickListener {
                        intent = Intent(this, Unidad::class.java)
                        intent.putExtra("idUnidad", idUnidad)
                        intent.putExtra("title", titulo)
                        this.startActivity(intent)
                    }
                    layout.addView(button)
                }
            } while (unidades.moveToNext())
        }

        unidades.close()
        db.close()
    }

    /*
    @Throws(IOException::class)
    private fun copyDB() {
        val outputFile = File(dbPath + dbName)
        if (outputFile.exists()) outputFile.delete()

        // Open your local db as the input stream
        val localDB = applicationContext.assets.open("databases/$dbName")

        // Path to the just created empty db
        val output = applicationContext.getDatabasePath(dbName)

        // Open the empty db as the output stream
        val outputStream = FileOutputStream(output)

        // transfer bytes from the input file to the output file
        val buffer = ByteArray(1024)
        var length = 0//localDB.read(buffer)
        do {
            outputStream.write(buffer, 0, length)
            length = localDB.read(buffer)
        } while (length > 0)

        // Close the streams
        outputStream.flush()
        outputStream.close()
        localDB.close()
    }
    */
}