package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.obermuhlner.math.big.BigDecimalMath.sqrt
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger

class CalcIntervaloConfianzaProporcionPoblacionConocida : AppCompatActivity() {

    private var nSample = BigInteger.ZERO
    private var nPopulation = BigInteger.ZERO
    private var p = BigDecimal.ZERO
    private var pComp = BigDecimal.ZERO
    private val z = BigDecimal("1.96")
    private var limInf = BigDecimal.ZERO
    private var limSup = BigDecimal.ZERO
    private val mc = MathContext(4, RoundingMode.HALF_UP)

    private lateinit var etP: EditText
    private lateinit var etNPopulation: EditText
    private lateinit var etNSample: EditText
    private lateinit var etZ: EditText
    private lateinit var etIC: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_intervalo_confianza_proporcion_poblacion_conocida)

        etP = findViewById(R.id.activity_calc_intervalo_confianza_proporcion_poblacion_conocida_et_p)
        etNPopulation = findViewById(R.id.activity_calc_intervalo_confianza_proporcion_poblacion_conocida_et_n_population)
        etNSample = findViewById(R.id.activity_calc_intervalo_confianza_proporcion_poblacion_conocida_et_n_sample)
        etZ = findViewById(R.id.activity_calc_intervalo_confianza_proporcion_poblacion_conocida_et_z)
        etIC = findViewById(R.id.activity_calc_intervalo_confianza_proporcion_poblacion_conocida_et_ic)
        btnClear = findViewById(R.id.activity_calc_intervalo_confianza_proporcion_poblacion_conocida_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        etZ.setText(z.toPlainString())

        etP.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                p = strToBigDecimal(text.toString())
                pComp = BigDecimal.ONE.subtract(p)
            }
        })

        etNPopulation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                nPopulation = strToBigInteger(text.toString())
            }
        })

        etNSample.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                nSample = strToBigInteger(text.toString())
            }
        })

        btnClear.setOnClickListener { v -> clear(v) }
    }

    private fun checkReqN(): Boolean {
        return if (nSample <= BigInteger.valueOf(30)) {
            etNSample.error = getText(R.string.req_n_30)
            etIC.setText("")
            false
        } else {
            etNSample.error = null
            true
        }
    }

    private fun checkReqNP(): Boolean {
        if (!checkReqN()) return false
        return if ((nSample.toBigDecimal() * p) <= BigDecimal.valueOf(5) || (nSample.toBigDecimal() * pComp) <= BigDecimal.valueOf(5)) {
            etP.error = getText(R.string.req_n_p_5)
            etIC.setText("")
            false
        } else {
            etP.error = null
            true
        }
    }

    fun calc() {
        if (!checkReqN() || !checkReqNP()) return

        try {
            val root1 = sqrt(((p * pComp).divide(nSample.toBigDecimal(), mc)), mc)
            val root2 = sqrt(((nPopulation - nSample).toBigDecimal().divide((nPopulation - BigInteger.ONE).toBigDecimal(), mc)), mc)
            val error = z * root1 * root2
            limInf = p - error
            limSup = p + error

            if (limInf == BigDecimal.ZERO || limSup == BigDecimal.ZERO) return

            val limInfStr = limInf.round(mc).toPlainString()
            val limSupStr = limSup.round(mc).toPlainString()
            val limStr = "[$limInfStr - $limSupStr]"
            etIC.setText(limStr)
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear(view: View) {
        if (!view.isClickable) return

        etP.setText("")
        etNPopulation.setText("")
        etNSample.setText("")
        etIC.setText("")

        etP.clearFocus()
        etNSample.clearFocus()

        nSample = BigInteger.ZERO
        nPopulation = BigInteger.ZERO
        p = BigDecimal.ZERO
        limInf = BigDecimal.ZERO
        limSup = BigDecimal.ZERO
    }
}