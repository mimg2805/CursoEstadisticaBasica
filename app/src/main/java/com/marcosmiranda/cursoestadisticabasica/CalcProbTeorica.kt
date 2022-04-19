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

class CalcProbTeorica : AppCompatActivity() {

    private var rfa: BigDecimal = BigDecimal.ZERO
    private var tre: BigDecimal = BigDecimal.ZERO
    private var pa: BigDecimal = BigDecimal.ZERO

    private lateinit var mRFATxt: EditText
    private lateinit var mTRETxt: EditText
    private lateinit var mPATxt: EditText

    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_prob_teorica)

        mRFATxt = findViewById(R.id.RFATxt)
        mTRETxt = findViewById(R.id.TRETxt)
        mPATxt = findViewById(R.id.PATxt)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        mRFATxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) rfa = strToBigDecimal(text.toString())
            }
        })

        mTRETxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) tre = strToBigDecimal(text.toString())
            }
        })

        btnLimpiar.setOnClickListener { view -> clear(view) }
    }

    fun calc() {
        try {
            if (tre != BigDecimal.ZERO) {
                pa = rfa.divide(tre, 4, RoundingMode.HALF_EVEN)
                mPATxt.setText(String.format(pa.toPlainString()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            toast?.cancel()
            toast = Toast.makeText(this, "Valores inválidos", Toast.LENGTH_SHORT)
            toast?.show()
        }
    }

    fun clear(view: View) {
        if (view.isClickable) {
            rfa = BigDecimal.ZERO
            tre = BigDecimal.ZERO
            pa = BigDecimal.ZERO

            mRFATxt.setText("")
            mTRETxt.setText("")
            mPATxt.setText("")

            mRFATxt.clearFocus()
            mTRETxt.clearFocus()
            mPATxt.clearFocus()
        }
    }
}