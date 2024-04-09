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
    private lateinit var tstEmpty: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matriz_datos)

        idTema = intent.getIntExtra("idTema", 0)
        subtemaTitle = intent.getStringExtra("title") ?: ""

        etAddToList = findViewById(R.id.activity_matriz_datos_et_add_to_list)
        if (idTema == 9) {
            etAddToList.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
            etAddToList.keyListener = DigitsKeyListener.getInstance("1234567890.")
        } else if (idTema == 10) {
            etAddToList.inputType = InputType.TYPE_CLASS_TEXT
        }

        tvNumberList = findViewById(R.id.activity_matriz_datos_tv_number_list)
        btnDeleteLast = findViewById(R.id.activity_matriz_datos_btn_delete_last)
        btnClear = findViewById(R.id.activity_matriz_datos_btn_clear)
        btnCalc = findViewById(R.id.activity_matriz_datos_btn_calc)

        tstEmpty = Toast.makeText(this, "La lista no puede estar vacía", Toast.LENGTH_LONG)

        etAddToList.setOnEditorActionListener { v, _, _ ->
            addToList(v)
            false
        }

        btnDeleteLast.setOnClickListener { v -> removeLastFromList(v) }

        btnClear.setOnClickListener { v -> clear(v) }

        btnCalc.setOnClickListener {
            intent = Intent()

            if (idTema == 9) {
                if (numValuesList.isEmpty()) {
                    tstEmpty.cancel()
                    tstEmpty.show()
                    return@setOnClickListener
                }

                intent = Intent(this, Tema::class.java)
            } else if (idTema == 10) {
                if (strValuesList.isEmpty()) {
                    tstEmpty.cancel()
                    tstEmpty.show()
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

    private fun addToList(v: View) {
        if (!v.isClickable) return

        val etAddToListStr = etAddToList.text.toString().lowercase().trim()
        if (idTema == 9) {
            numValuesList += strToBigDecimal(etAddToListStr)
        } else if (idTema == 10) {
            for (str in etAddToListStr.split(' ')) {
                strValuesList += str
            }
        }

        updateValuesList(v)
        etAddToList.text.clear()
    }

    private fun removeLastFromList(v: View) {
        if (!v.isClickable) return

        if (idTema == 9) {
            if (numValuesList.isNotEmpty()) numValuesList.removeLast()
        } else if (idTema == 10) {
            if (strValuesList.isNotEmpty()) strValuesList.removeLast()
        }
        updateValuesList(v)
    }

    private fun updateValuesList(v: View) {
        if (!v.isClickable) return

        if (idTema == 9) {
            valuesStr = numValuesList.joinToString(" ")
        } else if (idTema == 10) {
            valuesStr = strValuesList.joinToString(" ")
        }
        tvNumberList.text = valuesStr
    }

    private fun clear(v: View) {
        if (!v.isClickable) return

        numValuesList = mutableListOf()
        strValuesList = mutableListOf()
        valuesStr = ""

        etAddToList.setText("")
        tvNumberList.text = ""

        etAddToList.clearFocus()
    }
}