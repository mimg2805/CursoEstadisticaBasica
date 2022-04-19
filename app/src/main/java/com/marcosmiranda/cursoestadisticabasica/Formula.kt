package com.marcosmiranda.cursoestadisticabasica

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Formula : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var data: Cursor

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formula)

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

            val htmlPath = "file:/android_asset/html/"
            val htmlFilePath = "$htmlPath$htmlFileName"
            // val htmlFile = File(htmlFilePath)
            // Log.e("Exists?", htmlFile.exists().toString())
            webView.loadUrl(htmlFilePath)

            if (sub) {
                when (idSubTema) {
                    1 -> intent = Intent(this, CalcTamanioMuestraProporcion::class.java)
                    2 -> intent = Intent(this, CalcTamanioMuestraMedia::class.java)
                    3 -> intent = Intent(this, CalcProbTeorica::class.java)
                    4 -> intent = Intent(this, CalcProbEmpirica::class.java)
                    5 -> intent = Intent(this, CalcReglaComplemento::class.java)
                    6 -> intent = Intent(this, CalcReglaAdicion::class.java)
                    7 -> intent = Intent(this, CalcReglaMultiplicacion::class.java)
                    8 -> intent = Intent(this, CalcDistNormalEstandarizacion::class.java)
                    9 -> intent = Intent(this, CalcDistNormalProbabilidades::class.java)
                    10 -> intent = Intent(this, CalcIntervaloConfianzaProporcionPoblacionDesconocida::class.java)
                    11 -> intent = Intent(this, CalcIntervaloConfianzaProporcionPoblacionConocida::class.java)
                    12 -> intent = Intent(this, CalcIntervaloConfianzaMediaPoblacionDesconocida::class.java)
                    13 -> intent = Intent(this, CalcIntervaloConfianzaMediaPoblacionConocida::class.java)
                }
            } else {
                when (idTema) {
                    1 -> button.isEnabled = false
                    2 -> button.isEnabled = false
                    3 -> button.isEnabled = false
                    4 -> button.isEnabled = false
                    5 -> button.isEnabled = false
                    6 -> button.isEnabled = false
                    7 -> intent = Intent(this, RNG::class.java)
                    8 -> intent = Intent(this, CalcDistMuestra::class.java)
                    9 -> button.isEnabled = false
                    10 -> button.isEnabled = false
                    11 -> button.isEnabled = false
                    12 -> button.isEnabled = false
                    13 -> button.isEnabled = false
                    14 -> button.isEnabled = false
                    15 -> intent = Intent(this, CalcDistBinomial::class.java)
                    16 -> intent = Intent(this, CalcDistPoisson::class.java)
                    17 -> intent = Intent(this, CalcDistHipergeometrica::class.java)
                    18 -> button.isEnabled = false
                    19 -> button.isEnabled = false // intent = Intent(this, CalcIntervaloConfianzaProporcion::class.java)
                    20 -> button.isEnabled = false // intent = Intent(this, CalcIntervaloConfianzaMedia::class.java)
                    21 -> button.isEnabled = false
                    22 -> intent = Intent(this, CalcIntervaloConfianzaProporcionPoblacionDesconocida::class.java)
                    23 -> intent = Intent(this, CalcIntervaloConfianzaMediaPoblacionDesconocida::class.java)
                    24 -> button.isEnabled = false
                    25 -> button.isEnabled = false
                    26 -> button.isEnabled = false
                }
            }
            button.setOnClickListener {
                this.startActivity(intent)
            }
        }

        data.close()
        db.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        val button: Button = findViewById(R.id.button)
        button.visibility = View.INVISIBLE
    }
}