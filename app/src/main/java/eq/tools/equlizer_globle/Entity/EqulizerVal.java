package eq.tools.equlizer_globle.Entity;

/**
 * Created by Administrator on 2016/11/24 0024.
 */
public class EqulizerVal {
    private int id;
    private String name;
    private int[] value ;

    public EqulizerVal() {

    }

    public int[] getValue() {
        return value;
    }

    public void setValue(int[] value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
