#import "FlutterDSdkPlugin.h"
#import <flutter_d_sdk/flutter_d_sdk-Swift.h>

@implementation FlutterDSdkPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterDSdkPlugin registerWithRegistrar:registrar];
}
@end
