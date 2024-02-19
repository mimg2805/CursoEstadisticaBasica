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

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcReglaComplemento : AppCompatActivity() {

    private var pa = BigDecimal.ZERO
    private var pac = BigDecimal.ZERO

    private lateinit var etPA: EditText
    private lateinit var etPAc: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_regla_complemento)

        etPA = findViewById(R.id.activity_calc_regla_complemento_et_pa)
        etPAc = findViewById(R.id.activity_calc_regla_complemento_et_pac)
        btnClear = findViewById(R.id.activity_calc_regla_complemento_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        etPA.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                pa = strToBigDecimal(text.toString())
            }
        })

        btnClear.setOnClickListener { v -> clear(v) }
    }

    private fun calc() {
        try {
            pac = BigDecimal.ONE - pa
            etPAc.setText(pac.toPlainString())
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear(v: View) {
        if (!v.isClickable) return

        pa = BigDecimal.ZERO
        pac = BigDecimal.ZERO

        etPA.setText("")
        etPAc.setText("")

        etPA.clearFocus()
        etPAc.clearFocus()
    }
}