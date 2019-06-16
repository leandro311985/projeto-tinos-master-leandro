package br.com.iresult.gmfmobile.repository

import android.util.Log
import br.com.iresult.gmfmobile.model.bean.Usuario
import br.com.iresult.gmfmobile.model.dao.UsuarioDao
import br.com.iresult.gmfmobile.utils.defaultScheduler
import io.reactivex.Maybe
import io.reactivex.Observable
import org.jetbrains.anko.doAsync

class UserRepository(private val usuarioDao: UsuarioDao) {

    fun getUser(): Maybe<List<Usuario>> {
        return usuarioDao.getUsuario().defaultScheduler()
    }
}