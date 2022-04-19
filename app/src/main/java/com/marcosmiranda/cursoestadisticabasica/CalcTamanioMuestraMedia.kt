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

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcTamanioMuestraMedia : AppCompatActivity() {

    private var n: BigInteger = BigInteger.ZERO
    private var s: BigDecimal = BigDecimal.ZERO
    private var x: BigDecimal = BigDecimal.ZERO
    private var e: BigDecimal = BigDecimal.ZERO
    private val z: BigDecimal = BigDecimal("1.96")
    private var answer: BigInteger = BigInteger.ZERO

    private lateinit var mNTxt: EditText
    private lateinit var mSTxt: EditText
    private lateinit var mMediaTxt: EditText
    private lateinit var mETxt: EditText
    private lateinit var mZTxt: EditText
    private lateinit var mnTxt: EditText

    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_tamanio_muestra_media)

        mNTxt = findViewById(R.id.NTxt)
        mSTxt = findViewById(R.id.STxt)
        mMediaTxt = findViewById(R.id.mediaTxt)
        mETxt = findViewById(R.id.ETxt)
        mZTxt = findViewById(R.id.ZTxt)
        mnTxt = findViewById(R.id.nTxt)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        mZTxt.setText(z.toPlainString())

        mNTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) n = strToBigInteger(text.toString())
            }
        })

        mSTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) s = strToBigDecimal(text.toString())
            }
        })

        mMediaTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) x = strToBigDecimal(text.toString())
            }
        })

        btnLimpiar.setOnClickListener { view -> clear(view) }
    }

    fun calc() {
        try {
            e = x * BigDecimal("0.05")
            mETxt.setText(e.toPlainString())
            val top = n.toBigDecimal() * z.pow(2) * s.pow(2)
            val bottom = ((n - BigInteger.ONE).toBigDecimal() * e.pow(2)) + (z.pow(2) * s.pow(2))
            answer = (top / bottom).setScale(0, RoundingMode.UP).toBigInteger()
            mnTxt.setText(String.format(answer.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
            toast?.cancel()
            toast = Toast.makeText(this, "Valores inválidos", Toast.LENGTH_SHORT)
            toast?.show()
        }
    }

    fun clear(view: View) {
        if (view.isClickable) {
            n = BigInteger.ZERO
            s = BigDecimal.ZERO
            x = BigDecimal.ZERO
            e = BigDecimal.ZERO
            answer = BigInteger.ZERO

            mNTxt.setText("")
            mSTxt.setText("")
            mMediaTxt.setText("")
            mETxt.setText("")
            mnTxt.setText("")

            mNTxt.clearFocus()
            mSTxt.clearFocus()
            mMediaTxt.clearFocus()
        }
    }
}