//
//  HomeView.swift
//  iosApp
//
//  Created by Artekium on 22/04/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct HomeView: View {
    @Binding var selectedView: Int
    
    var body: some View {
        VStack {
            TopBar(buttonText: "", buttonIcon: "person", action: {print("hola")})
            Spacer()
            VStack(alignment: .leading) {
                Text("Hola Alex!")
                    .font(.title)
                    .padding(.bottom)
                Text("Comenzá una nueva carga de stock de productos")
                    .font(.subheadline)
                    .padding(.bottom)
                HStack {
                    Button(action: {
                        self.selectedView = 2
                    }) {
                        HStack {
                            Image(.addIcon)
                            Text("Cargar")
                                .font(.body)
                                .multilineTextAlignment(.center)
                                .foregroundColor(.white)
                        }
                        .frame(width: 120, height: 40)
                        .background(Color(.darkGreenReciklo))
                        .cornerRadius(100)
                        Spacer()
                    }
                }
                Text("Últimos guardados")
                    .padding(.top)
                    .font(.title2)
                HStack{
                    Image(.calendarIcon)
                    Text("Última sincronización 02/11/2000")
                        .font(.subheadline)
                }.padding(.top)
            }.padding(15)
            MotorListView()
        }.navigationBarHidden(true)
    }
}

#Preview {
    HomeView(selectedView: .constant(1))
}
