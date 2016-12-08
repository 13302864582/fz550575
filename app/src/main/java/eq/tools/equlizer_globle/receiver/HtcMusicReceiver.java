package eq.tools.equlizer_globle.receiver;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public class HtcMusicReceiver extends AbstractPlayerReceiver{
    public static final String PACKAGE_NAME = "com.htc.music";
    public static final String PLAYER_NAME = "HTC Player";
    public HtcMusicReceiver() {
        super(PACKAGE_NAME, PLAYER_NAME);
    }
}
