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
import org.apache.commons.math3.distribution.NormalDistribution

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger

class CalcPruebaHipotesis2MediasPoblacionales : AppCompatActivity() {

    private var n1 = BigInteger.ZERO
    private var n2 = BigInteger.ZERO
    private var x1 = BigDecimal.ZERO
    private var x2 = BigDecimal.ZERO
    private var s1 = BigDecimal.ZERO
    private var s2 = BigDecimal.ZERO
    private var z = BigDecimal.ZERO
    private var prob = BigDecimal.ZERO
    private var calc = ""
    private val mc = MathContext(5, RoundingMode.HALF_UP)

    private lateinit var etN1: EditText
    private lateinit var etN2: EditText
    private lateinit var etX1: EditText
    private lateinit var etX2: EditText
    private lateinit var etS1: EditText
    private lateinit var etS2: EditText
    private lateinit var etZ: EditText
    private lateinit var etProb: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    private lateinit var spnProb: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_prueba_hipotesis_2_medias_poblacionales)

        etN1 = findViewById(R.id.activity_calc_prueba_hipotesis_2_medias_poblacionales_et_n1)
        etN2 = findViewById(R.id.activity_calc_prueba_hipotesis_2_medias_poblacionales_et_n2)
        etX1 = findViewById(R.id.activity_calc_prueba_hipotesis_2_medias_poblacionales_et_x1)
        etX2 = findViewById(R.id.activity_calc_prueba_hipotesis_2_medias_poblacionales_et_x2)
        etS1 = findViewById(R.id.activity_calc_prueba_hipotesis_2_medias_poblacionales_et_s1)
        etS2 = findViewById(R.id.activity_calc_prueba_hipotesis_2_medias_poblacionales_et_s2)
        etZ = findViewById(R.id.activity_calc_prueba_hipotesis_2_medias_poblacionales_et_z)
        etProb = findViewById(R.id.activity_calc_prueba_hipotesis_2_medias_poblacionales_et_prob)
        btnClear = findViewById(R.id.activity_calc_prueba_hipotesis_2_medias_poblacionales_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        spnProb = findViewById(R.id.activity_calc_prueba_hipotesis_2_medias_poblacionales_spn_prob)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.probs_2, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnProb.adapter = adapter

        etN1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                n1 = strToBigInteger(text.toString())
            }
        })

        etN2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                n2 = strToBigInteger(text.toString())
            }
        })

        etX1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                x1 = strToBigDecimal(text.toString())
            }
        })

        etX2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                x2 = strToBigDecimal(text.toString())
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
        if (n1 <= BigInteger.valueOf(30)) {
            etN1.error = getText(R.string.req_n1_n2_30)
            etProb.setText("")
            return false
        }

        if (n2 <= BigInteger.valueOf(30)) {
            etN2.error = getText(R.string.req_n1_n2_30)
            etProb.setText("")
            return false
        }

        etN1.error = null
        etN2.error = null
        return true
    }

    fun calc() {
        if (!checkReqN()) return

        if (n1 == BigInteger.ZERO || n2 == BigInteger.ZERO || x1 == BigDecimal.ZERO || x2 == BigDecimal.ZERO || s1 == BigDecimal.ZERO || s2 == BigDecimal.ZERO) return

        try {
            val zTop = x1 - x2
            val zBottom1 = s1.pow(2).divide(n1.toBigDecimal(), mc)
            val zBottom2 = s2.pow(2).divide(n2.toBigDecimal(), mc)
            val zBottom = sqrt(zBottom1 + zBottom2, mc)
            z = zTop.divide(zBottom, mc)
            etZ.setText(String.format("%.2f", z))

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

            etProb.setText(String.format("%.4f", prob))
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear(v: View) {
        if (!v.isClickable) return

        n1 = BigInteger.ZERO
        n2 = BigInteger.ZERO
        x1 = BigDecimal.ZERO
        x2 = BigDecimal.ZERO
        s1 = BigDecimal.ZERO
        s2 = BigDecimal.ZERO
        z = BigDecimal.ZERO

        etN1.setText("")
        etN2.setText("")
        etX1.setText("")
        etX2.setText("")
        etS1.setText("")
        etS2.setText("")
        etZ.setText("")
        etProb.setText("")

        etN1.clearFocus()
        etN2.clearFocus()
        etX1.clearFocus()
        etX2.clearFocus()
        etS1.clearFocus()
        etS2.clearFocus()
        etProb.clearFocus()
    }
}
