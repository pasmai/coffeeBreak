//import firebase functions modules
const functions = require('firebase-functions');
//import admin module
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.pushNotification = functions.database.ref('/invites/{url}').onWrite(event => {
    console.log('Push notification event triggered');

    // Create a notification
    const payload = {
        notification: {
            title: "valueObject.title",
            body: "valueObject.message",
            sound: "default",
            timestamp: 1600547519,
            url: event.params.url,
        }
    };

    // Create an options object that contains the time to live for the notification and the priority
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };

    return admin.messaging().sendToTopic("all", payload, options);
});