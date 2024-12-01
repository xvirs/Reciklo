import SwiftUI
import Shared

@main
struct iOSApp: App {
    @StateObject private var productData = ProductData()

    init() {
        KoinDependenciesKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            NavigationStack {
                           LoginView()
                              
                       } .environmentObject(productData)
        }
    }
}
