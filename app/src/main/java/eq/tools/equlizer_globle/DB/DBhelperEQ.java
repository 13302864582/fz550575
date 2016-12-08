package eq.tools.equlizer_globle.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import eq.tools.equlizer_globle.R;

/**
 * Created by Administrator on 2016/11/24 0024.
 */
public class DBhelperEQ extends SQLiteOpenHelper{
    private static final int VERSION=1;
    public static final String DB_NAME="fz_equlizer.db";
    private Context mContext;

    public DBhelperEQ(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table if not exists equlizerVal(_id integer primary key autoincrement,name varchar(15) not null,val1 integer not null,val2 integer not null,val3 integer not null,val4 integer not null,val5 integer not null)";
        db.execSQL(sql);
        String[] eq_name=mContext.getResources().getStringArray(R.array.eq_name);
        String[] eq_val=mContext.getResources().getStringArray(R.array.eq_value);
        for(int i=0;i<eq_name.length;i++){
            String[] vals=eq_val[i]==null?"0,0,0,0,0".split(","):eq_val[i].split(",");
            ContentValues v=new ContentValues();
            v.put("name",eq_name[i]);
            v.put("val1",vals[0]);
            v.put("val2",vals[1]);
            v.put("val3",vals[2]);
            v.put("val4",vals[3]);
            v.put("val5",vals[4]);
            db.insert("equlizerVal","0",v);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
