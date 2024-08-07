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
import org.apache.commons.math3.distribution.PoissonDistribution

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcDistPoisson : AppCompatActivity() {

    private var x = BigInteger.ZERO
    private var lambda = BigDecimal.ZERO
    private var e = BigDecimal("2.71828")
    private var prob = BigDecimal.ZERO
    private var calc = ""
    private val mc = MathContext(4, RoundingMode.HALF_UP)

    private lateinit var etX: EditText
    private lateinit var etLambda: EditText
    private lateinit var etE: EditText
    private lateinit var etProb: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    private lateinit var spnProb: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_dist_poisson)

        etX = findViewById(R.id.activity_calc_dist_poisson_et_x)
        etLambda = findViewById(R.id.activity_calc_dist_poisson_et_lambda)
        etE = findViewById(R.id.activity_calc_dist_poisson_et_e)
        etProb = findViewById(R.id.activity_calc_dist_poisson_et_prob)
        btnClear = findViewById(R.id.activity_calc_dist_poisson_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        etE.setText(e.toPlainString())

        spnProb = findViewById(R.id.activity_calc_dist_poisson_spn_prob)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.x_probs_3, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnProb.adapter = adapter

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

        etLambda.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                lambda = strToBigDecimal(text.toString())
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

    private fun calc() {
        prob = BigDecimal.ZERO

        try {
            val xint = x.toInt()
            val lambdad = lambda.toDouble()

            val equal = PoissonDistribution(lambdad).probability(xint).toBigDecimal(mc).round(mc)
            val lesser = (PoissonDistribution(lambdad).cumulativeProbability(xint).toBigDecimal(mc) - equal).round(mc)
            val greater = (BigDecimal.ONE - (lesser + equal)).round(mc)

            prob = when {
                calc.contains('>') ->
                    greater
                calc.contains('<') ->
                    lesser
                else ->
                    equal
            }

            etProb.setText(prob.toPlainString())
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear(v: View) {
        if (!v.isClickable) return

        x = BigInteger.ZERO
        lambda = BigDecimal.ZERO
        prob = BigDecimal.ZERO

        etX.setText("")
        etLambda.setText("")
        etProb.setText("")

        etX.clearFocus()
        etLambda.clearFocus()
        etE.clearFocus()
        etProb.clearFocus()

        spnProb.setSelection(adapter.getPosition(resources.getStringArray(R.array.x_probs_3)[0]))
    }
}