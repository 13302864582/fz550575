package eq.tools.equlizer_globle.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;
import eq.tools.equlizer_globle.R;

/**
 * Created by Administrator on 2016/4/13 0013.
 */
public class LightText extends TextView{
    public void setShadowcolor(int shadowcolor) {
        this.shadowcolor = shadowcolor;
    }

    private int shadowcolor;

    public LightText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.LightTextView);
        shadowcolor = a.getColor(R.styleable.LightTextView_Lightshadow, Color.parseColor("#FF0000"));
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 设定阴影(柔边, X 轴位移, Y 轴位移, 阴影颜色)
        this.setShadowLayer(2, 1, 1, shadowcolor);
    }
}
