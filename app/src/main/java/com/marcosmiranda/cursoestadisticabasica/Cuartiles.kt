package com.marcosmiranda.cursoestadisticabasica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.cuartiles
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class Cuartiles : AppCompatActivity() {

    private val mc = MathContext(2, RoundingMode.HALF_UP)

    private var values = mutableListOf<BigDecimal>()
    private var valuesStr = ""

    private var cuartil1 = BigDecimal.ZERO
    private var cuartil2 = BigDecimal.ZERO
    private var cuartil3 = BigDecimal.ZERO

    private lateinit var etCuartil1: EditText
    private lateinit var etCuartil2: EditText
    private lateinit var etCuartil3: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuartiles)

        valuesStr = intent.getStringExtra("values") ?: ""
        val valuesArr = valuesStr.split(' ')
        valuesArr.forEach {
            values += strToBigDecimal(it)
        }

        val cuartiles = cuartiles(values)
        cuartil1 = cuartiles[0]
        cuartil2 = cuartiles[1]
        cuartil3 = cuartiles[2]

        etCuartil1 = findViewById(R.id.et_cuartil1)
        etCuartil2 = findViewById(R.id.et_cuartil2)
        etCuartil3 = findViewById(R.id.et_cuartil3)

        etCuartil1.setText(cuartil1.round(mc).toPlainString())
        etCuartil2.setText(cuartil2.round(mc).toPlainString())
        etCuartil3.setText(cuartil3.round(mc).toPlainString())
    }
}