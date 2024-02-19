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
import org.apache.commons.math3.distribution.TDistribution

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcTStudent : AppCompatActivity() {

    private var v = BigDecimal.ZERO
    private var x = BigDecimal.ZERO
    private var t = BigDecimal.ZERO
    private var calc = ""
    private val mc = MathContext(4, RoundingMode.HALF_UP)

    private lateinit var etV: EditText
    private lateinit var etX: EditText
    private lateinit var etProb: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    private lateinit var spnProb: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_t_student)

        etV = findViewById(R.id.activity_calc_t_student_et_v)
        etX = findViewById(R.id.activity_calc_t_student_et_x)
        etProb = findViewById(R.id.activity_calc_t_student_et_prob)
        btnClear = findViewById(R.id.activity_calc_t_student_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        spnProb = findViewById(R.id.activity_calc_t_student_spn_prob)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.probs_2_inv, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnProb.adapter = adapter

        etV.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                v = strToBigDecimal(text.toString())
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
        if (v == BigDecimal.ZERO) return

        try {
            val xd = x.toDouble()
            val vd = v.toDouble()
            val lesser = TDistribution(null, vd).cumulativeProbability(xd).toBigDecimal(mc)
            val greater = BigDecimal.ONE - lesser

            t = if (calc.contains('>'))
                    greater
                else
                    lesser

            etProb.setText(t.toPlainString())
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear(v: View) {
        if (!v.isClickable) return

        this.v = BigDecimal.ZERO
        x = BigDecimal.ZERO
        t = BigDecimal.ZERO
        calc = ""

        etV.setText("")
        etX.setText("")
        etProb.setText("")

        etV.clearFocus()
        etX.clearFocus()
        etProb.clearFocus()
        spnProb.clearFocus()
    }
}