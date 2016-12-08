package eq.tools.equlizer_globle.receiver;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public class RdioMusicReceiver extends AbstractPlayerReceiver{
    public static final String PACKAGE_NAME = "com.rdio.android";
    public static final String PLAYER_NAME = "Rdio Player";
    public RdioMusicReceiver() {
        super(PACKAGE_NAME, PLAYER_NAME);
    }
}
