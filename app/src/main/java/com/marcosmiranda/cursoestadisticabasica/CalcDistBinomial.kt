package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.math.*

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger
import org.apache.commons.math3.distribution.BinomialDistribution

class CalcDistBinomial : AppCompatActivity() {

    private var n: BigInteger = BigInteger.ZERO
    private var x: BigInteger = BigInteger.ZERO
    private var pi: BigDecimal = BigDecimal.ZERO
    private var prob: BigDecimal = BigDecimal.ZERO
    private var calc: String = ""

    private lateinit var mnTxt: EditText
    private lateinit var mxTxt: EditText
    private lateinit var mpiTxt: EditText
    private lateinit var mprobTxt: EditText

    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    private lateinit var mprobSpinner: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_dist_binomial)

        mnTxt = findViewById(R.id.nTxt)
        mxTxt = findViewById(R.id.xTxt)
        mpiTxt = findViewById(R.id.piTxt)
        mprobTxt = findViewById(R.id.probTxt)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        mprobSpinner = findViewById(R.id.probSpinner)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.probs_3, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mprobSpinner.adapter = adapter

        mnTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) n = strToBigInteger(text.toString())
            }
        })

        mxTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) x = strToBigInteger(text.toString())
            }
        })

        mpiTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) pi = strToBigDecimal(text.toString())
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
            if (n != BigInteger.ZERO && pi != BigDecimal.ZERO) {
                val xint = x.toInt()
                val binomial = BinomialDistribution(null, n.toInt(), pi.toDouble())

                val equal = BigDecimal(binomial.probability(xint).toString()).setScale(4, RoundingMode.HALF_UP)
                val lesser = BigDecimal(binomial.cumulativeProbability(xint).toString()).subtract(equal, mc).abs(mc).setScale(4, RoundingMode.HALF_UP)
                val greater = BigDecimal.ONE.subtract(equal + lesser, mc).abs(mc).setScale(4, RoundingMode.HALF_UP)

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

                //prob = prob.setScale(4, RoundingMode.HALF_UP)

                if (prob != BigDecimal.ZERO) {
                    mprobTxt.setText(prob.toPlainString())
                }
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
            n = BigInteger.ZERO
            x = BigInteger.ZERO
            pi = BigDecimal.ZERO
            prob = BigDecimal.ZERO

            mnTxt.setText("")
            mxTxt.setText("")
            mpiTxt.setText("")
            mprobTxt.setText("")

            mnTxt.clearFocus()
            mxTxt.clearFocus()
            mpiTxt.clearFocus()
            mprobSpinner.clearFocus()

            mprobSpinner.setSelection(0)
        }
    }
}