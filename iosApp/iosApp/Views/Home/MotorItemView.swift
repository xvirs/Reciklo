//
//  MotorItemView.swift
//  iosApp
//
//  Created by Artekium on 25/04/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct MotorItemView: View {
    @ObservedObject var productData: ProductData 
    
    var body: some View {
        VStack {
            HStack {
                Image(systemName: "car.fill")
                    .padding()
                
                VStack(alignment: .leading) {
                    Text("Motor \(productData.selectedBrand)")
                        .fontWeight(.semibold)
                    Text("Modelo: \(productData.selectedModel)")
                    Text("Año: \(productData.productYear)")
                }.padding()
                
                VStack(alignment: .leading) {
                    Text("Cantidad: \(productData.productQuantity)")
                    Text("Precio: $ \(productData.productPrice)")
                }
            }
            Divider()
        }
    }
}


