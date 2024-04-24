package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class CalcDescriptiva2VariablesCualitativas : AppCompatActivity() {

    private var valuesX: MutableMap<String, Int> = mutableMapOf()
    private var valuesY: MutableMap<String, Int> = mutableMapOf()
    private var valuesXY: MutableMap<String, Int> = mutableMapOf()
    private val xySeparator = "&"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_descriptiva_2_variables_cualitativas)

        var valuesXStr = intent.getStringExtra("valuesX") ?: ""
        if (valuesXStr.isEmpty()) valuesXStr = "M M M M M M F F F F F F"
        val valuesXArr = valuesXStr.split(" ")
        val totalNum = valuesXArr.count()
        valuesXArr.forEach { str ->
            var num = valuesX[str] ?: 0
            num++
            valuesX[str] = num
        }

        var valuesYStr = intent.getStringExtra("valuesY") ?: ""
        if (valuesYStr.isEmpty()) valuesYStr = "U U U R R R U U U U R R"
        val valuesYArr = valuesYStr.split(" ")
        valuesYArr.forEach { str ->
            var num = valuesY[str] ?: 0
            num++
            valuesY[str] = num
        }

        for (i in 0..< totalNum) {
            val varX = valuesXArr[i]
            val varY = valuesYArr[i]
            val index = varX + xySeparator + varY
            var num = valuesXY[index] ?: 0
            num++
            valuesXY[index] = num
        }


        var html =
            "<head>" +
                "<link rel='stylesheet' href='file:/android_asset/html/css/table.css'>" +
            "</head>" +
            "<body>" +
                "<table>" +
                    "<tr class='border'>" +
                        "<td>Variable X</td>"

        for ((varX, _) in valuesX) {
            html +=     "<td>$varX</td>"
        }

        html +=
                        "<td>Total</td>" +
                    "</tr>" +
                    "<tr>" +
                        "<td>Variable Y</td>" +
                        "<td></td>" +
                        "<td></td>" +
                        "<td></td>" +
                    "</tr>"

        for ((varY, numY) in valuesY) {
            html +=
                    "<tr>" +
                        "<td class='right'>$varY</td>"

            for ((varX, _) in valuesX) {
                val index = varX + xySeparator + varY
                html +=
                        "<td>" + (valuesXY[index] ?: 0) + "</td>"
            }

            html +=
                        "<td>$numY</td>" +
                    "</tr>"
        }

        html +=
                    "<tr class='border'>" +
                        "<td class='right'>Total</td>"

        for ((_, numX) in valuesX) {
            html +=     "<td>$numX</td>"
        }

        html +=
                        "<td>$totalNum</td>" +
                    "</tr>" +
                "</table>" +
            "</body>"

        val wv: WebView = findViewById(R.id.activity_calc_descriptiva_2_variables_cualitativas_wv)
        wv.loadDataWithBaseURL(null, html,"text/html", "utf-8", null)
    }
}