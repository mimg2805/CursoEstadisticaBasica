package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcProbEmpirica : AppCompatActivity() {

    private var rfa = BigDecimal.ZERO
    private var tro = BigDecimal.ZERO
    private var pA = BigDecimal.ZERO
    private val mc = MathContext(4, RoundingMode.HALF_EVEN)

    private lateinit var etRFA: EditText
    private lateinit var etTRO: EditText
    private lateinit var etPA: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_prob_empirica)

        etRFA = findViewById(R.id.activity_calc_prob_empirica_et_rfa)
        etTRO = findViewById(R.id.activity_calc_prob_empirica_et_tro)
        etPA = findViewById(R.id.activity_calc_prob_empirica_et_p_a)
        btnClear = findViewById(R.id.activity_calc_prob_empirica_btn_clear)
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

        etTRO.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                tro = strToBigDecimal(text.toString())
            }
        })

        btnClear.setOnClickListener { v -> clear(v) }
    }

    fun calc() {
        if (tro == BigDecimal.ZERO) return

        try {
            pA = (rfa / tro).round(mc)
            etPA.setText(String.format(pA.toPlainString()))
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear(v: View) {
        if (!v.isClickable) return

        rfa = BigDecimal.ZERO
        tro = BigDecimal.ZERO
        pA = BigDecimal.ZERO

        etRFA.setText("")
        etTRO.setText("")
        etPA.setText("")

        etRFA.clearFocus()
        etTRO.clearFocus()
        etPA.clearFocus()
    }
}
