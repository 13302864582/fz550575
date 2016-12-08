package eq.tools.equlizer_globle.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import eq.tools.equlizer_globle.Entity.SeekBarTheme;
import eq.tools.equlizer_globle.Entity.Theme;
import eq.tools.equlizer_globle.R;

/**
 * Created by Administrator on 2016/11/24 0024.
 */
public class EQSeekBar extends View{
    private RectF rectF;
    private int realH;
    private double perDbHeight=1f;
    private int dbValue = 0;
    private float mAddY;
    private boolean fromUser;
    private OnSeekBarChangeListener listener;
    private float mLastY;
    private Bitmap SeekBarbg;
    private Bitmap SeekBarColorBit;
    private Bitmap SeekBarPointBit;
    private int ColorBitRes;
    private int PointBitRes;
    private int centerX;
    private int mLastDbValue=-16;
    private Bitmap targetBgBit;
    private Bitmap targetColorBit;
    private int SeekBar_bgRes;
    private int SeekBar_pointRes;

    public EQSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        rectF=new RectF();
        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.EQ_SeekBar);
        SeekBar_bgRes=a.getResourceId(R.styleable.EQ_SeekBar_SeekBg,R.mipmap.eq_progress_bar01_bottom);
        SeekBar_pointRes=a.getResourceId(R.styleable.EQ_SeekBar_SeekBg,R.mipmap.eq_button01_top);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF.left=0;
        rectF.top=h;
        rectF.right=w;
        rectF.bottom=h;
        realH=getHeight()-getPaddingTop()-getPaddingBottom();
        perDbHeight=realH*1.0f/30;
        initBitmap();
        setVal(dbValue);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setVal(int dbValue) {
        this.dbValue=dbValue;
        rectF.top=getPaddingTop()+(float)((15-dbValue)*perDbHeight);
        mAddY=0;
        invalidate();
    }

    public void setTheme(SeekBarTheme theme) {
        ColorBitRes=theme.getBarResOn();
        PointBitRes=theme.getPointResOn();
    }



    public void initBitmap() {
        SeekBarbg=BitmapFactory.decodeResource(getResources(),SeekBar_bgRes);
        SeekBarColorBit=BitmapFactory.decodeResource(getResources(), isEnabled()?ColorBitRes:R.mipmap.eq_progress_bar01_off);
        SeekBarPointBit=BitmapFactory.decodeResource(getResources(),isEnabled()?PointBitRes:R.mipmap.eq_button01_top);
        Matrix matrix = new Matrix();
        matrix.postScale(1.0f, realH*1.0f/SeekBarbg.getHeight());
        targetBgBit = Bitmap.createBitmap(SeekBarbg,0,0,SeekBarbg.getWidth(),SeekBarbg.getHeight(), matrix, false);
        targetColorBit = Bitmap.createBitmap(SeekBarColorBit, 0, 0,SeekBarColorBit.getWidth() ,SeekBarColorBit.getHeight(), matrix, false);
        centerX = (getWidth() - SeekBarColorBit.getWidth())/2;
        SeekBarbg.recycle();
        SeekBarColorBit.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(targetBgBit!=null){
            canvas.drawBitmap(targetBgBit,centerX,getPaddingTop(),null);
        }
        rectF.top=rectF.top+mAddY<getPaddingTop()?getPaddingTop():rectF.top+mAddY>getPaddingTop()+realH?getPaddingTop()+realH:rectF.top+mAddY;
        double v= (realH-(rectF.top-getPaddingTop()))/perDbHeight;
        dbValue = (int) (Math.rint(v)-15);
        if(mLastDbValue != dbValue) {
            mLastDbValue = dbValue;
            dbValue=dbValue>15?15:dbValue<-15?-15:dbValue;
            if(listener != null)
            listener.onSeekBarChange(dbValue, fromUser);
        }
        fromUser=false;

        if(targetColorBit!=null){
            canvas.save();
            canvas.clipRect(rectF);
            canvas.drawBitmap(targetColorBit,centerX,getPaddingTop(),null);
            canvas.restore();
        }

        if(SeekBarPointBit!=null){
            canvas.drawBitmap(SeekBarPointBit,centerX,rectF.top - SeekBarPointBit.getHeight()/2,null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isEnabled())
            return false;
        fromUser=true;
        float y=event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastY=y;
                LightPress(true);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                mAddY=y-mLastY;
                mLastY=y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                LightPress(false);
                invalidate();
                break;
        }
        return true;
    }

    private void LightPress(boolean IsLight) {
        if(IsLight)
        SeekBarPointBit=BitmapFactory.decodeResource(getResources(),R.mipmap.eq_button01);
        else
            SeekBarPointBit=BitmapFactory.decodeResource(getResources(),PointBitRes);
        invalidate();
    }

    public interface OnSeekBarChangeListener{
        void onSeekBarChange(int dbValue, boolean fromUser);
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
        this.listener=listener;
    }

}
