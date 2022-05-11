package com.marcosmiranda.cursoestadisticabasica

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigInteger

class RNG : AppCompatActivity() {

    private var nums: Int = 0
    private var min: Int = 0
    private var max: Int = 0

    private lateinit var mNumbersTxt: EditText
    private lateinit var mMinNumTxt: EditText
    private lateinit var mMaxNumTxt: EditText
    private lateinit var mNumsView: TextView

    private lateinit var btnGenerar: Button
    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rng)

        mNumbersTxt = findViewById(R.id.numbersTxt)
        mMinNumTxt = findViewById(R.id.minNumTxt)
        mMaxNumTxt = findViewById(R.id.maxNumTxt)
        mNumsView = findViewById(R.id.numsView)
        btnGenerar = findViewById(R.id.btnGenerar)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        mNumbersTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) nums = strToBigInteger(text.toString()).toInt()
            }
        })

        mMinNumTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) min = strToBigInteger(text.toString()).toInt()
            }
        })

        mMaxNumTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) max = strToBigInteger(text.toString()).toInt()
            }
        })

        mNumsView.setOnClickListener { view -> copy(view) }
        btnGenerar.setOnClickListener { view -> generate(view) }
        btnLimpiar.setOnClickListener { view -> clear(view) }
    }

    fun generate(view : View) {
        if (!view.isClickable) return

        if (nums > max) {
            toast?.cancel()
            toast = Toast.makeText(
                this,
                "ERROR: El número de elementos generados debe ser menor que el número máximo.",
                Toast.LENGTH_LONG
            )
            toast?.show()
            return
        }

        if (nums > 0 && max != 0 && min != max && max > min) {
            val numsSet: MutableSet<Int> = mutableSetOf()
            while (numsSet.size < nums) numsSet.add((min..max).random())

            val finalNumsList = numsSet.shuffled()
            var numsStr = finalNumsList.toString().replace(",", "")
            val len = numsStr.length
            if (len > 2) numsStr = numsStr.slice(1 until numsStr.length - 1)
            mNumsView.text = numsStr
        }
    }

    fun clear(view: View) {
        if (!view.isClickable) return

        nums = 0
        min = 0
        max = 0

        mNumbersTxt.setText("")
        mMinNumTxt.setText("")
        mMaxNumTxt.setText("")
        mNumsView.text = ""

        mNumbersTxt.clearFocus()
        mMinNumTxt.clearFocus()
        mMaxNumTxt.clearFocus()
    }

    fun copy(view: View) {
        if (!view.isClickable) return

        val nums = mNumsView.text.toString()
        // Log.e("nums", nums)
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("nums", nums)
        clipboard.setPrimaryClip(clip)
        toast?.cancel()
        toast = Toast.makeText(
            this,
            "¡Copiado!",
            Toast.LENGTH_SHORT
        )
        toast?.show()
    }
}