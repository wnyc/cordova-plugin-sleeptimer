var exec = require("cordova/exec");

/**
 * This is a global variable called sleeptimer exposed by cordova
 */    
var SleepTimer = function(){};

SleepTimer.prototype.sleep = function(success, error, options) {
    exec(success, error, "SleepTimerPlugin", "sleep", [options]);
};

module.exports = new SleepTimer();
