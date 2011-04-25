package de.uniluebeck.itm.mdc.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import de.uniluebeck.itm.mdc.MobileDataCollector;
import de.uniluebeck.itm.mdc.R;

public class Notifications {

	public static Notification createNotification(Context context, String title, String ticker) {
		final long when = System.currentTimeMillis();
		final PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MobileDataCollector.class), 0);
		final Notification notification = new Notification(R.drawable.stat_sample, title, when);
		notification.setLatestEventInfo(context, context.getText(R.string.app_name), ticker, contentIntent);
		return notification;
	}
}
