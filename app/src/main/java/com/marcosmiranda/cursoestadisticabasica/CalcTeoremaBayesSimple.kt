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

class CalcTeoremaBayesSimple: AppCompatActivity() {

    private var pa = BigDecimal.ZERO
    private var pb = BigDecimal.ZERO
    private var pab = BigDecimal.ZERO
    private var pba = BigDecimal.ZERO

    private lateinit var mpaTxt: EditText
    private lateinit var mpbTxt: EditText
    private lateinit var mpabTxt: EditText
    private lateinit var mpbaTxt: EditText

    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_teorema_bayes_simple)

        mpaTxt = findViewById(R.id.paTxt)
        mpbTxt = findViewById(R.id.pbTxt)
        mpabTxt = findViewById(R.id.pabTxt)
        mpbaTxt = findViewById(R.id.pbaTxt)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        mpaTxt.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) pa = MathHelper.strToBigDecimal(text.toString())
            }
        })

        mpbTxt.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) pb = MathHelper.strToBigDecimal(text.toString())
            }
        })

        mpbaTxt.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) pba = MathHelper.strToBigDecimal(text.toString())
            }
        })

        btnLimpiar.setOnClickListener { view -> clear(view) }
    }

    fun calc() {
        try {
            val mc = MathContext(4, RoundingMode.HALF_UP)

            if (pa == BigDecimal.ZERO || pb == BigDecimal.ZERO || pba == BigDecimal.ZERO) return
            pab = pba.multiply(pa)
            pab = pab.divide(pb, mc)
            mpabTxt.setText(pab.toPlainString())
        } catch (e: Exception) {
            e.printStackTrace()
            toast?.cancel()
            toast = Toast.makeText(this, "Valores inválidos", Toast.LENGTH_SHORT)
            toast?.show()
        }
    }

    fun clear(view: View) {
        if (!view.isClickable) return

        pa = BigDecimal.ZERO
        pb = BigDecimal.ZERO
        pab = BigDecimal.ZERO
        pba = BigDecimal.ZERO

        mpaTxt.setText("")
        mpbTxt.setText("")
        mpabTxt.setText("")
        mpbaTxt.setText("")

        mpaTxt.clearFocus()
        mpbTxt.clearFocus()
        mpabTxt.clearFocus()
        mpbaTxt.clearFocus()
    }
}