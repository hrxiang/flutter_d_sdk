import Flutter
import UIKit

public class SwiftFlutterDSdkPlugin: NSObject, FlutterPlugin {
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "flutter_d_sdk", binaryMessenger: registrar.messenger())
        let instance = SwiftFlutterDSdkPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard
            "call" == call.method,
            var args = call.arguments as? [String : Any]
        else {
            print("Invalid flutter call.")
            return
        }
        guard
            let uri = args["uriString"] as? String,
            let _ = args["action"] as? String
        else {
            print("The required parameters are missing.")
            return
        }
        
        args.removeValue(forKey: "uriString")
        let jsonParams = args.obj2json()
        let containsSymbol = uri.contains("?")
        let fullUri = "\(uri)\(containsSymbol ? "":"?")params=\(jsonParams ?? "")"
            .addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
        
        guard
            let theUrl = URL(string: fullUri ?? "")
        else {
            print("Invalid url: \(String(describing: fullUri))")
            return
        }
        
        if #available(iOS 10.0, *) {
            UIApplication.shared.open(theUrl, options: [String:Any]()) { (_) in}
        } else {
            UIApplication.shared.openURL(theUrl)
        }
    }
}
