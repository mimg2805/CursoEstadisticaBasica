package com.marcosmiranda.cursoestadisticabasica

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.round

class CalcDescriptivaVariableCualitativa : AppCompatActivity() {

    private val mc = MathContext(4, RoundingMode.HALF_EVEN)

    private var valuesStr = ""
    private var values: MutableMap<String, Int> = mutableMapOf()

    private lateinit var mTable: TableLayout

    private var line = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_descriptiva_variable_cualitativa)

        mTable = findViewById(R.id.table)

        valuesStr = intent.getStringExtra("values") ?: ""

        for (i in 1..72) line += "-" // 72

        resetTable()
        updateTable()
    }

    private fun updateTable() {

        val valuesArr = valuesStr.split(' ')
        val valuesSize = valuesArr.size
        valuesArr.forEach {str ->
            var num = values[str] ?: 0
            if (values.contains(str)) {
                num++
            } else {
                num = 1
            }
            values[str] = num
        }

        val varParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,2f)
        val otherParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1f)
        for ((str: String, num: Int) in values) {
            val varRow = TableRow(this)

            val varTxt = TextView(this)
            varTxt.setTextColor(Color.BLACK)
            varTxt.text = str
            varTxt.layoutParams = varParams
            varRow.addView(varTxt)

            val noTxt = TextView(this)
            noTxt.setTextColor(Color.BLACK)
            noTxt.text = num.toString()
            noTxt.layoutParams = otherParams
            varRow.addView(noTxt)

            val freq = BigDecimal((num.toDouble() / valuesSize.toDouble()) * 100, mc)
            val porcTxt = TextView(this)
            porcTxt.setTextColor(Color.BLACK)
            porcTxt.text = freq.toPlainString()
            porcTxt.layoutParams = otherParams
            varRow.addView(porcTxt)

            mTable.addView(varRow)
        }

        addLine()

        val totalRow = TableRow(this)

        val totalTxt = TextView(this)
        totalTxt.setTextColor(Color.BLACK)
        totalTxt.text = "Total"
        totalTxt.layoutParams = varParams
        totalRow.addView(totalTxt)

        val noTxt = TextView(this)
        noTxt.setTextColor(Color.BLACK)
        noTxt.text = valuesSize.toString()
        noTxt.layoutParams = otherParams
        totalRow.addView(noTxt)

        val porcTxt = TextView(this)
        porcTxt.setTextColor(Color.BLACK)
        porcTxt.text = "100.0"
        porcTxt.layoutParams = otherParams
        totalRow.addView(porcTxt)

        mTable.addView(totalRow)

        addLine()
    }

    fun clear(view: View) {
        if (!view.isClickable) return

        resetTable()
    }

    private fun addLine() {
        val params = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,2f)
        val lineRow = TableRow(this)
        val lineTxt = TextView(this)
        lineTxt.setTextColor(Color.BLACK)
        lineTxt.text = line
        lineTxt.layoutParams = params
        lineRow.addView(lineTxt)
        mTable.addView(lineRow)
    }

    private fun resetTable() {
        // uniqueCharsList = mutableMapOf()
        val titleRow = mTable.getChildAt(0)
        val tableTitleRow = mTable.getChildAt(2)
        mTable.removeAllViews()
        mTable.addView(titleRow)
        addLine()
        mTable.addView(tableTitleRow)
        addLine()
        mTable.clearFocus()
    }
}