package com.anvipus.explore.di.module

import com.anvipus.explore.ui.xml.MainFragment
import com.anvipus.explore.ui.xml.SearchListFragment
import com.anvipus.explore.ui.compose.UserDetailComposeFragment
import com.anvipus.explore.ui.xml.UserDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun main(): MainFragment

    @ContributesAndroidInjector
    abstract fun detail(): UserDetailFragment

    @ContributesAndroidInjector
    abstract fun detailCompose(): UserDetailComposeFragment

    @ContributesAndroidInjector
    abstract fun search(): SearchListFragment

}