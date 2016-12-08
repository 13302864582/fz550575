package eq.tools.equlizer_globle;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import net.coocent.tool.visualizer.VisualParams;
import net.coocent.tool.visualizer.VisualizerActivity;

import eq.tools.equlizer_globle.Fragment.EQFragment;
import eq.tools.equlizer_globle.Service.DaemonActiviteService;
import eq.tools.equlizer_globle.Service.EQService;
import eq.tools.equlizer_globle.Util.Config;
import eq.tools.equlizer_globle.Util.EqulizerUtil;
import eq.tools.equlizer_globle.Util.PreferenceUtil;
import eq.tools.equlizer_globle.Util.SystemUtil;
import eq.tools.equlizer_globle.View.EQSeekBar;
import eq.tools.equlizer_globle.View.SwitchButton;

import static eq.tools.equlizer_globle.Util.EqulizerUtil.mSercice;

public class EQActivity extends AppCompatActivity implements ServiceConnection {
    public static Context instance;
    protected SwitchButton switchNotify;
    protected SwitchButton switchVibrate;
    protected SwitchButton switchLight;
    protected DrawerLayout drawer;
    private EqulizerUtil.ServiceToken mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_eq);
        SystemUtil.Init(this);
        instance=this;
        initView();
        initListener();
        startService(new Intent(EQActivity.this, EQService.class));
        startService(new Intent(EQActivity.this, DaemonActiviteService.class));
        mToken = EqulizerUtil.bindService(this, this);
        if (mToken == null) {
            Toast.makeText(EQActivity.this, R.string.app_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        switchNotify.setChecked(PreferenceUtil.getBoolean(EQActivity.this,Config.ISNOTIFYENABLE));
        switchVibrate.setChecked(PreferenceUtil.getBoolean(EQActivity.this,Config.ISVIRBATEENABLE));
        switchLight.setChecked(PreferenceUtil.getBoolean(EQActivity.this,Config.ISLIGHTTHEME));
    }

    private void initView() {
        switchNotify = (SwitchButton) findViewById(R.id.switch_notify);
        switchVibrate = (SwitchButton) findViewById(R.id.switch_vibrate);
        switchLight = (SwitchButton) findViewById(R.id.switch_light);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
    }

    private void initListener() {
        switchNotify.setOnChangedListener(new SwitchButton.OnChangedListener() {
            @Override
            public void OnChanged(boolean CheckState) {
                PreferenceUtil.putBoolean(EQActivity.this,Config.ISNOTIFYENABLE,CheckState);
                EQFragment eqFragment= (EQFragment) getSupportFragmentManager().findFragmentByTag(Config.FRAG_EQ);
                eqFragment.UpdateNotify(CheckState);
            }
        });
        switchVibrate.setOnChangedListener(new SwitchButton.OnChangedListener() {
            @Override
            public void OnChanged(boolean CheckState) {
                PreferenceUtil.putBoolean(EQActivity.this,Config.ISVIRBATEENABLE,CheckState);
                EQFragment eqFragment= (EQFragment) getSupportFragmentManager().findFragmentByTag(Config.FRAG_EQ);
                eqFragment.setVibrate(CheckState);
            }
        });
        switchLight.setOnChangedListener(new SwitchButton.OnChangedListener() {
            @Override
            public void OnChanged(boolean CheckState) {
                PreferenceUtil.putBoolean(EQActivity.this,Config.ISLIGHTTHEME,CheckState);
                EQFragment eqFragment= (EQFragment) getSupportFragmentManager().findFragmentByTag(Config.FRAG_EQ);
                eqFragment.setLightTheme(CheckState);
            }
        });
    }

    private void InitContent() {
        EQFragment eqFragment = new EQFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, eqFragment, Config.FRAG_EQ).commit();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mSercice = IEQAidlInterface.Stub.asInterface(service);
        try {
            mSercice.setTotalEnable(true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        InitContent();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mSercice = null;
    }

    public void OpenDrawer() {
        drawer.openDrawer(GravityCompat.START);
    }

    public void onSideItemClick(View v) {
        switch (v.getId()) {
            case R.id.menu_rate:

                break;
            case R.id.menu_recommand:

                break;
            case R.id.menu_spectrum:

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (EQFragment.MY_PERMISSIONS_REQUEST_RECORD_AUDIO == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(EQActivity.this, VisualizerActivity.class);
                intent.putExtra(VisualParams.AUDIOSESSION_ID, 0);
                startActivity(intent);
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.apply_permission);
                builder.setMessage(R.string.no_permission_sum);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //getAppDetailSetting();
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(EQActivity.this,
                                Manifest.permission.RECORD_AUDIO)) {
                            // 没有取得权限，且点了不再询问
                            getAppDetailSetting();
                            finish();
                        } else {
                            // 没有取得权限，没有点击不再询问，可以再次申请权限
                            ActivityCompat.requestPermissions(EQActivity.this,
                                    new String[]{Manifest.permission.RECORD_AUDIO}, EQFragment.MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
                        }
                    }
                });
                builder.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        }
    }

    private void getAppDetailSetting() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        SystemUtil.destory();
        EqulizerUtil.unbindService(mToken);
        try {
            if(!EqulizerUtil.isTotalEnable()){
                stopService(new Intent(EQActivity.this, DaemonActiviteService.class));
                sendBroadcast(new Intent(EQService.EXIT));
                EqulizerUtil.deleteNotify();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PreferenceUtil.putBoolean(instance,"preference_title",true);
        super.onDestroy();
    }

    public void switchEQ() {
        switchLight.setChecked(EqulizerUtil.isTotalEnable());
    }
}
