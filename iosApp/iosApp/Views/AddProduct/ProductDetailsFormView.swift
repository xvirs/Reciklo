//
//  form.swift
//  iosApp
//
//  Created by Artekium on 07/05/2024.
//  Copyright © 2024 orgName. All rights reserved.
//
//
import SwiftUI
import Shared

struct ProductDetailsFormView: View {
    @EnvironmentObject var productData: ProductData
    @State private var showingDefectForm = false
    @State private var navigateToSummary = false // Estado para la navegación a SummaryView
    @State private var selectedImage: UIImage?
    @State private var defectDescription: String = ""


    var body: some View {
        ScrollView {
                VStack(alignment: .leading) {
                    Text("Detalles del Producto")
                        .font(.title)
                    TextField("Número de Rudac", text: $productData.productCode)
                        .underlineTextField()
                    TextField("Nombre del Producto (Título de Publicación)", text: $productData.productName)
                        .underlineTextField()
                    TextField("Número de Chasis", text: $productData.chassisNumber)
                        .underlineTextField()

                    selectionMenu(label: "Marca: \(productData.selectedBrand)", options: productData.brands) {
                        productData.selectedBrand = $0
                    }
                    TextField("Año", text: $productData.productYear)
                        .underlineTextField()
                    selectionMenu(label: "Modelo: \(productData.selectedModel)", options: productData.models) {
                        productData.selectedModel = $0
                    }
                    selectionMenu(label: "Versión: \(productData.selectedVersion)", options: productData.versions) {
                        productData.selectedVersion = $0
                    }
                    selectionMenu(label: "Categoría: \(productData.selectedCategory)", options: productData.productCategories) {
                        productData.selectedCategory = $0
                    }
                    selectionMenu(label: "Tipo de Producto: \(productData.selectedProductType)", options: productData.productTypes) {
                        productData.selectedProductType = $0
                    }

                    HStack {
                        TextField("Precio", text: $productData.productPrice)
                            .underlineTextField()
                        TextField("Cantidad", text: $productData.productQuantity)
                            .underlineTextField()
                    }
                    HStack {
                        TextField("Altura", text: $productData.productHeight)
                            .underlineTextField()
                        TextField("Ancho", text: $productData.productWidth)
                            .underlineTextField()
                    }
                    HStack {
                        TextField("Largo", text: $productData.productLength)
                            .underlineTextField()
                        TextField("Peso", text: $productData.productWeight)
                            .underlineTextField()
                    }
                    TextField("Descripción del producto", text: $productData.productDescription, axis: .vertical)
                        .padding()
                        .lineLimit(5...10)
                        .border(Color.black)

                    Text("Deméritos").font(.title3)
                    HStack {
                        Text("Carga un defecto del producto")
                        Spacer()
                        Button(action: {
                            showingDefectForm = true
                        }) {
                            Image(.addIcon)
                                .font(.body)
                                .multilineTextAlignment(.center)
                                .foregroundColor(.white)
                                .frame(width: 40, height: 40)
                                .background(Color(.darkGreenReciklo))
                                .cornerRadius(100)
                        }
                    }
                }.padding()
            }
            .sheet(isPresented: $showingDefectForm) {
                DefectFormView(showingForm: $showingDefectForm, navigateToSummary: $navigateToSummary).presentationDetents([.medium])
            }
            .background(
                NavigationLink(destination: ProductSummaryView(productData: _productData), isActive: $navigateToSummary) {
                    EmptyView()
                }
            )

            Spacer()
    }

    @ViewBuilder
    private func selectionMenu(label: String, options: [String], selection: @escaping (String) -> Void) -> some View {
        Menu {
            ForEach(options, id: \.self) { option in
                Button(option) {
                    selection(option)
                }
            }
        } label: {
            HStack {
                Text(label)
                    .foregroundColor(.black)
                Spacer()
                Image(systemName: "chevron.down")
                    .foregroundColor(.black)
            }
           
            .frame(height: 44)
        }
    }
}

extension View {
    func underlineTextField() -> some View {
        HStack {
            self
            Spacer()
        }
        .frame(minWidth: 0, maxWidth: .infinity, alignment: .leading)
        .overlay(
            Rectangle()
                .frame(height: 1)
                .offset(y: 8),
            alignment: .bottom
        )
        .padding(.vertical)
    }
}




