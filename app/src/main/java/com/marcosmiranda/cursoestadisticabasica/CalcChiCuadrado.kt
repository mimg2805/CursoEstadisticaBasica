package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.apache.commons.math3.distribution.ChiSquaredDistribution
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import java.lang.Exception

class CalcChiCuadrado : AppCompatActivity() {

    private var v = BigDecimal.ZERO
    private var x = BigDecimal.ZERO
    private var chi = BigDecimal.ZERO
    private var calc = ""

    private lateinit var mvTxt: EditText
    private lateinit var mxTxt: EditText
    private lateinit var mProbSpinner: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>
    private lateinit var mprobTxt: EditText

    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_chi_cuadrado)

        mvTxt = findViewById(R.id.vTxt)
        mxTxt = findViewById(R.id.xTxt)
        mprobTxt = findViewById(R.id.probTxt)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        mProbSpinner = findViewById(R.id.probSpinner)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.probs_2_inv, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mProbSpinner.adapter = adapter

        mvTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) v = strToBigDecimal(text.toString())
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
            if (v != BigDecimal.ZERO) {
                val xd = x.toDouble()
                val vd = v.toDouble()
                val lesser = ChiSquaredDistribution(null, vd).cumulativeProbability(xd).toBigDecimal(mc)
                val greater = BigDecimal.ONE.subtract(lesser, mc)

                // Log.e("x", x.toPlainString())
                // Log.e("P(X > x)", greater.toPlainString())
                // Log.e("P(X < x)", lesser.toPlainString())
                // Log.e("sum", (greater + lesser).toPlainString())

                chi =
                    if (calc.contains('>'))
                        greater
                    else
                        lesser

                mprobTxt.setText(chi.toPlainString())
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
            v = BigDecimal.ZERO
            x = BigDecimal.ZERO
            chi = BigDecimal.ZERO
            calc = ""

            mvTxt.setText("")
            mxTxt.setText("")
            mprobTxt.setText("")

            mvTxt.clearFocus()
            mxTxt.clearFocus()
            mprobTxt.clearFocus()
            mProbSpinner.clearFocus()
        }
    }
}
