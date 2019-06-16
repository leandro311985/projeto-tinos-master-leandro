package br.com.iresult.gmfmobile.koin

import br.com.iresult.gmfmobile.print.RW420PrintManager
import br.com.iresult.gmfmobile.print.ZebraConnection
import br.com.iresult.gmfmobile.repository.*
import br.com.iresult.gmfmobile.ui.login.LoginViewModel
import br.com.iresult.gmfmobile.ui.main.MainViewModel
import br.com.iresult.gmfmobile.ui.main.estatistica.EstatisticaViewModel
import br.com.iresult.gmfmobile.ui.main.impressao.backup.ImpressaoBackupViewModel
import br.com.iresult.gmfmobile.ui.main.impressao.ultimos.ImpressaoUltimosViewModel
import br.com.iresult.gmfmobile.ui.main.pesquisa.PesquisaViewModel
import br.com.iresult.gmfmobile.ui.main.tarefas.TarefasViewModel
import br.com.iresult.gmfmobile.ui.main.tarefas.informative.InformativosViewModel
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.RoteiroViewModel
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.finish.FinishTaskViewModel
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.map.MapsHomeViewModel
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.LeituraViewModel
import br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading.map.MapHomeViewModel
import br.com.iresult.gmfmobile.ui.splash.SplashViewModel
import br.com.iresult.gmfmobile.utils.PreferencesManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object Modules {

    private val appModules = module {
        single { ServiceModules.provideGson() }
        single { ServiceModules.provideLoginService(get()) }
        single { ServiceModules.provideGeocodeService(get()) }
        single { ServiceModules.provideDataBase(get()) }
        single { PreferencesManager(get()) }
        single { ZebraConnection() }
        single { RW420PrintManager() }
    }

    private val repositoryModules = module {
        single { LoginRepository(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
        single { UserRepository(get()) }
        single { RoteiroRepository(get(), get(), get(), get()) }
        single { PesquisaRepository(get()) }
        single { LeituraRepository(get(), get(), get(), get()) }
        single { GeocodingRepository(get()) }
    }

    private val daoModules = module {
        single { DaoModules.provideUsuarioDao(get()) }
        single { DaoModules.provideEstatisticaDao(get()) }
        single { DaoModules.provideRoteiroDao(get()) }
        single { DaoModules.provideRuaDao(get()) }
        single { DaoModules.provideLigacaoDao(get()) }
        single { DaoModules.provideServicoDao(get()) }
        single { DaoModules.provideOccorenciaLeituraDao(get()) }
        single { DaoModules.provideFaixaDao(get()) }
        single { DaoModules.provideRubricaDao(get()) }
        single { DaoModules.provideCategoriaDao(get()) }
        single { DaoModules.provideParametroImpressaoDao(get()) }
        single { DaoModules.provideLeituraDao(get()) }
        single { DaoModules.provideParametroDao(get()) }
        single { DaoModules.provideTarifaDao(get()) }
        single { DaoModules.provideDominioDao(get()) }



    }

    private val viewModelModules = module {
        viewModel { SplashViewModel(get()) }
        viewModel { LoginViewModel(get()) }
        viewModel { MainViewModel(get(), get()) }
        viewModel { TarefasViewModel(get(), get()) }
        viewModel { RoteiroViewModel(get(), get()) }
        viewModel { LeituraViewModel(get()) }
        viewModel { FinishTaskViewModel() }
        viewModel { PesquisaViewModel(get()) }
        viewModel { ImpressaoBackupViewModel() }
        viewModel { ImpressaoUltimosViewModel(get()) }
        viewModel { EstatisticaViewModel() }
        viewModel { InformativosViewModel(get()) }
        viewModel { MapsHomeViewModel(get(), get()) }
        viewModel { MapHomeViewModel(get()) }
    }

    val all: List<Module> = {
        listOf(appModules, repositoryModules, daoModules, viewModelModules)
    }()
}