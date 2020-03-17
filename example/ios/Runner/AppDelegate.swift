import UIKit
import Flutter

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
    override func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        GeneratedPluginRegistrant.register(with: self)
        return super.application(application, didFinishLaunchingWithOptions: launchOptions)
    }
    
    override func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        if let controller = window.rootViewController as? FlutterViewController {
            FlutterMethodChannel(
                name: "flutter_d_sdk",
                binaryMessenger: controller.binaryMessenger
            ).invokeMethod(
                "resp",
                arguments: url.absoluteString.removingPercentEncoding
            )
        }
        return true
    }
}
