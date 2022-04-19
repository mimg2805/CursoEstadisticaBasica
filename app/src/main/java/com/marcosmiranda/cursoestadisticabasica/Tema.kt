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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tema)

        db = DBHelper(this, "CursoEstadisticaBasica.db", 1)
        val idTema = intent.getIntExtra("idTema", 0)
        val title = intent.getStringExtra("title")
        if (title != null) {
            Log.e("title", title)
        }
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
}