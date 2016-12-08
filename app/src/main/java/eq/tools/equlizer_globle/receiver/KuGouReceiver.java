package eq.tools.equlizer_globle.receiver;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public class KuGouReceiver extends AbstractPlayerReceiver{
    public static final String PACKAGE_NAME = "com.kugou.android.music";
    public static final String PLAYER_NAME = "KuGou Player";
    public KuGouReceiver() {
        super(PACKAGE_NAME, PLAYER_NAME);
    }
}
