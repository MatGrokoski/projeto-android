package com.example.myapplication.data.dal

import android.content.ContentValues
import android.content.Context
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.model.Transacao

internal class TransacaoDAO(context: Context) {
    private val appContext = context.applicationContext

    fun inserir(descricao: String, categoria: String, data: String, valor: Double, tipo: String): Long {
        return AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("descricao", descricao)
                put("categoria", categoria)
                put("data", data)
                put("valor", valor)
                put("tipo", tipo)
            }
            db.writableDatabase.insertOrThrow("transacoes", null, dados)
        }
    }

    fun listar(): List<Transacao> = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "transacoes",
            arrayOf("id", "descricao", "categoria", "data", "valor", "tipo"),
            null, null, null, null,
            "data DESC, id DESC"
        ).use { cursor ->
            buildList {
                while (cursor.moveToNext()) {
                    add(
                        Transacao(
                            cursor.getLong(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getDouble(4),
                            cursor.getString(5)
                        )
                    )
                }
            }
        }
    }

    fun buscarPorId(id: Long): Transacao? = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "transacoes",
            arrayOf("id", "descricao", "categoria", "data", "valor", "tipo"),
            "id = ?", arrayOf(id.toString()),
            null, null, null
        ).use { cursor ->
            if (cursor.moveToFirst()) Transacao(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getDouble(4),
                cursor.getString(5)
            )
            else null
        }
    }

    fun alterar(id: Long, descricao: String, categoria: String, data: String, valor: Double, tipo: String) {
        AppDatabase(appContext).use { db ->
            val valores = ContentValues().apply {
                put("descricao", descricao)
                put("categoria", categoria)
                put("data", data)
                put("valor", valor)
                put("tipo", tipo)
            }
            val alterado = db.writableDatabase.update(
                "transacoes", valores, "id = ?", arrayOf(id.toString())
            )
            require(alterado == 1) { "Transação não encontrada." }
        }
    }

    fun excluir(id: Long) {
        AppDatabase(appContext).use { db ->
            val excluido = db.writableDatabase.delete(
                "transacoes", "id = ?", arrayOf(id.toString())
            )
            require(excluido == 1) { "Transação não encontrada." }
        }
    }
}
