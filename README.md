# Sleep Timer PhoneGap/Cordova Plugin

### Platform Support

This plugin supports PhoneGap/Cordova apps running on both iOS and Android.

### Version Requirements

This plugin is meant to work with Cordova 3.5.0+.

## Installation

#### Automatic Installation using PhoneGap/Cordova CLI (iOS and Android)
1. Make sure you update your projects to Cordova iOS version 3.5.0+ before installing this plugin.

        cordova platform update ios
        cordova platform update android

2. Install this plugin using PhoneGap/Cordova cli:

        cordova local plugin add https://github.com/wnyc/cordova-plugin-sleeptimer.git

## Usage

    // all responses from the audio player are channeled through successCallback and errorCallback

    // set sleep timer
    window.sleeptimer.sleep(
      successCallback,
      errorCallback,
      {
        'sleep' : 5 * 60, // sleep in 5 minutes/300 seconds
        'countdown' : true // if true, send time-to-sleep countdown from native to javascript
      }
    );

    // example of a callback method
    var successCallback = function(result) {
      if (result.type==='sleep') {
        console.log('do something like stop audio playback');
      } else if (result.type==='countdown') {
        console.log('time until sleep: ' + result.timeLeft + ' seconds');
      } else {
        console.log('unhandled type (' + result.type + ')');
      }
    }; 
