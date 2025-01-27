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

class CalcProbTeorica : AppCompatActivity() {

    private var rfa = BigDecimal.ZERO
    private var tre = BigDecimal.ZERO
    private var pA = BigDecimal.ZERO
    private val mc = MathContext(4, RoundingMode.HALF_UP)

    private lateinit var etRFA: EditText
    private lateinit var etTRE: EditText
    private lateinit var etPA: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_prob_teorica)

        etRFA = findViewById(R.id.activity_calc_prob_teorica_et_rfa)
        etTRE = findViewById(R.id.activity_calc_prob_teorica_et_tre)
        etPA = findViewById(R.id.activity_calc_prob_teorica_et_p_a)
        btnClear = findViewById(R.id.activity_calc_prob_teorica_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        etRFA.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                rfa = strToBigDecimal(text.toString())
            }
        })

        etTRE.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                tre = strToBigDecimal(text.toString())
            }
        })

        btnClear.setOnClickListener { _ -> clear() }
    }

    fun calc() {
        if (tre == BigDecimal.ZERO) return

        try {
            pA = rfa.divide(tre, mc)
            etPA.setText(pA.toPlainString())
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear() {
        rfa = BigDecimal.ZERO
        tre = BigDecimal.ZERO
        pA = BigDecimal.ZERO

        etRFA.setText("")
        etTRE.setText("")
        etPA.setText("")

        etRFA.clearFocus()
        etTRE.clearFocus()
        etPA.clearFocus()
    }
}