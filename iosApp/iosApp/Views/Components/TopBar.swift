//
//  SwiftUIView.swift
//  iosApp
//
//  Created by Artekium on 23/04/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct TopBar: View {
    var buttonText: String
    var buttonIcon: String?
    var action: () -> Void
    var iconOpacity: Double = 1.0

    var body: some View {
        HStack {
            
            Button(action: action) {
                if let icon = buttonIcon {
                    HStack(spacing: 8) {
                        Image(systemName: icon)
                            .imageScale(.large)
                            .foregroundColor(.black)
                            .opacity(iconOpacity)
                        Text(buttonText)
                    }}
            }
            Spacer()
            
            Image(.logoIcon)
                .resizable()
                .scaledToFit()
            
            Spacer()
            
            Image(.userIcon)
                .opacity(0)
        }
        .frame(height: 40)
        .padding()
        .background(Color("greenBackgroundReciklo"))
    }
}

#Preview {
    TopBar(buttonText: "h", action: {})
}


