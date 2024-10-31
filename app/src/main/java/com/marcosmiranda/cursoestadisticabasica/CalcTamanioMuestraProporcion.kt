package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

class CalcTamanioMuestraProporcion : AppCompatActivity() {

    private var n = BigInteger.ZERO
    private var p = BigDecimal.ZERO
    private var pComp = BigDecimal.ZERO
    private var e = BigDecimal.ZERO
    private var eCalc = ""
    private val z = BigDecimal("1.96")
    private var result = BigInteger.ZERO

    private lateinit var etN: EditText
    private lateinit var etP: EditText
    private lateinit var spnE: Spinner
    private lateinit var etE: EditText
    private lateinit var etZ: EditText
    private lateinit var etNResult: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_tamanio_muestra_proporcion)

        etN = findViewById(R.id.activity_calc_tamanio_muestra_proporcion_et_N)
        etP = findViewById(R.id.activity_calc_tamanio_muestra_proporcion_et_P)
        etE = findViewById(R.id.activity_calc_tamanio_muestra_proporcion_et_E)
        etZ = findViewById(R.id.activity_calc_tamanio_muestra_proporcion_et_Z)
        etNResult = findViewById(R.id.activity_calc_tamanio_muestra_proporcion_et_n_result)
        btnClear = findViewById(R.id.activity_calc_tamanio_muestra_proporcion_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        spnE = findViewById(R.id.activity_calc_tamanio_muestra_proporcion_spn_E)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.errors, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnE.adapter = adapter

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

        spnE.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                eCalc = parent?.getItemAtPosition(pos).toString()
                calc()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                eCalc = ""
            }
        }

        btnClear.setOnClickListener { v -> clear(v) }
    }

    fun calc() {
        if (n == BigInteger.ZERO || p == BigDecimal.ZERO) return

        try {
            e = BigDecimal("0.05")
            if (eCalc.contains('R')) e *= p
            etE.setText(e.toPlainString())

            val pComp = BigDecimal.ONE - p
            val top = n.toBigDecimal() * z.pow(2) * p * pComp
            val bottom = (n - BigInteger.ONE).toBigDecimal() * e.pow(2) + z.pow(2) * p * pComp
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
        p = BigDecimal.ZERO
        e = BigDecimal.ZERO
        result = BigInteger.ZERO

        etN.setText("")
        etP.setText("")
        etE.setText("")
        etNResult.setText("")

        etN.clearFocus()
        etP.clearFocus()
    }
}