//
//  DefectFormView.swift
//  iosApp
//
//  Created by Artekium on 13/05/2024.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI

struct DefectFormView: View {
    @Binding var showingForm: Bool
    @Binding var navigateToSummary: Bool
    @EnvironmentObject var productData: ProductData
    @State private var defectDescription: String = ""
    @State private var images = [UIImage]()
    @State private var isShowingImagePicker = false
    @State private var isShowingActionSheet = false
    @State private var pickerSourceType: UIImagePickerController.SourceType?
    @State private var selectedImage: UIImage?

    var body: some View {
        VStack {
            HStack {
                Text("Deméritos")
                Spacer()
                Button("Listo") {
                    if let image = images.first {
                        let newDefect = Defect(description: defectDescription, image: image)
                        productData.defects.append(newDefect)
                    }
                    showingForm = false
                    navigateToSummary = true
                }
                .font(.body)
                .multilineTextAlignment(.center)
                .foregroundColor(.white)
                .frame(width: 120, height: 40)
                .background(Color(.darkGreenReciklo))
                .cornerRadius(100)
            }
            TextField("Descripción del producto", text: $defectDescription, axis: .vertical)
                .padding()
                .lineLimit(5...10)
                .border(Color.black)
            Button("Cargar Imagen") {
                isShowingActionSheet = true
            }
            .recikloGreenButton()
            .padding(.horizontal)
            
            ScrollView(.horizontal, showsIndicators: false) {
                HStack {
                    ForEach(images, id: \.self) { img in
                        Image(uiImage: img)
                            .resizable()
                            .scaledToFit()
                            .frame(width: 100, height: 100)
                            .clipShape(RoundedRectangle(cornerRadius: 10))
                            .padding(4)
                    }
                }
            }
            .frame(height: 110)
            .padding(.top)
        }
        .padding()
        .actionSheet(isPresented: $isShowingActionSheet) {
            ActionSheet(
                title: Text("Selecciona una opción"),
                buttons: [
                    .default(Text("Tomar Foto")) {
                        pickerSourceType = .camera
                        isShowingImagePicker = true
                    },
                    .default(Text("Elegir desde Galería")) {
                        pickerSourceType = .photoLibrary
                        isShowingImagePicker = true
                    },
                    .cancel()
                ]
            )
        }
        .sheet(isPresented: $isShowingImagePicker) {
            if let sourceType = pickerSourceType {
                ImagePicker(selectedImage: $selectedImage, sourceType: sourceType) {
                    if let selectedImage = selectedImage {
                        images.append(selectedImage)
                        self.selectedImage = nil
                    }
                }
            }
        }
    }
}

struct DefectFormView_Previews: PreviewProvider {
    static var previews: some View {
        DefectFormView(showingForm: .constant(true), navigateToSummary: .constant(false)).environmentObject(ProductData())
    }
}
