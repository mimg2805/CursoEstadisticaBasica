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

class CalcTamanioMuestraPruebaHipotesisProporcion : AppCompatActivity() {

    private var zAlpha = BigDecimal("1.96")
    private var zBeta = BigDecimal("0.84")
    private var pi0 = BigDecimal.ZERO
    private var p = BigDecimal.ZERO
    private var pComp = BigDecimal.ZERO
    private var result = BigInteger.ZERO
    private val mc4 = MathContext(4, RoundingMode.UP)

    private lateinit var etZAlpha: EditText
    private lateinit var etZBeta: EditText
    private lateinit var etPi0: EditText
    private lateinit var etP: EditText
    private lateinit var etNResult: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_tamanio_muestra_prueba_hipotesis_proporcion)

        etZAlpha = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_proporcion_et_Zalpha)
        etZBeta = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_proporcion_et_Zbeta)
        etPi0 = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_proporcion_et_pi0)
        etP = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_proporcion_et_p)
        etNResult = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_proporcion_et_n_result)
        btnClear = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_proporcion_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        etZAlpha.setText(zAlpha.toPlainString())
        etZBeta.setText(zBeta.toPlainString())

        etPi0.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                pi0 = strToBigDecimal(text.toString())
            }
        })

        etP.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                p = strToBigDecimal(text.toString())
                pComp = BigDecimal.ONE - p
            }
        })

        btnClear.setOnClickListener { _ -> clear() }
    }

    fun calc() {
        if (pi0 == BigDecimal.ZERO || p == BigDecimal.ZERO) return

        try {
            val es = (pi0 - p).abs().divide(sqrt(p * pComp), mc4)
            result = (zAlpha + zBeta).divide(es, mc4).pow(2).setScale(0, RoundingMode.UP).toBigInteger()
            etNResult.setText(String.format(result.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear() {
        pi0 = BigDecimal.ZERO
        p = BigDecimal.ZERO
        result = BigInteger.ZERO

        etPi0.setText("")
        etP.setText("")
        etNResult.setText("")

        etPi0.clearFocus()
        etP.clearFocus()
    }
}