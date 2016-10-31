/*global cordova, module*/

module.exports = {

    loginWithPhone: function (successCallback, errorCallback, responseType) {
        cordova.exec(successCallback, errorCallback, "FbAccountKit", "PHONE_LOGIN", [responseType]);
    }

};
