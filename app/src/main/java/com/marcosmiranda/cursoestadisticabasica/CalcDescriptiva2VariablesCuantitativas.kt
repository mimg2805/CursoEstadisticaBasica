package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.sqrt

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcDescriptiva2VariablesCuantitativas : AppCompatActivity() {

    private var valuesXStr = ""
    private var valuesXList = mutableListOf<BigDecimal>()
    private var valuesXSum = BigDecimal.ZERO
    private var valuesXSumSqr = BigDecimal.ZERO
    private var valuesXMedia = BigDecimal.ZERO
    private var valuesXListMinusMedia = mutableListOf<BigDecimal>()
    private var valuesXListMinusMediaSqr = BigDecimal.ZERO

    private var valuesYStr = ""
    private var valuesYList = mutableListOf<BigDecimal>()
    private var valuesYSum = BigDecimal.ZERO
    private var valuesYSumSqr = BigDecimal.ZERO
    private var valuesYMedia = BigDecimal.ZERO
    private var valuesYListMinusMedia = mutableListOf<BigDecimal>()
    private var valuesYListMinusMediaSqr = BigDecimal.ZERO

    private var valuesXYList = mutableListOf<BigDecimal>()
    private var valuesXYSum = BigDecimal.ZERO
    private var valuesXYListMinusMedia = mutableListOf<BigDecimal>()
    private var valuesXYMinusMediaSum = BigDecimal.ZERO

    private var n = 0
    private var a = BigDecimal.ZERO
    private var b = BigDecimal.ZERO
    private var r = BigDecimal.ZERO
    private var y = ""
    private val mc = MathContext(4, RoundingMode.HALF_EVEN)

    private lateinit var etA: EditText
    private lateinit var etB: EditText
    private lateinit var etR: EditText
    private lateinit var etY: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_descriptiva_2_variables_cuantitativas)

        etA = findViewById(R.id.activity_calc_descriptiva_2_variables_cuantitativas_et_a)
        etB = findViewById(R.id.activity_calc_descriptiva_2_variables_cuantitativas_et_b)
        etR = findViewById(R.id.activity_calc_descriptiva_2_variables_cuantitativas_et_r)
        etY = findViewById(R.id.activity_calc_descriptiva_2_variables_cuantitativas_et_y)

        valuesXStr = intent.getStringExtra("valuesX") ?: ""
        val values1Arr = valuesXStr.split(' ')
        values1Arr.forEach {str ->
            val num = strToBigDecimal(str)
            valuesXList += num
            valuesXSum += num
            valuesXSumSqr += num.pow(2)
            valuesXYList += num
            n++
        }
        valuesXMedia = valuesXSum.divide(n.toBigDecimal(), mc)

        valuesYStr = intent.getStringExtra("valuesY") ?: ""
        val values2Arr = valuesYStr.split(' ')
        var values2Index = 0
        values2Arr.forEach {str ->
            val num = strToBigDecimal(str)
            valuesYList += num
            valuesYSum += num
            valuesYSumSqr += num.pow(2)
            valuesXYList[values2Index] *= num
            values2Index++
        }
        valuesYMedia = valuesYSum.divide(n.toBigDecimal(), mc)

        valuesXYList.forEach { num ->
            valuesXYSum += num
        }

        valuesXList.forEach { num ->
            valuesXListMinusMedia += num - valuesXMedia
            valuesXListMinusMediaSqr += (num - valuesXMedia).pow(2)
            valuesXYListMinusMedia += num - valuesXMedia
        }

        values2Index = 0
        valuesYList.forEach { num ->
            valuesYListMinusMedia += num - valuesYMedia
            valuesYListMinusMediaSqr += (num - valuesYMedia).pow(2)
            valuesXYListMinusMedia[values2Index] *= (num - valuesYMedia)
            values2Index++
        }

        valuesXYListMinusMedia.forEach { num ->
            valuesXYMinusMediaSum += num
        }

        b = ((n.toBigDecimal() * valuesXYSum) - (valuesXSum * valuesYSum)).divide((n.toBigDecimal() * valuesXSumSqr) - (valuesXSum.pow(2)), mc)
        a = (valuesYSum - (b * valuesXSum)).divide(n.toBigDecimal(), mc)
        // r = sumValuesXYMinusMedia.divide((sumValuesXMinusMediaSqr.sqrt(mc) * sumValuesXMinusMediaSqr.sqrt(mc)), mc)
        r = valuesXYMinusMediaSum.divide((sqrt(valuesXListMinusMediaSqr.toDouble()) * sqrt(valuesYListMinusMediaSqr.toDouble())).toBigDecimal(), mc)
        y = if (b < BigDecimal.ZERO) {
            a.toPlainString() + " - " + b.abs().toPlainString() + "x"
        } else {
            a.toPlainString() + " + " + b.toPlainString() + "x"
        }

        etA.setText(a.toPlainString())
        etB.setText(b.toPlainString())
        etR.setText(r.toPlainString())
        etY.setText(y)
    }
}