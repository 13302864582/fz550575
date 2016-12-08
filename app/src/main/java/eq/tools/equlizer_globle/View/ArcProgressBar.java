package eq.tools.equlizer_globle.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import eq.tools.equlizer_globle.Entity.BaseTheme;
import eq.tools.equlizer_globle.R;
import eq.tools.equlizer_globle.Util.SystemUtil;

/**
 * Created by Administrator on 2016/11/28 0028.
 */
public class ArcProgressBar extends View{
    private final int bgDefault;
    private final RectF mOval;
    private final float r;
    private final int pointDefault;
    private final Paint mPaint2;
    private float mAngle = 0;
    private final Bitmap bgBitmap;
    private final float bgW;
    private final float bgH;
    private final double maxwidth;
    private final float o_x;
    private final float o_y;
    private OnPercentChangeListener listener;
    private int ColorBitRes;
    private int PointBitRes;
    private Paint mPaint;
    private Bitmap mBitmap;
    private boolean onTouch;
    private float down_x;
    private float down_y;
    private float current_degree;
    private int x1;
    private int y1;
    private Bitmap mPointBitmap;
    private int DegreeBggain = 0;
    private int DegreeFinal = 330;

    public ArcProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ArcProgress);
        bgDefault = a.getResourceId(R.styleable.ArcProgress_progressBg,
                R.mipmap.eq_progress_bar02_bottom);
        pointDefault = a.getResourceId(R.styleable.ArcProgress_progressOnBg,
                R.mipmap.eq_button01_top);
        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setFilterBitmap(true);

        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint2.setColor(Color.WHITE);
        mPaint2.setTextSize(SystemUtil.dptopx(15));
        mPaint2.setFakeBoldText(true);

        mOval = new RectF();
        bgBitmap = BitmapFactory.decodeResource(getResources(), bgDefault);
        bgW = bgBitmap.getWidth();
        bgH = bgBitmap.getHeight();
        maxwidth = Math.sqrt(bgW * bgW + bgH * bgH);
        o_x = o_y = (float) (maxwidth / 2);
        r=o_x/2;
    }

    public void setTheme(BaseTheme theme) {
        ColorBitRes=theme.getBarResOn();
        PointBitRes=theme.getPointResOn();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initBitmap();
    }

    private int mLastPercent = 0;

    private void sendChangeListener() {
        int nowPercent = (int) (mAngle / (DegreeFinal*1.0f/100));
        if (nowPercent != mLastPercent) {
            mLastPercent = nowPercent;
            if (listener != null)
                listener.onChange(nowPercent);
        }
    }

    public void initBitmap() {
        mPointBitmap= BitmapFactory.decodeResource(getResources(),isEnabled()?PointBitRes:R.mipmap.eq_button01_top);
        mBitmap = BitmapFactory.decodeResource(getResources(),isEnabled()?ColorBitRes: R.mipmap.eq_progress_bar02_off);
        Matrix m = new Matrix();
        final float bw = mBitmap.getWidth();
        final float bh = mBitmap.getHeight();
        RectF src = new RectF(0, 0, bw, bh);
        RectF dst = new RectF((getWidth() - bw) / 2, (getHeight() - bh) / 2, (getWidth() + bw) / 2, (getHeight() + bh) / 2);
        m.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
        Shader shader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        shader.setLocalMatrix(m);
        mPaint.setShader(shader);
        m.mapRect(mOval, src);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int)bgW,(int)bgH);
    }

    public void setValue(int v) {
        mAngle = DegreeFinal * (v * 1.0f / 100);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isEnabled())
            return false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouch = true;
                down_x = event.getX();
                down_y = event.getY();
                current_degree = detaDegree(o_x, o_y, down_x, down_y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                down_x = event.getX();
                down_y = event.getY();
                float degree = detaDegree(o_x, o_y, down_x, down_y);
                float dete = degree - current_degree;

                if (dete < -270) {
                    dete = dete + 360;
                } else if (dete > 270) {
                    dete = dete - 360;
                }
                current_degree = degree;
                mAngle += dete;
                if (mAngle > DegreeFinal)
                    mAngle = DegreeFinal;
                if (mAngle < DegreeBggain)
                    mAngle = DegreeBggain;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                invalidate();
                onTouch = false;
                break;
        }
        return true;
    }

    private float detaDegree(float o_x, float o_y, float down_x, float down_y) {
        float detaX = down_x - o_x;
        float detaY = down_y - o_y;
        double d;
        if(detaX!=0){
            float tan = Math.abs(detaY / detaX);
            double d0=Math.atan(tan);
            if(detaX>0){
                if(detaY>=0){
                    d=d0;
                }else {
                    d=2* Math.PI-d0;
                }
            }else {
                if(detaY>=0){
                    d=Math.PI-d0;
                }else {
                    d=Math.PI+d0;
                }
            }
        }else {
            if (detaY > 0) {
                d = Math.PI / 2;
            } else {
                d = -Math.PI / 2;
            }
        }
        return (float) ((d * 180) / Math.PI);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        sendChangeListener();
        if(bgBitmap==null)
            return;
            canvas.drawBitmap(bgBitmap,(getWidth() - bgW) / 2, (getHeight() - bgH) / 2, null);
        canvas.drawArc(mOval,78, mAngle, true, mPaint);
        x1 = (int) (getWidth() / 2 + r * Math.cos((mAngle + 90) * Math.PI / 180));
        y1 = (int) (getHeight() / 2 + r * Math.sin((mAngle + 90) * Math.PI / 180));

        if (mPointBitmap != null)
            canvas.drawBitmap(mPointBitmap, x1 - mPointBitmap.getWidth() / 2, y1 - mPointBitmap.getHeight() / 2, null);

        int val=(int) (mAngle / 3.3f);
        float height =getHeight()/2+SystemUtil.dptopx(8);
        float with = getWidth()/2-(val==100?SystemUtil.dptopx(18):val==0? SystemUtil.dptopx(10): val>9?SystemUtil.dptopx(14):SystemUtil.dptopx(9));
        canvas.drawText(String.valueOf(val)+"%",with,height,mPaint2);
    }

    public void setOnPercentChangeListener(OnPercentChangeListener listener) {
        this.listener = listener;
    }

    public interface OnPercentChangeListener {
        public void onChange(int percent);
    }
}
