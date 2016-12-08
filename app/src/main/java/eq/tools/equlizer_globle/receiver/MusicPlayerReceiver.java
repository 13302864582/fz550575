package eq.tools.equlizer_globle.receiver;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public class MusicPlayerReceiver extends AbstractPlayerReceiver{
    public static final String PACKAGE_NAME = "media.music.musicplayer";
    public static final String PLAYER_NAME = "Music Player White Note";
    public MusicPlayerReceiver() {
        super(PACKAGE_NAME, PLAYER_NAME);
    }
}
