package com.marcosmiranda.cursoestadisticabasica

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper

class DBHelper(
    context: Context?,
    name: String? = "CursoEstadisticaBasica.db",
    storageDirectory: String?,
    version: Int = 1
) : SQLiteAssetHelper(context, name, storageDirectory, null, version) {

    constructor(context : Context, name : String, version : Int) : this(context, name, null, version)

    fun getUnidades() : Cursor {
        val db = readableDatabase
        val qb = SQLiteQueryBuilder()

        val sqlSelect = arrayOf("idUnidad", "titulo", "show")
        val sqlTable = "unidades"

        qb.tables = sqlTable
        val c : Cursor = qb.query(db, sqlSelect, null, null, null, null, null)
        c.moveToFirst()
        return c
    }

    fun getTemasByUnidad(idUnidad : Int) : Cursor {
        val db = readableDatabase
        val qb = SQLiteQueryBuilder()

        val sqlSelect = arrayOf("idTema", "tema", "html", "show")
        val sqlTable = "temas"

        qb.tables = sqlTable
        val c : Cursor = qb.query(db, sqlSelect, "idUnidad = $idUnidad", null, null, null, null)
        c.moveToFirst()
        return c
    }

    fun getSubTemasByTema(idTema : Int) : Cursor {
        val db = readableDatabase
        val qb = SQLiteQueryBuilder()

        val sqlSelect = arrayOf("idSubTema", "subtema", "show")
        val sqlTable = "subtemas"

        qb.tables = sqlTable
        val c : Cursor = qb.query(db, sqlSelect, "idTema = $idTema", null, null, null, null)
        c.moveToFirst()
        return c
    }

    fun getTxtOfTema(idTema : Int) : Cursor {
        val db = readableDatabase
        val qb = SQLiteQueryBuilder()

        val sqlSelect = arrayOf("html")
        val sqlTable = "temas"

        qb.tables = sqlTable
        val c : Cursor = qb.query(db, sqlSelect, "idTema = $idTema", null, null, null, null)
        c.moveToFirst()
        return c
    }

    fun getTxtOfSubTema(idSubTema : Int) : Cursor {
        val db = readableDatabase
        val qb = SQLiteQueryBuilder()

        val sqlSelect = arrayOf("html")
        val sqlTable = "subtemas"

        qb.tables = sqlTable
        val c : Cursor = qb.query(db, sqlSelect, "idSubTema = $idSubTema", null, null, null, null)
        c.moveToFirst()
        return c
    }
}