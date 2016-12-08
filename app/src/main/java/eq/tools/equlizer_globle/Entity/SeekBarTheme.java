package eq.tools.equlizer_globle.Entity;

import eq.tools.equlizer_globle.R;

/**
 * Created by Administrator on 2016/11/24 0024.
 */
public class SeekBarTheme extends BaseTheme{
    public SeekBarTheme(int pointResOn, int barResOn) {
        super(R.mipmap.eq_progress_bar01_bottom,pointResOn, R.mipmap.eq_button01_top, barResOn, R.mipmap.eq_progress_bar01_off);
    }

    public SeekBarTheme(int barResOn) {
        super(R.mipmap.eq_progress_bar01_bottom,R.mipmap.eq_button01_top, R.mipmap.eq_button01_top, barResOn, R.mipmap.eq_progress_bar01_off);
    }

    public SeekBarTheme() {
    }
}
