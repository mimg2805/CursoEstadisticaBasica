package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import org.apache.commons.math3.distribution.HypergeometricDistribution
import java.math.MathContext
import java.math.RoundingMode

class CalcDistHipergeometrica : AppCompatActivity() {

    private var successes: BigDecimal = BigDecimal.ZERO
    private var populationSize: BigDecimal = BigDecimal.ZERO
    private var sampleSize: BigDecimal = BigDecimal.ZERO
    private var x: BigDecimal = BigDecimal.ZERO
    private var prob: BigDecimal = BigDecimal.ZERO
    private var calc : String = ""

    private lateinit var mSuccessesTxt: EditText
    private lateinit var mPopulationSizeTxt: EditText
    private lateinit var mSampleSizeTxt: EditText
    private lateinit var mxTxt: EditText
    private lateinit var mprobTxt: EditText

    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    private lateinit var mprobSpinner: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_dist_hipergeometrica)

        mSuccessesTxt = findViewById(R.id.MTxt)
        mPopulationSizeTxt = findViewById(R.id.NTxt)
        mSampleSizeTxt = findViewById(R.id.nTxt)
        mxTxt = findViewById(R.id.xTxt)
        mprobTxt = findViewById(R.id.probTxt)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        mprobSpinner = findViewById(R.id.probSpinner)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.probs_3, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mprobSpinner.adapter = adapter

        mSuccessesTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) successes = strToBigDecimal(text.toString())
            }
        })

        mPopulationSizeTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) populationSize = strToBigDecimal(text.toString())
            }
        })

        mSampleSizeTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) sampleSize = strToBigDecimal(text.toString())
            }
        })

        mxTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) x = strToBigDecimal(text.toString())
            }
        })

        mprobSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                calc = parent?.getItemAtPosition(pos).toString()
                calc()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                calc = ""
            }
        }

        btnLimpiar.setOnClickListener { view -> clear(view) }
    }

    fun calc() {
        prob = BigDecimal.ZERO
        val mc = MathContext(4, RoundingMode.HALF_UP)

        try {
            if (populationSize != BigDecimal.ZERO && successes != BigDecimal.ZERO && sampleSize != BigDecimal.ZERO) { // && x != BigDecimal.ZERO) {
                val xint = x.toInt()
                val hyper =
                    HypergeometricDistribution(null, populationSize.toInt(), successes.toInt(), sampleSize.toInt())

                val equal = BigDecimal(hyper.probability(xint).toString()).setScale(4, RoundingMode.HALF_UP)
                val greater = BigDecimal(hyper.upperCumulativeProbability(xint).toString()).subtract(equal, mc).setScale(4, RoundingMode.HALF_UP)
                val lesser = BigDecimal(hyper.cumulativeProbability(xint).toString()).subtract(equal, mc).setScale(4, RoundingMode.HALF_UP)

                Log.e("x", x.toString())
                Log.e("P(X = x)", equal.toPlainString())
                Log.e("P(X > x)", greater.toPlainString())
                Log.e("P(X < x)", lesser.toPlainString())
                Log.e("sum", (equal + lesser + greater).toPlainString())

                prob = when {
                    calc.contains('>') ->
                        greater
                    calc.contains('<') ->
                        lesser
                    else ->
                        equal
                }

                mprobTxt.setText(prob.round(mc).toPlainString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            toast?.cancel()
            toast = Toast.makeText(this, "Valores inválidos", Toast.LENGTH_SHORT)
            toast?.show()
        }
    }

    fun clear(view: View) {
        if (view.isClickable) {
            successes = BigDecimal.ZERO
            populationSize = BigDecimal.ZERO
            sampleSize = BigDecimal.ZERO
            x = BigDecimal.ZERO
            prob = BigDecimal.ZERO

            mSuccessesTxt.setText("")
            mPopulationSizeTxt.setText("")
            mSampleSizeTxt.setText("")
            mxTxt.setText("")
            mprobTxt.setText("")

            mSuccessesTxt.clearFocus()
            mPopulationSizeTxt.clearFocus()
            mSampleSizeTxt.clearFocus()
            mxTxt.clearFocus()

            mprobSpinner.setSelection(0)
        }
    }
}