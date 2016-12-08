package eq.tools.equlizer_globle.receiver;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public class AndroidMusicReceiver extends AbstractPlayerReceiver{
    public static final String PACKAGE_NAME = "com.android.music";
    public static final String PLAYER_NAME = "Android Music Player";
    public AndroidMusicReceiver() {
        super(PACKAGE_NAME, PLAYER_NAME);
    }
}
