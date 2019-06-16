package br.com.iresult.gmfmobile

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import br.com.iresult.gmfmobile.koin.Modules
import br.com.iresult.gmfmobile.model.database.AppDataBase
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


/**
 * Created by victorfernandes on 14/10/18.
 */
class GmfTinosApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        startKoin {
            androidContext(this@GmfTinosApplication)
            modules(Modules.all)
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}