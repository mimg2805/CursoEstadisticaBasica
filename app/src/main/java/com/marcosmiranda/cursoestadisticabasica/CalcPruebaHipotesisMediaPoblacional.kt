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

class CalcPruebaHipotesisMediaPoblacional : AppCompatActivity() {

    private var n = BigInteger.ZERO
    private var x = BigDecimal.ZERO
    private var u = BigDecimal.ZERO
    private var s = BigDecimal.ZERO
    private var z = BigDecimal.ZERO
    private var prob = BigDecimal.ZERO
    private var calc = ""
    private val mc = MathContext(5, RoundingMode.HALF_UP)

    private lateinit var etN: EditText
    private lateinit var etX: EditText
    private lateinit var etU: EditText
    private lateinit var etS: EditText
    private lateinit var etZ: EditText
    private lateinit var etProb: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    private lateinit var spnProb: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_prueba_hipotesis_media_poblacional)

        etN = findViewById(R.id.activity_calc_prueba_hipotesis_media_poblacional_et_n)
        etX = findViewById(R.id.activity_calc_prueba_hipotesis_media_poblacional_et_x)
        etS = findViewById(R.id.activity_calc_prueba_hipotesis_media_poblacional_et_s)
        etU = findViewById(R.id.activity_calc_prueba_hipotesis_media_poblacional_et_u)
        etZ = findViewById(R.id.activity_calc_prueba_hipotesis_media_poblacional_et_z)
        etProb = findViewById(R.id.activity_calc_prueba_hipotesis_media_poblacional_et_prob)
        btnClear = findViewById(R.id.activity_calc_prueba_hipotesis_media_poblacional_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        spnProb = findViewById(R.id.activity_calc_prueba_hipotesis_media_poblacional_spn_prob)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.x_probs_2, R.layout.spinner_item
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

        etX.addTextChangedListener(object : TextWatcher {
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

        etU.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                u = strToBigDecimal(text.toString())
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

    fun calc() {
        if (!checkReqN()) return
        if (s == BigDecimal.ZERO) return

        try {
            z = (x - u).divide(s.divide(sqrt(n.toBigDecimal(), mc), mc), mc)
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
        x = BigDecimal.ZERO
        u = BigDecimal.ZERO
        s = BigDecimal.ZERO
        z = BigDecimal.ZERO

        etN.setText("")
        etX.setText("")
        etU.setText("")
        etS.setText("")
        etZ.setText("")

        etN.clearFocus()
        etX.clearFocus()
        etU.clearFocus()
        etS.clearFocus()
    }
}
