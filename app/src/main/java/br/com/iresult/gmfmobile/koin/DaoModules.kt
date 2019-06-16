package br.com.iresult.gmfmobile.koin

import br.com.iresult.gmfmobile.model.database.AppDataBase

object DaoModules {

    fun provideCategoriaDao(db: AppDataBase) = db.categoriaDao()

    fun provideUsuarioDao(db: AppDataBase) = db.usuarioDao()

    fun provideEstatisticaDao(db: AppDataBase) = db.estatisticaDao()

    fun provideRoteiroDao(db: AppDataBase) = db.roteiroDao()

    fun provideRuaDao(db: AppDataBase) = db.ruaDao()

    fun provideLigacaoDao(db: AppDataBase) = db.ligacaoDao()

    fun provideServicoDao(db: AppDataBase) = db.servicoDao()

    fun provideRubricaDao(db: AppDataBase) = db.rubricaDao()

    fun provideOccorenciaLeituraDao(db: AppDataBase) = db.ocorrenciaLeituraDao()

    fun provideFaixaDao(db: AppDataBase) = db.faixaDao()

    fun provideParametroImpressaoDao(db: AppDataBase) = db.parametroImpressaoDao()

    fun provideLeituraDao(db: AppDataBase) = db.leituraDao()

    fun provideParametroDao(db: AppDataBase) = db.parametroDao()

    fun provideTarifaDao(db: AppDataBase) = db.tarifaDao()

    fun provideDominioDao(db: AppDataBase) = db.dominioDao()





}