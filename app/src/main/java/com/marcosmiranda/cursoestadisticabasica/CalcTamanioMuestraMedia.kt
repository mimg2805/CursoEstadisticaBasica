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

class CalcTamanioMuestraMedia : AppCompatActivity() {

    private var n = BigInteger.ZERO
    private var s = BigDecimal.ZERO
    private var x = BigDecimal.ZERO
    private var e = BigDecimal.ZERO
    private var eCalc = ""
    private var eRelative = false
    private var eAbsolute = false
    private val z = BigDecimal("1.96")
    private var result: BigInteger = BigInteger.ZERO

    private lateinit var etN: EditText
    private lateinit var etS: EditText
    private lateinit var etMedia: EditText
    private lateinit var spnE: Spinner
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

        spnE = findViewById(R.id.activity_calc_tamanio_muestra_media_spn_E)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.errors, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnE.adapter = adapter
        eRelative = eCalc.contains('R')
        eAbsolute = eCalc.contains('A')

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
                eCalc()
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                x = strToBigDecimal(text.toString())
            }
        })

        etE.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank() || eRelative) return
                // if (eAbsolute) e = strToBigDecimal(text.toString())
                eCalc()
                calc()
            }
        })

        spnE.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                eCalc = parent?.getItemAtPosition(pos).toString()
                eCalc()
                if (eAbsolute) etE.setText("")
                calc()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                eCalc = ""
            }
        }

        btnClear.setOnClickListener { v -> clear(v) }
    }

    fun eCalc() {
        eRelative = eCalc.contains('R')
        eAbsolute = eCalc.contains('A')
        if (eRelative) {
            // etE.isFocusable = false
            etE.isEnabled = false
            etE.isClickable = false
            e = BigDecimal("0.05") * x
            etE.setText(e.toPlainString())
        } else if (eAbsolute) {
            // etE.isFocusable = true
            etE.isEnabled = true
            etE.isClickable = true
            // e = BigDecimal.ZERO
            // etE.setText("")
            etE.requestFocus()
            e = strToBigDecimal(etE.text.toString())
        }
        // etE.setText(e.toPlainString())
    }

    fun calc() {
        if (n == BigInteger.ZERO || s == BigDecimal.ZERO || e == BigDecimal.ZERO) return

        try {
            val top = n.toBigDecimal() * z.pow(2) * s.pow(2)
            // Log.e("top", top.toPlainString())
            val bottom = ((n - BigInteger.ONE).toBigDecimal() * e.pow(2)) + (z.pow(2) * s.pow(2))
            // Log.e("bottom", bottom.toPlainString())
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
        etE.clearFocus()
    }
}