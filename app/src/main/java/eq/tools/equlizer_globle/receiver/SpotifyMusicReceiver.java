package eq.tools.equlizer_globle.receiver;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public class SpotifyMusicReceiver extends AbstractPlayerReceiver{
    public static final String PACKAGE_NAME = "com.spotify.music";
    public static final String PLAYER_NAME = "Spotify Player";
    public SpotifyMusicReceiver() {
        super(PACKAGE_NAME, PLAYER_NAME);
    }
}
