package com.example.myapplication.data.dal

import android.content.ContentValues
import android.content.Context
import com.example.myapplication.data.Usuario
import com.example.myapplication.data.local.AppDatabase

internal class SenhaSalva(
    val hash: ByteArray,
    val salt: ByteArray,
    val usuarioId: Long,
    val nome: String
)

internal class UsuarioDAO(context: Context) {
    private val appContext = context.applicationContext

    fun cadastrar(nome: String, email: String, senha: ByteArray, salt: ByteArray) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("nome", nome)
                put("email", email)
                put("senha", senha)
                put("salt", salt)
            }
            db.writableDatabase.insertOrThrow("usuarios", null, dados)
        }
    }

    fun buscarSenha(email: String): SenhaSalva? {
        return AppDatabase(appContext).use { db ->
            db.readableDatabase.query(
                "usuarios",
                arrayOf("senha", "salt", "id", "nome"),
                "email = ?",
                arrayOf(email),
                null, null, null
            ).use { cursor ->
                if (cursor.moveToFirst()) SenhaSalva(
                    cursor.getBlob(0),
                    cursor.getBlob(1),
                    cursor.getLong(2),
                    cursor.getString(3)
                )
                else null
            }
        }
    }

    fun listar(): List<Usuario> = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "usuarios", arrayOf("id", "nome", "email"),
            null, null, null, null,
            "nome COLLATE NOCASE, id"
        ).use { cursor ->
            buildList {
                while (cursor.moveToNext()) {
                    add(Usuario(cursor.getLong(0), cursor.getString(1), cursor.getString(2)))
                }
            }
        }
    }

    fun buscarPorId(id: Long): Usuario? = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "usuarios", arrayOf("id", "nome", "email"),
            "id = ?", arrayOf(id.toString()),
            null, null, null
        ).use { cursor ->
            if (cursor.moveToFirst()) Usuario(
                cursor.getLong(0), cursor.getString(1), cursor.getString(2)
            )
            else null
        }
    }

    fun alterar(id: Long, nome: String, hash: ByteArray? = null, salt: ByteArray? = null) {
        AppDatabase(appContext).use { db ->
            val valores = ContentValues().apply {
                put("nome", nome)
                if (hash != null) put("senha", hash)
                if (salt != null) put("salt", salt)
            }
            val alterado = db.writableDatabase.update(
                "usuarios", valores, "id = ?", arrayOf(id.toString())
            )
            require(alterado == 1) { "Usuário não encontrado. Atualize a lista." }
        }
    }

    fun excluir(id: Long) {
        AppDatabase(appContext).use { db ->
            val excluido = db.writableDatabase.delete(
                "usuarios", "id = ?", arrayOf(id.toString())
            )
            require(excluido == 1) { "Usuário não encontrado." }
        }
    }
}
