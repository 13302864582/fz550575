package eq.tools.equlizer_globle.receiver;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public class LgMusicReceiver extends AbstractPlayerReceiver {
    public static final String PACKAGE_NAME = "com.lge.music";
    public static final String PLAYER_NAME = "LG Player";
    public LgMusicReceiver() {
        super(PACKAGE_NAME, PLAYER_NAME);
    }
}
