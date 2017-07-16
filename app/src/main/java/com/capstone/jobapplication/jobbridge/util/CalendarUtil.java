package com.capstone.jobapplication.jobbridge.util;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;

/**
 * Created by Aicun on 7/3/2017.
 */

public class CalendarUtil {

    public static void addEventToCalendar(Context context, ContentValues values, int minutes) {

        EventHandler handler = new EventHandler(context.getContentResolver(),minutes);

        handler.startInsert(0, null, CalendarContract.Events.CONTENT_URI, values);
    }

    public static void addReminderToCalendar(ContentResolver cr, int minutes, long eventID) {

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.MINUTES, minutes);
        values.put(CalendarContract.Reminders.EVENT_ID, eventID);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        AsyncQueryHandler remindHandler = new AsyncQueryHandler(cr) {
        };

        remindHandler.startInsert(0,null,CalendarContract.Reminders.CONTENT_URI,values);
    }

    static class EventHandler extends AsyncQueryHandler {
        ContentResolver cr;
        int minutes;
        public EventHandler(ContentResolver cr,int minutes) {
            super(cr);
            this.cr = cr;
            this.minutes = minutes;
        }

        @Override
        protected void onInsertComplete(int token, Object cookie, Uri uri) {
            super.onInsertComplete(token, cookie, uri);
            long eventID = Long.parseLong(uri.getLastPathSegment());
            addReminderToCalendar(cr,minutes,eventID);
        }
    }
}

