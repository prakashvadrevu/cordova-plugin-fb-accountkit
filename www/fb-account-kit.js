/*global cordova, module*/

module.exports = {

    phoneLogin: function (successCallback, errorCallback, responseType) {
        cordova.exec(successCallback, errorCallback, "FbAccountKit", "PHONE_LOGIN", [responseType]);
    }

};
