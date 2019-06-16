package br.com.iresult.gmfmobile.repository

import br.com.iresult.gmfmobile.model.dao.OcorrenciaLeituraDao
import br.com.iresult.gmfmobile.model.dao.RoteiroDao
import br.com.iresult.gmfmobile.model.dao.ServicoDao
import br.com.iresult.gmfmobile.model.database.AppDataBase

class LeituraRepository(private val roteiroDao: RoteiroDao,
                        private val leituraDao: OcorrenciaLeituraDao,
                        private val servicoDao: ServicoDao,
                        val dataBase: AppDataBase)