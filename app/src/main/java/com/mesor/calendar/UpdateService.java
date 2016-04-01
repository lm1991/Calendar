package com.mesor.calendar;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.mesor.application.MyApplication;
import com.mesor.utils.Constants;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016/3/29.
 */
public class UpdateService extends RemoteViewsService {

    private Calendar currentShowCalendar;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        String action = intent.getAction();
        System.out.println(action);
        if (action.equals(Constants.PREVIOUSMONTH)) {
            currentShowCalendar.add(Calendar.MONTH, -1);
        } else if (action.equals(Constants.NEXTMONTH)) {
            currentShowCalendar.add(Calendar.MONTH, 1);
        } else if (action.equals(Constants.UPDATE)) {
        }
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private final Context mContext;
        private final List<String> mList;

        public ListRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            if (currentShowCalendar == null) {
                currentShowCalendar = Calendar.getInstance();
            }
            mList = MyApplication.getInstance().getDateList(currentShowCalendar);

            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public void onDestroy() {
            mList.clear();
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position < 0 || position >= mList.size())
                return null;
            String content = mList.get(position);
            int index = content.indexOf("\n");
            SpannableString spannableString = new SpannableString(content);
            spannableString.setSpan(new AbsoluteSizeSpan(9, true), index, content.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            final RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

            Intent intent = new Intent();
            //TODO
            //intent.setComponent(new ComponentName("包名", "类名"));
            //与CustomWidget中remoteViews.setPendingIntentTemplate配对使用
            rv.setOnClickFillInIntent(R.id.widget_list_item_layout, intent);

            rv.setTextViewText(R.id.widget_list_item_tv, spannableString);

            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
