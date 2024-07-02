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

class CalcTamanioMuestra2ProporcionesPoblaciones : AppCompatActivity() {

    private var p1 = BigDecimal.ZERO
    private var p1Comp = BigDecimal.ZERO
    private var p2 = BigDecimal.ZERO
    private var p2Comp = BigDecimal.ZERO
    private var e = BigDecimal.ZERO
    private var z = BigDecimal("1.96")
    private var n1 = BigDecimal.ZERO
    private var n2 = BigDecimal.ZERO
    private val mc = MathContext(4, RoundingMode.HALF_UP)

    private lateinit var etP1: EditText
    private lateinit var etP2: EditText
    private lateinit var etE: EditText
    private lateinit var etZ: EditText
    private lateinit var etN1: EditText
    private lateinit var etN2: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_tamanio_muestra_2_proporciones_poblaciones)

        etP1 = findViewById(R.id.activity_calc_tamanio_muestra_2_proporciones_poblaciones_et_p1)
        etP2 = findViewById(R.id.activity_calc_tamanio_muestra_2_proporciones_poblaciones_et_p2)
        etZ = findViewById(R.id.activity_calc_tamanio_muestra_2_proporciones_poblaciones_et_z)
        etE = findViewById(R.id.activity_calc_tamanio_muestra_2_proporciones_poblaciones_et_e)
        etN1 = findViewById(R.id.activity_calc_tamanio_muestra_2_proporciones_poblaciones_et_n1)
        etN2 = findViewById(R.id.activity_calc_tamanio_muestra_2_proporciones_poblaciones_et_n2)
        btnClear = findViewById(R.id.activity_calc_tamanio_muestra_2_proporciones_poblaciones_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        etZ.setText(z.toPlainString())

        etP1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                p1 = strToBigDecimal(text.toString())
                p1Comp = BigDecimal.ONE - p1
            }
        })

        etP2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                p2 = strToBigDecimal(text.toString())
                p2Comp = BigDecimal.ONE - p2
            }
        })

        etE.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                e = strToBigDecimal(text.toString())
            }
        })

        btnClear.setOnClickListener { v -> clear(v) }
    }

    fun calc() {
        // if (!checkReqN() || !checkReqNP()) return

        if (p1 == BigDecimal.ZERO || p2 == BigDecimal.ZERO || e == BigDecimal.ZERO) return

        try {
            val pTimesCompSum = (p1 * p1Comp) + (p2 * p2Comp)
            val zDivE = z.divide(e, mc)
            val zDivESqr = zDivE.pow(2)

            n1 = pTimesCompSum * zDivESqr
            n2 = pTimesCompSum * zDivESqr

            tstInvalid.cancel()
            etN1.setText(String.format(Locale.ENGLISH, "%.0f", n1))
            etN2.setText(String.format(Locale.ENGLISH, "%.0f", n2))
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear(v: View) {
        if (!v.isClickable) return

        p1 = BigDecimal.ZERO
        p2 = BigDecimal.ZERO
        e = BigDecimal.ZERO
        n1 = BigDecimal.ZERO
        n2 = BigDecimal.ZERO

        etP1.setText("")
        etP2.setText("")
        etE.setText("")
        etN1.setText("")
        etN2.setText("")

        etP1.clearFocus()
        etP2.clearFocus()
        etE.clearFocus()
        etN1.clearFocus()
        etN2.clearFocus()
    }
}