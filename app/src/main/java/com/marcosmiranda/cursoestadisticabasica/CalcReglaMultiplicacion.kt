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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcReglaMultiplicacion : AppCompatActivity() {

    private var pA = BigDecimal.ZERO
    private var pB = BigDecimal.ZERO
    private var pAAndB = BigDecimal.ZERO
    private var pBOverA = BigDecimal.ZERO
    private var indep = false

    private lateinit var etPA: EditText
    private lateinit var etPB: EditText
    private lateinit var tvPBOverA: TextView
    private lateinit var etPBOverA: EditText
    private lateinit var etPAAndB: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    private lateinit var spnExc: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_regla_multiplicacion)

        etPA = findViewById(R.id.activity_calc_regla_multiplicacion_et_p_a)
        etPB = findViewById(R.id.activity_calc_regla_multiplicacion_et_p_b)
        tvPBOverA = findViewById(R.id.activity_calc_regla_multiplicacion_tv_p_b_over_a)
        etPBOverA = findViewById(R.id.activity_calc_regla_multiplicacion_et_p_b_over_a)
        etPAAndB = findViewById(R.id.activity_calc_regla_multiplicacion_et_p_a_and_b)
        btnClear = findViewById(R.id.activity_calc_regla_multiplicacion_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        spnExc = findViewById(R.id.activity_calc_regla_multiplicacion_spn_exc)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.multiplication_rule, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnExc.adapter = adapter

        etPA.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                pA = strToBigDecimal(text.toString())
            }
        })

        etPB.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                pB = strToBigDecimal(text.toString())
            }
        })

        etPBOverA.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                pBOverA = strToBigDecimal(text.toString())
            }
        })

        spnExc.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val excStr = parent?.getItemAtPosition(pos).toString()
                indep = !excStr.contains("No")

                pAAndB = BigDecimal.ZERO
                val pAAndBStr = etPAAndB.text.toString()
                if (indep) {
                    tvPBOverA.visibility = View.GONE
                    etPBOverA.visibility = View.GONE
                } else {
                    tvPBOverA.visibility = View.VISIBLE
                    etPBOverA.visibility = View.VISIBLE
                    pAAndB = strToBigDecimal(pAAndBStr)
                }
                calc()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnClear.setOnClickListener { v -> clear(v) }
    }

    fun calc() {
        if (pA == BigDecimal.ZERO || pB == BigDecimal.ZERO) return

        try {
            pAAndB = if (indep) {
                pA * pB
            } else {
                pA * pBOverA
            }
            etPAAndB.setText(String.format(pAAndB.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear(v: View) {
        if (!v.isClickable) return

        pA = BigDecimal.ZERO
        pB = BigDecimal.ZERO
        pBOverA = BigDecimal.ZERO
        pAAndB = BigDecimal.ZERO

        etPA.setText("")
        etPB.setText("")
        etPBOverA.setText("")
        etPAAndB.setText("")

        etPA.clearFocus()
        etPB.clearFocus()
        etPBOverA.clearFocus()
        etPAAndB.clearFocus()
    }
}
