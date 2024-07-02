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
import java.util.Locale

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger

class CalcIntervaloConfianzaMediaPoblacionConocida : AppCompatActivity() {

    private var nPopulation = BigInteger.ZERO
    private var nSample = BigInteger.ZERO
    private var x = BigDecimal.ZERO
    private var s = BigDecimal.ZERO
    private val z = BigDecimal("1.96")
    private var limInf = BigDecimal.ZERO
    private var limSup = BigDecimal.ZERO
    private val mc = MathContext(4, RoundingMode.HALF_UP)

    private lateinit var etNPopulation: EditText
    private lateinit var etNSample: EditText
    private lateinit var etX: EditText
    private lateinit var etS: EditText
    private lateinit var etZ: EditText
    private lateinit var etIC: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_intervalo_confianza_media_poblacion_conocida)

        etNPopulation = findViewById(R.id.activity_calc_intervalo_confianza_media_poblacion_conocida_et_n_population)
        etNSample = findViewById(R.id.activity_calc_intervalo_confianza_media_poblacion_conocida_et_n_sample)
        etX = findViewById(R.id.activity_calc_intervalo_confianza_media_poblacion_conocida_et_x)
        etS = findViewById(R.id.activity_calc_intervalo_confianza_media_poblacion_conocida_et_s)
        etZ = findViewById(R.id.activity_calc_intervalo_confianza_media_poblacion_conocida_et_z)
        etIC = findViewById(R.id.activity_calc_intervalo_confianza_media_poblacion_conocida_et_ic)
        btnClear = findViewById(R.id.activity_calc_intervalo_confianza_media_poblacion_conocida_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        etZ.setText(z.toPlainString())

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

        etX.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                x = strToBigDecimal(text.toString())
            }
        })

        etS.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                s = strToBigDecimal(text.toString())
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

    fun calc() {
        if (!checkReqN()) return
        if (x == BigDecimal.ZERO || nPopulation == BigInteger.ZERO) return

        try {
            val root1 = s.divide(sqrt(nSample.toBigDecimal(), mc), mc)
            val root2 = sqrt(((nPopulation - nSample).toBigDecimal().divide((nPopulation - BigInteger.ONE).toBigDecimal(), mc)), mc)
            val error = z * root1 * root2
            limInf = x - error
            limSup = x + error

            if (limInf == BigDecimal.ZERO || limSup == BigDecimal.ZERO) return

            val limInfStr = String.format(Locale.ENGLISH, "%.2f", limInf)
            val limSupStr = String.format(Locale.ENGLISH, "%.2f", limSup)
            val limStr = "[$limInfStr - $limSupStr]"
            etIC.setText(limStr)
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear(v: View) {
        if (!v.isClickable) return

        etNPopulation.setText("")
        etNSample.setText("")
        etX.setText("")
        etS.setText("")
        etIC.setText("")

        etNPopulation.clearFocus()
        etNSample.clearFocus()
        etX.clearFocus()
        etS.clearFocus()

        nPopulation = BigInteger.ZERO
        nSample = BigInteger.ZERO
        x = BigDecimal.ZERO
        s = BigDecimal.ZERO
        limInf = BigDecimal.ZERO
        limSup = BigDecimal.ZERO
    }
}