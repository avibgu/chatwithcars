package com.appspot.chatwithcars;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	public GCMIntentService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Called after a registration intent is received, passes the registration
	 * ID assigned by GCM to that device/application pair as parameter.
	 * Typically, you should send the regid to your server so it can use it to
	 * send messages to this device.
	 */
	@Override
	protected void onRegistered(Context context, String regId) {
		ApplicationServer.sendIDAfterRegistering(regId);
	}

	/**
	 * Called after the device has been unregistered from GCM. Typically, you
	 * should send the regid to the server so it unregisters the device.
	 */
	@Override
	protected void onUnregistered(Context context, String regId) {
		ApplicationServer.sendIDAfterUnregistering(regId);
	}

	/**
	 * Called when your server sends a message to GCM, and GCM delivers it to
	 * the device. If the message has a payload, its contents are available as
	 * extras in the intent.
	 */
	@Override
	protected void onMessage(Context context, Intent intent) {

		System.out.println("NOTIFICATION RECEIVED: "
				+ intent.getExtras().get("msgbody"));

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.action_about)
				.setContentTitle("My notification")
				.setContentText("Hello World!");

		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, NotificationActivity.class);

		// The stack builder object will contain an artificial back stack for
		// the started Activity. This ensures that navigating backward from the
		// Activity leads out of your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(NotificationActivity.class);

		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);

		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(resultPendingIntent);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// mId allows you to update the notification later on.
		mNotificationManager.notify(0, mBuilder.build()); // TODO mId = 0
	}

	/**
	 * Called when the device tries to register or unregister, but GCM returned
	 * an error. Typically, there is nothing to be done other than evaluating
	 * the error(returned by errorId) and trying to fix the problem.
	 */
	@Override
	protected void onError(Context context, String errorId) {
		// Auto-generated method stub
	}
}
