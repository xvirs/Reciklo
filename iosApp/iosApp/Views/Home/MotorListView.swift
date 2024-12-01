//
//  MotorListView.swift
//  iosApp
//
//  Created by Artekium on 25/04/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct MotorListView: View {
    enum SyncStatus {
        case unsynced, synced
    }
    
    @State private var selectedStatus: SyncStatus = .unsynced
    @EnvironmentObject var productData: ProductData
    
    var body: some View {
        VStack {
            Picker("Sincronización", selection: $selectedStatus) {
                Text("Sin sincronizar").tag(SyncStatus.unsynced)
                Text("Sincronizado").tag(SyncStatus.synced)
            }
            .pickerStyle(SegmentedPickerStyle())
            .padding()
            
            ScrollView {
                VStack {
                    if selectedStatus == .unsynced && !productData.productName.isEmpty {
                        MotorItemView(productData: productData)
                    } else if selectedStatus == .unsynced {
                        Text("Aún no se han cargado productos.")
                    } else {
                        Text("No hay elementos sincronizados.")
                    }
                }
            }
        }
    }
}


struct MotorListView_Previews: PreviewProvider {
    static var previews: some View {
        let productData = ProductData()
        MotorListView()
            .environmentObject(productData)
    }
}

#Preview {
    MotorListView()
}
