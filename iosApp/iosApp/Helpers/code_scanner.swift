//
//  code_scanner.swift
//  iosApp
//
//  Created by Artekium on 22/05/2024.
//  Copyright © 2024 orgName. All rights reserved.
//
import SwiftUI
import AVFoundation

struct QRCodeScannerView: UIViewRepresentable {
    var supportedCodeTypes: [AVMetadataObject.ObjectType] = [.qr]
    @Binding var isScanning: Bool
    var session = AVCaptureSession()

    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    func makeUIView(context: Context) -> UIView {
        let view = UIView(frame: UIScreen.main.bounds)
        guard let videoCaptureDevice = AVCaptureDevice.default(for: .video),
              let videoInput = try? AVCaptureDeviceInput(device: videoCaptureDevice) else {
            return view
        }

        if session.canAddInput(videoInput) {
            session.addInput(videoInput)
        } else {
            return view
        }

        let metadataOutput = AVCaptureMetadataOutput()
        if session.canAddOutput(metadataOutput) {
            session.addOutput(metadataOutput)
            metadataOutput.setMetadataObjectsDelegate(context.coordinator, queue: DispatchQueue.main)
            metadataOutput.metadataObjectTypes = supportedCodeTypes
        } else {
            return view
        }

        let previewLayer = AVCaptureVideoPreviewLayer(session: session)
        previewLayer.frame = view.layer.bounds
        previewLayer.videoGravity = .resizeAspectFill
        view.layer.addSublayer(previewLayer)

        return view
    }

    func updateUIView(_ uiView: UIView, context: Context) {
        if isScanning && !session.isRunning {
            session.startRunning()
        } else if !isScanning && session.isRunning {
            session.stopRunning()
        }
    }

    class Coordinator: NSObject, AVCaptureMetadataOutputObjectsDelegate {
        var parent: QRCodeScannerView

        init(_ parent: QRCodeScannerView) {
            self.parent = parent
        }

        func metadataOutput(_ output: AVCaptureMetadataOutput, didOutput metadataObjects: [AVMetadataObject], from connection: AVCaptureConnection) {
            if let metadataObject = metadataObjects.first as? AVMetadataMachineReadableCodeObject {
                if parent.supportedCodeTypes.contains(metadataObject.type),
                   let scannedValue = metadataObject.stringValue {
                    DispatchQueue.main.async {
                        self.parent.isScanning = false
                    }
                }
            }
        }
    }
}
