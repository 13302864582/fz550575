package eq.tools.equlizer_globle.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import eq.tools.equlizer_globle.EQActivity;
import eq.tools.equlizer_globle.R;
import eq.tools.equlizer_globle.Service.EQService;


/**
 * Created by Administrator on 2016/3/30 0030.
 */
public class SwitchWidgetSong extends AppWidgetProvider{
    public static final String TAG = SwitchWidgetSong.class.getSimpleName();
    private static SwitchWidgetSong sInstance;
    private int[] appWidgetIds = null;
    RemoteViews views = null;
    private Context mContext;

    public static synchronized SwitchWidgetSong getInstance() {
        if (sInstance == null) {
            sInstance = new SwitchWidgetSong();
        }
        return sInstance;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        if(EQService.mContent==null){
            context.startService(new Intent(context,EQService.class));
        }
        this.appWidgetIds=appWidgetIds;
        Intent intent=new Intent(TAG);
        intent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
        context.sendBroadcast(intent);
    }


    public void performUpdate(EQService service,String trackname){
        mContext = service;
        views=new RemoteViews(service.getPackageName(), R.layout.widget_song);
        if(service.isMusicActivite()){
            views.setImageViewResource(R.id.widget_play,R.drawable.play_button_selector_stop);
        }else {
            views.setImageViewResource(R.id.widget_play,R.drawable.play_button_selector_on);
        }
        if(trackname!=null){
            views.setTextViewText(R.id.widget_tv,trackname+"");
        }else {
            views.setTextViewText(R.id.widget_tv,"Unknown");
        }
        Intent intent=new Intent(service,EQService.class);

        intent.setAction(EQService.NOTIFY_PRE_ACTION);
        PendingIntent pendingIntent = null;
        pendingIntent=PendingIntent.getService(service,0,intent,0);
        views.setOnClickPendingIntent(R.id.widget_pre,pendingIntent);

        intent.setAction(EQService.NOTIFY_PLAY_ACTION);
        pendingIntent=PendingIntent.getService(service,0,intent,0);
        views.setOnClickPendingIntent(R.id.widget_play,pendingIntent);

        intent.setAction(EQService.NOTIFY_NEXT_ACTION);
        pendingIntent=PendingIntent.getService(service,0,intent,0);
        views.setOnClickPendingIntent(R.id.widget_next,pendingIntent);

        pendingIntent=PendingIntent.getActivity(service,0,new Intent(service, EQActivity.class),0);
        views.setOnClickPendingIntent(R.id.song_Btn,pendingIntent);

        updataview(views);
    }

    private void updataview(RemoteViews views) {
        final AppWidgetManager gm = AppWidgetManager.getInstance(mContext);
        if (appWidgetIds != null) {
            gm.updateAppWidget(appWidgetIds, views);
        } else {
            gm.updateAppWidget(new ComponentName(mContext, this.getClass()), views);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
