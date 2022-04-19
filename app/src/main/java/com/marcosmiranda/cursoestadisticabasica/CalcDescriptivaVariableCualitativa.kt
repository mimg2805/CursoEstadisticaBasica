package com.marcosmiranda.cursoestadisticabasica

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import org.mariuszgromada.math.mxparser.mathcollection.MathFunctions.round

class CalcDescriptivaVariableCualitativa : AppCompatActivity() {

    private var uniqueCharsList: MutableMap<Char, Int> = mutableMapOf()

    private lateinit var mDataTxt: EditText
    private lateinit var mTable: TableLayout

    private var line = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_descriptiva_variable_cualitativa)

        mDataTxt = findViewById(R.id.dataTxt)
        mDataTxt.setSelection(mDataTxt.text.length)
        mTable = findViewById(R.id.table)

        for(i in 1..72) line += "-" //72

        mDataTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                resetTable()
                updateTable()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun updateTable() {
        val dataList = mDataTxt.text.toString().replace(" ", "").toList()
        val dataSize = dataList.size

        for (chr in dataList) {
            var num = uniqueCharsList[chr] ?: 0
            if (!uniqueCharsList.contains(chr)) {
                num = 1
            } else {
                num++
            }
            uniqueCharsList[chr] = num
        }
        Log.e("list", uniqueCharsList.toString())

        val varParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,2f)
        val otherParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1f)
        for ((chr:Char, num:Int) in uniqueCharsList) {
            val varRow = TableRow(this)

            val varTxt = TextView(this)
            varTxt.setTextColor(Color.BLACK)
            varTxt.text = chr.toString()
            varTxt.layoutParams = varParams
            varRow.addView(varTxt)

            val noTxt = TextView(this)
            noTxt.setTextColor(Color.BLACK)
            noTxt.text = num.toString()
            noTxt.layoutParams = otherParams
            varRow.addView(noTxt)

            val freq = round((num.toDouble() / dataSize.toDouble()) * 100, 2)
            val porcTxt = TextView(this)
            porcTxt.setTextColor(Color.BLACK)
            porcTxt.text = freq.toString()
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
        noTxt.text = dataSize.toString()
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
        if (view.isClickable) {
            mDataTxt.setText("")
            mDataTxt.clearFocus()
            resetTable()
        }
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
        uniqueCharsList = mutableMapOf()
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