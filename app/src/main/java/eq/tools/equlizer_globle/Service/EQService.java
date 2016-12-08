package eq.tools.equlizer_globle.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Virtualizer;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import eq.tools.equlizer_globle.EQActivity;
import eq.tools.equlizer_globle.Fragment.EQFragment;
import eq.tools.equlizer_globle.IEQAidlInterface;
import eq.tools.equlizer_globle.R;
import eq.tools.equlizer_globle.Util.Config;
import eq.tools.equlizer_globle.Util.PreferenceUtil;
import eq.tools.equlizer_globle.Util.SystemUtil;
import eq.tools.equlizer_globle.widget.SwitchWidget;
import eq.tools.equlizer_globle.widget.SwitchWidgetSong;


/**
 * Created by Administrator on 2016/11/22 0022.
 */
public class EQService extends Service{
    private static final String NOTIFT_EQ_SWITCH_ACTION = "notify_switch_action";
    public static final String EQ_SWITCH_ACTION = "eq_switch_action";
    public static final String PACKPAGE_NOW_PLAYING_NAME = "package_now_playing";
    public static final String PLYAER_STATUS_ACTION = "player_status_action";
    public static final String NOTIFY_PRE_ACTION= "notify_pre_action";
    public static final String NOTIFY_NEXT_ACTION = "notify_next_action";
    public static final String NOTIFY_PLAY_ACTION = "notify_play_action";
    public static final String NOTIFY_CLOSE_ACTION = "notify_close_action";
    public static final String WIDHET_EQ_SWITCH_ACTION="widget_eq_switch_action";
    public static final String EXIT="exit";
    private final IBinder mBinder = new ServiceStub(this);
    public static EQService mContent= null;
    private boolean isNotifyEnable;
    private boolean totalEnable =true;
    private Equalizer mEqulizer;
    private int AUDIO_ID;
    private short minBandLevel;
    private short maxBandLevel;
    private boolean isEQEnable = true;
    private BassBoost mBassBoost;
    private boolean isBassEnable = true;
    private Virtualizer mVitualizer;
    private boolean isVirtualEnable = true;
    private int bassVal;
    private int VitualVal;
    private String artist;
    private String trackName;
    private SwitchWidget wSwitch = SwitchWidget.getInstance();
    private SwitchWidgetSong wSong = SwitchWidgetSong.getInstance();
    public boolean isPlaying = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mContent = this;
        if (SystemUtil.eqValList == null || SystemUtil.eqValList.size() <= 0) {
            SystemUtil.initEqualizerVal(EQService.this);
        }
        isNotifyEnable = PreferenceUtil.getBoolean(mContent,Config.ISNOTIFYENABLE,Config.DEFAULT_NOTIFY);
        totalEnable = PreferenceUtil.getBoolean(mContent,Config.ISENABLE,Config.DEFAULT_NOTIFY);
        bassVal = PreferenceUtil.getInt(mContent,Config.BASSVAL,Config.DEFAULT_BASSVAL);
        VitualVal = PreferenceUtil.getInt(mContent,Config.VIRTULVAL,Config.DEFAULT_VIRTULVAL);
        if (isNotifyEnable) {
            createNotify(bassVal,VitualVal);
        }
        AUDIO_ID = 0;
        try {
            mEqulizer=new Equalizer(0,AUDIO_ID);
            short[] bands=mEqulizer.getBandLevelRange();
            minBandLevel = bands[0];
            maxBandLevel = bands[1];
        }catch (Exception e){
            mEqulizer = null;
            isEQEnable = false;
            Toast.makeText(EQService.this, R.string.eq_error, Toast.LENGTH_SHORT).show();
        }

        try {
            mBassBoost = new BassBoost(0, AUDIO_ID);
        } catch (Exception e) {
            mBassBoost = null;
            isBassEnable = false;
            Toast.makeText(EQService.this, R.string.bass_error, Toast.LENGTH_SHORT).show();
        }
        try {
            mVitualizer = new Virtualizer(0, AUDIO_ID);
        } catch (Exception e) {
            mVitualizer = null;
            isVirtualEnable = false;
            Toast.makeText(EQService.this, R.string.virtual_error, Toast.LENGTH_SHORT).show();
        }

        if (!isEQEnable && !isBassEnable && !isVirtualEnable) {
            totalEnable = false;
        }
        setEqualizerEnable();
        setBass(bassVal);
        setVirtual(VitualVal);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NOTIFY_CLOSE_ACTION);
        intentFilter.addAction(NOTIFY_PLAY_ACTION);
        intentFilter.addAction(NOTIFY_NEXT_ACTION);
        intentFilter.addAction(NOTIFY_PRE_ACTION);
        intentFilter.addAction(PLYAER_STATUS_ACTION);
        intentFilter.addAction(WIDHET_EQ_SWITCH_ACTION);
        intentFilter.addAction(EXIT);
        intentFilter.addAction(EQ_SWITCH_ACTION);
        intentFilter.addAction(PACKPAGE_NOW_PLAYING_NAME);
        registerReceiver(mreceiver, intentFilter);
        wSwitch.performUpdate(EQService.this);
        wSong.performUpdate(EQService.this,trackName);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent==null?"":intent.getAction();
        if (NOTIFY_PLAY_ACTION.equals(action)) {
            playchange(MediaIntent.PLAY_PAUSE);
        } else if (NOTIFY_NEXT_ACTION.equals(action)) {
            playchange(MediaIntent.NEXT);
        } else if (NOTIFY_PRE_ACTION.equals(action)) {
            playchange(MediaIntent.PREVIOUS);
        }else if (WIDHET_EQ_SWITCH_ACTION.equals(action)) {
            switchEq();
            if(EQActivity.instance != null){
                sendBroadcast(new Intent(EQFragment.EQ_SWITCH_ACTION));
            }
        }
        wSwitch.performUpdate(EQService.this);
        wSong.performUpdate(EQService.this,trackName);
        return START_STICKY;
    }

    BroadcastReceiver mreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(PLYAER_STATUS_ACTION)) {
                Log.d("EQService", "isPlaying");
                isPlaying = intent.getBooleanExtra("isPlaying", false);
                wSong.performUpdate(EQService.this,trackName);
            }else if(SwitchWidget.TAG.equals(action)){
                startService(new Intent(EQService.this, DaemonActiviteService.class));
                wSwitch.performUpdate(EQService.this);
            }else if(SwitchWidgetSong.TAG.equals(action)){
                startService(new Intent(EQService.this, DaemonActiviteService.class));
                wSong.performUpdate(EQService.this,trackName);
            }else if(action.equals(PACKPAGE_NOW_PLAYING_NAME)){
                artist = intent.getStringExtra("artist");
                trackName = intent.getStringExtra("track");
                wSong.performUpdate(EQService.this,trackName);
            }
            else if(EXIT.equals(action)){
                PreferenceUtil.putBoolean(getApplicationContext(),Config.ISENABLE, Config.DEFAULT_CUSTOM);
                stopSelf();
            }
            else if(WIDHET_EQ_SWITCH_ACTION.equals(action)){
                switchEq();
                if(EQActivity.instance != null){
                    sendBroadcast(new Intent(EQFragment.EQ_SWITCH_ACTION));
                }
            }
        }
    };

    private void switchEq() {
        totalEnable = !totalEnable;
        setEqualizerEnable();
        wSwitch.performUpdate(EQService.this);
    }

    private void createNotify(int bassVal, int vitualVal) {
        Intent nowPlayingIntent = new Intent(mContent, EQActivity.class);
        PendingIntent clickIntent = PendingIntent.getActivity(this, 0, nowPlayingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.icon2_xhdpi);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.notice_icon));
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText("BassBoost:"+bassVal+"%    "+"Virtualizer:"+vitualVal+"%");
        builder.setContentIntent(clickIntent);
        Notification notification= builder.build();
        NotificationManager nm= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(Config.NOTIFYID,notification);
    }

    private void deleteNotify() {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(Config.NOTIFYID);
    }

    public enum MediaIntent {
        NEXT, PLAY_PAUSE, PREVIOUS;
    }

    private void playchange(MediaIntent paramMediaIntent) {
        Context mcontext = getApplicationContext();
        Object paramContext = (AudioManager) mcontext.getSystemService(Context.AUDIO_SERVICE);
        long l = SystemClock.uptimeMillis();
        KeyEvent event = null;
        KeyEvent localKeyEvent = null;
        switch (paramMediaIntent) {
            case NEXT:
                event = new KeyEvent(l, l, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT, 0);
                localKeyEvent = new KeyEvent(l, l, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT, 0);
                break;
            case PREVIOUS:
                event = new KeyEvent(l, l, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0);
                localKeyEvent = new KeyEvent(l, l, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0);
                break;
            case PLAY_PAUSE:
                event = new KeyEvent(l, l, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0);
                localKeyEvent = new KeyEvent(l, l, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0);
                break;
        }
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                AudioManager audio = (AudioManager) mContent.getSystemService(Context.AUDIO_SERVICE);
                audio.dispatchMediaKeyEvent(event);
                audio.dispatchMediaKeyEvent(localKeyEvent);
            }else{
                paramContext = (IBinder) Class.forName("android.os.ServiceManager").getDeclaredMethod("checkService", new Class[]{String.class}).invoke(null, new Object[]{Context.AUDIO_SERVICE});
                paramContext = Class.forName("android.media.IAudioService$Stub").getDeclaredMethod("asInterface", new Class[]{IBinder.class}).invoke(null, new Object[]{paramContext});
                Class.forName("android.media.IAudioService").getDeclaredMethod("dispatchMediaKeyEvent", new Class[]{KeyEvent.class}).invoke(paramContext, new Object[]{event});
                Class.forName("android.media.IAudioService").getDeclaredMethod("dispatchMediaKeyEvent", new Class[]{KeyEvent.class}).invoke(paramContext, new Object[]{localKeyEvent});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private static class ServiceStub extends IEQAidlInterface.Stub{
        WeakReference<EQService> mService;

        public ServiceStub(EQService mService) {
            this.mService = new WeakReference<EQService>(mService);
        }

        @Override
        public void toast() throws RemoteException {
            mService.get().toast();
        }

        @Override
        public void setTotalEnable(boolean checkState) throws RemoteException {
            mService.get().setTotalEnable(checkState);
        }

        @Override
        public void setBandLevel(int band, float dbValue) throws RemoteException {
            mService.get().setBandLevel((short) band,(short)dbValue);
        }

        @Override
        public void setVirtualLevel(int strengh) throws RemoteException {
            mService.get().setVirtualLevel(strengh);
        }

        @Override
        public void setBassLevel(int strengh) throws RemoteException {
            mService.get().setBassLevel(strengh);
        }

        @Override
        public boolean isEQEnable() throws RemoteException {
            return mService.get().isEQEnable();
        }

        @Override
        public boolean isBassEnable() throws RemoteException {
            return mService.get().isBassEnable();
        }

        @Override
        public boolean isVirtualEnable() throws RemoteException {
            return mService.get().isVirtualEnable();
        }

        @Override
        public void createNotify(int bass, int virtual) throws RemoteException {
            mService.get().createNotify(bass,virtual);
        }

        @Override
        public void deleteNotify() throws RemoteException {
            mService.get().deleteNotify();
        }

        @Override
        public boolean isTotalEnable() throws RemoteException {
            return mService.get().isTotalEnable();
        }
    }

    public boolean isTotalEnable() {
        return totalEnable;
    }

    public boolean isMusicActivite(){
        return isPlaying;
    }

    private void setBassLevel(int strengh) {
        if(mBassBoost!=null){
            short level = (short) (strengh * 10);
            try {
                mBassBoost.setStrength(level);
                Log.d("EQService", "==bass:" + level);
            } catch (Exception e) {
                mBassBoost = null;
                isBassEnable = false;
                Log.d("EQService", "EQerror");
            }
        }
    }

    private void setVirtualLevel(int strengh) {
        if(mVitualizer!=null){
            short level = (short) (strengh * 10);
            try {
                mVitualizer.setStrength(level);
                Log.d("EQService", "==vir:" + level);
            } catch (Exception e) {
                mVitualizer = null;
                isVirtualEnable = false;
                Log.d("EQService", "EQerror");
            }
        }
    }

    public void setEqualizerEnable() {
        if (mEqulizer != null)
            mEqulizer.setEnabled(isEQEnable && totalEnable);
        if (mBassBoost != null)
            mBassBoost.setEnabled(isBassEnable && totalEnable);
        if (mVitualizer != null)
            mVitualizer.setEnabled(isVirtualEnable && totalEnable);
    }

    private void setBandLevel(short band, short dbValue) {
        if(mEqulizer!=null){
            short level = (short) (dbValue * 100);
            try {
                mEqulizer.setBandLevel(band,level<minBandLevel?minBandLevel:level > maxBandLevel ? maxBandLevel : level);
                Log.d("EQService","==level:" + level);
            } catch (Exception e) {
                mEqulizer = null;
                isEQEnable = false;
                Log.d("EQService", "EQerror");
            }
        }
    }

    private void setVirtual(int vitualVal) {
        if(mVitualizer!=null){
            int vb = vitualVal * 10;
            try {
                mVitualizer.setStrength((short) vb);
            } catch (Exception e) {
                mVitualizer = null;
                isVirtualEnable = false;
                Log.d("EQService", "BASSerror");
            }
        }
    }

    private void setBass(int bassVal) {
        if(mBassBoost!=null){
            int vb = bassVal * 10;
            try {
                mBassBoost.setStrength((short) vb);
            } catch (Exception e) {
                mBassBoost = null;
                isBassEnable = false;
                Log.d("EQService", "VIRTUALerror");
            }
        }
    }

    private boolean isEQEnable(){
        return (isEQEnable && totalEnable);
    }

    private boolean isBassEnable(){
        return (isBassEnable && totalEnable);
    }

    private boolean isVirtualEnable(){
        return (isVirtualEnable && totalEnable);
    }

    private void setTotalEnable(boolean checkState) {
        totalEnable = checkState;
        setEqualizerEnable();
        wSwitch.performUpdate(EQService.this);
    }

    private void toast() {
        Toast.makeText(EQService.this,"haha_FZ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mreceiver);
        stopService(new Intent(EQService.this, DaemonActiviteService.class));
        mContent = null;
        releaseAudioEffect();
    }

    private void releaseAudioEffect() {
        if (mBassBoost != null) {
            mBassBoost.setEnabled(false);
            mBassBoost.release();
            mBassBoost = null;
        }
        if (mVitualizer != null) {
            mVitualizer.setEnabled(false);
            mVitualizer.release();
            mVitualizer = null;
        }
        if (mEqulizer != null) {
            mEqulizer.setEnabled(false);
            mEqulizer.release();
            mEqulizer = null;
        }
    }
}
