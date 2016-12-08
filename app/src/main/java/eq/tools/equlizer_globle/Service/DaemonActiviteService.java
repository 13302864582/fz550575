package eq.tools.equlizer_globle.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2015/12/15.
 * 检测当前系统是否有播放音乐
 * 启动条件：当桌面widget(SwitchWidgetVisual,SwitchWidgetVisualBass)被用户使用
 * 停止条件：当上述两个widget被删除，则停止
 */

public class DaemonActiviteService extends Service {
    private AudioManager mAm;
    private boolean isPlaying;
    private Handler mHandler = new Handler();
    Intent intent = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("DaemonActiviteService", "onCreate");
        mAm = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        boolean play = mAm.isMusicActive();
        intent = new Intent(EQService.PLYAER_STATUS_ACTION);
        if(play != isPlaying){
            isPlaying = play;
            intent.putExtra("isPlaying", isPlaying);
            sendBroadcast(intent);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isRun){
            mHandler.post(runnable);
        }
        return START_STICKY;
    }

    AudioManager.OnAudioFocusChangeListener mListener = new AudioManager.OnAudioFocusChangeListener(){
        @Override
        public void onAudioFocusChange(int focusChange) {

        }
    };

    boolean isRun = false;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isRun = true;
            boolean play = mAm.isMusicActive();
            if(play != isPlaying) {
                isPlaying = play;
                intent.putExtra("isPlaying", isPlaying);
                sendBroadcast(intent);
            }
            mHandler.postDelayed(runnable, 10);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
