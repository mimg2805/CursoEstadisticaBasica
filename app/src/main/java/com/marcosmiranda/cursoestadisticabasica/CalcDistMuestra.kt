package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class CalcDistMuestra : AppCompatActivity() {

    private var size = BigDecimal.ZERO
    private var pop = BigDecimal.ZERO
    private var frac = BigDecimal.ZERO

    private lateinit var mnTxt : EditText
    private lateinit var mNnTxt : EditText
    private lateinit var mFnTxt : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_dist_muestra)

        mnTxt = findViewById(R.id.nTxt)
        mNnTxt = findViewById(R.id.NnTxt)
        mFnTxt = findViewById(R.id.FnTxt)

        mnTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) size = MathHelper.strToBigDecimal(text.toString())
            }
        })

        mNnTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) pop = MathHelper.strToBigDecimal(text.toString())
            }
        })
    }

    fun calc() {
        val mc = MathContext(4, RoundingMode.HALF_EVEN)

        if (pop != BigDecimal.ZERO) {
            frac = size.divide(pop, mc)
            mFnTxt.setText(frac.toPlainString())
        }
    }

    fun clear(view: View) {
        if (view.isClickable) {
            size = BigDecimal.ZERO
            pop = BigDecimal.ZERO
            frac = BigDecimal.ZERO

            mnTxt.setText("")
            mNnTxt.setText("")
            mFnTxt.setText("")

            mnTxt.clearFocus()
            mNnTxt.clearFocus()
            mFnTxt.clearFocus()
        }
    }
}
