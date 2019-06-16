package br.com.iresult.gmfmobile.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.iresult.gmfmobile.model.bean.*
import br.com.iresult.gmfmobile.model.converters.*
import br.com.iresult.gmfmobile.model.dao.*

/**
 * Created by victorfernandes on 20/02/19.
 */
@Database(version = 7, entities = [
    Usuario::class,
    Roteiro::class,
    Parametro::class,
    Address::class,
    Tarefa::class,
    Ligacao::class,
    Justificativa::class,
    Ocorrencia::class,
    OcorrenciaNotificacao::class,
    Rubrica::class,
    Servico::class,
    Categoria::class,
    Faixa::class,
    Tarifa::class,
    PreenchimentoEstatistica::class,
    ServicoLeitura::class,
    OcorrenciaLeitura::class,
    Coleta::class,
    Notificacao::class,
    ParametroImpressao::class,
    Dominio::class,
    Leitura::class
])
@TypeConverters(
        StringConverter::class,
        DateConverter::class,
        FileConverter::class,
        UsuarioConverter::class,
        RoteiroConverter::class,
        ParametroConverter::class,
        RuaConverter::class,
        TarefaConverter::class,
        LigacaoConverter::class,
        JustificativaConverter::class,
        OcorrenciaConverter::class,
        OcorrenciaNotificacaoConverter::class,
        RubricaConverter::class,
        ServicoConverter::class,
        CategoriaConverter::class,
        FaixaConverter::class,
        TarifaConverter::class,
        HashMapConverter::class,
        ServicoLeituraConverter::class,
        NotificacaoConverter::class,
        ColetaConverter::class,
        ParametroImpressaoConverter::class,
        OcorrenciaLeituraConverter::class,
        DominioConverter::class,
        LeituraConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun categoriaDao(): CategoriaDao
    abstract fun faixaDao(): FaixaDao
    abstract fun ocorrenciaDao(): OcorrenciaDao
    abstract fun roteiroDao(): RoteiroDao
    abstract fun ruaDao() : AddressDao
    abstract fun ligacaoDao() : LigacaoDao
    abstract fun usuarioDao() : UsuarioDao
    abstract fun estatisticaDao() : PreenchimentoEstatisticaDao
    abstract fun servicoDao() : ServicoDao
    abstract fun rubricaDao() : RubricaDao
    abstract fun ocorrenciaLeituraDao() : OcorrenciaLeituraDao
    abstract fun parametroImpressaoDao() : ParametroImpressaoDao
    abstract fun leituraDao() : LeituraDao
    abstract fun parametroDao() : ParametroDao
    abstract fun tarifaDao() : TarifaDao
    abstract fun dominioDao() : DominioDao


}