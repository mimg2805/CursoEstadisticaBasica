package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcReglaAdicion : AppCompatActivity() {

    private var pa: BigDecimal = BigDecimal.ZERO
    private var pb: BigDecimal = BigDecimal.ZERO
    private var payb: BigDecimal = BigDecimal.ZERO
    private var paob: BigDecimal = BigDecimal.ZERO

    private lateinit var mPATxt: EditText
    private lateinit var mPBTxt: EditText
    private lateinit var mPAyBLbl: TextView
    private lateinit var mPAyBTxt: EditText
    private lateinit var mPAoBTxt: EditText

    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    private lateinit var mexcSpinner: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    private var exc = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_regla_adicion)

        mPATxt = findViewById(R.id.PATxt)
        mPBTxt = findViewById(R.id.PBTxt)
        mPAyBLbl = findViewById(R.id.PAyBLbl)
        mPAyBTxt = findViewById(R.id.PAyBTxt)
        mPAoBTxt = findViewById(R.id.PAoBTxt)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        mexcSpinner = findViewById(R.id.excSpinner)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.regla_adicion, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mexcSpinner.adapter = adapter

        mPATxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) pa = strToBigDecimal(text.toString())
            }
        })

        mPBTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) pb = strToBigDecimal(text.toString())
            }
        })

        mPAyBTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) payb = strToBigDecimal(text.toString())
            }
        })

        mexcSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val str = parent?.getItemAtPosition(pos).toString()
                exc = true
                if (str.contains("No")) exc = false

                payb = BigDecimal.ZERO
                val paybstr = mPAyBTxt.text.toString()
                if (exc) {
                    mPAyBLbl.visibility = View.GONE
                    mPAyBTxt.visibility = View.GONE
                } else {
                    mPAyBLbl.visibility = View.VISIBLE
                    mPAyBTxt.visibility = View.VISIBLE
                    payb = strToBigDecimal(paybstr)
                }
                calc()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnLimpiar.setOnClickListener { view -> clear(view) }
    }

    fun calc() {
        try {
            if (pa != BigDecimal.ZERO && pb != BigDecimal.ZERO) {
                paob = pa + pb - payb
                mPAoBTxt.setText(String.format(paob.toString()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            toast?.cancel()
            toast = Toast.makeText(this, "Valores inválidos", Toast.LENGTH_SHORT)
            toast?.show()
        }
    }

    fun clear(view: View) {
        if (view.isClickable) {
            mPATxt.setText("")
            mPBTxt.setText("")
            mPAyBTxt.setText("")
            mPAoBTxt.setText("")

            mPATxt.clearFocus()
            mPBTxt.clearFocus()
            mPAyBTxt.clearFocus()
            mPAoBTxt.clearFocus()
        }
    }
}