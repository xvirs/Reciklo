//
//  AddProductData.swift
//  iosApp
//
//  Created by Artekium on 16/05/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

class ProductData: ObservableObject {
    @Published var productCode: String = ""
    @Published var productName: String = ""
    @Published var chassisNumber: String = ""
    @Published var selectedBrand: String = "Selecciona una marca"
    @Published var selectedModel: String = "Selecciona un modelo"
    @Published var selectedCategory: String = "Selecciona una categoría"
    @Published var selectedProductType: String = "Selecciona tipo"
    @Published var selectedVersion: String = "Selecciona una version"
    @Published var brands: [String] = ["a", "b", "c"]
    @Published var models: [String] = ["a", "b", "c"]
    @Published var productCategories: [String] = ["a", "b", "c"]
    @Published var productTypes: [String] = ["a", "b", "c"]
    @Published var versions: [String] = ["a", "b", "c"]
    @Published var productYear: String = ""
    @Published var version: String = ""
    @Published var defectDescription: String = ""
    @Published var productDescription: String = ""
    @Published var productPrice: String = ""
    @Published var productQuantity: String = ""
    @Published var productHeight: String = ""
    @Published var productWidth: String = ""
    @Published var productLength: String = ""
    @Published var productWeight: String = ""
    @Published var images: [UIImage] = []
    @Published var defects: [Defect] = []  // Aquí están los defectos
}

struct Defect: Identifiable {
    var id = UUID()
    var description: String
    var image: UIImage
}

