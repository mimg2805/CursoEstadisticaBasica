package com.marcosmiranda.cursoestadisticabasica

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.round

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

        val htmlBackColor = Integer.toHexString(ContextCompat.getColor(this, R.color.gray)).substring(2)
        var html =
            "<style>" +
                "body { background-color: #$htmlBackColor; }" +
                ".right { text-align: right; }" +
                ".center { text-align: center; }" +
                "table { width: 90%; border-collapse: collapse; margin: 40% auto; }" +
                "td { padding: 0.5rem; }" +
                "tr.border td { border-top: 1px dotted black; border-bottom: 1px dotted black; }" +
            "</style>" +
            "<p class='center'>Distribución de Frecuencias</p>" +
            "<table>" +
                "<tr class='border'>" +
                    "<td>Variable</td>" +
                    "<td>No.</td>" +
                    "<td>%</td>" +
                "</tr>"

        for ((str, num) in values) {
            val freq = BigDecimal((num.toDouble() / valuesSize.toDouble()) * 100, mc).toString()
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
        "</table>"

        val wv: WebView = findViewById(R.id.activity_calc_descriptiva_variable_cualitativa_wv)
        wv.loadDataWithBaseURL(null, html,"text/html", "utf-8", null)
    }
}