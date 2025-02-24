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

        val sqlSelect = arrayOf("id", "nombre", "nombre_en", "mostrar")
        val sqlTable = "unidades"

        qb.tables = sqlTable
        val c : Cursor = qb.query(db, sqlSelect, null, null, null, null, null)
        if (c.count > 0) c.moveToFirst()
        return c
    }

    fun getTemasByUnidad(unidad : Int) : Cursor {
        val db = readableDatabase
        val qb = SQLiteQueryBuilder()

        val sqlSelect = arrayOf("id", "nombre", "nombre_en", "html", "mostrar")
        val sqlTable = "temas"

        qb.tables = sqlTable
        val c : Cursor = qb.query(db, sqlSelect, "unidad = ?", arrayOf("$unidad"), null, null, null)
        if (c.count > 0) c.moveToFirst()
        return c
    }

    fun getSubtemasByTema(tema : Int) : Cursor {
        val db = readableDatabase
        val qb = SQLiteQueryBuilder()

        val sqlSelect = arrayOf("id", "nombre", "nombre_en", "mostrar")
        val sqlTable = "subtemas"

        qb.tables = sqlTable
        val c : Cursor = qb.query(db, sqlSelect, "tema = ?", arrayOf("$tema"), null, null, null)
        if (c.count > 0) c.moveToFirst()
        return c
    }

    fun getSubsubtemasBySubtema(subtema : Int) : Cursor {
        val db = readableDatabase
        val qb = SQLiteQueryBuilder()

        val sqlSelect = arrayOf("id", "nombre", "nombre_en", "mostrar")
        val sqlTable = "subsubtemas"

        qb.tables = sqlTable
        val c : Cursor = qb.query(db, sqlSelect, "subtema = ?", arrayOf("$subtema"), null, null, null)
        if (c.count > 0) c.moveToFirst()
        return c
    }

    fun getHtmlOfTema(tema : Int) : Cursor {
        val db = readableDatabase
        val qb = SQLiteQueryBuilder()

        val sqlSelect = arrayOf("html", "html_en")
        val sqlTable = "temas"

        qb.tables = sqlTable
        val c : Cursor = qb.query(db, sqlSelect, "id = ?", arrayOf("$tema"), null, null, null)
        if (c.count > 0) c.moveToFirst()
        return c
    }

    fun getHtmlOfSubtema(subtema : Int) : Cursor {
        val db = readableDatabase
        val qb = SQLiteQueryBuilder()

        val sqlSelect = arrayOf("html", "html_en")
        val sqlTable = "subtemas"

        qb.tables = sqlTable
        val c : Cursor = qb.query(db, sqlSelect, "id = ?", arrayOf("$subtema"), null, null, null)
        if (c.count > 0) c.moveToFirst()
        return c
    }

    fun getHtmlOfSubsubtema(subsubtema : Int) : Cursor {
        val db = readableDatabase
        val qb = SQLiteQueryBuilder()

        val sqlSelect = arrayOf("html", "html_en")
        val sqlTable = "subsubtemas"

        qb.tables = sqlTable
        val c : Cursor = qb.query(db, sqlSelect, "id = ?", arrayOf("$subsubtema"), null, null, null)
        if (c.count > 0) c.moveToFirst()
        return c
    }
}