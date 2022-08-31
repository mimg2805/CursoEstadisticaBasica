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

class CalcPruebaHipotesis2ProporcionesPoblacionales : AppCompatActivity() {

    private var n1: BigInteger = BigInteger.ZERO
    private var n2: BigInteger = BigInteger.ZERO
    private var p1: BigDecimal = BigDecimal.ZERO
    private var p2: BigDecimal = BigDecimal.ZERO
    private var pc: BigDecimal = BigDecimal.ZERO
    private var Z: BigDecimal = BigDecimal.ZERO
    private var prob: BigDecimal = BigDecimal.ZERO
    private var calc: String = ""

    private lateinit var mn1Txt: EditText
    private lateinit var mn2Txt: EditText
    private lateinit var mp1Txt: EditText
    private lateinit var mp2Txt: EditText
    private lateinit var mpcTxt: EditText
    private lateinit var mZTxt: EditText
    private lateinit var mprobTxt: EditText

    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    private lateinit var mprobSpinner: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_prueba_hipotesis_2_proporciones_poblacionales)

        mn1Txt = findViewById(R.id.n1Txt)
        mn2Txt = findViewById(R.id.n2Txt)
        mp1Txt = findViewById(R.id.p1Txt)
        mp2Txt = findViewById(R.id.p2Txt)
        mpcTxt = findViewById(R.id.pcTxt)
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

        mp1Txt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) p1 = strToBigDecimal(text.toString())
            }
        })

        mp2Txt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) p2 = strToBigDecimal(text.toString())
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
        pc = BigDecimal.ZERO
        Z = BigDecimal.ZERO
        val mc4 = MathContext(4, RoundingMode.HALF_UP)
        val mc5 = MathContext(5, RoundingMode.HALF_UP)
        val mc6 = MathContext(6, RoundingMode.HALF_UP)

        if (p1.times(BigDecimal.ONE.subtract(p1)).plus(p2.times(BigDecimal.ONE.subtract(p2))) <= BigDecimal.valueOf(30)) {
            toast?.cancel()
            toast = Toast.makeText(this, "p1 (1 - p1) + p2 (1 - p2) debe ser mayor que 30", Toast.LENGTH_SHORT)
            toast?.show()
            return
        }

        try {
            if (n1 != BigInteger.ZERO && n2 != BigInteger.ZERO && p1 != BigDecimal.ZERO && p2 != BigDecimal.ZERO) {
                pc = p1.times(BigDecimal(100)).plus(p2.times(BigDecimal(100))).divide(n1.plus(n2).toBigDecimal(), mc5)
                mpcTxt.setText(pc.toPlainString())
                Z = p1.minus(p2).divide(sqrt(pc.times(BigDecimal.ONE.subtract(pc)).divide(n1.toBigDecimal()).plus(pc.times(BigDecimal.ONE.subtract(pc)).divide(n2.toBigDecimal())), mc5), mc4)
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
            }
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
        p1 = BigDecimal.ZERO
        p2 = BigDecimal.ZERO
        pc = BigDecimal.ZERO
        Z = BigDecimal.ZERO

        mn1Txt.setText("")
        mn2Txt.setText("")
        mp1Txt.setText("")
        mp2Txt.setText("")
        mpcTxt.setText("")
        mZTxt.setText("")
        mprobTxt.setText("")

        mn1Txt.clearFocus()
        mn2Txt.clearFocus()
        mp1Txt.clearFocus()
        mp2Txt.clearFocus()
        mpcTxt.clearFocus()
        mprobTxt.clearFocus()
    }
}
