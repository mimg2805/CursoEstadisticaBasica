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
import java.math.BigInteger
import java.math.RoundingMode

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcTamanioMuestraMedia : AppCompatActivity() {

    private var n = BigInteger.ZERO
    private var s = BigDecimal.ZERO
    private var x = BigDecimal.ZERO
    private var e = BigDecimal.ZERO
    private val z = BigDecimal("1.96")
    private var result: BigInteger = BigInteger.ZERO

    private lateinit var etN: EditText
    private lateinit var etS: EditText
    private lateinit var etMedia: EditText
    private lateinit var etE: EditText
    private lateinit var etZ: EditText
    private lateinit var etNResult: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_tamanio_muestra_media)

        etN = findViewById(R.id.activity_calc_tamanio_muestra_media_et_N)
        etS = findViewById(R.id.activity_calc_tamanio_muestra_media_et_S)
        etMedia = findViewById(R.id.activity_calc_tamanio_muestra_media_et_media)
        etE = findViewById(R.id.activity_calc_tamanio_muestra_media_et_E)
        etZ = findViewById(R.id.activity_calc_tamanio_muestra_media_et_Z)
        etNResult = findViewById(R.id.activity_calc_tamanio_muestra_media_et_n_result)
        btnClear = findViewById(R.id.activity_calc_tamanio_muestra_media_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        etZ.setText(z.toPlainString())

        etN.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                n = strToBigInteger(text.toString())
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

        etMedia.addTextChangedListener(object : TextWatcher {
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

        btnClear.setOnClickListener { v -> clear(v) }
    }

    fun calc() {
        if (n == BigInteger.ZERO || s == BigDecimal.ZERO) return

        try {
            e = x * BigDecimal("0.05")
            etE.setText(e.toPlainString())

            val top = n.toBigDecimal() * z.pow(2) * s.pow(2)
            val bottom = ((n - BigInteger.ONE).toBigDecimal() * e.pow(2)) + (z.pow(2) * s.pow(2))
            result = (top / bottom).setScale(0, RoundingMode.UP).toBigInteger()
            etNResult.setText(String.format(result.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear(v: View) {
        if (!v.isClickable) return

        n = BigInteger.ZERO
        s = BigDecimal.ZERO
        x = BigDecimal.ZERO
        e = BigDecimal.ZERO
        result = BigInteger.ZERO

        etN.setText("")
        etS.setText("")
        etMedia.setText("")
        etE.setText("")
        etNResult.setText("")

        etN.clearFocus()
        etS.clearFocus()
        etMedia.clearFocus()
    }
}