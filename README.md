# cordova-sharingreceptor

A cordova plugin that permits to receive data sent from other applications (like urls, images...)
It handles cases where app is started or resumed.

This is for doc purpose, you should customize this plugin for your needs because currently I only made it work to share browser urls (like a clipper)

For iOS, it is [harder but achievable!](http://stackoverflow.com/questions/26436544/share-image-using-share-extension-in-ios8)


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



## Notes


It is initially inspired by (cordova-webintent)[https://github.com/Initsogar/cordova-webintent] plugin but simpler.
This Android-only plugin is now unmaintained and has a larger goal than this plugin.


## Licence: MIT
