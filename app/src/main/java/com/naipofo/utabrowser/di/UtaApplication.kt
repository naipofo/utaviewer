package com.naipofo.utabrowser.di

import android.app.Application
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule

class UtaApplication: Application(), DIAware {
    override val di by DI.lazy {
        import(androidXModule(this@UtaApplication))
        import(mainModule)
    }
}