package eq.tools.equlizer_globle.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import eq.tools.equlizer_globle.R;
import eq.tools.equlizer_globle.Util.Config;
import eq.tools.equlizer_globle.Util.PreferenceUtil;

/**
 * Created by Administrator on 2016/11/23 0023.
 */
public class SwitchButton extends View implements View.OnTouchListener{
    private int mOnBg=0;
    private int mOffBg=0;
    private int width;
    private int height;
    private Context mContext;
    private boolean isChecked;
    private OnChangedListener onChangeListener;

    public SwitchButton(Context context) {
        super(context);
        init(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if(attrs!=null){
            //将自定义属性的值设置到对应的变量保存
            TypedArray t=getContext().obtainStyledAttributes(attrs, R.styleable.SwitchButton);
            mOnBg = t.getResourceId(R.styleable.SwitchButton_onImage, R.mipmap.side_button01_on);
            mOffBg = t.getResourceId(R.styleable.SwitchButton_offImage, R.mipmap.side_button01_off);
            t.recycle();
            //测量图片宽高
            Bitmap mBitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.side_button01_on);
            width = mBitmap.getWidth();
            height = mBitmap.getHeight();
            mBitmap.recycle();
        }
        mContext = context;
        setOnTouchListener(this);
        isChecked=PreferenceUtil.getBoolean(context, Config.SWITCHBUTTON,true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(width>0 && height>0){
            setMeasuredDimension(width,height);
        }else
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isChecked)
            setBackgroundResource(mOnBg);
        else
            setBackgroundResource(mOffBg);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                isChecked = isChecked?false:true;
                if (onChangeListener != null) {
                    onChangeListener.OnChanged(isChecked);
                }
                PreferenceUtil.putBoolean(mContext,Config.SWITCHBUTTON,isChecked);
                break;
        }
        invalidate();
        return true;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean flag) {
        this.isChecked = flag;
        invalidate();
    }

   /* @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }*/

    public interface OnChangedListener {
        void OnChanged(boolean CheckState);
    }

    public void setOnChangedListener(OnChangedListener l) {
        this.onChangeListener = l;
    }
}
