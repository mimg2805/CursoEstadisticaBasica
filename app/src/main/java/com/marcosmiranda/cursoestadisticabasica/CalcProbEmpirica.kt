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
import java.math.RoundingMode

class CalcProbEmpirica : AppCompatActivity() {

    private var rfa: BigDecimal = BigDecimal.ZERO
    private var tro: BigDecimal = BigDecimal.ZERO
    private var pa: BigDecimal = BigDecimal.ZERO

    private lateinit var mRFATxt: EditText
    private lateinit var mTROTxt: EditText
    private lateinit var mPATxt: EditText

    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_prob_empirica)

        mRFATxt = findViewById(R.id.RFATxt)
        mTROTxt = findViewById(R.id.TROTxt)
        mPATxt = findViewById(R.id.PATxt)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        mRFATxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) rfa = MathHelper.strToBigDecimal(text.toString())
            }
        })

        mTROTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) tro = MathHelper.strToBigDecimal(text.toString())
            }
        })

        btnLimpiar.setOnClickListener { view -> clear(view) }
    }

    fun calc() {
        try {
            if (tro != BigDecimal.ZERO) {
                pa = rfa.divide(tro, 4, RoundingMode.HALF_EVEN)
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
            tro = BigDecimal.ZERO
            pa = BigDecimal.ZERO

            mRFATxt.setText("")
            mTROTxt.setText("")
            mPATxt.setText("")

            mRFATxt.clearFocus()
            mTROTxt.clearFocus()
            mPATxt.clearFocus()
        }
    }
}
