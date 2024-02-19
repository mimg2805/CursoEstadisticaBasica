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

class CalcReglaAdicion : AppCompatActivity() {

    private var pA = BigDecimal.ZERO
    private var pB = BigDecimal.ZERO
    private var pAAndB = BigDecimal.ZERO
    private var pAOrB = BigDecimal.ZERO

    private lateinit var etPA: EditText
    private lateinit var etPB: EditText
    private lateinit var tvPAAndB: TextView
    private lateinit var etPAAndB: EditText
    private lateinit var etPAOrB: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    private lateinit var spnExc: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>
    private var exc = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_regla_adicion)

        etPA = findViewById(R.id.activity_calc_regla_adicion_et_pa)
        etPB = findViewById(R.id.activity_calc_regla_adicion_et_pb)
        tvPAAndB = findViewById(R.id.activity_calc_regla_adicion_tv_p_and_b)
        etPAAndB = findViewById(R.id.activity_calc_regla_adicion_et_p_and_b)
        etPAOrB = findViewById(R.id.activity_calc_regla_adicion_et_p_or_b)
        btnClear = findViewById(R.id.activity_calc_regla_adicion_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        spnExc = findViewById(R.id.activity_calc_regla_adicion_spn_exc)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.regla_adicion, R.layout.spinner_item
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

        etPAAndB.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                pAAndB = strToBigDecimal(text.toString())
            }
        })

        spnExc.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val spnTxt = parent?.getItemAtPosition(pos).toString()
                exc = !spnTxt.contains("No")

                pAAndB = BigDecimal.ZERO
                val pAAndBStr = etPAAndB.text.toString()
                if (exc) {
                    tvPAAndB.visibility = View.GONE
                    etPAAndB.visibility = View.GONE
                } else {
                    tvPAAndB.visibility = View.VISIBLE
                    etPAAndB.visibility = View.VISIBLE
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
            pAOrB = pA + pB - pAAndB
            etPAOrB.setText(String.format(pAOrB.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear(v: View) {
        if (!v.isClickable) return

        etPA.setText("")
        etPB.setText("")
        etPAAndB.setText("")
        etPAOrB.setText("")

        etPA.clearFocus()
        etPB.clearFocus()
        etPAAndB.clearFocus()
        etPAOrB.clearFocus()
    }
}