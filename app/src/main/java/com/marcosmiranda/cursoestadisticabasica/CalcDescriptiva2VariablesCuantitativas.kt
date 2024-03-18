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

    private var valuesStrX = ""
    private var numValuesListX = mutableListOf<BigDecimal>()
    private var sumValuesX = BigDecimal.ZERO
    private var sumValuesXSqr = BigDecimal.ZERO
    private var mediaX = BigDecimal.ZERO
    private var numValuesXMinusMedia = mutableListOf<BigDecimal>()
    private var sumValuesXMinusMediaSqr = BigDecimal.ZERO

    private var valuesStrY = ""
    private var numValuesListY = mutableListOf<BigDecimal>()
    private var sumValuesY = BigDecimal.ZERO
    private var sumValuesYSqr = BigDecimal.ZERO
    private var mediaY = BigDecimal.ZERO
    private var numValuesYMinusMedia = mutableListOf<BigDecimal>()
    private var sumValuesYMinusMediaSqr = BigDecimal.ZERO

    private var numValuesXY = mutableListOf<BigDecimal>()
    private var sumValuesXY = BigDecimal.ZERO
    private var numValuesXYMinusMedia = mutableListOf<BigDecimal>()
    private var sumValuesXYMinusMedia = BigDecimal.ZERO

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

        valuesStrX = intent.getStringExtra("valuesX") ?: ""
        val values1Arr = valuesStrX.split(' ')
        values1Arr.forEach {str ->
            val num = strToBigDecimal(str)
            numValuesListX += num
            sumValuesX += num
            sumValuesXSqr += num.pow(2)
            numValuesXY += num
            n++
        }
        mediaX = sumValuesX.divide(n.toBigDecimal(), mc)

        valuesStrY = intent.getStringExtra("valuesY") ?: ""
        val values2Arr = valuesStrY.split(' ')
        var values2Index = 0
        values2Arr.forEach {str ->
            val num = strToBigDecimal(str)
            numValuesListY += num
            sumValuesY += num
            sumValuesYSqr += num.pow(2)
            numValuesXY[values2Index] *= num
            values2Index++
        }
        mediaY = sumValuesY.divide(n.toBigDecimal(), mc)

        numValuesXY.forEach {num ->
            sumValuesXY += num
        }

        numValuesListX.forEach {num ->
            numValuesXMinusMedia += num - mediaX
            sumValuesXMinusMediaSqr += (num - mediaX).pow(2)
            numValuesXYMinusMedia += num - mediaX
        }

        values2Index = 0
        numValuesListY.forEach {num ->
            numValuesYMinusMedia += num - mediaY
            sumValuesYMinusMediaSqr += (num - mediaY).pow(2)
            numValuesXYMinusMedia[values2Index] *= (num - mediaY)
            values2Index++
        }

        numValuesXYMinusMedia.forEach {num ->
            sumValuesXYMinusMedia += num
        }

        b = ((n.toBigDecimal() * sumValuesXY) - (sumValuesX * sumValuesY)).divide((n.toBigDecimal() * sumValuesXSqr) - (sumValuesX.pow(2)), mc)
        a = (sumValuesY - (b * sumValuesX)).divide(n.toBigDecimal(), mc)
        // r = sumValuesXYMinusMedia.divide((sumValuesXMinusMediaSqr.sqrt(mc) * sumValuesXMinusMediaSqr.sqrt(mc)), mc)
        r = sumValuesXYMinusMedia.divide((sqrt(sumValuesXMinusMediaSqr.toDouble()) * sqrt(sumValuesYMinusMediaSqr.toDouble())).toBigDecimal(), mc)
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