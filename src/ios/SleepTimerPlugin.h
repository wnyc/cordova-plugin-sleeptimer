//
//  SleepTimerPlugin.h
//
//  Created by Brad Kammin on 4/29/14.
//
//

#import <Cordova/CDVPlugin.h>
#import <Cordova/CDVPluginResult.h>

@interface SleepTimerPlugin : CDVPlugin

- (void)sleep:(CDVInvokedUrlCommand*)command;

@end
