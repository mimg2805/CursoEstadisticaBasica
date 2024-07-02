package com.marcosmiranda.cursoestadisticabasica

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger

class MuestreoAleatorioSistematico : AppCompatActivity() {

    private var population = 0
    private var sample = 0
    private var selectionInterval = 0
    private var randomStart = 0

    private lateinit var etPopulationSize: EditText
    private lateinit var etSampleSize: EditText
    private lateinit var etSelectionInterval: EditText
    private lateinit var etRandomStart: EditText
    private lateinit var tvRandomNums: TextView
    private lateinit var btnGenerate: Button
    private lateinit var btnClear: Button
    private lateinit var tstMaxError: Toast
    private lateinit var tstCopy: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muestreo_aleatorio_sistematico)

        etPopulationSize = findViewById(R.id.activity_muestreo_aleatorio_sistematico_et_population_size)
        etSampleSize = findViewById(R.id.activity_muestreo_aleatorio_sistematico_et_sample_size)
        etSelectionInterval = findViewById(R.id.activity_muestreo_aleatorio_sistematico_et_selection_interval)
        etRandomStart = findViewById(R.id.activity_muestreo_aleatorio_sistematico_et_random_start)
        tvRandomNums = findViewById(R.id.activity_muestreo_aleatorio_sistematico_tv_random_nums)
        btnGenerate = findViewById(R.id.activity_muestreo_aleatorio_sistematico_btn_generate)
        btnClear = findViewById(R.id.activity_muestreo_aleatorio_sistematico_btn_clear)
        tstMaxError = Toast.makeText(this, R.string.random_max_error, Toast.LENGTH_SHORT)
        tstCopy = Toast.makeText(this, R.string.copy, Toast.LENGTH_SHORT)

        etPopulationSize.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                // population = strToBigInteger(text.toString()).toInt()
                population = text.toString().toInt()
                calc()
            }
        })

        etSampleSize.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                // sample = strToBigInteger(text.toString()).toInt()
                sample = text.toString().toInt()
                calc()
            }
        })

        tvRandomNums.setOnClickListener { _ -> copy() }
        btnGenerate.setOnClickListener { _ -> generate() }
        btnClear.setOnClickListener { _ -> clear() }
    }

    private fun calc() {
        if (sample == 0) return

        selectionInterval = population / sample
        etSelectionInterval.setText(selectionInterval.toString())

        randomStart = (1..selectionInterval).random()
        etRandomStart.setText(randomStart.toString())
    }

    private fun generate() {
        etPopulationSize.clearFocus()
        etSampleSize.clearFocus()

        if (sample > population) {
            tstMaxError.cancel()
            tstMaxError.show()
            return
        }

        val numsSet: MutableSet<Int> = mutableSetOf()
        numsSet.add(randomStart)
        var mult = 1
        while (numsSet.size < sample) {
            numsSet.add(randomStart + selectionInterval * mult)
            mult += 1
        }

        var numsStr = numsSet.toString().replace(",", "")
        val len = numsStr.length
        if (len > 2) numsStr = numsStr.slice(1 until numsStr.length - 1)
        tvRandomNums.text = numsStr

        btnGenerate.setText(R.string.repeat)
    }

    private fun clear() {
        population = 0
        sample = 0
        selectionInterval = 0
        randomStart = 0

        etPopulationSize.setText("")
        etSampleSize.setText("")
        etSelectionInterval.setText("")
        etRandomStart.setText("")
        tvRandomNums.text = ""

        etPopulationSize.clearFocus()
        etSampleSize.clearFocus()

        btnGenerate.setText(R.string.generate)
    }

    private fun copy() {
        val nums = tvRandomNums.text.toString()
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("nums", nums)
        clipboard.setPrimaryClip(clip)
        tstCopy.cancel()
        tstCopy.show()
    }
}