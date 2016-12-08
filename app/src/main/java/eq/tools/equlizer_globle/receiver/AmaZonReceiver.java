package eq.tools.equlizer_globle.receiver;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public class AmaZonReceiver extends AbstractPlayerReceiver{
    public static final String PACKAGE_NAME = "com.amazon.mp3";
    public static final String PLAYER_NAME = "Amazon Music";
    public AmaZonReceiver() {
        super(PACKAGE_NAME, PLAYER_NAME);
    }
}
