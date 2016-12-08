package eq.tools.equlizer_globle.receiver;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public class SEMCMusicReceiver extends AbstractPlayerReceiver{
    public static final String PACKAGE_NAME = "com.sonyericsson.music";
    public static final String PLAYER_NAME = "SonyEricsson Player";
    public SEMCMusicReceiver() {
        super(PACKAGE_NAME, PLAYER_NAME);
    }
}
