//
//  SwiftUIView.swift
//  iosApp
//
//  Created by Artekium on 18/05/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct ProductSummaryView: View {
    @Environment(\.presentationMode) var presentationMode
    @EnvironmentObject var productData: ProductData  
    @State private var navigateToHome = false

    var body: some View {
        VStack {
            TopBar(buttonText: "", buttonIcon: "chevron.backward", action: {self.presentationMode.wrappedValue.dismiss()},iconOpacity: 1)
            VStack(alignment: .leading) {
                ScrollView {
                    ForEach(productData.images, id: \.self) { img in
                        Image(uiImage: img)
                            .resizable()
                            .scaledToFit()
                            .frame(width: 100, height: 100)
                            .clipShape(RoundedRectangle(cornerRadius: 10))
                            .padding(8)
                    }
                    Text("Detalles del Producto")
                        .font(.title)
                    
                    Text("Número de Rudac: \(productData.productCode)")
                        .underlineTextField()
                    
                    Text("Nombre del Producto: \(productData.productName)")
                        .underlineTextField()
                    
                    Text("Número de Chasis: \(productData.chassisNumber)")
                        .underlineTextField()
                    
                    Text("Marca: \(productData.selectedBrand)")
                        .underlineTextField()
                    
                    Text("Año: \(productData.productYear)")
                        .underlineTextField()
                    
                    Text("Modelo: \(productData.selectedModel)")
                        .underlineTextField()
                    
                    Text("Versión: \(productData.selectedVersion)")
                        .underlineTextField()
                    
                    Text("Categoría: \(productData.selectedCategory)")
                        .underlineTextField()
                    
                    Text("Tipo de Producto: \(productData.selectedProductType)")
                        .underlineTextField()
                    
                    HStack {
                        Text("Precio: \(productData.productPrice)")
                            .underlineTextField()
                        Spacer()
                        Text("Cantidad: \(productData.productQuantity)")
                            .underlineTextField()
                    }
                    
                    HStack {
                        Text("Altura: \(productData.productHeight)")
                            .underlineTextField()
                        Spacer()
                        Text("Ancho: \(productData.productWidth)")
                            .underlineTextField()
                    }
                    
                    HStack {
                        Text("Largo: \(productData.productLength)")
                            .underlineTextField()
                        Spacer()
                        Text("Peso: \(productData.productWeight)")
                            .underlineTextField()
                    }
                    
                    Text("Descripción del Defecto:")
                        .font(.title2)
                    
                    Text(productData.productDescription)
                    
                    ForEach(productData.defects) { defect in
                        VStack(alignment: .leading) {
                            Text(defect.description)
                                .font(.headline)
                            Image(uiImage: defect.image)
                                .resizable()
                                .scaledToFit()
                                .frame(width: 100, height: 100)
                                .clipShape(RoundedRectangle(cornerRadius: 10))
                                .padding(4)
                        }
                        .padding()
                        .background(Color.gray.opacity(0.1))
                        .cornerRadius(10)
                        .padding(.bottom, 10)
                    }
                    Button("Continuar a Inicio") {
                                  navigateToHome = true
                              }
                              NavigationLink(destination: CustomTabBarView(), isActive: $navigateToHome) {
                                  EmptyView()
                              }
                          
                }
            }
            .padding(.horizontal)
        }
        .navigationBarBackButtonHidden(true)
    }
}

struct ProductSummaryView_Previews: PreviewProvider {
    static var previews: some View {
        ProductSummaryView().environmentObject(ProductData())
    }
}
