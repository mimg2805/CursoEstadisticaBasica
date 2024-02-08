package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.obermuhlner.math.big.BigDecimalMath.sqrt
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger

class CalcIntervaloConfianzaProporcionPoblacionConocida : AppCompatActivity() {

    private var n: BigInteger = BigInteger.ZERO
    private var pobl: BigInteger = BigInteger.ZERO
    private var p: BigDecimal = BigDecimal.ZERO
    private var pComp: BigDecimal = BigDecimal.ZERO
    private val z: BigDecimal = BigDecimal("1.96")
    private var limInf: BigDecimal = BigDecimal.ZERO
    private var limSup: BigDecimal = BigDecimal.ZERO
    private var str: String = "[$limInf - $limSup]"

    private lateinit var mpTxt: EditText
    private lateinit var mNTxt: EditText
    private lateinit var mnTxt: EditText
    private lateinit var mZTxt: EditText
    private lateinit var mICTxt: EditText
    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_intervalo_confianza_proporcion_poblacion_conocida)

        mpTxt = findViewById(R.id.pTxt)
        mNTxt = findViewById(R.id.NTxt)
        mnTxt = findViewById(R.id.nTxt)
        mZTxt = findViewById(R.id.ZTxt)
        mICTxt = findViewById(R.id.ICTxt)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        mZTxt.setText(String.format(z.toString()))

        mpTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) {
                    p = strToBigDecimal(text.toString())
                    pComp = BigDecimal.ONE.subtract(p)
                }
            }
        })

        mNTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) pobl = strToBigInteger(text.toString())
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

        if (n.toBigDecimal().multiply(p) <= BigDecimal.valueOf(5) && n.toBigDecimal().multiply(pComp) <= BigDecimal.valueOf(5)) {
            toast?.cancel()
            toast = Toast.makeText(this, "(n * p) y (n * (1 - p)) deben ser mayores que 5", Toast.LENGTH_SHORT)
            toast?.show()
            return
        } else {
            toast?.cancel()
        }

        if (n <= BigInteger.valueOf(30)) {
            toast?.cancel()
            toast = Toast.makeText(this, "n debe ser mayor que 30", Toast.LENGTH_SHORT)
            toast?.show()
            return
        } else {
            toast?.cancel()
        }

        try {
            if (n == BigInteger.ZERO && p == BigDecimal.ZERO) return

            val root1 = sqrt(((p.multiply(pComp)).divide(n.toBigDecimal(), mc)), mc)
            val root2 = sqrt(((pobl.subtract(n)).toBigDecimal().divide(pobl.subtract(BigInteger.ONE).toBigDecimal(), mc)), mc)
            val error = z.multiply(root1).multiply(root2)
            limInf = p.subtract(error)
            limSup = p.add(error)

            if (limInf == BigDecimal.ZERO && limSup == BigDecimal.ZERO) return

            val limInfStr = "%.4f".format(limInf)
            val limSupStr = "%.4f".format(limSup)
            str = "[$limInfStr - $limSupStr]"
            mICTxt.setText(str)
        } catch (e: Exception) {
            e.printStackTrace()
            toast?.cancel()
            toast = Toast.makeText(this, "Valores inválidos", Toast.LENGTH_SHORT)
            toast?.show()
        }
    }

    fun clear(view: View) {
        if (!view.isClickable) return

        mpTxt.setText("")
        mNTxt.setText("")
        mnTxt.setText("")
        mICTxt.setText("")

        mpTxt.clearFocus()
        mnTxt.clearFocus()

        n = BigInteger.ZERO
        pobl = BigInteger.ZERO
        p = BigDecimal.ZERO
        limInf = BigDecimal.ZERO
        limSup = BigDecimal.ZERO
    }
}