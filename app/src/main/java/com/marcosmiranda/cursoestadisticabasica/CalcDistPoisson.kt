package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.BigInteger

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import org.apache.commons.math3.distribution.PoissonDistribution
import java.math.MathContext
import java.math.RoundingMode

class CalcDistPoisson : AppCompatActivity() {

    private var x: BigInteger = BigInteger.ZERO
    private var lambda: BigDecimal = BigDecimal.ZERO
    private var e: BigDecimal = BigDecimal("2.71828")
    private var prob: BigDecimal = BigDecimal.ZERO
    private var calc: String = ""

    private lateinit var mxTxt: EditText
    private lateinit var mLambdaTxt: EditText
    private lateinit var meTxt: EditText
    private lateinit var mProbTxt: EditText

    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    private lateinit var mProbSpinner: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_dist_poisson)

        mxTxt = findViewById(R.id.xTxt)
        mLambdaTxt = findViewById(R.id.lambdaTxt)
        meTxt = findViewById(R.id.eTxt)
        mProbTxt = findViewById(R.id.probTxt)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        meTxt.setText(e.toPlainString())

        mProbSpinner = findViewById(R.id.probSpinner)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.probs_3, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mProbSpinner.adapter = adapter

        mxTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) x = strToBigInteger(text.toString())
            }
        })

        mLambdaTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) lambda = strToBigDecimal(text.toString())
            }
        })

        mProbSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    private fun calc() {
        prob = BigDecimal.ZERO
        val mc = MathContext(4, RoundingMode.HALF_UP)

        try {
            val xint = x.toInt()
            val lambdad = lambda.toDouble()

            val equal = PoissonDistribution(lambdad).probability(xint).toBigDecimal(mc).setScale(4, RoundingMode.HALF_UP)
            val lesser = PoissonDistribution(lambdad).cumulativeProbability(xint).toBigDecimal(mc).subtract(equal, mc).setScale(4, RoundingMode.HALF_UP)
            val greater = BigDecimal.ONE.subtract(lesser + equal, mc).setScale(4, RoundingMode.HALF_UP)

            //Log.e("x", x.toString())
            //Log.e("P(X = x)", equal.toPlainString())
            //Log.e("P(X > x)", greater.toPlainString())
            //Log.e("P(X < x)", lesser.toPlainString())
            //Log.e("sum", (equal + lesser + greater).toPlainString())

            prob = when {
                calc.contains('>') ->
                    greater
                calc.contains('<') ->
                    lesser
                else ->
                    equal
            }

            mProbTxt.setText(prob.toPlainString())
        } catch (e: Exception) {
            e.printStackTrace()
            toast?.cancel()
            toast = Toast.makeText(this, "Valores inválidos", Toast.LENGTH_SHORT)
            toast?.show()
        }
    }

    fun clear(view: View) {
        if (view.isClickable) {
            x = BigInteger.ZERO
            lambda = BigDecimal.ZERO
            prob = BigDecimal.ZERO

            mxTxt.setText("")
            mLambdaTxt.setText("")
            mProbTxt.setText("")

            mxTxt.clearFocus()
            mLambdaTxt.clearFocus()
            meTxt.clearFocus()
            mProbTxt.clearFocus()

            mProbSpinner.setSelection(adapter.getPosition(resources.getStringArray(R.array.probs_3)[0]))
        }
    }
}