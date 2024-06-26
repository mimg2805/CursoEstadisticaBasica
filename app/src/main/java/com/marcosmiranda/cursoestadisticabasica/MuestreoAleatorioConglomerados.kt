package com.marcosmiranda.cursoestadisticabasica

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MuestreoAleatorioConglomerados : AppCompatActivity() {

    private var population = 0
    private var sample = 0
    private val min = 1

    private lateinit var etPopulationSize: EditText
    private lateinit var etSampleSize: EditText
    private lateinit var tvRandomNums: TextView
    private lateinit var btnGenerate: Button
    private lateinit var btnClear: Button
    private lateinit var tstMaxError: Toast
    private lateinit var tstCopy: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muestreo_aleatorio_conglomerados)

        etPopulationSize = findViewById(R.id.activity_muestreo_aleatorio_conglomerados_et_population_size)
        etSampleSize = findViewById(R.id.activity_muestreo_aleatorio_conglomerados_et_sample_size)
        tvRandomNums = findViewById(R.id.activity_muestreo_aleatorio_conglomerados_tv_random_nums)
        btnGenerate = findViewById(R.id.activity_muestreo_aleatorio_conglomerados_btn_generate)
        btnClear = findViewById(R.id.activity_muestreo_aleatorio_conglomerados_btn_clear)
        tstMaxError = Toast.makeText(this, R.string.random_max_error, Toast.LENGTH_SHORT)
        tstCopy = Toast.makeText(this, R.string.copy, Toast.LENGTH_SHORT)

        etPopulationSize.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                // population = strToBigInteger(text.toString()).toInt()
                population = text.toString().toInt()
            }
        })

        etSampleSize.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                // sample = strToBigInteger(text.toString()).toInt()
                sample = text.toString().toInt()
            }
        })

        tvRandomNums.setOnClickListener { v -> copy(v) }
        btnGenerate.setOnClickListener { v -> generate(v) }
        btnClear.setOnClickListener { v -> clear(v) }
    }

    private fun generate(v : View) {
        if (!v.isClickable) return

        if (sample > population) {
            tstMaxError.cancel()
            tstMaxError.show()
            return
        }

        if (sample > 0 && population > min) {
            val numsSet: MutableSet<Int> = mutableSetOf()
            while (numsSet.size < sample) numsSet.add((min..population).random())

            val finalNumsList = numsSet.shuffled()
            var numsStr = finalNumsList.toString().replace(",", "")
            val len = numsStr.length
            if (len > 2) numsStr = numsStr.slice(1 until numsStr.length - 1)
            tvRandomNums.text = numsStr
        }

        btnGenerate.setText(R.string.repeat)
    }

    private fun clear(v: View) {
        if (!v.isClickable) return

        population = 0
        sample = 0

        etPopulationSize.setText("")
        etSampleSize.setText("")
        tvRandomNums.text = ""

        etPopulationSize.clearFocus()
        etSampleSize.clearFocus()

        btnGenerate.setText(R.string.generate)
    }

    private fun copy(v: View) {
        if (!v.isClickable) return

        val nums = tvRandomNums.text.toString()
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("nums", nums)
        clipboard.setPrimaryClip(clip)
        tstCopy.cancel()
        tstCopy.show()
    }
}