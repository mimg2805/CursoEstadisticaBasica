package com.marcosmiranda.cursoestadisticabasica

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class CalcReglaMultiplicacion : AppCompatActivity() {

    private var pa: BigDecimal = BigDecimal.ZERO
    private var pb: BigDecimal = BigDecimal.ZERO
    private var payb: BigDecimal = BigDecimal.ZERO
    private var pbovera: BigDecimal = BigDecimal.ZERO
    private var indep = false

    private lateinit var mPATxt: EditText
    private lateinit var mPBTxt: EditText
    private lateinit var mPBoverALbl: TextView
    private lateinit var mPBoverATxt: EditText
    private lateinit var mPAyBTxt: EditText

    private lateinit var btnLimpiar: Button
    private var toast: Toast? = null

    private lateinit var mexcSpinner: Spinner
    private lateinit var adapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc_regla_multiplicacion)

        mPATxt = findViewById(R.id.PATxt)
        mPBTxt = findViewById(R.id.PBTxt)
        mPBoverALbl = findViewById(R.id.PBoverALbl)
        mPBoverATxt = findViewById(R.id.PBoverATxt)
        mPAyBTxt = findViewById(R.id.PAyBTxt)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        mexcSpinner = findViewById(R.id.excSpinner)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.regla_multiplicacion, R.layout.spinner_item
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

        mPBoverATxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrBlank()) calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) pbovera = strToBigDecimal(text.toString())
            }
        })

        mexcSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val str = parent?.getItemAtPosition(pos).toString()
                indep = true
                if (str.contains("No")) indep = false

                payb = BigDecimal.ZERO
                val paybstr = mPAyBTxt.text.toString()
                if (indep) {
                    mPBoverALbl.visibility = View.GONE
                    mPBoverATxt.visibility = View.GONE
                } else {
                    mPBoverALbl.visibility = View.VISIBLE
                    mPBoverATxt.visibility = View.VISIBLE
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
                payb = if (indep) {
                    pa * pb
                } else {
                    pa * pbovera
                }
                mPAyBTxt.setText(String.format(payb.toString()))
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
            pa = BigDecimal.ZERO
            pb = BigDecimal.ZERO
            pbovera = BigDecimal.ZERO
            payb = BigDecimal.ZERO

            mPATxt.setText("")
            mPBTxt.setText("")
            mPBoverATxt.setText("")
            mPAyBTxt.setText("")

            mPATxt.clearFocus()
            mPBTxt.clearFocus()
            mPBoverATxt.clearFocus()
            mPAyBTxt.clearFocus()
        }
    }
}
