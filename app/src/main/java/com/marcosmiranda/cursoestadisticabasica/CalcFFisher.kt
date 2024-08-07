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
import org.apache.commons.math3.distribution.FDistribution

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcFFisher : AppCompatActivity() {

    private var d1 = BigDecimal.ZERO
    private var d2 = BigDecimal.ZERO
    private var x = BigDecimal.ZERO
    private var f = BigDecimal.ZERO
    private var calc = ""
    private val mc = MathContext(4, RoundingMode.HALF_UP)

    private lateinit var etD1: EditText
    private lateinit var etD2: EditText
    private lateinit var etX: EditText
    private lateinit var etProb: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast
    
    private lateinit var spnProb: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_f_fisher)

        etD1 = findViewById(R.id.activity_calc_f_fisher_et_d1)
        etD2 = findViewById(R.id.activity_calc_f_fisher_et_d2)
        etX = findViewById(R.id.activity_calc_f_fisher_et_x)
        etProb = findViewById(R.id.activity_calc_f_fisher_et_prob)
        btnClear = findViewById(R.id.activity_calc_f_fisher_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        spnProb = findViewById(R.id.activity_calc_f_fisher_spn_prob)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.x_probs_2, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnProb.adapter = adapter

        etD1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return 
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                d1 = strToBigDecimal(text.toString())
            }
        })

        etD2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                d2 = strToBigDecimal(text.toString())
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

        btnClear.setOnClickListener { v -> clear(v) }
    }

    fun calc() {
        if (d1 == BigDecimal.ZERO || d2 == BigDecimal.ZERO || x == BigDecimal.ZERO) return

        try {
            val d1d = d1.toDouble()
            val d2d = d2.toDouble()
            val xd = x.toDouble()
            val lesser = FDistribution(null, d1d, d2d).cumulativeProbability(xd).toBigDecimal(mc)
            val greater = BigDecimal.ONE - lesser

            f = if (calc.contains('>'))
                greater
            else
                lesser

            etProb.setText(f.toPlainString())
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear(v: View) {
        if (!v.isClickable) return
        
        d1 = BigDecimal.ZERO
        d2 = BigDecimal.ZERO
        x = BigDecimal.ZERO
        // f = BigDecimal.ZERO
        calc = ""

        etD1.setText("")
        etD2.setText("")
        etX.setText("")
        etProb.setText("")

        etD1.clearFocus()
        etD2.clearFocus()
        etX.clearFocus()
        etProb.clearFocus()
        spnProb.clearFocus()
    }
}