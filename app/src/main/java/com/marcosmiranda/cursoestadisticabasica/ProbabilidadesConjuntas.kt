package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class ProbabilidadesConjuntas : AppCompatActivity() {

    private lateinit var etPA1B1: EditText
    private lateinit var etPA1B2: EditText
    private lateinit var etPA2B1: EditText
    private lateinit var etPA2B2: EditText

    private lateinit var a1b1: BigDecimal
    private lateinit var a1b2: BigDecimal
    private lateinit var a2b1: BigDecimal
    private lateinit var a2b2: BigDecimal
    private lateinit var rowTotal1: BigDecimal
    private lateinit var rowTotal2: BigDecimal
    private lateinit var columnTotal1: BigDecimal
    private lateinit var columnTotal2: BigDecimal
    private lateinit var total: BigDecimal
    private lateinit var pa1b1: BigDecimal
    private lateinit var pa1b2: BigDecimal
    private lateinit var pa2b1: BigDecimal
    private lateinit var pa2b2: BigDecimal
    private var mc = MathContext(4, RoundingMode.HALF_UP)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_probabilidades_conjuntas)

        etPA1B1 = findViewById(R.id.activity_probabilidades_conjuntas_et_pa1b1)
        etPA1B2 = findViewById(R.id.activity_probabilidades_conjuntas_et_pa1b2)
        etPA2B1 = findViewById(R.id.activity_probabilidades_conjuntas_et_pa2b1)
        etPA2B2 = findViewById(R.id.activity_probabilidades_conjuntas_et_pa2b2)

        a1b1 = strToBigDecimal(intent.getStringExtra("a1b1").toString())
        a1b2 = strToBigDecimal(intent.getStringExtra("a1b2").toString())
        a2b1 = strToBigDecimal(intent.getStringExtra("a2b1").toString())
        a2b2 = strToBigDecimal(intent.getStringExtra("a2b2").toString())
        Log.e("a1b1", a1b1.toString())
        Log.e("a1b2", a1b2.toString())
        Log.e("a2b1", a2b1.toString())
        Log.e("a2b2", a2b2.toString())

        if (a1b1 != BigDecimal.ZERO || a2b1 != BigDecimal.ZERO) rowTotal1 = a1b1 + a2b1
        if (a1b2 != BigDecimal.ZERO || a2b2 != BigDecimal.ZERO) rowTotal2 = a1b2 + a2b2
        if (a1b1 != BigDecimal.ZERO || a1b2 != BigDecimal.ZERO) columnTotal1 = a1b1 + a1b2
        if (a2b1 != BigDecimal.ZERO || a2b2 != BigDecimal.ZERO) columnTotal2 = a2b1 + a2b2
        total = a1b1 + a2b1 + a1b2 + a2b2

        pa1b1 = a1b1.divide(total, mc).setScale(mc.precision, mc.roundingMode)
        pa1b2 = a1b2.divide(total, mc).setScale(mc.precision, mc.roundingMode)
        pa2b1 = a2b1.divide(total, mc).setScale(mc.precision, mc.roundingMode)
        pa2b2 = a2b2.divide(total, mc).setScale(mc.precision, mc.roundingMode)
        etPA1B1.setText(String.format(pa1b1.toString()))
        etPA1B2.setText(String.format(pa1b2.toString()))
        etPA2B1.setText(String.format(pa2b1.toString()))
        etPA2B2.setText(String.format(pa2b2.toString()))
    }
}