package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcTeoremaBayesSimple: AppCompatActivity() {

    private var pA = BigDecimal.ZERO
    private var pB = BigDecimal.ZERO
    private var pAOverB = BigDecimal.ZERO
    private var pBOverA = BigDecimal.ZERO

    private lateinit var etPA: EditText
    private lateinit var etPB: EditText
    private lateinit var etPAOverB: EditText
    private lateinit var etPBOverA: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_teorema_bayes_simple)

        etPA = findViewById(R.id.activity_calc_teorema_bayes_simple_et_p_a)
        etPB = findViewById(R.id.activity_calc_teorema_bayes_simple_et_p_b)
        etPAOverB = findViewById(R.id.activity_calc_teorema_bayes_simple_et_p_a_over_b)
        etPBOverA = findViewById(R.id.activity_calc_teorema_bayes_simple_et_p_b_over_a)
        btnClear = findViewById(R.id.activity_calc_teorema_bayes_simple_btn_clear)

        etPA.addTextChangedListener(object: TextWatcher {
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

        etPB.addTextChangedListener(object: TextWatcher {
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

        etPBOverA.addTextChangedListener(object: TextWatcher {
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

        btnClear.setOnClickListener { v -> clear(v) }
    }

    fun calc() {
        val mc = MathContext(4, RoundingMode.HALF_UP)

        if (pA == BigDecimal.ZERO || pB == BigDecimal.ZERO || pBOverA == BigDecimal.ZERO) return

        try {
            pAOverB = pBOverA * pA
            pAOverB = (pAOverB / pB).round(mc)
            etPAOverB.setText(pAOverB.toPlainString())
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
        pAOverB = BigDecimal.ZERO
        pBOverA = BigDecimal.ZERO

        etPA.setText("")
        etPB.setText("")
        etPAOverB.setText("")
        etPBOverA.setText("")

        etPA.clearFocus()
        etPB.clearFocus()
        etPAOverB.clearFocus()
        etPBOverA.clearFocus()
    }
}