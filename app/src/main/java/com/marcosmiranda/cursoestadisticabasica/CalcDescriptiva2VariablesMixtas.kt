package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.desviacionEstandar
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.desviacionMedia
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.media

class CalcDescriptiva2VariablesMixtas : AppCompatActivity() {

    private var valuesX: List<String> = listOf()
    private var valuesY: List<BigDecimal> = listOf()
    private var valuesXY: MutableMap<String, MutableList<BigDecimal>> = mutableMapOf()
    private var check2YPerX = true
    private val mc = MathContext(3, RoundingMode.HALF_EVEN)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_descriptiva_2_variables_mixtas)

        var valuesXStr = intent.getStringExtra("valuesX") ?: ""
        if (valuesXStr.isEmpty()) valuesXStr = "U R R R R U U"
        // if (valuesXStr.isEmpty()) valuesXStr = "U R R R R R R"
        valuesX = valuesXStr.split(" ")
        val totalNum = valuesX.count()

        var valuesYStr = intent.getStringExtra("valuesY") ?: ""
        if (valuesYStr.isEmpty()) valuesYStr = "2 3 2 4 3 5 6"
        valuesY = valuesYStr.split(" ").map { str -> strToBigDecimal(str) }

        for (i in 0..< totalNum) {
            val varX = valuesX[i]
            val varY = valuesY[i]
            val varXY = valuesXY[varX] ?: mutableListOf()
            varXY.add(varY)
            valuesXY[varX] = varXY
        }

        for ((_, listY) in valuesXY) {
            if (listY.count() < 2) {
                check2YPerX = false
                break
            }
        }

        Log.e("x", valuesX.toString())
        Log.e("y", valuesY.toString())
        Log.e("xy", valuesXY.toString())

        var html =
            "<head>" +
                "<link rel='stylesheet' href='file:/android_asset/html/css/table.css'>" +
            "</head>"
        if (!check2YPerX) {
            html += "<h3 style='color: red'>ERROR: Debe haber al menos 2 variables cuantitativas por cada variable cualitativa</h3>"
        } else {
            html +=
                "<body>" +
                    "<table>" +
                        "<tr class='border'>" +
                            "<td>Variable X</td>"

            for ((varX, _) in valuesXY) {
                html +=     "<td>$varX</td>"
            }
            html +=     "</tr>"

            html +=     "<tr>" +
                            "<td>n</td>"
            for ((_, listXY) in valuesXY) {
                html +=     "<td>" + listXY.count().toString() + "</td>"
            }
            html +=     "</tr>"

            html +=     "<tr>" +
                            "<td>Media</td>"
            for ((_, listXY) in valuesXY) {
                // Log.e("list", listXY.toString())
                // Log.e("media", media(listXY).round(mc).toPlainString())
                // Log.e("desviacionMedia", desviacionMedia(listXY).round(mc).toPlainString())
                html +=     "<td>" + media(listXY).round(mc).toPlainString() + "</td>"
                // html +=     "<td>" + desviacionMedia(listXY).round(mc).toPlainString() + "</td>"
            }
            html +=     "</tr>"

            html +=     "<tr>" +
                            "<td>Desviación<br>Estándar</td>"
            for ((_, listXY) in valuesXY) {
                html +=     "<td>" + desviacionEstandar(listXY).round(mc).toPlainString() + "</td>"
            }
            html +=     "</tr>"

            html +=     "<tr>" +
                            "<td>Mínimo</td>"
            for ((_, listXY) in valuesXY) {
                html +=     "<td>" + listXY.min().toPlainString() + "</td>"
            }
            html +=     "</tr>"

            html +=     "<tr>" +
                            "<td>Máximo</td>"
            for ((_, listXY) in valuesXY) {
                html +=     "<td>" + listXY.max().toPlainString() + "</td>"
            }
            html +=     "</tr>" +
                    "</table>" +
                "</body>"
        }

        val wv: WebView = findViewById(R.id.activity_calc_descriptiva_2_variables_mixtas_wv)
        wv.loadDataWithBaseURL(null, html,"text/html", "utf-8", null)
    }
}