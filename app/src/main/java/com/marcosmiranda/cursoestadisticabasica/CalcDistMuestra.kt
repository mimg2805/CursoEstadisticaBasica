package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.Locale

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcDistMuestra : AppCompatActivity() {

    private var size = BigDecimal.ZERO
    private var pop = BigDecimal.ZERO
    private var frac = BigDecimal.ZERO
    private val mc = MathContext(4, RoundingMode.HALF_EVEN)

    private lateinit var etSize : EditText
    private lateinit var etPop : EditText
    private lateinit var etFrac : EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_dist_muestra)

        etSize = findViewById(R.id.activity_calc_dist_muestra_et_size)
        etPop = findViewById(R.id.activity_calc_dist_muestra_et_pop)
        etFrac = findViewById(R.id.activity_calc_dist_muestra_et_frac)
        btnClear = findViewById(R.id.activity_calc_dist_muestra_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        etSize.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                size = strToBigDecimal(text.toString())
            }
        })

        etPop.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                pop = strToBigDecimal(text.toString())
            }
        })

        btnClear.setOnClickListener { _ -> clear() }
    }

    fun calc() {
        if (pop == BigDecimal.ZERO) return

        try {
            frac = size.divide(pop, mc)
            etFrac.setText(String.format(Locale.ENGLISH, "%.2f", frac).replace(".00", ""))
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }

    }

    fun clear() {
        size = BigDecimal.ZERO
        pop = BigDecimal.ZERO
        frac = BigDecimal.ZERO

        etSize.setText("")
        etPop.setText("")
        etFrac.setText("")

        etSize.clearFocus()
        etPop.clearFocus()
        etFrac.clearFocus()
    }
}
