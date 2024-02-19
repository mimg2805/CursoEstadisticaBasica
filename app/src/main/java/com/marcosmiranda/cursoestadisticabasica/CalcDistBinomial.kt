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
import java.math.MathContext
import java.math.RoundingMode
import org.apache.commons.math3.distribution.BinomialDistribution

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger

class CalcDistBinomial : AppCompatActivity() {

    private var n = BigInteger.ZERO
    private var x = BigInteger.ZERO
    private var pi = BigDecimal.ZERO
    private var prob = BigDecimal.ZERO
    private var calc = ""
    private val mc = MathContext(4, RoundingMode.HALF_UP)

    private lateinit var etN: EditText
    private lateinit var etX: EditText
    private lateinit var etPi: EditText
    private lateinit var etProb: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    private lateinit var spnProb: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_dist_binomial)

        etN = findViewById(R.id.activity_calc_dist_binomial_et_n)
        etX = findViewById(R.id.activity_calc_dist_binomial_et_x)
        etPi = findViewById(R.id.activity_calc_dist_binomial_et_pi)
        etProb = findViewById(R.id.activity_calc_dist_binomial_et_prob)
        btnClear = findViewById(R.id.activity_calc_dist_binomial_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        spnProb = findViewById(R.id.activity_calc_dist_binomial_spn_prob)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.probs_3, R.layout.spinner_item
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
                x = strToBigInteger(text.toString())
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

    fun calc() {
        prob = BigDecimal.ZERO
        if (n == BigInteger.ZERO || pi == BigDecimal.ZERO) return

        try {
            val xint = x.toInt()
            val binomial = BinomialDistribution(null, n.toInt(), pi.toDouble())

            val equal = BigDecimal.valueOf(binomial.probability(xint)).round(mc)
            val lesser = (BigDecimal.valueOf(binomial.cumulativeProbability(xint)) - equal).abs(mc).round(mc)
            val greater = (BigDecimal.ONE - (equal + lesser)).abs(mc).round(mc)

            prob = when {
                calc.contains('>') ->
                    greater
                calc.contains('<') ->
                    lesser
                else ->
                    equal
            }

            if (prob != BigDecimal.ZERO) {
                // prob = prob.round(mc)
                etProb.setText(prob.toPlainString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear(v: View) {
        if (!v.isClickable) return

        n = BigInteger.ZERO
        x = BigInteger.ZERO
        pi = BigDecimal.ZERO
        prob = BigDecimal.ZERO

        etN.setText("")
        etX.setText("")
        etPi.setText("")
        etProb.setText("")

        etN.clearFocus()
        etX.clearFocus()
        etPi.clearFocus()
        spnProb.clearFocus()

        spnProb.setSelection(0)
    }
}