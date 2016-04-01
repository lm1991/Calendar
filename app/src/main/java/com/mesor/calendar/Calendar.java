package com.mesor.calendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.mesor.utils.Constants;

/**
 * Implementation of App Widget functionality.
 */
public class Calendar extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.calendar);
        if(action.equals(Constants.PREVIOUSMONTH)) {
            Intent preIntent = new Intent(context, UpdateService.class);
            preIntent.setAction(Constants.PREVIOUSMONTH);
            //preIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            //设置适配器
            views.setRemoteAdapter(R.id.appwidget_gridview, preIntent);
        }else if(action.equals(Constants.NEXTMONTH)) {
            Intent nextIntent = new Intent(context, UpdateService.class);
            nextIntent.setAction(Constants.NEXTMONTH);
            //preIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            //设置适配器
            views.setRemoteAdapter(R.id.appwidget_gridview, nextIntent);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        CharSequence widgetText = context.getResources().getDisplayMetrics().density + "";
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.calendar);
//        views.setTextViewText(R.id.monthTv, widgetText);

        Intent btPreIntent = new Intent().setAction(Constants.PREVIOUSMONTH);
        Intent btNextIntent = new Intent().setAction(Constants.NEXTMONTH);

        PendingIntent btPrePendingIntent = PendingIntent.getBroadcast(context, 0, btPreIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent btNextPendingIntent = PendingIntent.getBroadcast(context, 0, btNextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.preMonthImgV, btPrePendingIntent);
        views.setOnClickPendingIntent(R.id.nextMonthImgV, btNextPendingIntent);

        Intent intent = new Intent(context, UpdateService.class);
        intent.setAction(Constants.UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //设置适配器
        views.setRemoteAdapter(R.id.appwidget_gridview, intent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

