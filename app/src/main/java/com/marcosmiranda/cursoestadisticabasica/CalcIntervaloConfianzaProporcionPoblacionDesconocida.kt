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
import java.util.Locale

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger

class CalcIntervaloConfianzaProporcionPoblacionDesconocida : AppCompatActivity() {

    private var n = BigInteger.ZERO
    private var p = BigDecimal.ZERO
    private var pComp = BigDecimal.ZERO
    private val z = BigDecimal("1.96")
    private var limInf = BigDecimal.ZERO
    private var limSup = BigDecimal.ZERO
    private val mc = MathContext(4, RoundingMode.HALF_UP)

    private lateinit var etP: EditText
    private lateinit var etN: EditText
    private lateinit var etZ: EditText
    private lateinit var etIC: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_intervalo_confianza_proporcion_poblacion_desconocida)

        etP = findViewById(R.id.activity_calc_intervalo_confianza_proporcion_poblacion_desconocida_et_p)
        etN = findViewById(R.id.activity_calc_intervalo_confianza_proporcion_poblacion_desconocida_et_n)
        etZ = findViewById(R.id.activity_calc_intervalo_confianza_proporcion_poblacion_desconocida_et_z)
        etIC = findViewById(R.id.activity_calc_intervalo_confianza_proporcion_poblacion_desconocida_et_ic)
        btnClear = findViewById(R.id.activity_calc_intervalo_confianza_proporcion_poblacion_desconocida_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        etZ.setText(z.toPlainString())

        etP.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                p = strToBigDecimal(text.toString())
                pComp = BigDecimal.ONE - p
            }
        })

        etN.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                n = strToBigInteger(text.toString())
            }
        })

        btnClear.setOnClickListener { v -> clear(v) }
    }

    private fun checkReqN(): Boolean {
        return if (n <= BigInteger.valueOf(30)) {
            etN.error = getText(R.string.req_n_30)
            etIC.setText("")
            false
        } else {
            etN.error = null
            true
        }
    }

    private fun checkReqNP(): Boolean {
        if (!checkReqN()) return false
        return if ((n.toBigDecimal() * p) <= BigDecimal.valueOf(5) || (n.toBigDecimal() * pComp) <= BigDecimal.valueOf(5)) {
            etP.error = getText(R.string.req_n_p_5)
            etIC.setText("")
            false
        } else {
            etP.error = null
            true
        }
    }

    fun calc() {
        if (!checkReqN() || !checkReqNP()) return

        try {
            val error = z * sqrt((p * pComp).divide(n.toBigDecimal(), mc), mc)
            limInf = p - error
            limSup = p + error

            if (limInf == BigDecimal.ZERO || limSup == BigDecimal.ZERO) return

            val limInfStr = String.format(Locale.ENGLISH, "%.2f", limInf)
            val limSupStr = String.format(Locale.ENGLISH, "%.2f", limSup)
            val limStr = "[$limInfStr - $limSupStr]"
            etIC.setText(limStr)
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear(v: View) {
        if (!v.isClickable) return

        etP.setText("")
        etN.setText("")
        etIC.setText("")

        etP.clearFocus()
        etN.clearFocus()

        n = BigInteger.ZERO
        p = BigDecimal.ZERO
        limInf = BigDecimal.ZERO
        limSup = BigDecimal.ZERO
    }
}