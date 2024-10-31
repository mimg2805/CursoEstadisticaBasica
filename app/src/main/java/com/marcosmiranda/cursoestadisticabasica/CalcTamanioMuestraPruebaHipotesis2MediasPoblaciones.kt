package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.Locale

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcTamanioMuestraPruebaHipotesis2MediasPoblaciones : AppCompatActivity() {

    private var zAlpha = BigDecimal("1.96")
    private var zBeta = BigDecimal("0.84")
    private var u1 = BigDecimal.ZERO
    private var u1Comp = BigDecimal.ZERO
    private var u2 = BigDecimal.ZERO
    private var u2Comp = BigDecimal.ZERO
    private var s1 = BigDecimal.ZERO
    private var s1Comp = BigDecimal.ZERO
    private var s2 = BigDecimal.ZERO
    private var s2Comp = BigDecimal.ZERO
    private var s = BigDecimal.ZERO
    private var sComp = BigDecimal.ZERO
    private var n1 = BigDecimal.ZERO
    private var n2 = BigDecimal.ZERO
    private val mc4 = MathContext(4, RoundingMode.UP)

    private lateinit var etZAlpha: EditText
    private lateinit var etZBeta: EditText
    private lateinit var etU1: EditText
    private lateinit var etU2: EditText
    private lateinit var etS1: EditText
    private lateinit var etS2: EditText
    private lateinit var etS: EditText
    private lateinit var etN1: EditText
    private lateinit var etN2: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_tamanio_muestra_prueba_hipotesis_2_medias_poblaciones)

        etZAlpha = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_2_medias_poblaciones_et_Zalpha)
        etZBeta = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_2_medias_poblaciones_et_Zbeta)
        etU1 = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_2_medias_poblaciones_et_u1)
        etU2 = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_2_medias_poblaciones_et_u2)
        etS1 = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_2_medias_poblaciones_et_s1)
        etS2 = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_2_medias_poblaciones_et_s2)
        etS = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_2_medias_poblaciones_et_s)
        etN1 = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_2_medias_poblaciones_et_n1)
        etN2 = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_2_medias_poblaciones_et_n2)
        btnClear = findViewById(R.id.activity_calc_tamanio_muestra_prueba_hipotesis_2_medias_poblaciones_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        etZAlpha.setText(zAlpha.toPlainString())
        etZBeta.setText(zBeta.toPlainString())

        etU1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                u1 = strToBigDecimal(text.toString())
                u1Comp = BigDecimal.ONE - u1
            }
        })

        etU2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                u2 = strToBigDecimal(text.toString())
                u2Comp = BigDecimal.ONE - u2
            }
        })

        etS1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                s1 = strToBigDecimal(text.toString())
                s1Comp = BigDecimal.ONE - s1
                calcS()
            }
        })

        etS2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                s2 = strToBigDecimal(text.toString())
                s2Comp = BigDecimal.ONE - s2
                calcS()
            }
        })

        btnClear.setOnClickListener { _ -> clear() }
    }

    fun calcS() {
        s = (s1 + s2).divide(BigDecimal.valueOf(2), mc4)
        sComp = BigDecimal.ONE - s
        etS.setText(s.toPlainString())
    }

    fun calc() {
        if (u1 == BigDecimal.ZERO || u2 == BigDecimal.ZERO || s1 == BigDecimal.ZERO || s2 == BigDecimal.ZERO) return

        try {
            val es = (u1 - u2).abs().divide(s, mc4)
            val result = (zAlpha + zBeta).divide(es, mc4).pow(2).times(BigDecimal.valueOf(2)).setScale(0, RoundingMode.UP)
            n1 = result
            n2 = result

            tstInvalid.cancel()
            etS.setText(s.toPlainString())
            etN1.setText(n1.toPlainString())
            etN2.setText(n2.toPlainString())
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear() {
        u1 = BigDecimal.ZERO
        u2 = BigDecimal.ZERO
        s1 = BigDecimal.ZERO
        s2 = BigDecimal.ZERO
        s = BigDecimal.ZERO
        n1 = BigDecimal.ZERO
        n2 = BigDecimal.ZERO

        etU1.setText("")
        etU2.setText("")
        etS1.setText("")
        etS2.setText("")
        etS.setText("")
        etN1.setText("")
        etN2.setText("")

        etU1.clearFocus()
        etU2.clearFocus()
        etS1.clearFocus()
        etS2.clearFocus()
        etS.clearFocus()
        etN1.clearFocus()
        etN2.clearFocus()
    }
}