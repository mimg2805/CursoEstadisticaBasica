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
import ch.obermuhlner.math.big.BigDecimalMath.sqrt
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode
import org.apache.commons.math3.distribution.NormalDistribution

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger

class CalcPruebaHipotesis2MediasPoblacionales : AppCompatActivity() {

    private var n1: BigInteger = BigInteger.ZERO
    private var n2: BigInteger = BigInteger.ZERO
    private var x1: BigDecimal = BigDecimal.ZERO
    private var x2: BigDecimal = BigDecimal.ZERO
    private var s1: BigDecimal = BigDecimal.ZERO
    private var s2: BigDecimal = BigDecimal.ZERO
    private var Z: BigDecimal = BigDecimal.ZERO
    private var prob: BigDecimal = BigDecimal.ZERO
    private var calc: String = ""

    private lateinit var mn1Txt: EditText
    private lateinit var mn2Txt: EditText
    private lateinit var mx1Txt: EditText
    private lateinit var mx2Txt: EditText
    private lateinit var ms1Txt: EditText
    private lateinit var ms2Txt: EditText
    private lateinit var mZTxt: EditText
    private lateinit var mprobTxt: EditText

    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    private lateinit var mprobSpinner: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_prueba_hipotesis_2_medias_poblacionales)

        mn1Txt = findViewById(R.id.n1Txt)
        mn2Txt = findViewById(R.id.n2Txt)
        mx1Txt = findViewById(R.id.x1Txt)
        mx2Txt = findViewById(R.id.x2Txt)
        ms1Txt = findViewById(R.id.s1Txt)
        ms2Txt = findViewById(R.id.s2Txt)
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

        mn1Txt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) n1 = strToBigInteger(text.toString())
            }
        })

        mn2Txt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) n2 = strToBigInteger(text.toString())
            }
        })

        mx1Txt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) x1 = strToBigDecimal(text.toString())
            }
        })

        mx2Txt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) x2 = strToBigDecimal(text.toString())
            }
        })

        ms1Txt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) s1 = strToBigDecimal(text.toString())
            }
        })

        ms2Txt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) s2 = strToBigDecimal(text.toString())
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
        val mc4 = MathContext(4, RoundingMode.HALF_UP)
        val mc5 = MathContext(5, RoundingMode.HALF_UP)
        val mc6 = MathContext(6, RoundingMode.HALF_UP)

        if (n1 <= BigInteger.valueOf(30) && n2 <= BigInteger.valueOf(30)) {
            toast?.cancel()
            toast = Toast.makeText(this, "n1 y n2 deben ser mayores que 30", Toast.LENGTH_SHORT)
            toast?.show()
            return
        } else {
            toast?.cancel()
        }

        try {
            if (n1 == BigInteger.ZERO && n2 == BigInteger.ZERO && x1 == BigDecimal.ZERO && x2 == BigDecimal.ZERO && s1 == BigDecimal.ZERO && s2 == BigDecimal.ZERO) return

            Z = x1.subtract(x2).divide(sqrt(s1.pow(2).divide(n1.toBigDecimal(), mc6).add(s2.pow(2).divide(n2.toBigDecimal(), mc6)), mc5), mc4)
            mZTxt.setText(Z.toPlainString())

            val mi = 0.0
            val sigma = 1.0
            val zd = Z.toDouble()
            val lesser = NormalDistribution(null, mi, sigma).cumulativeProbability(zd).toBigDecimal(mc6)
            val greater = BigDecimal.ONE.subtract(lesser, mc6)

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

        n1 = BigInteger.ZERO
        n2 = BigInteger.ZERO
        x1 = BigDecimal.ZERO
        x2 = BigDecimal.ZERO
        s1 = BigDecimal.ZERO
        s2 = BigDecimal.ZERO
        Z = BigDecimal.ZERO

        mn1Txt.setText("")
        mn2Txt.setText("")
        mx1Txt.setText("")
        mx2Txt.setText("")
        ms1Txt.setText("")
        ms2Txt.setText("")
        mZTxt.setText("")
        mprobTxt.setText("")

        mn1Txt.clearFocus()
        mn2Txt.clearFocus()
        mx1Txt.clearFocus()
        mx2Txt.clearFocus()
        ms1Txt.clearFocus()
        ms2Txt.clearFocus()
        mprobTxt.clearFocus()
    }
}
