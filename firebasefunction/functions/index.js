//import firebase functions modules
const functions = require('firebase-functions');
//import admin module
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.pushNotification = functions.database.ref('/invites/{urlString}/{timestampString}').onWrite((change, context) => {
    console.log('Push notification event triggered');
    console.log(context.params.urlString);
    console.log(context.params.timestampString);
    // Create a notification
    const payload = {
       data: {
            timestamp: context.params.timestampString,
            url: context.params.urlString
        },
        notification: {
            title: "CoffeeBreak",
            body: "You have been invited to a coffee break",
            sound: "default"
        }
    };
    console.log(payload);
    // Create an options object that contains the time to live for the notification and the priority
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };

    return admin.messaging().sendToTopic("all", payload, options);
});
