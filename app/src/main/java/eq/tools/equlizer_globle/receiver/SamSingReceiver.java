package eq.tools.equlizer_globle.receiver;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public class SamSingReceiver extends AbstractPlayerReceiver{
    public static final String PACKAGE_NAME = "com.samsung.music";
    public static final String PLAYER_NAME = "Samsung Player";
    public SamSingReceiver() {
        super(PACKAGE_NAME, PLAYER_NAME);
    }
}
