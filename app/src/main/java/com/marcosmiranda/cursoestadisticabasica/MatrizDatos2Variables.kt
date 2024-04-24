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

class MatrizDatos2Variables : AppCompatActivity() {

    private var idTema = 0
    private var subtemaTitle = ""

    private var valuesXStr = ""
    private var valuesYStr = ""
    private var valuesXList = mutableListOf<String>()
    private var valuesYList = mutableListOf<String>()

    private lateinit var tvVariableX: TextView
    private lateinit var etAddToListX: EditText
    private lateinit var tvNumberListX: TextView
    private lateinit var btnDeleteLastX: Button
    private lateinit var btnClearX: Button
    private lateinit var tvVariableY: TextView
    private lateinit var etAddToListY: EditText
    private lateinit var tvNumberListY: TextView
    private lateinit var btnDeleteLastY: Button
    private lateinit var btnClearY: Button
    private lateinit var btnCalc: Button
    private lateinit var tstEmpty: Toast
    private lateinit var tstListEmpty: Toast
    private lateinit var tstListNotMatch: Toast
    private lateinit var tstList2Unique: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matriz_datos_2_variables)

        idTema = intent.getIntExtra("idTema", 0)
        subtemaTitle = intent.getStringExtra("title") ?: ""

        tvVariableX = findViewById(R.id.activity_matriz_datos_2_variables_tv_variable_x)
        tvVariableY = findViewById(R.id.activity_matriz_datos_2_variables_tv_variable_y)
        etAddToListX = findViewById(R.id.activity_matriz_datos_2_variables_et_add_to_list_x)
        etAddToListY = findViewById(R.id.activity_matriz_datos_2_variables_et_add_to_list_y)
        when (idTema) {
            11 -> {
                etAddToListX.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
                etAddToListX.keyListener = DigitsKeyListener.getInstance("1234567890.")
                etAddToListY.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
                etAddToListY.keyListener = DigitsKeyListener.getInstance("1234567890.")
            }
            12 -> {
                etAddToListX.inputType = InputType.TYPE_CLASS_TEXT
                etAddToListY.inputType = InputType.TYPE_CLASS_TEXT
            }
            13 -> {
                tvVariableX.text = getString(R.string.variable_x_cualitativa)
                tvVariableY.text = getString(R.string.variable_y_cuantitativa)
                etAddToListX.inputType = InputType.TYPE_CLASS_TEXT
                etAddToListY.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
                etAddToListY.keyListener = DigitsKeyListener.getInstance("1234567890.")
            }
        }

        tvNumberListX = findViewById(R.id.activity_matriz_datos_2_variables_tv_number_list_x)
        btnDeleteLastX = findViewById(R.id.activity_matriz_datos_2_variables_btn_delete_last_x)
        btnClearX = findViewById(R.id.activity_matriz_datos_2_variables_btn_clear_x)
        tvNumberListY = findViewById(R.id.activity_matriz_datos_2_variables_tv_number_list_y)
        btnDeleteLastY = findViewById(R.id.activity_matriz_datos_2_variables_btn_delete_last_y)
        btnClearY = findViewById(R.id.activity_matriz_datos_2_variables_btn_clear_y)
        btnCalc = findViewById(R.id.activity_matriz_datos_2_variables_btn_calc)

        tstEmpty = Toast.makeText(this, R.string.list_error_empty_input, Toast.LENGTH_LONG)
        tstListEmpty = Toast.makeText(this, R.string.list_error_empty_list, Toast.LENGTH_LONG)
        tstListNotMatch = Toast.makeText(this, R.string.list_error_not_match, Toast.LENGTH_LONG)
        tstList2Unique = Toast.makeText(this, R.string.list_error_2_unique, Toast.LENGTH_LONG)

        etAddToListX.setOnEditorActionListener { v, _, _ ->
            addToListX(v)
            false
        }

        etAddToListY.setOnEditorActionListener { v, _, _ ->
            addToListY(v)
            false
        }

        btnDeleteLastX.setOnClickListener { v -> removeLastFromListX(v) }
        btnDeleteLastY.setOnClickListener { v -> removeLastFromListY(v) }

        btnClearX.setOnClickListener { v -> clearListX(v) }
        btnClearY.setOnClickListener { v -> clearListY(v) }

        btnCalc.setOnClickListener {
            etAddToListX.clearFocus()
            etAddToListY.clearFocus()

            if (valuesXList.isEmpty() || valuesYList.isEmpty()) {
                cancelToasts()
                tstEmpty.show()
                return@setOnClickListener
            }

            if (valuesXList.count() != valuesYList.count()) {
                cancelToasts()
                tstListNotMatch.show()
                return@setOnClickListener
            }

            if (valuesXList.distinct().count() < 2 || valuesYList.distinct().count() < 2) {
                cancelToasts()
                tstList2Unique.show()
                return@setOnClickListener
            }

            intent = when (idTema) {
                11 -> Intent(this, CalcDescriptiva2VariablesCuantitativas::class.java)
                12 -> Intent(this, CalcDescriptiva2VariablesCualitativas::class.java)
                13 -> Intent(this, CalcDescriptiva2VariablesMixtas::class.java)
                else -> Intent()
            }

            intent.putExtra("idTema", idTema)
            intent.putExtra("title", subtemaTitle)
            intent.putExtra("valuesX", valuesXStr)
            intent.putExtra("valuesY", valuesYStr)
            this.startActivity(intent)
        }
    }

    private fun cancelToasts() {
        tstEmpty.cancel()
        tstListEmpty.cancel()
        tstListNotMatch.cancel()
        tstList2Unique.cancel()
    }

    private fun addToListX(v: View) {
        if (!v.isClickable) return

        val etAddToListXStr = etAddToListX.text.toString().lowercase().trim()
        if (etAddToListXStr.isEmpty()) {
            cancelToasts()
            tstEmpty.show()
            return
        }
        tstEmpty.cancel()

        for (str in etAddToListXStr.split(" ")) {
            valuesXList += str
        }
        updateValuesListX(v)
        etAddToListX.text.clear()
    }

    private fun addToListY(v: View) {
        if (!v.isClickable) return

        val etAddToListYStr = etAddToListY.text.toString().lowercase().trim()
        if (etAddToListYStr.isEmpty()) {
            cancelToasts()
            tstEmpty.show()
            return
        }
        tstEmpty.cancel()

        for (str in etAddToListYStr.split(" ")) {
            valuesYList += str
        }
        updateValuesListY(v)
        etAddToListY.text.clear()
    }

    private fun removeLastFromListX(v: View) {
        if (!v.isClickable) return

        if (valuesXList.isNotEmpty()) valuesXList.removeLast()
        updateValuesListX(v)
    }

    private fun removeLastFromListY(v: View) {
        if (!v.isClickable) return

        if (valuesYList.isNotEmpty()) valuesYList.removeLast()
        updateValuesListY(v)
    }

    private fun updateValuesListX(v: View) {
        if (!v.isClickable) return

        valuesXStr = valuesXList.joinToString(" ")
        tvNumberListX.text = valuesXStr
    }

    private fun updateValuesListY(v: View) {
        if (!v.isClickable) return

        valuesYStr = valuesYList.joinToString(" ")
        tvNumberListY.text = valuesYStr
    }

    private fun clearListX(v: View) {
        if (!v.isClickable) return

        valuesXList = mutableListOf()
        valuesXStr = ""

        etAddToListX.setText("")
        tvNumberListX.text = ""

        etAddToListX.clearFocus()
    }

    private fun clearListY(v: View) {
        if (!v.isClickable) return

        valuesYList = mutableListOf()
        valuesYStr = ""

        etAddToListY.setText("")
        tvNumberListY.text = ""

        etAddToListY.clearFocus()
    }
}