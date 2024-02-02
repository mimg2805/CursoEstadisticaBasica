package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.media
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.mediana
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.moda
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class MedidasTendenciaCentral : AppCompatActivity() {

    private val mc = MathContext(4, RoundingMode.HALF_EVEN)

    private var values = mutableListOf<BigDecimal>()
    private var valuesStr = ""

    private var media = BigDecimal.ZERO
    private var mediana = BigDecimal.ZERO
    private var moda = listOf<BigDecimal>()
    private var modaStr = ""

    private lateinit var etMedia: EditText
    private lateinit var etMediana: EditText
    private lateinit var etModa: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medidas_tendencia_central)

        valuesStr = intent.getStringExtra("values") ?: ""
        val valuesArr = valuesStr.split(' ')
        valuesArr.forEach {
            values += strToBigDecimal(it)
        }

        media = media(values)
        mediana = mediana(values)
        moda = moda(values)
        modaStr = if (moda.isNotEmpty()) moda.joinToString().replace(".0", "") else "No hay moda"

        etMedia = findViewById(R.id.et_media)
        etMediana = findViewById(R.id.et_mediana)
        etModa = findViewById(R.id.et_moda)

        etMedia.setText(media.round(mc).toPlainString())
        etMediana.setText(mediana.round(mc).toPlainString())
        etModa.setText(modaStr)
    }
}