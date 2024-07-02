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
import ch.obermuhlner.math.big.BigDecimalMath.sqrt
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode
import java.util.Locale
import org.apache.commons.math3.distribution.NormalDistribution

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger

class CalcPruebaHipotesisProporcionPoblacional : AppCompatActivity() {

    private var n = BigInteger.ZERO
    private var p = BigDecimal.ZERO
    private var pComp = BigDecimal.ZERO
    private var pi = BigDecimal.ZERO
    private var piComp = BigDecimal.ZERO
    private var z = BigDecimal.ZERO
    private var prob = BigDecimal.ZERO
    private var calc = ""
    private val mc = MathContext(4, RoundingMode.HALF_UP)

    private lateinit var etN: EditText
    private lateinit var etP: EditText
    private lateinit var etPi: EditText
    private lateinit var etZ: EditText
    private lateinit var etProb: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    private lateinit var spnProb: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_prueba_hipotesis_proporcion_poblacional)

        etN = findViewById(R.id.activity_calc_prueba_hipotesis_proporcion_poblacional_et_n)
        etP = findViewById(R.id.activity_calc_prueba_hipotesis_proporcion_poblacional_et_p)
        etPi = findViewById(R.id.activity_calc_prueba_hipotesis_proporcion_poblacional_et_pi)
        etZ = findViewById(R.id.activity_calc_prueba_hipotesis_proporcion_poblacional_et_z)
        etProb = findViewById(R.id.activity_calc_prueba_hipotesis_proporcion_poblacional_et_prob)
        btnClear = findViewById(R.id.activity_calc_prueba_hipotesis_proporcion_poblacional_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        spnProb = findViewById(R.id.activity_calc_prueba_hipotesis_proporcion_poblacional_spn_prob)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.probs_2, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnProb.adapter = adapter

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

        etPi.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                pi = strToBigDecimal(text.toString())
                piComp = BigDecimal.ONE - pi
            }
        })

        spnProb.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                calc = parent?.getItemAtPosition(pos).toString()
                calc()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                calc = ""
            }
        }

        btnClear.setOnClickListener { v -> clear(v) }
    }

    private fun checkReqN(): Boolean {
        return if (n <= BigInteger.valueOf(30)) {
            etN.error = getText(R.string.req_n_30)
            etProb.setText("")
            false
        } else {
            etN.error = null
            true
        }
    }

    private fun checkReqNP(): Boolean {
        if (!checkReqN()) return false
        return if ((n.toBigDecimal() * p) <= BigDecimal.valueOf(5) || (n.toBigDecimal() * pComp) <= BigDecimal.valueOf(5)) {
            etP.error = getText(R.string.req_n_p_5)
            etProb.setText("")
            false
        } else {
            etP.error = null
            true
        }
    }

    fun calc() {
        if (!checkReqN() || !checkReqNP()) return
        if (pi == BigDecimal.ZERO) return

        try {
            z = (p - pi).divide(sqrt((pi * piComp).divide(n.toBigDecimal(), mc), mc), mc)
            etZ.setText(String.format(Locale.ENGLISH, "%.2f", z))

            val mi = 0.0
            val sigma = 1.0
            val zd = z.toDouble()
            val lesser = NormalDistribution(null, mi, sigma).cumulativeProbability(zd).toBigDecimal(mc)
            val greater = BigDecimal.ONE - lesser

            prob = if (calc.contains('>')) {
                greater
            } else {
                lesser
            }

            etProb.setText(String.format(Locale.ENGLISH, "%.4f", prob))
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
        pi = BigDecimal.ZERO
        z = BigDecimal.ZERO
        prob = BigDecimal.ZERO

        etN.setText("")
        etP.setText("")
        etPi.setText("")
        etZ.setText("")
        etProb.setText("")

        etN.clearFocus()
        etP.clearFocus()
        etPi.clearFocus()
        etZ.clearFocus()
    }
}
