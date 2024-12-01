//
//  SentView.swift
//  LoginR
//
//  Created by Artekium on 17/04/2024.
//

import SwiftUI
import UIKit

struct AddProductView: View {
    @State private var isShowingImagePicker = false
    @State private var isShowingActionSheet = false
    @State private var selectedImage: UIImage?
    @State private var pickerSourceType: UIImagePickerController.SourceType?
    @State private var isShowingForm = false
    @EnvironmentObject var productData: ProductData  // Usa ProductData

    var body: some View {
        VStack {
            TopBar(buttonText: "", buttonIcon: "person", action: {print("hola")},iconOpacity: 0)
            ScrollView {
                if !isShowingForm {
                    Text("Sumá fotos del producto")
                        .font(.title2)
                        .padding()
                    Text("Comenzá agregando fotos generales del producto, utilizando fondo blanco")
                        .padding(.horizontal, 5)
                        .multilineTextAlignment(.center)
                    Spacer()
                    Image(.addPhoto)
                        .padding(.top)
                    Spacer()
                    Button(action: {
                        self.isShowingActionSheet = true
                    }) {
                        Label("Agregar Foto", systemImage: "camera.fill")
                    }
                    .font(.body)
                    .multilineTextAlignment(.center)
                    .foregroundColor(Color(.darkGreenReciklo))
                    .frame(width: 191, height: 40)
                    .overlay(
                        RoundedRectangle(cornerRadius: 100)
                            .stroke(Color(.darkGreenReciklo))
                    )
                    .padding()
                    HStack {
                        Text("Galería")
                            .fontWeight(.bold)
                            .padding(.horizontal, 25)
                            .font(.callout)
                        Spacer()
                    }
                    Text("Las imágenes que guardes se mostrarán en la publicación")
                        .padding(.top,5)
                        .font(.callout)
                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack {
                            ForEach(productData.images, id: \.self) { img in
                                Image(uiImage: img)
                                    .resizable()
                                    .scaledToFit()
                                    .frame(width: 100, height: 100)
                                    .clipShape(RoundedRectangle(cornerRadius: 10))
                                    .padding(8)
                                
                            }
                            if productData.images.count >= 3 {
                                Image(systemName: "chevron.right")
                                    .padding(.trailing, 8)
                                    .transition(.move(edge: .trailing))
                                    .animation(.default, value: productData.images)
                            }
                        }
                    }
                    .frame(height: 110)
                    
                    HStack {
                        Spacer()
                        Button("Continuar") {
                            isShowingForm = true
                        }
                        .font(.body)
                        .multilineTextAlignment(.center)
                        .foregroundColor(productData.images.isEmpty ? .gray : .white)
                        .frame(width: 120, height: 40)
                        .background(productData.images.isEmpty ? Color(.lightGrayReciklo) : Color(.darkGreenReciklo))
                        .cornerRadius(100)
                        .padding()
                    }
                } else {
                    ProductDetailsFormView()
                }
            }
            .actionSheet(isPresented: $isShowingActionSheet) {
                ActionSheet(
                    title: Text("Selecciona una opción"),
                    buttons: [
                        .default(Text("Tomar Foto")) {
                            self.pickerSourceType = .camera
                            self.isShowingImagePicker = true
                        },
                        .default(Text("Elegir desde Galería")) {
                            self.pickerSourceType = .photoLibrary
                            self.isShowingImagePicker = true
                        },
                        .cancel()
                    ]
                )
            }
            .sheet(isPresented: $isShowingImagePicker) {
                if let sourceType = pickerSourceType {
                    ImagePicker(selectedImage: $selectedImage, sourceType: sourceType) {
                        if let selectedImage = selectedImage {
                            productData.images.append(selectedImage)
                            self.selectedImage = nil
                        }
                    }
                }
            }
            .navigationBarBackButtonHidden(true)
        }
    }
}

struct AddProductView_Previews: PreviewProvider {
    static var previews: some View {
        AddProductView().environmentObject(ProductData())
    }
}
