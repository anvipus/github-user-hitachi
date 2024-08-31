package com.anvipus.explore.di.module

import com.anvipus.explore.ui.xml.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun main(): MainFragment

}