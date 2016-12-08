package eq.tools.equlizer_globle.Util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.HashMap;
import eq.tools.equlizer_globle.IEQAidlInterface;
import eq.tools.equlizer_globle.Service.EQService;

/**
 * Created by Administrator on 2016/11/22 0022.
 */
public class EqulizerUtil {
    public static IEQAidlInterface mSercice =null;
    public static HashMap<Context, ServiceBinder>sConnectionMap=new HashMap<>();

    public static class ServiceToken{
        private ContextWrapper mWrappedContext;
        public ServiceToken(ContextWrapper cw) {
            mWrappedContext=cw;
        }
    }

    public static final boolean isServiceConnected() {
        return mSercice != null;
    }

    public static ServiceToken bindService(Activity activity, ServiceConnection mCallback) {
        Activity realActivity=activity.getParent();
        if(realActivity==null)
            realActivity=activity;
        ContextWrapper cw=new ContextWrapper(realActivity);
        cw.startService(new Intent(cw,EQService.class));
        ServiceBinder sb=new ServiceBinder(mCallback);
        if(cw.bindService(new Intent().setClass(cw,EQService.class),sb,0)){
            sConnectionMap.put(cw,sb);
            return new ServiceToken(cw);
        }
        return null;
    }

    public static void unbindService(ServiceToken token) {
        if(token==null){
            sConnectionMap.clear();
            sConnectionMap = null;
            return;
        }
        ContextWrapper cw = token.mWrappedContext;
        ServiceBinder sb=sConnectionMap.remove(cw);
        if(sb==null)
            return;
        cw.unbindService(sb);
        if(sConnectionMap.isEmpty())
            mSercice=null;
    }


    public static class ServiceBinder implements ServiceConnection{
        private ServiceConnection mCallback;

        public ServiceBinder(ServiceConnection mCallback) {
            this.mCallback=mCallback;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mSercice=IEQAidlInterface.Stub.asInterface(service);
            if(mCallback!=null)
                mCallback.onServiceConnected(name,service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if(mCallback!=null)
                mCallback.onServiceDisconnected(name);
            mSercice=null;
        }
    }

    public static void setBassLevel(int level) {
        try {
            if (mSercice != null)
                mSercice.setBassLevel(level);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void setVirtualLevel(int level) {
        try {
            if (mSercice != null)
                mSercice.setVirtualLevel(level);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void toast() {
                try {
                    if (mSercice != null)
                    mSercice.toast();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
    }


    public static void setBandLevel(int band, int dbValue) {
        try {
            if (mSercice != null)
                mSercice.setBandLevel(band,dbValue);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void setTotalEnable(boolean checkState) {
        try {
            if (mSercice != null)
                mSercice.setTotalEnable(checkState);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static boolean isTotalEnable() {
        try {
            if (mSercice != null)
               return mSercice.isTotalEnable();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean isEQEnable() {
        try {
            if (mSercice != null)
               return mSercice.isEQEnable();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isBassEnable() {
        try {
            if (mSercice != null)
                return mSercice.isBassEnable();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isVirtualEnable() {
        try {
            if (mSercice != null)
                return mSercice.isVirtualEnable();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void createNotify(int bass,int virtual) {
        try {
            if (mSercice != null)
                 mSercice.createNotify(bass,virtual);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void deleteNotify() {
        try {
            if (mSercice != null)
                 mSercice.deleteNotify();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
