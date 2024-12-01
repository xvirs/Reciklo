//
//  AccountView.swift
//  LoginR
//
//  Created by Artekium on 17/04/2024.
//

import SwiftUI
import Shared

struct StockView : View {
    var body: some View {
        TopBar(buttonText: "", buttonIcon: "person", action: {print("hola")},iconOpacity: 0)
        MotorListView()
            .navigationBarBackButtonHidden(true)
    }
}

#Preview {
    StockView()
}
