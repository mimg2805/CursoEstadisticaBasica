package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.amplitud
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.desviacionEstandar
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.desviacionMedia
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.media
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class Desviaciones : AppCompatActivity() {

    private val mc = MathContext(2, RoundingMode.HALF_EVEN)

    private var values = mutableListOf<BigDecimal>()
    private var valuesStr = ""

    private var amplitud = BigDecimal.ZERO
    private var desviacionMedia = BigDecimal.ZERO
    private var desviacionEstandar = BigDecimal.ZERO

    private lateinit var etAmplitud: EditText
    private lateinit var etDesviacionMedia: EditText
    private lateinit var etDesvacionEstandar: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desviaciones)

        valuesStr = intent.getStringExtra("values") ?: ""
        val valuesArr = valuesStr.split(' ')
        valuesArr.forEach {
            values += strToBigDecimal(it)
        }

        amplitud = amplitud(values)
        desviacionMedia = desviacionMedia(values)
        desviacionEstandar = desviacionEstandar(values)

        etAmplitud = findViewById(R.id.et_amplitud)
        etDesviacionMedia = findViewById(R.id.et_desviacion_media)
        etDesvacionEstandar = findViewById(R.id.et_desviacion_estandar)

        etAmplitud.setText(amplitud.round(mc).toPlainString())
        etDesviacionMedia.setText(desviacionMedia.round(mc).toPlainString())
        etDesvacionEstandar.setText(desviacionEstandar.round(mc).toPlainString())
    }


}