package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.cuartiles
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.desviacionEstandar
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.mediana
import java.lang.Exception
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class CalcMedidasResumenVariableCuantitativa : AppCompatActivity() {

    private var numsList: List<BigDecimal> = emptyList()
    private var max = BigDecimal.ZERO
    private var min = BigDecimal.ZERO
    private var crt1 = BigDecimal.ZERO
    private var crt3 = BigDecimal.ZERO
    private var mediana = BigDecimal.ZERO
    private var media = BigDecimal.ZERO
    private var dest = BigDecimal.ZERO

    private lateinit var mNumsTxt: EditText
    private lateinit var mMaxTxt: EditText
    private lateinit var mMinTxt: EditText
    private lateinit var mCrt1Txt: EditText
    private lateinit var mCrt3Txt: EditText
    private lateinit var mMedianaTxt: EditText
    private lateinit var mMediaTxt: EditText
    private lateinit var mDestTxt: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_medidas_resumen_variable_cuantitativa)

        mNumsTxt = findViewById(R.id.numsTxt)
        mNumsTxt.setSelection(mNumsTxt.length())
        mMaxTxt = findViewById(R.id.maxTxt)
        mMinTxt = findViewById(R.id.minTxt)
        mCrt1Txt = findViewById(R.id.crt1Txt)
        mCrt3Txt = findViewById(R.id.crt3Txt)
        mMedianaTxt = findViewById(R.id.medianaTxt)
        mMediaTxt = findViewById(R.id.mediaTxt)
        mDestTxt = findViewById(R.id.destTxt)

        mNumsTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    val str = text.toString()
                    if (str.isNotEmpty() && str.isNotBlank()) {
                        val numsStr = str.trim()
                        numsList = numsStr.trim().split(" ").map{ MathHelper.strToBigDecimal(it.trim()) }
                        //Log.e("numsStr", numsStr.toString())
                        //Log.e("numsList", numsList.toString())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun calc() {
        reset()
        if (numsList.isNotEmpty()) {
            val size = numsList.size.toBigDecimal()
            var sum = BigDecimal.ZERO

            for (i in numsList) {
                //Log.e("i", i.toPlainString())
                sum += i
                if (i == numsList[0]) {
                    max = i
                    min = i
                } else {
                    when {
                        i < min -> min = i
                        i > max -> max = i
                    }
                }
            }
            media = sum.divide(size, MathContext(4, RoundingMode.HALF_EVEN))
            mediana = mediana(numsList)
            val cuartiles = cuartiles(numsList)
            crt1 = cuartiles[0]
            crt3 = cuartiles[2]
            dest = desviacionEstandar(numsList)
        } else {
            reset()
        }

        mMaxTxt.setText(max.toPlainString())
        mMinTxt.setText(min.toPlainString())
        mMediaTxt.setText(media.toPlainString())
        mMedianaTxt.setText(mediana.toPlainString())
        mCrt1Txt.setText(crt1.toPlainString())
        mCrt3Txt.setText(crt3.toPlainString())
        mDestTxt.setText(dest.toPlainString())
    }

    private fun reset() {
        min = BigDecimal.ZERO
        max = BigDecimal.ZERO
        crt1 = BigDecimal.ZERO
        crt3 = BigDecimal.ZERO
        mediana = BigDecimal.ZERO
        media = BigDecimal.ZERO
        dest = BigDecimal.ZERO
    }

    fun clear(view: View) {
        if (view.isClickable) {
            reset()

            mNumsTxt.setText("")
            mMaxTxt.setText("")
            mMinTxt.setText("")
            mCrt1Txt.setText("")
            mCrt3Txt.setText("")
            mMedianaTxt.setText("")
            mMediaTxt.setText("")
            mDestTxt.setText("")

            mNumsTxt.clearFocus()
            mMaxTxt.clearFocus()
            mMinTxt.clearFocus()
            mCrt1Txt.clearFocus()
            mCrt3Txt.clearFocus()
            mMedianaTxt.clearFocus()
            mMediaTxt.clearFocus()
            mDestTxt.clearFocus()
        }
    }
}