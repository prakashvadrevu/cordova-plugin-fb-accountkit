# Cordova Facebook Account Kit Plugin

This plugin integrates the Facebook's Account Kit. For more info see [Account Kit](https://www.accountkit.com/)

This is a WIP. Not recommended for production usage.

## Supported platforms

* Android

## Plugin Installation

* Clone this project
* Add the plugin to your cordova project with the following command

```
cordova plugin add <PATH_TO>/cordova-plugin-fb-accountkit --variable FB_APP_NAME=<APP_NAME> --variable FB_APP_ID=<APP_ID> --variable FB_CLIENT_TOKEN=<CLIENT_TOKEN>
```

## API

#### loginWithPhone
```
fbAccountKit.loginWithPhone(success, failure, {"type" : "CODE"}); // currently only 'CODE' is supported
```

Response:
```
{
    status : "SUCCESS",
    authorizationCode : "<auth-code-string>",
    finalAuthorizationState : "",
    tokenRefreshIntervalInSeconds : 120
}
```

#### logout
```
fbAccountKit.logout(success, failure);
```

Response:
* 'success' callback gets called
