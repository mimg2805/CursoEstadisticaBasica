package com.marcosmiranda.cursoestadisticabasica

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.BigInteger
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class Tabla2x2 : AppCompatActivity() {

    private var A1B1 = BigDecimal.ZERO
    private var A1B2 = BigDecimal.ZERO
    private var A2B1 = BigDecimal.ZERO
    private var A2B2 = BigDecimal.ZERO
    private var rowTotal1 = BigDecimal.ZERO
    private var rowTotal2 = BigDecimal.ZERO
    private var columnTotal1 = BigDecimal.ZERO
    private var columnTotal2 = BigDecimal.ZERO
    private var total = BigDecimal.ZERO

    private lateinit var etA1B1: EditText
    private lateinit var etA1B2: EditText
    private lateinit var etA2B1: EditText
    private lateinit var etA2B2: EditText
    private lateinit var etRowTotal1: EditText
    private lateinit var etRowTotal2: EditText
    private lateinit var etColumnTotal1: EditText
    private lateinit var etColumnTotal2: EditText
    private lateinit var etTotal: EditText
    private lateinit var btnClear: Button
    private lateinit var btnCalc: Button
    private lateinit var tstEmpty: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabla2x2)

        etA1B1 = findViewById(R.id.activity_tabla2x2_et_a1b1)
        etA1B2 = findViewById(R.id.activity_tabla2x2_et_a1b2)
        etA2B1 = findViewById(R.id.activity_tabla2x2_et_a2b1)
        etA2B2 = findViewById(R.id.activity_tabla2x2_et_a2b2)
        etRowTotal1 = findViewById(R.id.activity_tabla2x2_et_row_total_1)
        etRowTotal2 = findViewById(R.id.activity_tabla2x2_et_row_total_2)
        etColumnTotal1 = findViewById(R.id.activity_tabla2x2_et_column_total_1)
        etColumnTotal2 = findViewById(R.id.activity_tabla2x2_et_column_total_2)
        etTotal = findViewById(R.id.activity_tabla2x2_et_total)
        btnClear = findViewById(R.id.activity_tabla2x2_btn_clear)
        btnCalc = findViewById(R.id.activity_tabla2x2_btn_calc)
        tstEmpty = Toast.makeText(this, R.string.table_values_empty, Toast.LENGTH_LONG)

        val subtemaId = intent.getIntExtra("subtemaId", 0)
        val subtemaNombre = intent.getStringExtra("subtemaNombre")
        Log.e("subtemaId", subtemaId.toString())
        Log.e("subtemaNombre", subtemaNombre.toString())

        etA1B1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                A1B1 = strToBigDecimal(text.toString())
            }
        })

        etA2B1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                A2B1 = strToBigDecimal(text.toString())
            }
        })

        etA1B2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                A1B2 = strToBigDecimal(text.toString())
            }
        })

        etA2B2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                A2B2 = strToBigDecimal(text.toString())
            }
        })

        btnClear.setOnClickListener { clear() }

        btnCalc.setOnClickListener {
            etA1B1.clearFocus()
            etA2B1.clearFocus()
            etA1B2.clearFocus()
            etA2B2.clearFocus()

            if (A1B1 == BigDecimal.ZERO && A2B1 == BigDecimal.ZERO && A1B2 == BigDecimal.ZERO && A2B2 == BigDecimal.ZERO) {
                tstEmpty.cancel()
                tstEmpty.show()
                return@setOnClickListener
            }

            intent = Intent(this, Subtema::class.java)
            intent.putExtra("subtemaId", subtemaId)
            intent.putExtra("subtemaNombre", subtemaNombre)
            intent.putExtra("tabla2x2", true)
            intent.putExtra("a1b1", A1B1.toString())
            intent.putExtra("a2b1", A2B1.toString())
            intent.putExtra("a1b2", A1B2.toString())
            intent.putExtra("a2b2", A2B2.toString())
            startActivity(intent)
        }
    }

    private fun calc() {
        if (A1B1 != BigDecimal.ZERO || A2B1 != BigDecimal.ZERO) rowTotal1 = A1B1 + A2B1
        if (A1B2 != BigDecimal.ZERO || A2B2 != BigDecimal.ZERO) rowTotal2 = A1B2 + A2B2
        if (A1B1 != BigDecimal.ZERO || A1B2 != BigDecimal.ZERO) columnTotal1 = A1B1 + A1B2
        if (A2B1 != BigDecimal.ZERO || A2B2 != BigDecimal.ZERO) columnTotal2 = A2B1 + A2B2
        total = A1B1 + A2B1 + A1B2 + A2B2

        if (rowTotal1 != BigDecimal.ZERO) etRowTotal1.setText(String.format(rowTotal1.toString()))
        if (rowTotal2 != BigInteger.ZERO) etRowTotal2.setText(String.format(rowTotal2.toString()))
        if (columnTotal1 != BigInteger.ZERO) etColumnTotal1.setText(String.format(columnTotal1.toString()))
        if (columnTotal2 != BigInteger.ZERO) etColumnTotal2.setText(String.format(columnTotal2.toString()))
        if (total != BigDecimal.ZERO) etTotal.setText(String.format(total.toString()))
    }

    private fun clear() {
        A1B1 = BigDecimal.ZERO
        A1B2 = BigDecimal.ZERO
        A2B1 = BigDecimal.ZERO
        A2B2 = BigDecimal.ZERO
        rowTotal1 = BigDecimal.ZERO
        rowTotal2 = BigDecimal.ZERO
        columnTotal1 = BigDecimal.ZERO
        columnTotal2 = BigDecimal.ZERO
        total = BigDecimal.ZERO

        etA1B1.setText("")
        etA1B2.setText("")
        etA2B1.setText("")
        etA2B2.setText("")
        etRowTotal1.setText("")
        etRowTotal2.setText("")
        etColumnTotal1.setText("")
        etColumnTotal2.setText("")
        etTotal.setText("")

        etA1B1.clearFocus()
        etA1B2.clearFocus()
        etA2B1.clearFocus()
        etA2B2.clearFocus()
    }

}