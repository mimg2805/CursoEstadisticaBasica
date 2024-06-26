package com.marcosmiranda.cursoestadisticabasica

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MuestreoAleatorioEstratificado : AppCompatActivity() {

    private var population = 0
    private var sample = 0
    private var listPopulationSizes = mutableListOf<Int>()
    private var listSampleSizes = mutableListOf<Int>()
    private var listRandomSamples = mutableListOf<String>()
    private var stratumsNum = 0

    private lateinit var tvEstrato: TextView
    private lateinit var etPopulationSize: EditText
    private lateinit var etSampleSize: EditText
    private lateinit var tvRandomNums: TextView
    private lateinit var llArrayButtons: LinearLayout
    private lateinit var btnGenerateSave: Button
    private lateinit var btnClearAll: Button
    private lateinit var tstMaxError: Toast
    private lateinit var tstStratumSaved: Toast
    private lateinit var tstCopy: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muestreo_aleatorio_estratificado)

        tvEstrato = findViewById(R.id.activity_muestreo_aleatorio_estratificado_tv_estrato)
        etPopulationSize = findViewById(R.id.activity_muestreo_aleatorio_estratificado_et_population_size)
        etSampleSize = findViewById(R.id.activity_muestreo_aleatorio_estratificado_et_sample_size)
        tvRandomNums = findViewById(R.id.activity_muestreo_aleatorio_estratificado_tv_random_nums)
        llArrayButtons = findViewById(R.id.activity_muestreo_aleatorio_estratificado_ll_array_buttons)
        btnGenerateSave = findViewById(R.id.activity_muestreo_aleatorio_estratificado_btn_generate_save)
        btnClearAll = findViewById(R.id.activity_muestreo_aleatorio_estratificado_btn_clear_all)
        tstMaxError = Toast.makeText(this, R.string.random_max_error, Toast.LENGTH_SHORT)
        tstStratumSaved = Toast.makeText(this, R.string.stratum_num_saved, Toast.LENGTH_SHORT)
        tstStratumSaved.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
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

        tvRandomNums.setOnClickListener { _ -> copy() }
        btnGenerateSave.setOnClickListener { _ -> generate() }
        btnClearAll.setOnClickListener { _ -> clearAll() }

        tvEstrato.text = getString(R.string.stratum_num, (stratumsNum + 1).toString())
    }

    private fun updateButtons() {
        llArrayButtons.removeAllViews()

        val btnParams = LinearLayout.LayoutParams(
            150,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // A button for each list element
        (0..< stratumsNum).forEach { i ->
            val indexStr = "${i + 1}"
            val btn = Button(this)
            btn.text = indexStr
            btn.setOnClickListener {
                etPopulationSize.setText(listPopulationSizes[i].toString())
                etSampleSize.setText(listSampleSizes[i].toString())
                tvRandomNums.text = listRandomSamples[i]
                tvEstrato.text = getString(R.string.stratum_num, indexStr)
            }
            btn.layoutParams = btnParams
            llArrayButtons.addView(btn)
        }

        // Add button
        val addBtn = Button(this)
        addBtn.text = "+"
        addBtn.setOnClickListener {
            clearInputs()
            btnGenerateSave.visibility = View.VISIBLE
            tvEstrato.text = getString(R.string.stratum_num, (stratumsNum + 1).toString())
        }
        addBtn.layoutParams = btnParams
        llArrayButtons.addView(addBtn)
    }

    private fun generate() {
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )

        if (sample == 0 || population == 0) {
            return
        }

        if (sample > population) {
            tstMaxError.cancel()
            tstMaxError.show()
            return
        }

        val numsSet = mutableSetOf<Int>()
        while (numsSet.size < sample) numsSet.add((1..population).random())

        var numsStr = numsSet.shuffled().toString().replace(",", "")
        val len = numsStr.length
        if (len > 2) numsStr = numsStr.slice(1 until numsStr.length - 1)
        tvRandomNums.text = numsStr

        listPopulationSizes.add(population)
        listSampleSizes.add(sample)
        listRandomSamples.add(numsStr)
        stratumsNum += 1

        tstStratumSaved.cancel()
        tstStratumSaved.setText(getString(R.string.stratum_num_saved, stratumsNum.toString()))
        tstStratumSaved.show()

        etPopulationSize.clearFocus()
        etSampleSize.clearFocus()
        btnGenerateSave.visibility = View.GONE
        updateButtons()
        // clear()

        // btnGenerate.setText(R.string.repeat)
    }

    private fun clearInputs() {
        population = 0
        sample = 0

        etPopulationSize.text.clear()
        etSampleSize.text.clear()
        tvRandomNums.text = ""

        etPopulationSize.clearFocus()
        etSampleSize.clearFocus()

        // btnGenerate.setText(R.string.generate)
    }

    private fun clearAll() {
        clearInputs()
        llArrayButtons.removeAllViews()
        listPopulationSizes.clear()
        listSampleSizes.clear()
        listRandomSamples.clear()
        tvEstrato.text = getString(R.string.stratum_num, "1")
    }

    private fun copy() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val text = tvRandomNums.text.toString()
        if (text.isNotEmpty()) {
            val clip = ClipData.newPlainText("nums", text)
            clipboard.setPrimaryClip(clip)
            // tstCopy.cancel()
            // tstCopy.show()
        }
    }
}