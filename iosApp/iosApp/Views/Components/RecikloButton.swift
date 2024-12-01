//
//  RecikloButton.swift
//  iosApp
//
//  Created by Artekium on 07/05/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct StyledButton: View {
    var text: String
    var action: () -> Void
    var color: Color = .blue

    var body: some View {
        Button(action: action) {
            Text(text)
                .font(.body)
                .foregroundColor(.white)
                .frame(width: 120, height: 40)
                .background(color)
                .cornerRadius(100)
        }
    }
}

