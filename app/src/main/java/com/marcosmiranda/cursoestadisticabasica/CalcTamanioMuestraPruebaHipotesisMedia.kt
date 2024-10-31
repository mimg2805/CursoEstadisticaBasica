package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import java.math.MathContext

class CalcTamanioMuestraPruebaHipotesisMedia : AppCompatActivity() {

    private var zAlpha = BigDecimal("1.96")
    private var zBeta = BigDecimal("0.84")
    private var mu0 = BigDecimal.ZERO
    private var m = BigDecimal.ZERO
    private var s = BigDecimal.ZERO
    private var result = BigInteger.ZERO
    private var mc4 = MathContext(4, RoundingMode.UP)

    private lateinit var etZAlpha: EditText
    private lateinit var etZBeta: EditText
    private lateinit var etMu0: EditText
    private lateinit var etM: EditText
    private lateinit var etS: EditText
    private lateinit var etNResult: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_tamanio_muestra_prueba_hipotesis_media)

        etZAlpha = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_media_et_Zalpha)
        etZBeta = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_media_et_Zbeta)
        etMu0 = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_media_et_mu0)
        etM = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_media_et_m)
        etS = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_media_et_S)
        etNResult = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_media_et_n_result)
        btnClear = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_media_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        etZAlpha.setText(zAlpha.toPlainString())
        etZBeta.setText(zBeta.toPlainString())

        etMu0.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                mu0 = strToBigDecimal(text.toString())
            }
        })

        etM.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                m = strToBigDecimal(text.toString())
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

        btnClear.setOnClickListener { _ -> clear() }
    }

    fun calc() {
        if (mu0 == BigInteger.ZERO || m == BigDecimal.ZERO || s == BigDecimal.ZERO) return

        try {
            val es = (mu0 - m).abs().divide(s, mc4)
            result = (zAlpha + zBeta).divide(es, mc4).pow(2).setScale(0, RoundingMode.UP).toBigInteger()
            etNResult.setText(String.format(result.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear() {
        mu0 = BigDecimal.ZERO
        m = BigDecimal.ZERO
        s = BigDecimal.ZERO
        result = BigInteger.ZERO

        etMu0.setText("")
        etM.setText("")
        etS.setText("")
        etNResult.setText("")

        etMu0.clearFocus()
        etM.clearFocus()
        etS.clearFocus()
    }
}