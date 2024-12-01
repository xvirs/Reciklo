//
//  LoginView.swift
//  iosApp
//
//  Created by Artekium on 19/04/2024.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import Shared

struct LoginView: View {


    
    private let email = "example@email.com"
    private let password = "password123"
    @State private var showGreeting = true

    var body: some View {
            ZStack {
                Color(.greenBackgroundReciklo).ignoresSafeArea()
                VStack {
                    Spacer()
                    Image(.logoIcon)
                    Spacer()
                    VStack(spacing: 20) {
                        TextField("Email", text: .constant(email))
                            .padding()
                            .overlay(
                                RoundedRectangle(cornerRadius:2).stroke(Color.black)
                            )

                        SecureField("Password", text: .constant(password))
                            .padding()
                            .overlay(
                                RoundedRectangle(cornerRadius: 2).stroke(Color.black)
                            )
                    }
                    Spacer()

                    NavigationLink(destination:CustomTabBarView()) {
                        Text("Acceder")
                            .foregroundColor(.white)
                            .padding(5)
                    }.recikloGreenButton()
                    Spacer()
                    Text("¿Olvidaste la contraseña?").multilineTextAlignment(.center)
                        .padding(.bottom)
                    Text("Contactanos a soporte@reciklo.com.ar o a 11569649 para recuperarla").multilineTextAlignment(.center)
                    Spacer()
                }.padding(18)
            }.navigationBarHidden(true)
        }
}


struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView()
    }
}
