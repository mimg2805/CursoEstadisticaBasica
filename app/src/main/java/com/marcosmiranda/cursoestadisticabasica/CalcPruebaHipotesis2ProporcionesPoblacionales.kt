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

class CalcPruebaHipotesis2ProporcionesPoblacionales : AppCompatActivity() {

    private var n1 = BigInteger.ZERO
    private var n2 = BigInteger.ZERO
    private var p1 = BigDecimal.ZERO
    private var p1Comp = BigDecimal.ZERO
    private var p2 = BigDecimal.ZERO
    private var p2Comp = BigDecimal.ZERO
    private var pc = BigDecimal.ZERO
    private var pcComp = BigDecimal.ZERO
    private var z = BigDecimal.ZERO
    private var prob = BigDecimal.ZERO
    private var calc = ""
    private val mc = MathContext(5, RoundingMode.HALF_UP)

    private lateinit var etN1: EditText
    private lateinit var etN2: EditText
    private lateinit var etP1: EditText
    private lateinit var etP2: EditText
    private lateinit var etPC: EditText
    private lateinit var etZ: EditText
    private lateinit var etProb: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    private lateinit var spnProb: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_prueba_hipotesis_2_proporciones_poblacionales)

        etN1 = findViewById(R.id.activity_calc_prueba_hipotesis_2_proporciones_poblacionales_et_n1)
        etN2 = findViewById(R.id.activity_calc_prueba_hipotesis_2_proporciones_poblacionales_et_n2)
        etP1 = findViewById(R.id.activity_calc_prueba_hipotesis_2_proporciones_poblacionales_et_p1)
        etP2 = findViewById(R.id.activity_calc_prueba_hipotesis_2_proporciones_poblacionales_et_p2)
        etPC = findViewById(R.id.activity_calc_prueba_hipotesis_2_proporciones_poblacionales_et_pc)
        etZ = findViewById(R.id.activity_calc_prueba_hipotesis_2_proporciones_poblacionales_et_z)
        etProb = findViewById(R.id.activity_calc_prueba_hipotesis_2_proporciones_poblacionales_et_prob)
        btnClear = findViewById(R.id.activity_calc_prueba_hipotesis_2_proporciones_poblacionales_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        spnProb = findViewById(R.id.activity_calc_prueba_hipotesis_2_proporciones_poblacionales_spn_prob)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.probs_2, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnProb.adapter = adapter

        etN1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                n1 = strToBigInteger(text.toString())
            }
        })

        etN2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                n2 = strToBigInteger(text.toString())
            }
        })

        etP1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                p1 = strToBigDecimal(text.toString())
                p1Comp = BigDecimal.ONE - p1
            }
        })

        etP2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                p2 = strToBigDecimal(text.toString())
                p2Comp = BigDecimal.ONE - p2
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

    private fun checkReqN(): Boolean {
        if (n1 <= BigInteger.valueOf(30)) {
            etN1.error = getText(R.string.req_n1_n2_30)
            etProb.setText("")
            return false
        }

        if (n2 <= BigInteger.valueOf(30)) {
            etN2.error = getText(R.string.req_n1_n2_30)
            etProb.setText("")
            return false
        }

        etN1.error = null
        etN2.error = null
        return true
    }

    private fun checkReqNP(): Boolean {
        if (!checkReqN()) return false

        if ((n1.toBigDecimal() * p1) <= BigDecimal.valueOf(5) || (n1.toBigDecimal() * p1Comp) <= BigDecimal.valueOf(5)) {
            etP1.error = getText(R.string.req_n1_p1_5)
            return false
        }

        if ((n2.toBigDecimal() * p2) <= BigDecimal.valueOf(5) || (n2.toBigDecimal() * p2Comp) <= BigDecimal.valueOf(5)) {
            etP2.error = getText(R.string.req_n2_p2_5)
            return false
        }

        etP1.error = null
        etP2.error = null
        return true
    }

    fun calc() {
        if (!checkReqN() || !checkReqNP()) return

        if (n1 == BigInteger.ZERO || n2 == BigInteger.ZERO || p1 == BigDecimal.ZERO || p2 == BigDecimal.ZERO) return

        try {
            val pcTop = (p1 * BigDecimal(100)) + (p2 * BigDecimal(100))
            val pcBottom = (n1 + n2).toBigDecimal()
            pc = pcTop.divide(pcBottom, mc)
            pcComp = BigDecimal.ONE - pc
            etPC.setText(pc.toPlainString())

            val zTop = p1 - p2
            val zBottom1 = (pc * pcComp).divide(n1.toBigDecimal(), mc)
            val zBottom2 = (pc * pcComp).divide(n2.toBigDecimal(), mc)
            val zBottom = sqrt(zBottom1 + zBottom2, mc)
            z = zTop.divide(zBottom, mc)
            etZ.setText(z.toPlainString())

            val mi = 0.0
            val sigma = 1.0
            val zd = z.toDouble()
            val lesser = NormalDistribution(null, mi, sigma).cumulativeProbability(zd).toBigDecimal(mc)
            val greater = BigDecimal.ONE - lesser

            prob = if (calc.contains('>')) {
                greater
            } else {
                lesser
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

        n1 = BigInteger.ZERO
        n2 = BigInteger.ZERO
        p1 = BigDecimal.ZERO
        p2 = BigDecimal.ZERO
        pc = BigDecimal.ZERO
        z = BigDecimal.ZERO

        etN1.setText("")
        etN2.setText("")
        etP1.setText("")
        etP2.setText("")
        etPC.setText("")
        etZ.setText("")
        etProb.setText("")

        etN1.clearFocus()
        etN2.clearFocus()
        etP1.clearFocus()
        etP2.clearFocus()
        etPC.clearFocus()
        etProb.clearFocus()
    }
}
