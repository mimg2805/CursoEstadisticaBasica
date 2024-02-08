package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ch.obermuhlner.math.big.BigDecimalMath.sqrt
import java.math.*
import org.apache.commons.math3.distribution.NormalDistribution

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger

class CalcPruebaHipotesisMediaPoblacional : AppCompatActivity() {

    private var n: BigInteger = BigInteger.ZERO
    private var x: BigDecimal = BigDecimal.ZERO
    private var u: BigDecimal = BigDecimal.ZERO
    private var s: BigDecimal = BigDecimal.ZERO
    private var Z: BigDecimal = BigDecimal.ZERO
    private var prob: BigDecimal = BigDecimal.ZERO
    private var calc: String = ""

    private lateinit var mnTxt: EditText
    private lateinit var mxTxt: EditText
    private lateinit var muTxt: EditText
    private lateinit var msTxt: EditText
    private lateinit var mZTxt: EditText
    private lateinit var mprobTxt: EditText

    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    private lateinit var mprobSpinner: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_prueba_hipotesis_media_poblacional)

        mnTxt = findViewById(R.id.nTxt)
        mxTxt = findViewById(R.id.xTxt)
        muTxt = findViewById(R.id.uTxt)
        msTxt = findViewById(R.id.sTxt)
        mZTxt = findViewById(R.id.ZTxt)
        mprobTxt = findViewById(R.id.probTxt)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        mprobSpinner = findViewById(R.id.probSpinner)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.probs_2, R.layout.spinner_item
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
                if (!text.isNullOrBlank()) x = strToBigDecimal(text.toString())
            }
        })

        muTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) u = strToBigDecimal(text.toString())
            }
        })

        msTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) s = strToBigDecimal(text.toString())
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
        Z = BigDecimal.ZERO
        val mc5 = MathContext(5, RoundingMode.HALF_UP)
        val mc6 = MathContext(6, RoundingMode.HALF_UP)

        if (n <= BigInteger.valueOf(30)) {
            toast?.cancel()
            toast = Toast.makeText(this, "n debe ser mayor que 30", Toast.LENGTH_SHORT)
            toast?.show()
            return
        } else {
            toast?.cancel()
        }

        try {
            if (n == BigInteger.ZERO && s == BigDecimal.ZERO) return

            Z = x.subtract(u).divide(s.divide(sqrt(n.toBigDecimal(), mc6), mc6), mc5)
            mZTxt.setText(Z.toPlainString())

            val mi = 0.0
            val sigma = 1.0
            val zd = Z.toDouble()
            val lesser = NormalDistribution(null, mi, sigma).cumulativeProbability(zd).toBigDecimal(mc6)
            val greater = BigDecimal.ONE.subtract(lesser, mc5)

            prob = if (calc.contains('>')) {
                greater
            } else {
                lesser
            }

            mprobTxt.setText(prob.toPlainString())
        } catch (e: Exception) {
            e.printStackTrace()
            toast?.cancel()
            toast = Toast.makeText(this, "Valores inválidos", Toast.LENGTH_SHORT)
            toast?.show()
        }
    }

    fun clear(view: View) {
        if (!view.isClickable) return

        n = BigInteger.ZERO
        x = BigDecimal.ZERO
        u = BigDecimal.ZERO
        s = BigDecimal.ZERO
        Z = BigDecimal.ZERO

        mnTxt.setText("")
        mxTxt.setText("")
        muTxt.setText("")
        msTxt.setText("")
        mZTxt.setText("")

        mnTxt.clearFocus()
        mxTxt.clearFocus()
        muTxt.clearFocus()
        msTxt.clearFocus()
    }
}
