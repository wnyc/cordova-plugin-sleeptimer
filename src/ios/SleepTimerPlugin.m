//
//  SleepTimerPlugin.m
//
//  Created by Brad Kammin on 4/29/14.
//
//

#import "SleepTimerPlugin.h"

static NSString * const kSleepTimerPluginJSONTypeKey = @"type";
static NSString * const kSleepTimerPluginJSONSleepKey = @"sleep";
static NSString * const kSleepTimerPluginJSONCountdownKey = @"countdown";
static NSString * const kSleepTimerPluginJSONTimeLeftKey = @"timeLeft";

static NSString * const kSleepTimerPluginJSONSleepValue = @"sleep";
static NSString * const kSleepTimerPluginJSONCountdownValue = @"countdown";

@interface SleepTimerPlugin ()

@property BOOL countdown;
@property NSTimer * timer;
@property NSTimer * remainingTimeTimer;
@property NSString * callbackId;

@end

@implementation SleepTimerPlugin

#pragma mark Plugin methods

- (void)sleep:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSDictionary  * options = [command.arguments objectAtIndex:0];
    
    long seconds = [[options valueForKey:kSleepTimerPluginJSONSleepKey] integerValue];
    self.countdown = [[options valueForKey:kSleepTimerPluginJSONCountdownKey] boolValue];
    self.callbackId = command.callbackId;
    
    if ([self.timer isValid]) {
        [self.timer invalidate];
        self.timer=nil;
    }
    
    if (self.remainingTimeTimer && [self.remainingTimeTimer isValid]) {
        [self.remainingTimeTimer invalidate];
        self.remainingTimeTimer=nil;
    }
    
    if (seconds > 0){
        NSLog (@"SleepTimer Plugin sleeping..." );
        self.timer = [NSTimer scheduledTimerWithTimeInterval: seconds
                                                  target: self
                                                selector: @selector(sleepTimerExpired)
                                                userInfo: nil
                                                 repeats: NO];

        if (self.countdown) {
            self.remainingTimeTimer = [NSTimer scheduledTimerWithTimeInterval: 1
                                                      target: self
                                                    selector: @selector(sleepTimerCountdown)
                                                    userInfo: nil
                                                     repeats: YES];
        }
    }
    
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

-(void) sleepTimerExpired {
    CDVPluginResult * pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{kSleepTimerPluginJSONTypeKey : kSleepTimerPluginJSONSleepValue}];
    
    if ([self.timer isValid]) {
        [self.timer invalidate];
    }
    self.timer=nil;
    if (self.remainingTimeTimer && [self.remainingTimeTimer isValid]) {
        [self.remainingTimeTimer invalidate];
    }
    self.remainingTimeTimer=nil;
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.callbackId];
}

-(void) sleepTimerCountdown {
    NSTimeInterval result = 0;
	if (self.timer && [self.timer isValid]) {
		result = [[self.timer fireDate] timeIntervalSinceNow];
	}
 
    CDVPluginResult * pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{kSleepTimerPluginJSONTypeKey: kSleepTimerPluginJSONCountdownValue, kSleepTimerPluginJSONTimeLeftKey : @((int)ceil(result))}];
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.callbackId];
}

@end
