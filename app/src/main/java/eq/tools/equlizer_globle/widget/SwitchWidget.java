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
 * Created by Administrator on 2016/4/13 0013.
 */
public class SwitchWidget extends AppWidgetProvider{
    public static final String TAG = SwitchWidget.class.getSimpleName();
    private static SwitchWidget sInstance;
    private int[] appWidgetIds = null;


    public static synchronized SwitchWidget getInstance(){
        if(sInstance==null){
            sInstance=new SwitchWidget();
        }
        return sInstance;
    }

    public void performUpdate(EQService service){
        RemoteViews views=new RemoteViews(service.getPackageName(), R.layout.widget_switch);
        if(service.isTotalEnable()) {
            views.setImageViewResource(R.id.switchBtn, R.mipmap.widget4_button);
            views.setImageViewResource(R.id.switchImg, R.mipmap.widget4_icon);
        }else{
            views.setImageViewResource(R.id.switchBtn, R.mipmap.widget4_button_off);
            views.setImageViewResource(R.id.switchImg, R.mipmap.widget4_icon_off);
        }
        Intent intent=new Intent(service,EQService.class);
        intent.setAction(EQService.WIDHET_EQ_SWITCH_ACTION);
        PendingIntent pendingIntent=PendingIntent.getService(service,0,intent,0);
        views.setOnClickPendingIntent(R.id.switchBtn,pendingIntent);

        pendingIntent=PendingIntent.getActivity(service,0,new Intent(service,EQActivity.class),0);
        views.setOnClickPendingIntent(R.id.switchImg,pendingIntent);
        final AppWidgetManager gm = AppWidgetManager.getInstance(service);
        if (appWidgetIds != null) {
            gm.updateAppWidget(appWidgetIds, views);
        } else {
            gm.updateAppWidget(new ComponentName(service, this.getClass()), views);
        }
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

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
