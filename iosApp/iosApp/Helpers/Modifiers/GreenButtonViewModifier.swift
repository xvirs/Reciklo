//
//  GreenBottonViewModifier.swift
//  LoginR
//
//  Created by Artekium on 11/04/2024.
//

import SwiftUI
import Shared

struct GreenButtonViewModifier: ViewModifier {
    func body(content: Content) -> some View {
        content
            .font(.body)
            .fontWeight(.medium)
            .foregroundColor(Color(.whiteReciklo))
            .frame(maxWidth: .infinity)
            .padding()
            .background(Color(.darkGreenReciklo))
            .clipShape(Capsule())
    }
}
