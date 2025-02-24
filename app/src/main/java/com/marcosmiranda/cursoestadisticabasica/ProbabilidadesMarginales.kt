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

class ProbabilidadesMarginales : AppCompatActivity() {

    private lateinit var etPA1: EditText
    private lateinit var etPA2: EditText
    private lateinit var etPB1: EditText
    private lateinit var etPB2: EditText

    private lateinit var a1b1: BigDecimal
    private lateinit var a1b2: BigDecimal
    private lateinit var a2b1: BigDecimal
    private lateinit var a2b2: BigDecimal
    private lateinit var rowTotal1: BigDecimal
    private lateinit var rowTotal2: BigDecimal
    private lateinit var columnTotal1: BigDecimal
    private lateinit var columnTotal2: BigDecimal
    private lateinit var total: BigDecimal
    private lateinit var pa1: BigDecimal
    private lateinit var pa2: BigDecimal
    private lateinit var pb1: BigDecimal
    private lateinit var pb2: BigDecimal
    private var mc = MathContext(4, RoundingMode.HALF_UP)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_probabilidades_marginales)

        etPA1 = findViewById(R.id.activity_probabilidades_marginales_et_pa1)
        etPA2 = findViewById(R.id.activity_probabilidades_marginales_et_pa2)
        etPB1 = findViewById(R.id.activity_probabilidades_marginales_et_pb1)
        etPB2 = findViewById(R.id.activity_probabilidades_marginales_et_pb2)

        a1b1 = strToBigDecimal(intent.getStringExtra("a1b1").toString())
        a1b2 = strToBigDecimal(intent.getStringExtra("a1b2").toString())
        a2b1 = strToBigDecimal(intent.getStringExtra("a2b1").toString())
        a2b2 = strToBigDecimal(intent.getStringExtra("a2b2").toString())
        // Log.e("a1b1", a1b1.toString())
        // Log.e("a1b2", a1b2.toString())
        // Log.e("a2b1", a2b1.toString())
        // Log.e("a2b2", a2b2.toString())

        if (a1b1 != BigDecimal.ZERO || a2b1 != BigDecimal.ZERO) rowTotal1 = a1b1 + a2b1
        if (a1b2 != BigDecimal.ZERO || a2b2 != BigDecimal.ZERO) rowTotal2 = a1b2 + a2b2
        if (a1b1 != BigDecimal.ZERO || a1b2 != BigDecimal.ZERO) columnTotal1 = a1b1 + a1b2
        if (a2b1 != BigDecimal.ZERO || a2b2 != BigDecimal.ZERO) columnTotal2 = a2b1 + a2b2
        total = a1b1 + a2b1 + a1b2 + a2b2

        pa1 = columnTotal1.divide(total, mc).setScale(mc.precision, mc.roundingMode)
        pa2 = columnTotal2.divide(total, mc).setScale(mc.precision, mc.roundingMode)
        pb1 = rowTotal1.divide(total, mc).setScale(mc.precision, mc.roundingMode)
        pb2 = rowTotal2.divide(total, mc).setScale(mc.precision, mc.roundingMode)
        etPA1.setText(String.format(pa1.toString()))
        etPA2.setText(String.format(pa2.toString()))
        etPB1.setText(String.format(pb1.toString()))
        etPB2.setText(String.format(pb2.toString()))
    }
}