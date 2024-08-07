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
import java.math.MathContext
import java.math.RoundingMode
import org.apache.commons.math3.distribution.HypergeometricDistribution

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcDistHipergeometrica : AppCompatActivity() {

    private var successes = BigDecimal.ZERO
    private var populationSize = BigDecimal.ZERO
    private var sampleSize = BigDecimal.ZERO
    private var x = BigDecimal.ZERO
    private var prob = BigDecimal.ZERO
    private var calc = ""
    private val mc = MathContext(4, RoundingMode.HALF_UP)

    private lateinit var etSuccesses: EditText
    private lateinit var etPopulationSize: EditText
    private lateinit var etSampleSize: EditText
    private lateinit var etX: EditText
    private lateinit var etProb: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    private lateinit var spnProb: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_dist_hipergeometrica)

        etSuccesses = findViewById(R.id.activity_calc_dist_hipergeometrica_et_successes)
        etPopulationSize = findViewById(R.id.activity_calc_dist_hipergeometrica_et_population_size)
        etSampleSize = findViewById(R.id.activity_calc_dist_hipergeometrica_et_sample_size)
        etX = findViewById(R.id.activity_calc_dist_hipergeometrica_et_x)
        etProb = findViewById(R.id.activity_calc_dist_hipergeometrica_et_prob)
        btnClear = findViewById(R.id.activity_calc_dist_hipergeometrica_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        spnProb = findViewById(R.id.activity_calc_dist_hipergeometrica_spn_prob)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.x_probs_3, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnProb.adapter = adapter

        etSuccesses.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                successes = strToBigDecimal(text.toString())
            }
        })

        etPopulationSize.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                populationSize = strToBigDecimal(text.toString())
            }
        })

        etSampleSize.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                sampleSize = strToBigDecimal(text.toString())
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

        if (populationSize == BigDecimal.ZERO || successes == BigDecimal.ZERO || sampleSize == BigDecimal.ZERO) return // || x == BigDecimal.ZERO)

        try {
            val xint = x.toInt()
            val hyper = HypergeometricDistribution(null, populationSize.toInt(), successes.toInt(), sampleSize.toInt())

            val equal = BigDecimal.valueOf(hyper.probability(xint)).round(mc)
            val greater = (BigDecimal.valueOf(hyper.upperCumulativeProbability(xint)) - equal).round(mc)
            val lesser = (BigDecimal.valueOf(hyper.cumulativeProbability(xint)) - equal).round(mc)

            prob = when {
                calc.contains('>') ->
                    greater
                calc.contains('<') ->
                    lesser
                else ->
                    equal
            }

            etProb.setText(prob.round(mc).toPlainString())
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear(v: View) {
        if (!v.isClickable) return

        successes = BigDecimal.ZERO
        populationSize = BigDecimal.ZERO
        sampleSize = BigDecimal.ZERO
        x = BigDecimal.ZERO
        prob = BigDecimal.ZERO

        etSuccesses.setText("")
        etPopulationSize.setText("")
        etSampleSize.setText("")
        etX.setText("")
        etProb.setText("")

        etSuccesses.clearFocus()
        etPopulationSize.clearFocus()
        etSampleSize.clearFocus()
        etX.clearFocus()

        spnProb.setSelection(0)
    }
}