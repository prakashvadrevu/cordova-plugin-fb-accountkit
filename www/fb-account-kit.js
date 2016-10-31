/*global cordova, module*/

module.exports = {

    loginWithPhone: function (successCallback, errorCallback, responseType) {
        cordova.exec(successCallback, errorCallback, "FbAccountKit", "PHONE_LOGIN", [responseType]);
    },

    logout: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "FbAccountKit", "LOGOUT", []);
    }

};
