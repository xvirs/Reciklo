package com.jetbrains.kmpapp.DI

import com.jetbrains.kmpapp.data.datasource.local.CategoryProductDataSourceLocal
import com.jetbrains.kmpapp.data.datasource.local.ListProductDataSourceLocal
import com.jetbrains.kmpapp.data.datasource.local.ModelCarDataSourceLocal
import com.jetbrains.kmpapp.data.datasource.local.TypeProductDataSourceLocal
import com.jetbrains.kmpapp.data.datasource.remote.CategoryDataSourceRemote
import com.jetbrains.kmpapp.data.datasource.remote.DataVehicleCompleteDataSourceRemoteImpl
import com.jetbrains.kmpapp.data.datasource.remote.ModelCarDataSourceRemote
import com.jetbrains.kmpapp.data.datasource.remote.ProductPostDataSourceRemote
import com.jetbrains.kmpapp.data.datasource.remote.RudacDataSourceRemote
import com.jetbrains.kmpapp.data.datasource.remote.TypeProductDataSourceRemote
import com.jetbrains.kmpapp.data.datasource.remote.VehicleCompleteLoginDataSourceRemoteImpl
import com.jetbrains.kmpapp.data.datasource.remote.VehicleModelDataSourceRemote
import com.jetbrains.kmpapp.data.datasource.remote.VehicleVersionDataSourceRemote
import com.jetbrains.kmpapp.data.interfaces.DataVehicleCompleteDataSourceInterface
import com.jetbrains.kmpapp.data.interfaces.VehicleCompleteLoginDataSourceInterface
import com.jetbrains.kmpapp.data.repository.CategoryRepository
import com.jetbrains.kmpapp.data.repository.DataVehicleCompleteRepositoryImpl
import com.jetbrains.kmpapp.data.repository.ModelCarRepository
import com.jetbrains.kmpapp.data.repository.ProductRepository
import com.jetbrains.kmpapp.data.repository.RudacRepository
import com.jetbrains.kmpapp.data.repository.TypeProductRepository
import com.jetbrains.kmpapp.data.repository.VehicleCompleteLoginRepositoryImpl
import com.jetbrains.kmpapp.data.repository.VehicleModelRepository
import com.jetbrains.kmpapp.data.repository.VehicleVersionRepository
import com.jetbrains.kmpapp.domain.interfaces.DataVehicleCompleteRepositoryInterface
import com.jetbrains.kmpapp.domain.interfaces.VehicleCompleteLoginRepositoryInterface
import com.jetbrains.kmpapp.domain.usecase.CategoryUseCase
import com.jetbrains.kmpapp.domain.usecase.DataVehicleCompleteUseCase
import com.jetbrains.kmpapp.domain.usecase.ModelCarUseCase
import com.jetbrains.kmpapp.domain.usecase.ProductUseCase
import com.jetbrains.kmpapp.domain.usecase.RudacUseCase
import com.jetbrains.kmpapp.domain.usecase.TypeProductUseCase
import com.jetbrains.kmpapp.domain.usecase.VehicleCompleteLoginUseCase
import com.jetbrains.kmpapp.domain.usecase.VehicleModelUseCase
import com.jetbrains.kmpapp.domain.usecase.VehicleVersionUseCase
import org.koin.dsl.module
import com.jetbrains.kmpapp.screens.addProduct.AddProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModel

val moduleAddProduct = module {

    //intancia de data sources que utilicen la db
    single { ListProductDataSourceLocal(get()) }
    single { TypeProductDataSourceLocal(get()) }
    single { CategoryProductDataSourceLocal(get()) }

    single { RudacDataSourceRemote() }
    single { RudacRepository(get<RudacDataSourceRemote>()) }
    single { RudacUseCase(get<RudacRepository>()) }

    single { CategoryDataSourceRemote() }
    single {
        CategoryRepository(
            get<CategoryDataSourceRemote>(),
            get<CategoryProductDataSourceLocal>()
        )
    }
    single { CategoryUseCase(get<CategoryRepository>()) }

    single { TypeProductDataSourceRemote() }
    single {
        TypeProductRepository(
            get<TypeProductDataSourceRemote>(),
            get<TypeProductDataSourceLocal>()
        )
    }
    single { TypeProductUseCase(get<TypeProductRepository>()) }

    single { ListProductDataSourceLocal( get() ) }
    single { ProductPostDataSourceRemote() }
    single { ProductRepository(get<ListProductDataSourceLocal>(), get<ProductPostDataSourceRemote>()) }
    single { ProductUseCase(get<ProductRepository>()) }

    single { ModelCarDataSourceRemote() }
    single { ModelCarDataSourceLocal(get()) }
    single { ModelCarRepository(get<ModelCarDataSourceRemote>(), get<ModelCarDataSourceLocal>()) }
    single { ModelCarUseCase(get<ModelCarRepository>()) }

    single { VehicleModelDataSourceRemote() }
    single { VehicleModelRepository(get<VehicleModelDataSourceRemote>()) }
    single { VehicleModelUseCase(get<VehicleModelRepository>()) }

    single { VehicleVersionDataSourceRemote() }
    single { VehicleVersionRepository(get<VehicleVersionDataSourceRemote>()) }
    single { VehicleVersionUseCase(get<VehicleVersionRepository>()) }


    //VehicleCompleteData
    single<DataVehicleCompleteDataSourceInterface>{ DataVehicleCompleteDataSourceRemoteImpl() }
    single<DataVehicleCompleteRepositoryInterface>{ DataVehicleCompleteRepositoryImpl(get()) }
    single { DataVehicleCompleteUseCase(get()) }

    single<VehicleCompleteLoginDataSourceInterface>{ VehicleCompleteLoginDataSourceRemoteImpl() }
    single<VehicleCompleteLoginRepositoryInterface>{ VehicleCompleteLoginRepositoryImpl(get()) }
    single { VehicleCompleteLoginUseCase(get()) }

}

val moduleAddProductViewModel = module {
     viewModel{AddProductViewModel(get(), get(), get(), get(), get(), get(),get(), get(),get())}
}