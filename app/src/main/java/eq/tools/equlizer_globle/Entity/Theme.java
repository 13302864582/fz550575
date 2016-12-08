package eq.tools.equlizer_globle.Entity;

/**
 * Created by Administrator on 2016/11/24 0024.
 */
public class Theme {
    private SeekBarTheme seekBar;
    private BassBoostTheme bass;
    private VirtualizerTheme virtual;

    public Theme(SeekBarTheme seekBar,BassBoostTheme bass, VirtualizerTheme virtual) {
        this.bass = bass;
        this.seekBar = seekBar;
        this.virtual = virtual;
    }

    public Theme() {
        seekBar=new SeekBarTheme();
        bass=new BassBoostTheme();
        virtual=new VirtualizerTheme();
    }

    public SeekBarTheme getSeekBar() {
        return seekBar;
    }

    public void setSeekBar(SeekBarTheme seekBar) {
        this.seekBar = seekBar;
    }

    public BassBoostTheme getBass() {
        return bass;
    }

    public void setBass(BassBoostTheme bass) {
        this.bass = bass;
    }

    public VirtualizerTheme getVirtual() {
        return virtual;
    }

    public void setVirtual(VirtualizerTheme virtual) {
        this.virtual = virtual;
    }
}
