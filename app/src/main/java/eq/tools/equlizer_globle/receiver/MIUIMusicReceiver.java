package eq.tools.equlizer_globle.receiver;



/**
 * Created by Administrator on 2016/4/25 0025.
 */
public class MIUIMusicReceiver extends AbstractPlayerReceiver{
    public static final String PACKAGE_NAME = "com.miui.player";
    public static final String PLAYER_NAME = "MIUI Player";
    public MIUIMusicReceiver() {
        super(PACKAGE_NAME, PLAYER_NAME);
    }
}
