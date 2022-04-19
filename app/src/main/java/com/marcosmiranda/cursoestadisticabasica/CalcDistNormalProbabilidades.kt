package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import org.apache.commons.math3.distribution.NormalDistribution

class CalcDistNormalProbabilidades : AppCompatActivity() {

    private var mi: BigDecimal = BigDecimal.ZERO
    private var sigma: BigDecimal = BigDecimal.ZERO
    private var x: BigDecimal = BigDecimal.ZERO
    private var prob: BigDecimal = BigDecimal.ZERO
    private var calc: String = ""

    private lateinit var mmiTxt: EditText
    private lateinit var msigmaTxt: EditText
    private lateinit var mxTxt: EditText
    private lateinit var mprobTxt: EditText

    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    private lateinit var mprobSpinner: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_dist_normal_probabilidades)

        mmiTxt = findViewById(R.id.miTxt)
        msigmaTxt = findViewById(R.id.sigmaTxt)
        mxTxt = findViewById(R.id.xTxt)
        mprobTxt = findViewById(R.id.probTxt)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        mprobSpinner = findViewById(R.id.probSpinner)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.probs_2_inv, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mprobSpinner.adapter = adapter

        mmiTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) mi = strToBigDecimal(text.toString())
            }
        })

        msigmaTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) sigma = strToBigDecimal(text.toString())
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
        val mc = MathContext(4, RoundingMode.HALF_EVEN)
        val mid = mi.toDouble()
        val sigmad = sigma.toDouble()
        val xd = x.toDouble()

        try {
            if (sigma != BigDecimal.ZERO) { // && x != BigDecimal.ZERO) {
                val lesser = NormalDistribution(null, mid, sigmad).cumulativeProbability(xd).toBigDecimal(mc).setScale(4, RoundingMode.HALF_UP)
                val greater = BigDecimal.ONE.subtract(lesser, mc)

                prob = if (calc.contains('>')) {
                    greater
                } else {
                    lesser
                }

                // Log.e("x", x.toString())
                // Log.e("P(X > x)", greater.toPlainString())
                // Log.e("P(X < x)", lesser.toPlainString())

                mprobTxt.setText(prob.toPlainString())
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
            mi = BigDecimal.ZERO
            sigma = BigDecimal.ZERO
            x = BigDecimal.ZERO
            prob = BigDecimal.ZERO

            mmiTxt.setText("")
            msigmaTxt.setText("")
            mxTxt.setText("")
            mprobTxt.setText("")

            mmiTxt.clearFocus()
            msigmaTxt.clearFocus()
            mxTxt.clearFocus()
            mprobTxt.clearFocus()
            mprobSpinner.clearFocus()

            mprobSpinner.setSelection(0)
        }
    }
}