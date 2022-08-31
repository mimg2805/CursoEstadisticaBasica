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
import kotlin.math.*

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger

class CalcIntervaloConfianzaMediaPoblacionDesconocida : AppCompatActivity() {

    private var x: BigDecimal = BigDecimal.ZERO
    private var s: BigDecimal = BigDecimal.ZERO
    private var n: BigInteger = BigInteger.ZERO
    private val z: BigDecimal = BigDecimal("1.96")
    private var limInf: BigDecimal = BigDecimal.ZERO
    private var limSup: BigDecimal = BigDecimal.ZERO
    private var str: String = "[$limInf - $limSup]"

    private lateinit var mxTxt: EditText
    private lateinit var mSTxt: EditText
    private lateinit var mnTxt: EditText
    private lateinit var mZTxt: EditText
    private lateinit var mICTxt: EditText
    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_intervalo_confianza_media_poblacion_desconocida)

        mxTxt = findViewById(R.id.xTxt)
        mSTxt = findViewById(R.id.STxt)
        mnTxt = findViewById(R.id.nTxt)
        mZTxt = findViewById(R.id.ZTxt)
        mICTxt = findViewById(R.id.ICTxt)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        mZTxt.setText(String.format(z.toString()))

        mxTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) x = strToBigDecimal(text.toString())
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

        mnTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) n = strToBigInteger(text.toString())
            }
        })

        btnLimpiar.setOnClickListener { view -> clear(view) }
    }

    fun calc() {
        val decimals = 10
        val mc = MathContext(decimals, RoundingMode.HALF_UP)

        if (n <= BigInteger.valueOf(30)) {
            toast?.cancel()
            toast = Toast.makeText(this, "n debe ser mayor que 30", Toast.LENGTH_SHORT)
            toast?.show()
            return
        }

        try {
            if (n != BigDecimal.ZERO && x != BigDecimal.ZERO && s != BigDecimal.ZERO) {
                val error = z.times((s.divide(sqrt(n.toDouble()).toBigDecimal(), mc)))
                limInf = x.minus(error)
                limSup = x.plus(error)
                if (limInf != BigDecimal.ZERO && limSup != BigDecimal.ZERO) {
                    val limInfStr = "%.4f".format(limInf)
                    val limSupStr = "%.4f".format(limSup)
                    str = "[$limInfStr - $limSupStr]"
                    mICTxt.setText(str)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            toast?.cancel()
            toast = Toast.makeText(this, "Valores inválidos", Toast.LENGTH_SHORT)
            toast?.show()
        }
    }

    fun clear(view: View) {
        if (!view.isClickable) return

        mxTxt.setText("")
        mSTxt.setText("")
        mnTxt.setText("")
        mICTxt.setText("")

        mxTxt.clearFocus()
        mSTxt.clearFocus()
        mnTxt.clearFocus()

        x = BigDecimal.ZERO
        s = BigDecimal.ZERO
        n = BigInteger.ZERO
        limInf = BigDecimal.ZERO
        limSup = BigDecimal.ZERO
    }
}