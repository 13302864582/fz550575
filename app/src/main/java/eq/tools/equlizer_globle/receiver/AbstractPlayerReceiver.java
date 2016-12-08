package eq.tools.equlizer_globle.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Administrator on 2016/4/22 0022.
 */
public abstract class AbstractPlayerReceiver extends BroadcastReceiver {
    private static final String PACKPAGE_NOW_PLAYING_NAME = "package_now_playing";
    protected Context mContext;
    protected final String mPlayerName;
    protected final String mPlayerPackageName;

    public AbstractPlayerReceiver(String paramString1, String paramString2) {
        this.mPlayerPackageName = paramString1;
        this.mPlayerName = paramString2;
    }

    public final void onReceive(Context paramContext, Intent paramIntent) {
        this.mContext = paramContext;
        Bundle bundle = paramIntent.getExtras();
        String artist = bundle.getString("artist");
        String track = bundle.getString("track");
        Intent intent=new Intent();
        intent.setAction(PACKPAGE_NOW_PLAYING_NAME);
        intent.putExtra("artist",artist);
        intent.putExtra("track",track);
        intent.putExtra("packagename",mPlayerPackageName);
        paramContext.sendBroadcast(intent);
    }
}
