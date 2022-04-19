package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import org.apache.commons.math3.distribution.FDistribution
import java.math.MathContext
import java.math.RoundingMode

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcFFisher : AppCompatActivity() {

    private var d1: BigDecimal = BigDecimal.ZERO
    private var d2: BigDecimal = BigDecimal.ZERO
    private var x: BigDecimal = BigDecimal.ZERO
    private var f: BigDecimal = BigDecimal.ZERO
    private var calc = ""

    private lateinit var md1Txt: EditText
    private lateinit var md2Txt: EditText
    private lateinit var mxTxt: EditText
    private lateinit var mProbSpinner: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>
    private lateinit var mprobTxt: EditText

    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_f_fisher)

        md1Txt = findViewById(R.id.d1Txt)
        md2Txt = findViewById(R.id.d2Txt)
        mxTxt = findViewById(R.id.xTxt)
        mprobTxt = findViewById(R.id.probTxt)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        mProbSpinner = findViewById(R.id.probSpinner)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.probs_2, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mProbSpinner.adapter = adapter

        md1Txt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) d1 = strToBigDecimal(text.toString())
            }
        })

        md2Txt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) d2 = strToBigDecimal(text.toString())
            }
        })

        mxTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                x = strToBigDecimal(text.toString())
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

    fun calc() {
        val mc = MathContext(4, RoundingMode.HALF_UP)

        try {
            if (d1 != BigDecimal.ZERO && d2 != BigDecimal.ZERO && x != BigDecimal.ZERO) {
                val d1d = d1.toDouble()
                val d2d = d2.toDouble()
                val xd = x.toDouble()
                val lesser = FDistribution(null, d1d, d2d).cumulativeProbability(xd).toBigDecimal(mc)
                val greater = BigDecimal.ONE.subtract(lesser, mc)

                //Log.e("x", x.toPlainString())
                //Log.e("P(X > x)", greater.toPlainString())
                //Log.e("P(X < x)", lesser.toPlainString())
                //Log.e("sum", (greater + lesser).toPlainString())

                f =
                    if (calc.contains('>'))
                        greater
                    else
                        lesser

                mprobTxt.setText(f.toPlainString())
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
            d1 = BigDecimal.ZERO
            d2 = BigDecimal.ZERO
            x = BigDecimal.ZERO
            calc = ""

            md1Txt.setText("")
            md2Txt.setText("")
            mxTxt.setText("")
            mprobTxt.setText("")

            md1Txt.clearFocus()
            md2Txt.clearFocus()
            mxTxt.clearFocus()
            mprobTxt.clearFocus()
            mProbSpinner.clearFocus()
        }
    }
}