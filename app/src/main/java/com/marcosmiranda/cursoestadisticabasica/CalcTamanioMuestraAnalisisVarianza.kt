package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.obermuhlner.math.big.DefaultBigDecimalMath.sqrt
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcTamanioMuestraAnalisisVarianza : AppCompatActivity() {

    private var zAlpha = BigDecimal("1.96")
    private var zBeta = BigDecimal("0.84")
    private var S = BigDecimal.ZERO
    private var delta = BigDecimal.ZERO
    private var result = BigInteger.ZERO
    private val mc4 = MathContext(4, RoundingMode.UP)

    private lateinit var etZAlpha: EditText
    private lateinit var etZBeta: EditText
    private lateinit var etS: EditText
    private lateinit var etDelta: EditText
    private lateinit var etNResult: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_tamanio_muestra_analisis_varianza)

        etZAlpha = findViewById(R.id.activity_calc_tamanio_muestra_analisis_varianza_et_Zalpha)
        etZBeta = findViewById(R.id.activity_calc_tamanio_muestra_analisis_varianza_et_Zbeta)
        etS = findViewById(R.id.activity_calc_tamanio_muestra_analisis_varianza_et_S)
        etDelta = findViewById(R.id.activity_calc_tamanio_muestra_analisis_varianza_et_delta)
        etNResult = findViewById(R.id.activity_calc_tamanio_muestra_analisis_varianza_et_n_result)
        btnClear = findViewById(R.id.activity_calc_tamanio_muestra_analisis_varianza_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        etZAlpha.setText(zAlpha.toPlainString())
        etZBeta.setText(zBeta.toPlainString())

        etS.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                S = strToBigDecimal(text.toString())
            }
        })

        etDelta.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                delta = strToBigDecimal(text.toString())
            }
        })

        btnClear.setOnClickListener { _ -> clear() }
    }

    fun calc() {
        if (S == BigDecimal.ZERO || delta == BigDecimal.ZERO) return

        try {
            val top = BigDecimal(2) * S.pow(2) * (zAlpha + zBeta).pow(2)
            result = top.divide(delta.pow(2), mc4).setScale(0, RoundingMode.UP).toBigInteger()
            etNResult.setText(String.format(result.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear() {
        S = BigDecimal.ZERO
        delta = BigDecimal.ZERO
        result = BigInteger.ZERO

        etS.setText("")
        etDelta.setText("")
        etNResult.setText("")

        etS.clearFocus()
        etDelta.clearFocus()
    }
}