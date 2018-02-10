package link.k3n.sticky_recyclerview_sample

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.squareup.leakcanary.LeakCanary
import link.k3n.sticky_recyclerview_sample.utils.CustomDebugTree
import timber.log.Timber

/**
 * Application class
 */
class StickyRecyclerViewSampleApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        // This process is dedicated to LeakCanary for heap analysis.
        // You should not init your app in this process.
        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)

        if (BuildConfig.DEBUG) Timber.plant(CustomDebugTree())

        Timber.d("onCreate")
    }
}