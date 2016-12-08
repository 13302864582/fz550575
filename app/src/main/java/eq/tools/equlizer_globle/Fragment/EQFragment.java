package eq.tools.equlizer_globle.Fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.coocent.tool.visualizer.VisualParams;
import net.coocent.tool.visualizer.VisualizerActivity;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import eq.tools.equlizer_globle.Adapter.RecyclerAdapter;
import eq.tools.equlizer_globle.DB.EQDButil;
import eq.tools.equlizer_globle.Dialog.AlertDialogFragment;
import eq.tools.equlizer_globle.Dialog.DialogUtil;
import eq.tools.equlizer_globle.EQActivity;
import eq.tools.equlizer_globle.Entity.EqulizerVal;
import eq.tools.equlizer_globle.R;
import eq.tools.equlizer_globle.Service.DaemonActiviteService;
import eq.tools.equlizer_globle.Service.EQService;
import eq.tools.equlizer_globle.Util.Config;
import eq.tools.equlizer_globle.Util.EqulizerUtil;
import eq.tools.equlizer_globle.Util.PreferenceUtil;
import eq.tools.equlizer_globle.Util.SystemUtil;
import eq.tools.equlizer_globle.View.ArcProgressBar;
import eq.tools.equlizer_globle.View.EQSeekBar;
import eq.tools.equlizer_globle.View.EqualizerView;
import eq.tools.equlizer_globle.View.LightText;
import eq.tools.equlizer_globle.View.SwitchButton;

/**
 * Created by Administrator on 2016/11/22 0022.
 */
public class EQFragment extends Fragment implements View.OnClickListener {
    public static final String EQ_SWITCH_ACTION = "eq_switch_action";
    public static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    @InjectView(R.id.home_menu)
    ImageView homeMenu;
    @InjectView(R.id.switch_eq)
    SwitchButton switchEq;
    @InjectView(R.id.eq_save)
    ImageView eqSave;
    @InjectView(R.id.eq_style)
    TextView eqStyle;
    @InjectView(R.id.head)
    LinearLayout head;
    @InjectView(R.id.seekbarVal1)
    LightText seekbarVal1;
    @InjectView(R.id.seekbar1)
    EQSeekBar seekbar1;
    @InjectView(R.id.seekbarVal2)
    LightText seekbarVal2;
    @InjectView(R.id.seekbar2)
    EQSeekBar seekbar2;
    @InjectView(R.id.seekbarVal3)
    LightText seekbarVal3;
    @InjectView(R.id.seekbar3)
    EQSeekBar seekbar3;
    @InjectView(R.id.seekbarVal4)
    LightText seekbarVal4;
    @InjectView(R.id.seekbar4)
    EQSeekBar seekbar4;
    @InjectView(R.id.seekbarVal5)
    LightText seekbarVal5;
    @InjectView(R.id.seekbar5)
    EQSeekBar seekbar5;
    @InjectView(R.id.bass)
    ArcProgressBar bass;
    @InjectView(R.id.virtal)
    ArcProgressBar virtal;
    @InjectView(R.id.bottom_img1)
    EqualizerView bottomImg1;
    @InjectView(R.id.bottom_tvtitle)
    TextView musicTitle;
    @InjectView(R.id.bottom_tv1artist)
    TextView musicArtist;
    @InjectView(R.id.bottom_play)
    ImageButton bottomPlay;
    @InjectView(R.id.bottom_pre)
    ImageButton bottomPre;
    @InjectView(R.id.bottom_next)
    ImageButton bottomNext;
    @InjectView(R.id.play_tv)
    TextView playTv;
    @InjectView(R.id.play_bt)
    ImageButton playBt;
    @InjectView(R.id.bottom_play_layout)
    RelativeLayout bottomPlayLayout;
    @InjectView(R.id.opennowplay)
    RelativeLayout opennowplay;
    private int[] seekBarVal = new int[5];
    private ArrayList<EQSeekBar> eqSeekBars;
    private ArrayList<TextView> eqSeekBarVals;
    private boolean isVibrate;
    private int bassPerecent;
    private int vitrualPercent;
    private Context mContext;
    private Vibrator vb;
    private int[] eqVal;
    private PopupWindow eqPopupWindow;
    private RecyclerView eqstyleRecyclerView;
    private Toolbar eqstyletoolbar;
    private RecyclerAdapter adapter;
    private EQDButil db;
    private boolean hasSongPage = false;

    public void openLibrary() {
        Intent intent = new Intent(getActivity(), VisualizerActivity.class);
        intent.putExtra(VisualParams.AUDIOSESSION_ID, 0);
        startActivity(intent);
    }

    public void setVibrate(boolean vibrate) {
        isVibrate = vibrate;
    }

    public void setLightTheme(boolean lightTheme) {
        SystemUtil.changeTheme(lightTheme);
        initTheme();
        invalidate();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        isVibrate = PreferenceUtil.getBoolean(mContext, Config.ISVIRBATEENABLE);
        bassPerecent = PreferenceUtil.getInt(mContext, Config.BASSVAL, Config.DEFAULT_BASSVAL);
        vitrualPercent = PreferenceUtil.getInt(mContext, Config.VIRTULVAL, Config.DEFAULT_VIRTULVAL);
        db = new EQDButil(getActivity());

    }

    //用于接收通知栏的通知事件
    BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //切换Equalizer是否可用
            if (action.equals(EQService.PACKPAGE_NOW_PLAYING_NAME)) {
                artist = intent.getStringExtra("artist");
                trackName = intent.getStringExtra("track");
                packagename = intent.getStringExtra("packagename");
                Log.e("===message", artist + "============" + trackName);
                setMusicMess();
            } else if (action.equals(EQService.PLYAER_STATUS_ACTION)) {
                boolean isActive = intent.getBooleanExtra("isPlaying", false);
                Log.e("===isActive", "============" + isActive);
                checkMusicActive(isActive);
            }else  if (action.equals(EQ_SWITCH_ACTION)) {
                EQActivity activity= (EQActivity) getActivity();
                activity.switchEQ();
                invalidate();
            }
            musicArtist.setTextColor(Color.WHITE);
            hasSongPage = true;
            PreferenceUtil.putBoolean(context, "hasplaybox", true);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_eq, container, false);
        ButterKnife.inject(this, rootView);
        initView();
        initpop();

        return rootView;
    }

    public void initTheme() {
        for (int i = 0; i < eqSeekBars.size(); i++) {
            final int finalI = i;
            eqSeekBars.get(i).setEnabled(EqulizerUtil.isEQEnable());
            eqSeekBars.get(i).setTheme(SystemUtil.mCurrentTheme.getSeekBar());
        }
        bass.setEnabled(EqulizerUtil.isBassEnable());
        bass.setTheme(SystemUtil.mCurrentTheme.getBass());
        virtal.setEnabled(EqulizerUtil.isVirtualEnable());
        virtal.setTheme(SystemUtil.mCurrentTheme.getVirtual());
        switchEq.setChecked(EqulizerUtil.isTotalEnable());
    }

    private void initListener() {
        bass.setValue(bassPerecent);
        virtal.setValue(vitrualPercent);
        for (int i = 0; i < eqSeekBars.size(); i++) {
            final int finalI = i;
            eqSeekBars.get(i).setOnSeekBarChangeListener(new EQSeekBar.OnSeekBarChangeListener() {
                @Override
                public void onSeekBarChange(int dbValue, boolean fromUser) {
                    seekBarVal[finalI] = dbValue;
                    eqSeekBarVals.get(finalI).setText(dbValue + "");
                    if (fromUser) {
                        setBandLevel(finalI, dbValue);
                        checkSeekVal();
                    }
                    if (isVibrate)
                        vb.vibrate(new long[]{0, 10}, -1);
                }
            });
        }
        opennowplay.setOnClickListener(this);
        playBt.setOnClickListener(this);
        playTv.setOnClickListener(this);
        bottomPlay.setOnClickListener(this);
        bottomPre.setOnClickListener(this);
        bottomNext.setOnClickListener(this);
        bottomImg1.setOnClickListener(EQFragment.this);
        homeMenu.setOnClickListener(EQFragment.this);
        eqStyle.setOnClickListener(EQFragment.this);
        eqSave.setOnClickListener(EQFragment.this);
        switchEq.setOnChangedListener(new SwitchButton.OnChangedListener() {
            @Override
            public void OnChanged(boolean CheckState) {
                power(CheckState);
            }
        });

        bass.setOnPercentChangeListener(new ArcProgressBar.OnPercentChangeListener() {
            @Override
            public void onChange(int percent) {
                bassPerecent = percent;
                setBassLevel(percent);
                UpdateNotify(true);
                if (isVibrate)
                    vb.vibrate(new long[]{0, 10}, -1);
            }
        });
        virtal.setOnPercentChangeListener(new ArcProgressBar.OnPercentChangeListener() {
            @Override
            public void onChange(int percent) {
                vitrualPercent = percent;
                setVirtualLevel(percent);
                UpdateNotify(true);
                if (isVibrate)
                    vb.vibrate(new long[]{0, 10}, -1);
            }
        });
    }

    private void initpop() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.eqlist_window, null);
        int h = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        int w = getActivity().getWindowManager().getDefaultDisplay().getWidth();

        eqPopupWindow = new PopupWindow(v, w, (int) (h - SystemUtil.dptopx(20)));
        eqPopupWindow.setBackgroundDrawable(new ColorDrawable());
        eqPopupWindow.setFocusable(true);
        eqPopupWindow.setOutsideTouchable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                eqPopupWindow.setEnterTransition(new Fade());
                eqPopupWindow.setExitTransition(new Fade());
            }
        }
        eqPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
        eqstyleRecyclerView = (RecyclerView) v.findViewById(R.id.eq_style_RecyclerView);
        eqstyletoolbar = (Toolbar) v.findViewById(R.id.eq_style_toolbar);
        eqstyletoolbar.setTitle("Select a preset");
        eqstyletoolbar.setNavigationIcon(R.mipmap.ic_back);
        eqstyletoolbar.setTitleTextColor(Color.WHITE);
        eqstyletoolbar.setBackgroundColor(Color.parseColor("#1a1a1a"));
        eqstyletoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eqPopupWindow.dismiss();
            }
        });

        eqstyleRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        eqstyleRecyclerView.setHasFixedSize(true);

        adapter = new RecyclerAdapter(getActivity());
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position < 0)
                    position = 0;
                SystemUtil.mCurEqposition = position;
                adapter.notifyDataSetChanged();
                changeEqValue(position);
                eqStyle.setText(SystemUtil.eqValList.get(position).getName());
                eqSave.setVisibility(View.GONE);
            }
        });
        eqstyleRecyclerView.setAdapter(adapter);
    }

    private void power(boolean CheckState) {
        EqulizerUtil.setTotalEnable(CheckState);
        invalidate();
    }

    private void checkSeekVal() {
        boolean isEqual = false;
        int j = 0;
        String seekBarValStr = Arrays.toString(seekBarVal);
        for (; j < SystemUtil.eqValList.size(); j++) {
            int[] presetEqVal = SystemUtil.eqValList.get(j).getValue();
            if (seekBarValStr.equals(Arrays.toString(presetEqVal))) {
                isEqual = true;
                break;
            }
        }
        if (!isEqual) {
            eqStyle.setText(R.string.custom);
            eqSave.setVisibility(View.VISIBLE);
        } else {
            eqStyle.setText(SystemUtil.eqValList.get(j).getName());
            eqSave.setVisibility(View.GONE);
        }
    }

    private void invalidate() {
        for (int i = 0; i < eqSeekBars.size(); i++) {
            final int finalI = i;
            eqSeekBars.get(i).setEnabled(EqulizerUtil.isEQEnable());
            eqSeekBars.get(i).initBitmap();
            eqSeekBars.get(i).invalidate();
        }

        bass.setEnabled(EqulizerUtil.isBassEnable());
        bass.initBitmap();
        bass.invalidate();

        virtal.setEnabled(EqulizerUtil.isVirtualEnable());
        virtal.initBitmap();
        virtal.invalidate();
    }

    private void setBandLevel(int band, int dbValue) {
        EqulizerUtil.setBandLevel(band, dbValue);
    }

    private void setBassLevel(int level) {
        EqulizerUtil.setBassLevel(level);
    }

    private void setVirtualLevel(int level) {
        EqulizerUtil.setVirtualLevel(level);
    }

    private void initView() {
        head.setBackgroundColor(PreferenceUtil.getInt(mContext, Config.COLOR_STATE, Config.DEFAULT_COLOR_STATE));
        eqSeekBars = new ArrayList<>();
        eqSeekBars.add(seekbar1);
        eqSeekBars.add(seekbar2);
        eqSeekBars.add(seekbar3);
        eqSeekBars.add(seekbar4);
        eqSeekBars.add(seekbar5);

        eqSeekBarVals = new ArrayList<>();
        eqSeekBarVals.add(seekbarVal1);
        eqSeekBarVals.add(seekbarVal2);
        eqSeekBarVals.add(seekbarVal3);
        eqSeekBarVals.add(seekbarVal4);
        eqSeekBarVals.add(seekbarVal5);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vb = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        initTheme();
        initListener();
        initData();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EQ_SWITCH_ACTION);
        intentFilter.addAction(EQService.PACKPAGE_NOW_PLAYING_NAME);
        intentFilter.addAction(EQService.PLYAER_STATUS_ACTION);
        getActivity().registerReceiver(bReceiver, intentFilter);
        AudioManager ama = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        boolean isplay = ama.isMusicActive();
        if (isplay) {
            checkMusicActive(true);
        } else {
            checkMusicActive(false);
        }
        setMusicMess();
    }

    private void changeEqValue(int position) {
        eqVal = SystemUtil.eqValList.get(position).getValue();
        for (int i = 0; i < eqVal.length; i++) {
            eqSeekBars.get(i).setVal(eqVal[i]);
            eqSeekBarVals.get(i).setText(eqVal[i] + "");
            EqulizerUtil.setBandLevel(i,eqVal[i]);
        }
    }

    private void initData() {
        boolean isCustom = PreferenceUtil.getBoolean(mContext, Config.IS_CUSTOM, Config.DEFAULT_CUSTOM);
        if (isCustom) {
            eqStyle.setText(getResources().getString(R.string.custom));
            eqVal = new int[5];
            eqVal[0] = PreferenceUtil.getInt(mContext, Config.K_BAR60, Config.V_BAR);
            eqVal[1] = PreferenceUtil.getInt(mContext, Config.K_BAR230, Config.V_BAR);
            eqVal[2] = PreferenceUtil.getInt(mContext, Config.K_BAR910, Config.V_BAR);
            eqVal[3] = PreferenceUtil.getInt(mContext, Config.K_BAR36H, Config.V_BAR);
            eqVal[4] = PreferenceUtil.getInt(mContext, Config.K_BAR14K, Config.V_BAR);
            for (int i = 0; i < eqVal.length; i++) {
                eqSeekBars.get(i).setVal(eqVal[i]);
                eqSeekBarVals.get(i).setText(eqVal[i] + "");
                EqulizerUtil.setBandLevel(i,eqVal[i]);
            }
        } else {
            if (SystemUtil.eqValList != null) {
                eqStyle.setText(SystemUtil.eqValList.get(SystemUtil.mCurEqposition).getName());
            }
            changeEqValue(SystemUtil.mCurEqposition);
        }
    }

    private String artist;
    private String trackName;
    private String packagename;

    public void checkMusicActive(boolean isMusic) {
        if (isMusic) {
            playTv.setVisibility(View.GONE);
            playBt.setVisibility(View.GONE);
            bottomPlayLayout.setVisibility(View.VISIBLE);
            bottomPlay.setBackgroundResource(R.drawable.play_button_selector_stop);
            bottomImg1.animateBars();
        } else {
            if (!hasSongPage) {
                playTv.setVisibility(View.VISIBLE);
                playBt.setVisibility(View.VISIBLE);
                bottomPlayLayout.setVisibility(View.INVISIBLE);
            } else {
                playTv.setVisibility(View.INVISIBLE);
                playBt.setVisibility(View.INVISIBLE);
                bottomPlayLayout.setVisibility(View.VISIBLE);
                bottomPlay.setBackgroundResource(R.drawable.play_button_selector_on);
            }
            bottomImg1.stopBars();
        }
    }

    public void setMusicMess() {
        musicTitle.setText(trackName == null ? "Unknown" : trackName);
        musicArtist.setText(artist == null ? "Unknown" : artist);
    }

    public static enum MediaIntent {
        NEXT, PLAY_PAUSE, PREVIOUS;
    }

    private void playchange(MediaIntent paramMediaIntent) {
        Context mcontext = getActivity().getApplicationContext();
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                AudioManager audio = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
                audio.dispatchMediaKeyEvent(event);
                audio.dispatchMediaKeyEvent(localKeyEvent);
            } else {
                paramContext = (IBinder) Class.forName("android.os.ServiceManager").getDeclaredMethod("checkService", new Class[]{String.class}).invoke(null, new Object[]{Context.AUDIO_SERVICE});
                paramContext = Class.forName("android.media.IAudioService$Stub").getDeclaredMethod("asInterface", new Class[]{IBinder.class}).invoke(null, new Object[]{paramContext});
                Class.forName("android.media.IAudioService").getDeclaredMethod("dispatchMediaKeyEvent", new Class[]{KeyEvent.class}).invoke(paramContext, new Object[]{event});
                Class.forName("android.media.IAudioService").getDeclaredMethod("dispatchMediaKeyEvent", new Class[]{KeyEvent.class}).invoke(paramContext, new Object[]{localKeyEvent});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.opennowplay:
                PackageManager manager = getActivity().getPackageManager();
                Intent open = manager.getLaunchIntentForPackage(packagename);
                if (packagename != null && open != null) {
                    this.startActivity(open);
                } else {
                    Open_Player();
                }
                break;
            case R.id.bottom_play:
                playchange(MediaIntent.PLAY_PAUSE);
                break;
            case R.id.bottom_pre:
                playchange(MediaIntent.PREVIOUS);
                break;
            case R.id.bottom_next:
                playchange(MediaIntent.NEXT);
                break;
            case R.id.home_menu:
                EQActivity activity = (EQActivity) mContext;
                activity.OpenDrawer();
                break;
            case R.id.eq_style:
                eqPopupWindow.showAtLocation(view, Gravity.TOP, 0, 0);
                break;
            case R.id.bottom_img1:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
                    Log.e("requestPermissions", "==============");
                } else {
                    openLibrary();
                }
                break;
            case R.id.play_tv:
            case R.id.play_bt:
                try {
                    Open_Player();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "can_not_open_musicplayer", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.eq_save:
                int accentcolor = Color.parseColor("#263238");
                View v = LayoutInflater.from(getActivity()).inflate(
                        R.layout.save_preset, null);
                TextView save = (TextView) v.findViewById(R.id.save);
                TextView cancel = (TextView) v.findViewById(R.id.cancel);
                save.setTextColor(accentcolor);
                cancel.setTextColor(accentcolor);
                final EditText renameet = (EditText) v.findViewById(R.id.rename_etv);
                renameet.requestFocus();
                DialogUtil.showAlertDialog(0, "Save", v, new AlertDialogFragment.DialogOnClickListener() {
                    @Override
                    public void onPositiveClick(DialogInterface dialog) {
                        final String savename = renameet.getText().toString();
                        if (savename == null || savename.isEmpty()) {
                            Toast.makeText(getActivity(), R.string.name_not_null, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (savename.length() >= 10) {
                            Toast.makeText(getActivity(), R.string.name_max_length, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        final EqulizerVal entity = new EqulizerVal();
                        entity.setName(savename);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int[] vals = new int[5];
                                System.arraycopy(seekBarVal, 0, vals, 0, vals.length);
                                entity.setValue(vals);
                                int id = db.saveEqVal(entity);
                                if (id < 0) {
                                    Toast.makeText(getActivity(), R.string.fail, Toast.LENGTH_SHORT).show();
                                } else {
                                    entity.setId(id);
                                    SystemUtil.eqValList = db.Query();
                                    SystemUtil.mCurEqposition = SystemUtil.eqValList.size() - 1;
                                    eqStyle.setText(savename);
                                    Toast.makeText(getActivity(), R.string.success, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, 500);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegativeClick(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    private void Open_Player() {
        Intent intent;
        intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
        startActivity(intent);
    }

    public void UpdateNotify(boolean checkState) {
        if (checkState)
            EqulizerUtil.createNotify(bassPerecent, vitrualPercent);
        else
            EqulizerUtil.deleteNotify();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(bReceiver);
        PreferenceUtil.putInt(mContext, Config.BASSVAL, bassPerecent);
        PreferenceUtil.putInt(mContext, Config.VIRTULVAL, vitrualPercent);
        PreferenceUtil.putInt(mContext, Config.EQ_STYLE, SystemUtil.mCurEqposition);
        boolean flag = (eqStyle != null && eqStyle.getText().toString().equals(getResources().getString(R.string.custom)));
        if (flag) {
            PreferenceUtil.putBoolean(mContext, Config.IS_CUSTOM, true);
            PreferenceUtil.putInt(mContext, Config.K_BAR60, seekBarVal[0]);
            PreferenceUtil.putInt(mContext, Config.K_BAR230, seekBarVal[1]);
            PreferenceUtil.putInt(mContext, Config.K_BAR910, seekBarVal[2]);
            PreferenceUtil.putInt(mContext, Config.K_BAR36H, seekBarVal[3]);
            PreferenceUtil.putInt(mContext, Config.K_BAR14K, seekBarVal[4]);
        } else {
            PreferenceUtil.putBoolean(mContext, Config.IS_CUSTOM, false);
        }
        ButterKnife.reset(this);
    }
}
