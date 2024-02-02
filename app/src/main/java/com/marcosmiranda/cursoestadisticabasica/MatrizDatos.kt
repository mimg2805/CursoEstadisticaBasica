package com.marcosmiranda.cursoestadisticabasica

import android.content.Intent
import android.os.Bundle
import android.text.method.DigitsKeyListener
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import com.marcosmiranda.cursoestadisticabasica.MathHelper.Companion.strToBigDecimal
import java.math.BigDecimal


class MatrizDatos : AppCompatActivity() {

    private var idTema = 0
    private var subtemaTitle = ""

    private var values = mutableListOf<BigDecimal>()
    private var valuesStr = ""

    private lateinit var etAddToList: EditText
    private lateinit var tvNumberList: TextView
    private lateinit var btnClear: Button
    private lateinit var btnCalc: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matriz_datos)

        etAddToList = findViewById(R.id.et_add_to_list)

        idTema = intent.getIntExtra("idTema", 0)
        subtemaTitle = intent.getStringExtra("title") ?: ""

        tvNumberList = findViewById(R.id.tv_number_list)
        btnClear = findViewById(R.id.btn_clear)
        btnCalc = findViewById(R.id.btn_calc)

        etAddToList.setOnEditorActionListener { v, _, _ ->
            addToList(v)
            false
        }

        btnClear.setOnClickListener {
            clear(it)
        }

        btnCalc.setOnClickListener {
            if (values.size == 0) {
                return@setOnClickListener
            }

            intent = Intent(this, Tema::class.java)
            intent.putExtra("idTema", idTema)
            intent.putExtra("title", subtemaTitle)
            intent.putExtra("values", valuesStr)
            this.startActivity(intent)
        }
    }

    private fun addToList(view: View) {
        if (!view.isClickable) return

        values += strToBigDecimal(etAddToList.text.trim().toString())

        valuesStr = values.joinToString(" ")
        tvNumberList.text = valuesStr.replace(".0", "")
        etAddToList.text.clear()
    }

    fun clear(view: View) {
        if (!view.isClickable) return

        values = mutableListOf()
        valuesStr = ""

        etAddToList.setText("")
        tvNumberList.text = ""

        etAddToList.clearFocus()
    }
}