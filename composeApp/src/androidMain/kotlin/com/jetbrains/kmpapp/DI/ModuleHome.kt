package com.jetbrains.kmpapp.DI

import com.jetbrains.kmpapp.data.datasource.local.ListProductDataSourceLocal
import com.jetbrains.kmpapp.data.datasource.remote.LatestProductSavedDataSource
import com.jetbrains.kmpapp.data.datasource.remote.ProductPostDataSourceRemote
import com.jetbrains.kmpapp.data.datasource.remote.ProfileDataDataSourceImpl
import com.jetbrains.kmpapp.data.interfaces.LatestProductSavedDataSourceInterface
import com.jetbrains.kmpapp.data.interfaces.ProfileDataSourceInterface
import com.jetbrains.kmpapp.data.repository.LastProductsSavedRepository
import com.jetbrains.kmpapp.data.repository.ProductRepository
import com.jetbrains.kmpapp.data.repository.ProfileDataRepositoryImpl
import com.jetbrains.kmpapp.domain.interfaces.LastProductsSavedRepositoryInterface
import com.jetbrains.kmpapp.domain.interfaces.ProfileDataRepositoryInterface
import com.jetbrains.kmpapp.domain.usecase.LastProductsSavedUseCase
import com.jetbrains.kmpapp.domain.usecase.ProductUseCase
import com.jetbrains.kmpapp.domain.usecase.ProfileDataUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import com.jetbrains.kmpapp.screens.home.HomeViewModel
import com.jetbrains.kmpapp.screens.stock.StockViewModel
import com.jetbrains.kmpapp.utils.clearsession.CleanSessionUseCase
import org.koin.dsl.module


val moduleHome = module {

    //latestProductSaved
    single<LatestProductSavedDataSourceInterface>{ LatestProductSavedDataSource() }
    single<LastProductsSavedRepositoryInterface>{ LastProductsSavedRepository(get()) }
    single { LastProductsSavedUseCase(get()) }

    //profileData
    single<ProfileDataSourceInterface>{ ProfileDataDataSourceImpl() }
    single<ProfileDataRepositoryInterface>{ ProfileDataRepositoryImpl(get()) }
    single { ProfileDataUseCase(get()) }

    //add product

    single { ListProductDataSourceLocal( get() ) }
    single { ProductPostDataSourceRemote() }
    single { ProductRepository(get<ListProductDataSourceLocal>(), get<ProductPostDataSourceRemote>()) }
    single { ProductUseCase(get<ProductRepository>()) }

    single { CleanSessionUseCase() }
}

val moduleHomeViewModel = module {
    factory{HomeViewModel(get(), get(), get(), get())}
    viewModelOf(::StockViewModel)
}