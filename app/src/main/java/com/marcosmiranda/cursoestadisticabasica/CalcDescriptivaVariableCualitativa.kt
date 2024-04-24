package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class CalcDescriptivaVariableCualitativa : AppCompatActivity() {

    private val mc = MathContext(4, RoundingMode.HALF_EVEN)

    private var valuesStr = ""
    private var values: MutableMap<String, Int> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_descriptiva_variable_cualitativa)

        valuesStr = intent.getStringExtra("values") ?: ""
        val valuesArr = valuesStr.split(' ')
        val valuesSize = valuesArr.size
        valuesArr.forEach {str ->
            var num = values[str] ?: 0
            num++
            values[str] = num
        }

        var html =
            "<head>" +
                "<link rel='stylesheet' href='file:/android_asset/html/css/table.css'>" +
            "</head>" +
            "<body>" +
                "<p class='center'>Distribución de Frecuencias</p>" +
                "<table>" +
                    "<tr class='border'>" +
                        "<td>Variable</td>" +
                        "<td>No.</td>" +
                        "<td>%</td>" +
                    "</tr>"

        for ((str, num) in values) {
            val freq = BigDecimal((num.toDouble() / valuesSize.toDouble()) * 100, mc).setScale(1, RoundingMode.HALF_UP).toPlainString()
            html +=
                    "<tr>" +
                        "<td>$str</td>" +
                        "<td>$num</td>" +
                        "<td>$freq</td>" +
                    "</tr>"
        }

        html +=
                "<tr class='border'>" +
                    "<td>Total</td>" +
                    "<td>$valuesSize</td>" +
                    "<td>100.0</td>" +
                "</tr>" +
            "</table>" +
        "</body>"

        val wv: WebView = findViewById(R.id.activity_calc_descriptiva_variable_cualitativa_wv)
        wv.loadDataWithBaseURL(null, html,"text/html", "utf-8", null)
    }
}