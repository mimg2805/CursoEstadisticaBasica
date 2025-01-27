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

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger

class CalcTamanioMuestraTablaContingencia : AppCompatActivity() {

    private var c = BigInteger.ZERO
    private var f = BigInteger.ZERO
    private var cf = BigInteger.ZERO
    private var result = BigInteger.ZERO
    private val mc4 = MathContext(4, RoundingMode.UP)

    private lateinit var etC: EditText
    private lateinit var etF: EditText
    private lateinit var etCF: EditText
    private lateinit var etNResult: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_tamanio_muestra_tabla_contingencia)

        etC = findViewById(R.id.activity_calc_tamanio_muestra_tabla_contingencia_et_C)
        etF = findViewById(R.id.activity_calc_tamanio_muestra_tabla_contingencia_et_F)
        etCF = findViewById(R.id.activity_calc_tamanio_muestra_tabla_contingencia_et_CF)
        etNResult = findViewById(R.id.activity_calc_tamanio_muestra_tabla_contingencia_et_n_result)
        btnClear = findViewById(R.id.activity_calc_tamanio_muestra_tabla_contingencia_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        etC.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                c = strToBigInteger(text.toString())
            }
        })

        etF.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                f = strToBigInteger(text.toString())
            }
        })

        btnClear.setOnClickListener { _ -> clear() }
    }

    fun calc() {
        if (c == BigInteger.ZERO || f == BigInteger.ZERO) return

        try {
            cf = c * f
            etCF.setText(String.format(cf.toString()))
            result = cf * BigInteger("10")
            etNResult.setText(String.format(result.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear() {
        c = BigInteger.ZERO
        f = BigInteger.ZERO
        cf = BigInteger.ZERO
        result = BigInteger.ZERO

        etC.setText("")
        etF.setText("")
        etCF.setText("")
        etNResult.setText("")

        etC.clearFocus()
        etF.clearFocus()
        etCF.clearFocus()
    }
}