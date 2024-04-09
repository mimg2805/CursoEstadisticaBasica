package com.marcosmiranda.cursoestadisticabasica

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal

import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal

class MatrizDatos2Variables : AppCompatActivity() {

    private var idTema = 0
    private var subtemaTitle = ""

    private var valuesStrX = ""
    private var numValuesListX = mutableListOf<BigDecimal>()
    private var strValuesListX = mutableListOf<String>()
    private var valuesStrY = ""
    private var numValuesListY = mutableListOf<BigDecimal>()
    private var strValuesListY = mutableListOf<String>()

    private lateinit var etAddToListX: EditText
    private lateinit var tvNumberListX: TextView
    private lateinit var btnDeleteLastX: Button
    private lateinit var btnClearX: Button
    private lateinit var etAddToListY: EditText
    private lateinit var tvNumberListY: TextView
    private lateinit var btnDeleteLastY: Button
    private lateinit var btnClearY: Button
    private lateinit var btnCalc: Button
    private lateinit var tstEmpty: Toast
    private lateinit var tstListEmpty: Toast
    private lateinit var tstListNotMatch: Toast
    private lateinit var tstListNotMatchUnique: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matriz_datos_2_variables)

        idTema = intent.getIntExtra("idTema", 0)
        subtemaTitle = intent.getStringExtra("title") ?: ""

        etAddToListX = findViewById(R.id.activity_matriz_datos_2_variables_et_add_to_list_x)
        etAddToListY = findViewById(R.id.activity_matriz_datos_2_variables_et_add_to_list_y)
        if (idTema == 11) {
            etAddToListX.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
            etAddToListX.keyListener = DigitsKeyListener.getInstance("1234567890.")
            etAddToListY.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
            etAddToListY.keyListener = DigitsKeyListener.getInstance("1234567890.")
        } else if (idTema == 12) {
            etAddToListX.inputType = InputType.TYPE_CLASS_TEXT
            etAddToListY.inputType = InputType.TYPE_CLASS_TEXT
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
        tstListNotMatchUnique = Toast.makeText(this, R.string.list_error_not_match_unique, Toast.LENGTH_LONG)

        etAddToListX.setOnEditorActionListener { v, _, _ ->
            addToList1(v)
            false
        }

        etAddToListY.setOnEditorActionListener { v, _, _ ->
            addToList2(v)
            false
        }

        btnDeleteLastX.setOnClickListener { v -> removeLastFromList1(v) }
        btnDeleteLastY.setOnClickListener { v -> removeLastFromList2(v) }

        btnClearX.setOnClickListener { v -> clearList1(v) }
        btnClearY.setOnClickListener { v -> clearList2(v) }

        btnCalc.setOnClickListener {
            intent = Intent()
            if (idTema == 11) {
                if (numValuesListX.isEmpty() || numValuesListY.isEmpty()) {
                    tstEmpty.cancel()
                    tstListEmpty.cancel()
                    tstListNotMatch.cancel()
                    tstListNotMatchUnique.cancel()
                    tstEmpty.show()
                    return@setOnClickListener
                }

                if (numValuesListX.count() != numValuesListY.count()) {
                    tstEmpty.cancel()
                    tstListEmpty.cancel()
                    tstListNotMatch.cancel()
                    tstListNotMatchUnique.cancel()
                    tstListNotMatch.show()
                    return@setOnClickListener
                }

                if (numValuesListX.distinct().count() != numValuesListY.distinct().count()) {
                    tstEmpty.cancel()
                    tstListEmpty.cancel()
                    tstListNotMatch.cancel()
                    tstListNotMatchUnique.cancel()
                    tstListNotMatchUnique.show()
                    return@setOnClickListener
                }

                intent = Intent(this, CalcDescriptiva2VariablesCuantitativas::class.java)
            } else if (idTema == 12) {
                if (strValuesListX.isEmpty() || strValuesListY.isEmpty()) {
                    tstEmpty.cancel()
                    tstListEmpty.cancel()
                    tstListNotMatch.cancel()
                    tstListNotMatchUnique.cancel()
                    tstEmpty.show()
                    return@setOnClickListener
                }

                if (strValuesListX.count() != strValuesListY.count()) {
                    tstEmpty.cancel()
                    tstListEmpty.cancel()
                    tstListNotMatch.cancel()
                    tstListNotMatchUnique.cancel()
                    tstListNotMatch.show()
                    return@setOnClickListener
                }

                if (strValuesListX.distinct().count() != strValuesListY.distinct().count()) {
                    tstEmpty.cancel()
                    tstListEmpty.cancel()
                    tstListNotMatch.cancel()
                    tstListNotMatchUnique.cancel()
                    tstListNotMatchUnique.show()
                    return@setOnClickListener
                }

                intent = Intent(this, CalcDescriptiva2VariablesCualitativas::class.java)
            }

            intent.putExtra("idTema", idTema)
            intent.putExtra("title", subtemaTitle)
            intent.putExtra("valuesX", valuesStrX)
            intent.putExtra("valuesY", valuesStrY)
            this.startActivity(intent)
        }
    }

    private fun addToList1(v: View) {
        if (!v.isClickable) return

        val etAddToListXStr = etAddToListX.text.toString().lowercase().trim()
        if (etAddToListXStr.isEmpty()) {
            tstEmpty.cancel()
            tstListEmpty.cancel()
            tstListNotMatch.cancel()
            tstListNotMatchUnique.cancel()
            tstEmpty.show()
            return
        }
        tstEmpty.cancel()

        if (idTema == 11) {
            numValuesListX += strToBigDecimal(etAddToListXStr)
        } else if (idTema == 12) {
            for (str in etAddToListXStr.split(' ')) {
                strValuesListX += str
            }
        }

        updateValuesList1(v)
        etAddToListX.text.clear()
    }

    private fun addToList2(v: View) {
        if (!v.isClickable) return

        val etAddToListYStr = etAddToListY.text.toString().lowercase().trim()
        if (etAddToListYStr.isEmpty()) {
            tstEmpty.cancel()
            tstListEmpty.cancel()
            tstListNotMatch.cancel()
            tstListNotMatchUnique.cancel()
            tstEmpty.show()
            return
        }
        tstEmpty.cancel()

        if (idTema == 11) {
            numValuesListY += strToBigDecimal(etAddToListYStr)
        } else if (idTema == 12) {
            for (str in etAddToListYStr.split(' ')) {
                strValuesListY += str
            }
        }

        updateValuesList2(v)
        etAddToListY.text.clear()
    }

    private fun removeLastFromList1(v: View) {
        if (!v.isClickable) return

        if (idTema == 11) {
            if (numValuesListX.isNotEmpty()) numValuesListX.removeLast()
        } else if (idTema == 12) {
            if (strValuesListX.isNotEmpty()) strValuesListX.removeLast()
        }
        updateValuesList1(v)
    }

    private fun removeLastFromList2(v: View) {
        if (!v.isClickable) return

        if (idTema == 11) {
            if (numValuesListY.isNotEmpty()) numValuesListY.removeLast()
        } else if (idTema == 12) {
            if (strValuesListY.isNotEmpty()) strValuesListY.removeLast()
        }
        updateValuesList2(v)
    }

    private fun updateValuesList1(v: View) {
        if (!v.isClickable) return

        if (idTema == 11) {
            valuesStrX = numValuesListX.joinToString(" ")
        } else if (idTema == 12) {
            valuesStrX = strValuesListX.joinToString(" ")
        }
        tvNumberListX.text = valuesStrX
    }

    private fun updateValuesList2(v: View) {
        if (!v.isClickable) return

        if (idTema == 11) {
            valuesStrY = numValuesListY.joinToString(" ")
        } else if (idTema == 12) {
            valuesStrY = strValuesListY.joinToString(" ")
        }
        tvNumberListY.text = valuesStrY
    }

    private fun clearList1(v: View) {
        if (!v.isClickable) return

        numValuesListX = mutableListOf()
        strValuesListX = mutableListOf()
        valuesStrX = ""

        etAddToListX.setText("")
        tvNumberListX.text = ""

        etAddToListX.clearFocus()
    }

    private fun clearList2(v: View) {
        if (!v.isClickable) return

        numValuesListY = mutableListOf()
        strValuesListY = mutableListOf()
        valuesStrY = ""

        etAddToListY.setText("")
        tvNumberListY.text = ""

        etAddToListY.clearFocus()
    }
}