
var exec = require('cordova/exec');

module.exports = {
    listen: function(success, error) {
	return exec(success, error, 'SharingReceptor', 'listen', []);	
    }
};

