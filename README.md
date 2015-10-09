# cordova-sharingreceptor

A cordova plugin that permits to receive data sent from other applications (like urls, images...), for Android and IOS.

It handles cases where app is started or resumed.

It currently works for Android and iOS (contributors welcome).


## Usage:

```javascript
cordova.SharingReceptor.listen(function(data) {
    // do something
});
```

## Android

Listens for data of intents SEND / SEND_MULIPLE.
Intent filters have been set to receive data of any mime type (better solution exists?)

The data contains the intent name and the intent extras.

Here is an example of data that is send when sharing a web page with Chrome:

```json
{  
   "intent":{  
      "action":"android.intent.action.SEND",
      "extras":{  
         "android.intent.extra.TEXT":"http:\/\/www.conforama.fr\/",
         "android.intent.extra.SUBJECT":"Meuble, cuisine, décoration, électroménager, image et son, informatique : Conforama, c'est bon de changer - Conforama"
      }
   },
   "platform":"android"
}

```


## iOS

It uses the plist files

TODO Work in progress


## Notes


It is initially inspired by (cordova-webintent)[https://github.com/Initsogar/cordova-webintent] plugin but simpler.
This Android-only plugin is now unmaintained and has a larger goal than this plugin.


## Licence: MIT
