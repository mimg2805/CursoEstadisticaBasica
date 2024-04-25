package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class CalcTamanioMuestra2MediasPoblaciones : AppCompatActivity() {

    private var s1 = BigDecimal.ZERO
    private var s1Comp = BigDecimal.ZERO
    private var s2 = BigDecimal.ZERO
    private var s2Comp = BigDecimal.ZERO
    private var z = BigDecimal("1.96")
    private var e = BigDecimal.ZERO
    private var s = BigDecimal.ZERO
    private var n1 = BigDecimal.ZERO
    private var n2 = BigDecimal.ZERO
    private val mc = MathContext(4, RoundingMode.HALF_UP)

    private lateinit var etS1: EditText
    private lateinit var etS2: EditText
    private lateinit var etZ: EditText
    private lateinit var etE: EditText
    private lateinit var etS: EditText
    private lateinit var etN1: EditText
    private lateinit var etN2: EditText
    private lateinit var btnClear: Button
    private lateinit var tstInvalid: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_tamanio_muestra_2_medias_poblaciones)

        etS1 = findViewById(R.id.activity_calc_tamanio_muestra_2_medias_poblaciones_et_s1)
        etS2 = findViewById(R.id.activity_calc_tamanio_muestra_2_medias_poblaciones_et_s2)
        etZ = findViewById(R.id.activity_calc_tamanio_muestra_2_medias_poblaciones_et_z)
        etE = findViewById(R.id.activity_calc_tamanio_muestra_2_medias_poblaciones_et_e)
        etS = findViewById(R.id.activity_calc_tamanio_muestra_2_medias_poblaciones_et_s)
        etN1 = findViewById(R.id.activity_calc_tamanio_muestra_2_medias_poblaciones_et_n1)
        etN2 = findViewById(R.id.activity_calc_tamanio_muestra_2_medias_poblaciones_et_n2)
        btnClear = findViewById(R.id.activity_calc_tamanio_muestra_2_medias_poblaciones_btn_clear)
        tstInvalid = Toast.makeText(this, R.string.invalid_values, Toast.LENGTH_SHORT)

        etZ.setText(z.toPlainString())

        etS1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                s1 = strToBigDecimal(text.toString())
                s1Comp = BigDecimal.ONE - s1
            }
        })

        etS2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                s2 = strToBigDecimal(text.toString())
                s2Comp = BigDecimal.ONE - s2
            }
        })

        etE.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                e = strToBigDecimal(text.toString())
            }
        })

        btnClear.setOnClickListener { v -> clear(v) }
    }

    fun calc() {
        // if (!checkReqN() || !checkReqNP()) return

        if (s1 == BigDecimal.ZERO || s2 == BigDecimal.ZERO || e == BigDecimal.ZERO) return

        try {
            s = (s1 + s2).divide(BigDecimal.valueOf(2), mc)

            n1 = (s * z).divide(e, mc).pow(2) * BigDecimal.valueOf(2)
            n2 = (s * z).divide(e, mc).pow(2) * BigDecimal.valueOf(2)

            tstInvalid.cancel()
            etS.setText(String.format("%.1f", s))
            etN1.setText(String.format("%.0f", n1))
            etN2.setText(String.format("%.0f", n2))
        } catch (e: Exception) {
            e.printStackTrace()
            tstInvalid.cancel()
            tstInvalid.show()
        }
    }

    fun clear(v: View) {
        if (!v.isClickable) return

        s1 = BigDecimal.ZERO
        s2 = BigDecimal.ZERO
        e = BigDecimal.ZERO
        s = BigDecimal.ZERO
        n1 = BigDecimal.ZERO
        n2 = BigDecimal.ZERO

        etS1.setText("")
        etS2.setText("")
        etE.setText("")
        etS.setText("")
        etN1.setText("")
        etN2.setText("")

        etS1.clearFocus()
        etS2.clearFocus()
        etE.clearFocus()
        etS.clearFocus()
        etN1.clearFocus()
        etN2.clearFocus()
    }
}