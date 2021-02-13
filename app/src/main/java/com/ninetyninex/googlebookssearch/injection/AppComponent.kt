package com.ninetyninex.googlebookssearch.injection

import com.ninetyninex.googlebookssearch.app.App
import com.ninetyninex.googlebookssearch.ui.main.volume.BooksVolumeActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(app: App)

    fun inject(activity: BooksVolumeActivity)
}