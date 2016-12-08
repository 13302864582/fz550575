package eq.tools.equlizer_globle.Util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

import eq.tools.equlizer_globle.DB.EQDButil;
import eq.tools.equlizer_globle.Entity.BassBoostTheme;
import eq.tools.equlizer_globle.Entity.EqulizerVal;
import eq.tools.equlizer_globle.Entity.SeekBarTheme;
import eq.tools.equlizer_globle.Entity.Theme;
import eq.tools.equlizer_globle.Entity.VirtualizerTheme;
import eq.tools.equlizer_globle.R;

/**
 * Created by Administrator on 2016/11/22 0022.
 */
public class SystemUtil {
    public static ArrayList<EqulizerVal> eqValList;
    public static List<Theme> themesList;
    public static Theme mCurrentTheme;
    public static int mCurEqposition;
    private static float SCREEN_DENSITY;

    public static int[]EQImages=new int[]{
            R.drawable.eq_preselection_02_selector,
            R.drawable.eq_preselection_03_selector,
            R.drawable.eq_preselection_04_selector,
            R.drawable.eq_preselection_05_selector,
            R.drawable.eq_preselection_06_selector,
            R.drawable.eq_preselection_07_selector,
            R.drawable.eq_preselection_08_selector,
            R.drawable.eq_preselection_09_selector,
            R.drawable.eq_preselection_10_selector,
            R.drawable.eq_preselection_11_selector,
    };


    private static void InitStatebar(Context context) {
        Activity activity= (Activity) context;
        int color=PreferenceUtil.getInt(activity,Config.COLOR_STATE,Config.DEFAULT_COLOR_STATE);
        /*if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            // 设置状态栏颜色
            activity.getWindow().setStatusBarColor(color);
            activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }else*/ if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusView(context,color);
            ViewGroup rootView = (ViewGroup) activity.findViewById(R.id.state_bar);
            rootView.addView(statusView);
            rootView.setVisibility(View.VISIBLE);
        }
    }

    public static View createStatusView(Context context, int color){
        Resources resources=context.getResources();
        int resid=resources.getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = resources.getDimensionPixelSize(resid);
        View ststeView=new View(context);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,statusBarHeight);
        ststeView.setLayoutParams(params);
        ststeView.setBackgroundColor(color);
        return ststeView;
    }

    public static void Init(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        SCREEN_DENSITY = dm.density;
        PreferenceUtil.putInt(context,Config.COLOR_STATE,Config.DEFAULT_COLOR_STATE);
        InitStatebar(context);
        boolean isLight=PreferenceUtil.getBoolean(context,Config.ISLIGHTTHEME,true);
        initTheme(isLight);
        initEqualizerVal(context);
    }

    public static void initEqualizerVal(Context context) {
        SystemUtil.mCurEqposition = PreferenceUtil.getInt(context, Config.EQ_STYLE, Config.DEFAULT_EQSTYLE);
        EQDButil dbutil = new EQDButil(context);
        eqValList = dbutil.Query();
    }

    private static void initTheme(boolean isLight) {
        themesList=new ArrayList<>();
        SeekBarTheme seekBarTheme=new SeekBarTheme(R.mipmap.eq_button01,R.mipmap.eq_progress_bar01_on);
        BassBoostTheme bassboostTheme=new BassBoostTheme(R.mipmap.eq_button02,R.mipmap.eq_progress_bar02_on);
        VirtualizerTheme virtualizerTheme=new VirtualizerTheme(R.mipmap.eq_button03,R.mipmap.eq_progress_bar03_on);
        Theme theme=new Theme(seekBarTheme,bassboostTheme,virtualizerTheme);
        themesList.add(theme);

        seekBarTheme=new SeekBarTheme(R.mipmap.eq_progress_bar01);
        bassboostTheme=new BassBoostTheme(R.mipmap.eq_progress_bar02);
        virtualizerTheme=new VirtualizerTheme(R.mipmap.eq_progress_bar03);
        theme=new Theme(seekBarTheme,bassboostTheme,virtualizerTheme);
        themesList.add(theme);
        changeTheme(isLight);
    }

    public static void changeTheme(boolean isLight){
        mCurrentTheme = themesList.get(isLight?0:1);
    }

    public static int dptopx(int dp) {
        return (int) (dp * SCREEN_DENSITY);
    }

    public static void destory() {
        mCurrentTheme = null;
        if (themesList != null) {
            themesList.clear();
        }
        themesList = null;
        eqValList.clear();
        eqValList = null;
    }

}
