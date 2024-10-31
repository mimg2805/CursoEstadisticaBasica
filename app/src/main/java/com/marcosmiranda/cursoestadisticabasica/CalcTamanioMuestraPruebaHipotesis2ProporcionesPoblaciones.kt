package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.obermuhlner.math.big.DefaultBigDecimalMath.sqrt
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.Locale

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcTamanioMuestraPruebaHipotesis2ProporcionesPoblaciones : AppCompatActivity() {

    private var zAlpha = BigDecimal("1.96")
    private var zBeta = BigDecimal("0.84")
    private var pi1 = BigDecimal.ZERO
    private var pi1Comp = BigDecimal.ZERO
    private var pi2 = BigDecimal.ZERO
    private var pi2Comp = BigDecimal.ZERO
    private var pi = BigDecimal.ZERO
    private var piComp = BigDecimal.ZERO
    private var n1 = BigDecimal.ZERO
    private var n2 = BigDecimal.ZERO
    private val mc4 = MathContext(4, RoundingMode.UP)

    private lateinit var etZAlpha: EditText
    private lateinit var etZBeta: EditText
    private lateinit var etPi1: EditText
    private lateinit var etPi2: EditText
    private lateinit var etPi: EditText
    private lateinit var etN1: EditText
    private lateinit var etN2: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_tamanio_muestra_prueba_hipotesis_2_proporciones_poblaciones)

        etZAlpha = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_2_proporciones_poblaciones_et_Zalpha)
        etZBeta = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_2_proporciones_poblaciones_et_Zbeta)
        etPi1 = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_2_proporciones_poblaciones_et_pi1)
        etPi2 = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_2_proporciones_poblaciones_et_pi2)
        etPi = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_2_proporciones_poblaciones_et_pi)
        etN1 = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_2_proporciones_poblaciones_et_n1)
        etN2 = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_2_proporciones_poblaciones_et_n2)
        btnClear = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_2_proporciones_poblaciones_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        etZAlpha.setText(zAlpha.toPlainString())
        etZBeta.setText(zBeta.toPlainString())

        etPi1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                pi1 = strToBigDecimal(text.toString())
                pi1Comp = BigDecimal.ONE - pi1
                calcPi()
            }
        })

        etPi2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                pi2 = strToBigDecimal(text.toString())
                pi2Comp = BigDecimal.ONE - pi2
                calcPi()
            }
        })

        btnClear.setOnClickListener { _ -> clear() }
    }

    fun calcPi() {
        pi = (pi1 + pi2).divide(BigDecimal.valueOf(2), mc4)
        piComp = BigDecimal.ONE - pi
        etPi.setText(pi.toPlainString())
    }

    fun calc() {
        if (pi1 == BigDecimal.ZERO || pi2 == BigDecimal.ZERO) return

        try {
            val es = (pi1 - pi2).abs().divide(sqrt(pi * piComp), mc4)
            val result = (zAlpha + zBeta).divide(es, mc4).pow(2).times(BigDecimal.valueOf(2)).setScale(0, RoundingMode.UP)

            n1 = result
            n2 = result

            tstInvalid.cancel()
            etN1.setText(n1.toPlainString())
            etN2.setText(n2.toPlainString())
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear() {
        pi1 = BigDecimal.ZERO
        pi2 = BigDecimal.ZERO
        pi = BigDecimal.ZERO
        n1 = BigDecimal.ZERO
        n2 = BigDecimal.ZERO

        etPi1.setText("")
        etPi2.setText("")
        etPi.setText("")
        etN1.setText("")
        etN2.setText("")

        etPi1.clearFocus()
        etPi2.clearFocus()
        etPi.clearFocus()
        etN1.clearFocus()
        etN2.clearFocus()
    }
}