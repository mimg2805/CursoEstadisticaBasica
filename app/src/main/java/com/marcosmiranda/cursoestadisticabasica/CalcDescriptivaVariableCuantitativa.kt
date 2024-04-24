package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.media
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.mediana
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.moda
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.amplitud
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.cuartiles
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.desviacionEstandar
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.desviacionMedia

class CalcDescriptivaVariableCuantitativa : AppCompatActivity() {

    private val mc = MathContext(4, RoundingMode.HALF_EVEN)

    private var values = listOf<BigDecimal>()
    private var valuesStr = ""

    private var media = BigDecimal.ZERO
    private var mediana = BigDecimal.ZERO
    private var moda = listOf<BigDecimal>()
    private var modaStr = ""
    private var cuartil1 = BigDecimal.ZERO
    private var cuartil2 = BigDecimal.ZERO
    private var cuartil3 = BigDecimal.ZERO
    private var amplitud = BigDecimal.ZERO
    private var desviacionMedia = BigDecimal.ZERO
    private var desviacionEstandar = BigDecimal.ZERO

    private lateinit var etMedia: EditText
    private lateinit var etMediana: EditText
    private lateinit var etModa: EditText
    private lateinit var etCuartil1: EditText
    private lateinit var etCuartil2: EditText
    private lateinit var etCuartil3: EditText
    private lateinit var etAmplitud: EditText
    private lateinit var etDesviacionMedia: EditText
    private lateinit var etDesvacionEstandar: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_descriptiva_variable_cuantitativa)

        valuesStr = intent.getStringExtra("values") ?: ""
        values = valuesStr.split(' ').map { str -> strToBigDecimal(str) }

        etMedia = findViewById(R.id.activity_calc_descriptiva_variable_cuantitativa_et_media)
        etMediana = findViewById(R.id.activity_calc_descriptiva_variable_cuantitativa_et_mediana)
        etModa = findViewById(R.id.activity_calc_descriptiva_variable_cuantitativa_et_moda)
        etCuartil1 = findViewById(R.id.activity_calc_descriptiva_variable_cuantitativa_et_cuartil1)
        etCuartil2 = findViewById(R.id.activity_calc_descriptiva_variable_cuantitativa_et_cuartil2)
        etCuartil3 = findViewById(R.id.activity_calc_descriptiva_variable_cuantitativa_et_cuartil3)
        etAmplitud = findViewById(R.id.activity_calc_descriptiva_variable_cuantitativa_et_amplitud)
        etDesviacionMedia = findViewById(R.id.activity_calc_descriptiva_variable_cuantitativa_et_desviacion_media)
        etDesvacionEstandar = findViewById(R.id.activity_calc_descriptiva_variable_cuantitativa_et_desviacion_estandar)

        media = media(values)
        mediana = mediana(values)
        moda = moda(values)
        modaStr = if (moda.isNotEmpty()) moda.joinToString().replace(".0", "") else "No hay moda"

        val cuartiles = cuartiles(values)
        cuartil1 = cuartiles[0]
        cuartil2 = cuartiles[1]
        cuartil3 = cuartiles[2]

        amplitud = amplitud(values)
        desviacionMedia = desviacionMedia(values)
        desviacionEstandar = desviacionEstandar(values)

        etMedia.setText(media.round(mc).toPlainString())
        etMediana.setText(mediana.round(mc).toPlainString())
        etModa.setText(modaStr)
        etCuartil1.setText(cuartil1.round(mc).toPlainString())
        etCuartil2.setText(cuartil2.round(mc).toPlainString())
        etCuartil3.setText(cuartil3.round(mc).toPlainString())
        etAmplitud.setText(amplitud.round(mc).toPlainString())
        etDesviacionMedia.setText(desviacionMedia.round(mc).toPlainString())
        etDesvacionEstandar.setText(desviacionEstandar.round(mc).toPlainString())
    }
}