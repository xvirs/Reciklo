//
//  HoleView.swift
//  LoginR
//
//  Created by Artekium on 17/04/2024.
//

import SwiftUI
import Shared


struct CustomTabBarView: View {
    @State private var selectedView = 1
    
    var body: some View {
        VStack {
            switch selectedView {
            case 1:
                HomeView(selectedView: $selectedView)
            case 2:
                AddProductView()
            case 3:
                StockView()
            default:
                Text("Selección no válida")
            }
            HStack {
                Button(action: {
                    self.selectedView = 1
                }) {
                    VStack {
                        Image(.homeIcon)
                        Text("Home")
                            .foregroundColor(.black)
                        
                    }
                }
                Spacer()
                
                Button(action: {
                    self.selectedView = 2
                }) {
                    VStack {
                        Image(.addProductIcon)
                        Text("Carga")
                            .foregroundColor(.black)
                    }
                }
                Spacer()
                
                Button(action: {
                    self.selectedView = 3
                }) {
                    VStack {
                        Image(.inboxIcon)
                        Text("Stock")
                            .foregroundColor(.black)
                    }
                }
            }
            .padding()
            .background(Color(.greenBackgroundReciklo)) 
        }
    }
}

