package eq.tools.equlizer_globle.Entity;

import eq.tools.equlizer_globle.R;

/**
 * Created by Administrator on 2016/11/24 0024.
 */
public abstract class CircleTheme extends BaseTheme{
    public CircleTheme() {
    }

    public CircleTheme(int pointResOn,int barResOn) {
        super(R.mipmap.eq_progress_bar02_bottom, pointResOn, R.mipmap.eq_button01_top, barResOn,R.mipmap.eq_progress_bar02_off);
    }

    public CircleTheme(int barResOn) {
        super(R.mipmap.eq_progress_bar02_bottom, R.mipmap.eq_button01_top, R.mipmap.eq_button01_top, barResOn,R.mipmap.eq_progress_bar02_off);
    }
}
