package com.marcosmiranda.cursoestadisticabasica

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import java.util.Collections

class MatrizDatos : AppCompatActivity() {

    private var temaId = 0
    private var subtemaNombre = ""

    private var valuesStr = ""
    private var valuesList = mutableListOf<String>()

    private lateinit var etAddToList: EditText
    private lateinit var tvNumberList: TextView
    private lateinit var btnDeleteLast: Button
    private lateinit var btnClear: Button
    private lateinit var btnCalc: Button
    private lateinit var tstEmpty: Toast
    private lateinit var tstListEmpty: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matriz_datos)

        temaId = intent.getIntExtra("temaId", 0)
        subtemaNombre = intent.getStringExtra("subtemaNombre") ?: ""

        etAddToList = findViewById(R.id.activity_matriz_datos_et_add_to_list)
        if (temaId == 10) {
            etAddToList.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
            etAddToList.keyListener = DigitsKeyListener.getInstance("1234567890.")
        } else if (temaId == 11) {
            etAddToList.inputType = InputType.TYPE_CLASS_TEXT
        }

        tvNumberList = findViewById(R.id.activity_matriz_datos_tv_number_list)
        btnDeleteLast = findViewById(R.id.activity_matriz_datos_btn_delete_last)
        btnClear = findViewById(R.id.activity_matriz_datos_btn_clear)
        btnCalc = findViewById(R.id.activity_matriz_datos_btn_calc)

        tstEmpty = Toast.makeText(this, R.string.list_error_empty_input, Toast.LENGTH_LONG)
        tstListEmpty = Toast.makeText(this, R.string.list_error_empty_list, Toast.LENGTH_LONG)

        etAddToList.setOnEditorActionListener { v, _, _ ->
            addToList(v)
            false
        }

        btnDeleteLast.setOnClickListener { v -> removeLastFromList(v) }

        btnClear.setOnClickListener { v -> clear(v) }

        btnCalc.setOnClickListener {
            if (valuesList.isEmpty()) {
                tstEmpty.cancel()
                tstEmpty.show()
                return@setOnClickListener
            }

            intent = Intent()
            if (temaId == 10) {
                intent = Intent(this, CalcDescriptivaVariableCuantitativa::class.java)
            } else if (temaId == 11) {
                intent = Intent(this, CalcDescriptivaVariableCualitativa::class.java)
            }

            intent.putExtra("temaId", temaId)
            intent.putExtra("subtemaNombre", subtemaNombre)
            intent.putExtra("values", valuesStr)
            this.startActivity(intent)
        }
    }

    private fun cancelToasts() {
        tstEmpty.cancel()
        tstListEmpty.cancel()
    }

    private fun addToList(v: View) {
        if (!v.isClickable) return

        val etAddToListStr = etAddToList.text.toString().lowercase().trim()
        if (etAddToListStr.isEmpty()) {
            cancelToasts()
            tstEmpty.show()
            return
        }
        tstEmpty.cancel()

        for (str in etAddToListStr.split(' ')) {
            valuesList += str
        }

        valuesList.sortByDescending { str -> Collections.frequency(valuesList, str) }

        updateValuesList(v)
        etAddToList.text.clear()
    }

    private fun removeLastFromList(v: View) {
        if (!v.isClickable) return

        if (valuesList.isNotEmpty()) valuesList.removeAt(valuesList.count() - 1)
        updateValuesList(v)
    }

    private fun updateValuesList(v: View) {
        if (!v.isClickable) return

        valuesStr = valuesList.joinToString(" ")
        tvNumberList.text = valuesStr
    }

    private fun clear(v: View) {
        if (!v.isClickable) return

        valuesStr = ""
        valuesList = mutableListOf()

        etAddToList.setText("")
        tvNumberList.text = ""

        etAddToList.clearFocus()
    }
}