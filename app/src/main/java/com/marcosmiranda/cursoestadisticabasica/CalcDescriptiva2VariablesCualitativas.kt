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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_descriptiva_2_variables_cualitativas)

        var valuesXStr = intent.getStringExtra("valuesX") ?: ""
        // var valuesXStr = intent.getStringExtra("valuesX") ?: ""
        // if (valuesXStr.isEmpty()) valuesXStr = "M M M M M M M M M F F F F F F F F F F F"
        // if (valuesXStr.isEmpty()) valuesXStr = "M M M M M M M M M F F F F F F F F F T T T"
        val valuesXArr = valuesXStr.split(" ")
        val totalNum = valuesXArr.count()
        valuesXArr.forEach { str ->
            var num = valuesX[str] ?: 0
            num++
            valuesX[str] = num
        }

        var valuesYStr = intent.getStringExtra("valuesY") ?: ""
        // var valuesYStr = intent.getStringExtra("valuesY") ?: ""
        // if (valuesYStr.isEmpty()) valuesYStr = "U U U U R R R R R U U U U U R R R R R R"
        // if (valuesYStr.isEmpty()) valuesYStr = "U U U U R R R R C U U U U U R R R C U R C"
        val valuesYArr = valuesYStr.split(" ")
        valuesYArr.forEach { str ->
            var num = valuesY[str] ?: 0
            num++
            valuesY[str] = num
        }

        for (i in 0..< totalNum) {
            val varX = valuesXArr[i]
            val varY = valuesYArr[i]
            val index = "$varX&$varY"
            var num = valuesXY[index] ?: 0
            num++
            valuesXY[index] = num
        }

        Log.e("valuesX", valuesX.toString())
        Log.e("valuesY", valuesY.toString())
        Log.e("valuesXY", valuesXY.toString())

        val htmlBackColor = Integer.toHexString(ContextCompat.getColor(this, R.color.gray)).substring(2)
        var html =
            "<style>" +
                "body { background-color: #$htmlBackColor; }" +
                ".right { text-align: right; }" +
                "table { border-collapse: collapse; margin: 10% auto; }" +
                "td { padding: 0.5rem; }" +
                "tr.border td { border-top: 1px dotted black; border-bottom: 1px dotted black; }" +
            "</style>" +
            "<table>" +
                "<tr class='border'>" +
                    "<td>Variable X</td>"

        for ((varX, _) in valuesX) {
            html += "<td>$varX</td>"
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
                val index = "$varX&$varY"
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
            html += "<td>$numX</td>"
        }

        html +=
                    "<td>$totalNum</td>" +
            "</tr>" +
        "</table>"

        val wv: WebView = findViewById(R.id.activity_calc_descriptiva_2_variables_cualitativas_wv)
        wv.loadDataWithBaseURL(null, html,"text/html", "utf-8", null)
    }
}