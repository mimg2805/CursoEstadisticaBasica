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

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.distNormalEstandar
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcDistNormalEstandarizacion : AppCompatActivity() {

    private var x: BigDecimal = BigDecimal.ZERO
    private var mi: BigDecimal = BigDecimal.ZERO
    private var sigma: BigDecimal = BigDecimal.ZERO
    private var n: BigDecimal = BigDecimal.ONE
    private var prob: BigDecimal = BigDecimal.ZERO

    private lateinit var mxTxt: EditText
    private lateinit var mmiTxt: EditText
    private lateinit var msigmaTxt: EditText
    private lateinit var mnTxt: EditText
    private lateinit var mprobTxt: EditText

    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_dist_normal_estandarizacion)

        mxTxt = findViewById(R.id.xTxt)
        mmiTxt = findViewById(R.id.miTxt)
        msigmaTxt = findViewById(R.id.sigmaTxt)
        mnTxt = findViewById(R.id.nTxt)
        mprobTxt = findViewById(R.id.probTxt)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        mnTxt.setText(n.toPlainString())

        mxTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) x = strToBigDecimal(text.toString())
            }
        })

        mmiTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) mi = strToBigDecimal(text.toString())
            }
        })

        msigmaTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) sigma = strToBigDecimal(text.toString())
            }
        })

        mnTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) n = strToBigDecimal(text.toString())
            }
        })

        btnLimpiar.setOnClickListener { view -> clear(view) }
    }

    fun calc() {
        try {
            if (sigma != BigDecimal.ZERO && n != BigDecimal.ZERO) {
                prob = distNormalEstandar(x, mi, sigma, n)
                mprobTxt.setText(prob.toPlainString())
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
            x = BigDecimal.ZERO
            mi = BigDecimal.ZERO
            sigma = BigDecimal.ZERO
            n = BigDecimal.ZERO
            prob = BigDecimal.ZERO

            mxTxt.setText("")
            mmiTxt.setText("")
            msigmaTxt.setText("")
            mnTxt.setText("")
            mprobTxt.setText("")

            mxTxt.clearFocus()
            mmiTxt.clearFocus()
            msigmaTxt.clearFocus()
            mnTxt.clearFocus()
            mprobTxt.clearFocus()
        }
    }
}