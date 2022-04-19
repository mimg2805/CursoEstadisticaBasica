package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.math.*

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcReglaComplemento : AppCompatActivity() {

    private var pa: BigDecimal = BigDecimal.ZERO
    private var pac: BigDecimal = BigDecimal.ZERO

    private lateinit var mPATxt: EditText
    private lateinit var mPAcTxt: EditText

    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_regla_complemento)

        mPATxt = findViewById(R.id.PATxt)
        mPAcTxt = findViewById(R.id.PAcTxt)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        mPATxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) pa = strToBigDecimal(text.toString())
            }
        })

        btnLimpiar.setOnClickListener { view -> clear(view) }
    }

    private fun calc() {
        try {
            pac = BigDecimal.ONE.subtract(pa)
            mPAcTxt.setText(pac.toPlainString())
        } catch (e: Exception) {
            e.printStackTrace()
            toast?.cancel()
            toast = Toast.makeText(this, "Valores inválidos", Toast.LENGTH_SHORT)
            toast?.show()
        }
    }

    fun clear(view: View) {
        if (view.isClickable) {
            pa = BigDecimal.ZERO
            pac = BigDecimal.ZERO

            mPATxt.setText("")
            mPAcTxt.setText("")

            mPATxt.clearFocus()
            mPAcTxt.clearFocus()
        }
    }
}