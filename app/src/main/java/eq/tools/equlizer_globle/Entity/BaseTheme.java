package eq.tools.equlizer_globle.Entity;

/**
 * Created by Administrator on 2016/11/24 0024.
 */
public abstract class BaseTheme{
    private int pointResOn;
    private int pointResOff;
    private int backgroundRes;
    private int barResOn;
    private int barResOff;

    public BaseTheme(int backgroundRes, int pointResOn, int pointResOff, int barResOn, int barResOff) {
        this.backgroundRes = backgroundRes;
        this.pointResOn = pointResOn;
        this.pointResOff = pointResOff;
        this.barResOn = barResOn;
        this.barResOff = barResOff;
    }

    public BaseTheme() {
    }

    public int getBackgroundRes() {
        return backgroundRes;
    }

    public void setBackgroundRes(int backgroundRes) {
        this.backgroundRes = backgroundRes;
    }

    public int getPointResOn() {
        return pointResOn;
    }

    public void setPointResOn(int pointResOn) {
        this.pointResOn = pointResOn;
    }

    public int getPointResOff() {
        return pointResOff;
    }

    public void setPointResOff(int pointResOff) {
        this.pointResOff = pointResOff;
    }

    public int getBarResOn() {
        return barResOn;
    }

    public void setBarResOn(int barResOn) {
        this.barResOn = barResOn;
    }

    public int getBarResOff() {
        return barResOff;
    }

    public void setBarResOff(int barResOff) {
        this.barResOff = barResOff;
    }


}
