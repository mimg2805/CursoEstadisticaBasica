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
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import org.apache.commons.math3.distribution.NormalDistribution

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcDistNormalProbabilidades : AppCompatActivity() {

    private var mi = BigDecimal.ZERO
    private var sigma = BigDecimal.ZERO
    private var x = BigDecimal.ZERO
    private var prob = BigDecimal.ZERO
    private var calc = ""
    private val mc = MathContext(4, RoundingMode.HALF_EVEN)

    private lateinit var etMi: EditText
    private lateinit var etSigma: EditText
    private lateinit var etX: EditText
    private lateinit var etProb: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    private lateinit var spnProb: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_dist_normal_probabilidades)

        etMi = findViewById(R.id.activity_calc_dist_normal_probabilidades_et_mi)
        etSigma = findViewById(R.id.activity_calc_dist_normal_probabilidades_et_sigma)
        etX = findViewById(R.id.activity_calc_dist_normal_probabilidades_et_x)
        etProb = findViewById(R.id.activity_calc_dist_normal_probabilidades_et_prob)
        btnClear = findViewById(R.id.activity_calc_dist_normal_probabilidades_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        spnProb = findViewById(R.id.activity_calc_dist_normal_probabilidades_spn_prob)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.z_probs_2_inv, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnProb.adapter = adapter

        etMi.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                mi = strToBigDecimal(text.toString())
            }
        })

        etSigma.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                sigma = strToBigDecimal(text.toString())
            }
        })

        etX.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                x = strToBigDecimal(text.toString())
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

        btnClear.setOnClickListener { _ -> clear() }
    }

    fun calc() {
        prob = BigDecimal.ZERO

        if (sigma == BigDecimal.ZERO) return // || x == BigDecimal.ZERO)

        try {
            val mid = mi.toDouble()
            val sigmad = sigma.toDouble()
            val xd = x.toDouble()
            val lesser = NormalDistribution(null, mid, sigmad).cumulativeProbability(xd).toBigDecimal(mc)
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

    fun clear() {
        mi = BigDecimal.ZERO
        sigma = BigDecimal.ZERO
        x = BigDecimal.ZERO
        prob = BigDecimal.ZERO

        etMi.setText("")
        etSigma.setText("")
        etX.setText("")
        etProb.setText("")

        etMi.clearFocus()
        etSigma.clearFocus()
        etX.clearFocus()
        etProb.clearFocus()
        spnProb.clearFocus()

        spnProb.setSelection(0)
    }
}