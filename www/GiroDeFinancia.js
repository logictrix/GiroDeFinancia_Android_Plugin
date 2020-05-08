var exec = require('cordova/exec');

 
function plugin() {

}

plugin.prototype.launcher  = function() {
    exec(function(res){}, function(err){}, "GiroDeFinancia", "launcher", []);
}

module.exports = new plugin();
