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

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger

class RNG : AppCompatActivity() {

    private var nums: Int = 0
    private var min: Int = 0
    private var max: Int = 0

    private lateinit var etNums: EditText
    private lateinit var etMin: EditText
    private lateinit var etMax: EditText
    private lateinit var tvRandomNums: TextView
    private lateinit var btnGenerate: Button
    private lateinit var btnClear: Button
    private lateinit var tstMaxError: Toast
    private lateinit var tstCopy: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rng)

        etNums = findViewById(R.id.activity_rng_et_nums)
        etMin = findViewById(R.id.activity_rng_et_min)
        etMax = findViewById(R.id.activity_rng_et_max)
        tvRandomNums = findViewById(R.id.activity_rng_tv_random_nums)
        btnGenerate = findViewById(R.id.activity_rng_btn_generate)
        btnClear = findViewById(R.id.activity_rng_btn_clear)
        tstMaxError = Toast.makeText(this, R.string.random_max_error, Toast.LENGTH_SHORT)
        tstCopy = Toast.makeText(this, R.string.copy, Toast.LENGTH_SHORT)

        etNums.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                nums = strToBigInteger(text.toString()).toInt()
            }
        })

        etMin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return 
                min = strToBigInteger(text.toString()).toInt()
            }
        })

        etMax.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                max = strToBigInteger(text.toString()).toInt()
            }
        })

        tvRandomNums.setOnClickListener { v -> copy(v) }
        btnGenerate.setOnClickListener { v -> generate(v) }
        btnClear.setOnClickListener { v -> clear(v) }
    }

    private fun generate(v : View) {
        if (!v.isClickable) return

        if (nums > max) {
            tstMaxError.cancel()
            tstMaxError.show()
            return
        }

        if (nums > 0 && max != 0 && min != max && max > min) {
            val numsSet: MutableSet<Int> = mutableSetOf()
            while (numsSet.size < nums) numsSet.add((min..max).random())

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

        nums = 0
        min = 0
        max = 0

        etNums.setText("")
        etMin.setText("")
        etMax.setText("")
        tvRandomNums.text = ""

        etNums.clearFocus()
        etMin.clearFocus()
        etMax.clearFocus()

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