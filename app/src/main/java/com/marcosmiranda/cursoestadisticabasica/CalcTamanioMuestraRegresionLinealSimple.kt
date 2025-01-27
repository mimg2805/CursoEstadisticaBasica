package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcTamanioMuestraRegresionLinealSimple : AppCompatActivity() {

    private var zAlpha = BigDecimal("1.96")
    private var zBeta = BigDecimal("0.84")
    private var r = BigDecimal.ZERO
    private var result = BigInteger.ZERO
    private val mc4 = MathContext(4, RoundingMode.UP)

    private lateinit var etZAlpha: EditText
    private lateinit var etZBeta: EditText
    private lateinit var etR: EditText
    private lateinit var etNResult: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_tamanio_muestra_regresion_lineal_simple)

        etZAlpha = findViewById(R.id.activity_calc_tamanio_muestra_regresion_lineal_simple_et_Zalpha)
        etZBeta = findViewById(R.id.activity_calc_tamanio_muestra_regresion_lineal_simple_et_Zbeta)
        etR = findViewById(R.id.activity_calc_tamanio_muestra_regresion_lineal_simple_et_R)
        etNResult = findViewById(R.id.activity_calc_tamanio_muestra_regresion_lineal_simple_et_n_result)
        btnClear = findViewById(R.id.activity_calc_tamanio_muestra_regresion_lineal_simple_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        etZAlpha.setText(zAlpha.toPlainString())
        etZBeta.setText(zBeta.toPlainString())

        etR.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                r = strToBigDecimal(text.toString())
            }
        })

        btnClear.setOnClickListener { _ -> clear() }
    }

    fun calc() {
        if (r == BigDecimal.ZERO) return

        try {
            val div = r.pow(2).divide(BigDecimal.ONE - r.pow(2), mc4)
            result = (zAlpha + zBeta).pow(2).divide(div, mc4).setScale(0, RoundingMode.UP).toBigInteger() + BigInteger("2")
            etNResult.setText(String.format(result.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear() {
        r = BigDecimal.ZERO
        result = BigInteger.ZERO

        etR.setText("")
        etNResult.setText("")

        etR.clearFocus()
    }
}