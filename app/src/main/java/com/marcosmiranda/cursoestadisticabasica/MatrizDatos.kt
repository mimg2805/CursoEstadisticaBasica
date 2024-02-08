package com.marcosmiranda.cursoestadisticabasica

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import java.math.BigDecimal


class MatrizDatos : AppCompatActivity() {

    private var idTema = 0
    private var subtemaTitle = ""

    private var valuesStr = ""
    private var numValuesList = mutableListOf<BigDecimal>()
    private var strValuesList = mutableListOf<String>()

    private lateinit var etAddToList: EditText
    private lateinit var tvNumberList: TextView
    private lateinit var btnDeleteLast: Button
    private lateinit var btnClear: Button
    private lateinit var btnCalc: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matriz_datos)

        idTema = intent.getIntExtra("idTema", 0)
        Log.e("idTema", idTema.toString())
        subtemaTitle = intent.getStringExtra("title") ?: ""

        etAddToList = findViewById(R.id.et_add_to_list)
        if (idTema == 9) {
            etAddToList.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
            etAddToList.keyListener = DigitsKeyListener.getInstance("1234567890.")
        } else if (idTema == 10) {
            etAddToList.inputType = InputType.TYPE_CLASS_TEXT
        }

        tvNumberList = findViewById(R.id.tv_number_list)
        btnDeleteLast = findViewById(R.id.btn_delete_last)
        btnClear = findViewById(R.id.btn_clear)
        btnCalc = findViewById(R.id.btn_calc)

        etAddToList.setOnEditorActionListener { v, _, _ ->
            addToList(v)
            false
        }

        btnDeleteLast.setOnClickListener {
            removeLastFromList(it)
        }

        btnClear.setOnClickListener {
            clear(it)
        }

        btnCalc.setOnClickListener {
            intent = Intent()

            if (idTema == 9) {
                if (numValuesList.isEmpty()) {
                    return@setOnClickListener
                }

                intent = Intent(this, Tema::class.java)
            } else if (idTema == 10) {
                if (strValuesList.isEmpty()) {
                    return@setOnClickListener
                }

                intent = Intent(this, CalcDescriptivaVariableCualitativa::class.java)
            }

            intent.putExtra("idTema", idTema)
            intent.putExtra("title", subtemaTitle)
            intent.putExtra("values", valuesStr)
            this.startActivity(intent)
        }
    }

    private fun addToList(view: View) {
        if (!view.isClickable) return

        val etAddToListStr = etAddToList.text.trim().toString()
        if (idTema == 9) {
            numValuesList += strToBigDecimal(etAddToListStr)
        } else if (idTema == 10) {
            strValuesList += etAddToListStr
        }

        updateValuesList(view)
        etAddToList.text.clear()
    }

    private fun removeLastFromList(view: View) {
        if (!view.isClickable) return

        if (idTema == 9) {
            if (numValuesList.isNotEmpty()) numValuesList.removeLast()
        } else if (idTema == 10) {
            if (strValuesList.isNotEmpty()) strValuesList.removeLast()
        }
        updateValuesList(view)
    }

    private fun updateValuesList(view: View) {
        if (!view.isClickable) return

        if (idTema == 9) {
            valuesStr = numValuesList.joinToString(" ")
        } else if (idTema == 10) {
            valuesStr = strValuesList.joinToString(" ")
        }
        tvNumberList.text = valuesStr
    }

    private fun clear(view: View) {
        if (!view.isClickable) return

        numValuesList = mutableListOf()
        strValuesList = mutableListOf()
        valuesStr = ""

        etAddToList.setText("")
        tvNumberList.text = ""

        etAddToList.clearFocus()
    }
}