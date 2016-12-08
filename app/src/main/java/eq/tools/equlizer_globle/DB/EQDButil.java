package eq.tools.equlizer_globle.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;

import eq.tools.equlizer_globle.Entity.EqulizerVal;

/**
 * Created by Administrator on 2016/11/24 0024.
 */
public class EQDButil {
    private DBhelperEQ dBhelperEQ;
    private SQLiteDatabase db;

    public EQDButil(Context context) {
        dBhelperEQ = new DBhelperEQ(context);
    }

    public void Delete() {

    }

    public void Update() {

    }

    public ArrayList<EqulizerVal> Query() {
        db=dBhelperEQ.getReadableDatabase();
        ArrayList<EqulizerVal>map=null;
        Cursor cursor=db.query("equlizerVal",null,null,null,null,null,null);
        if(cursor!=null){
            map=new ArrayList<>();
            EqulizerVal equlizerVal = null;
            int[] eqVals =null;
            while (cursor.moveToNext()){
                equlizerVal=new EqulizerVal();
                int id=cursor.getInt(cursor.getColumnIndex("_id"));
                equlizerVal.setId(id);
                equlizerVal.setName(cursor.getString(cursor.getColumnIndex("name")));
                eqVals=new int[5];
                eqVals[0]=cursor.getInt(cursor.getColumnIndex("val1"));
                eqVals[1]=cursor.getInt(cursor.getColumnIndex("val2"));
                eqVals[2]=cursor.getInt(cursor.getColumnIndex("val3"));
                eqVals[3]=cursor.getInt(cursor.getColumnIndex("val4"));
                eqVals[4]=cursor.getInt(cursor.getColumnIndex("val5"));
                equlizerVal.setValue(eqVals);
                map.add(equlizerVal);
            }
            cursor.close();
            db.close();
        }
        return map;
    }

    public void Insert() {

    }


    //保存
    public int saveEqVal(EqulizerVal equalizerEntity) {
        db = dBhelperEQ.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", equalizerEntity.getName());
        int[] val = equalizerEntity.getValue();
        values.put("val1", val[0]);
        values.put("val2", val[1]);
        values.put("val3", val[2]);
        values.put("val4", val[3]);
        values.put("val5", val[4]);
        long id = db.insert("equlizerVal", "0", values);
        db.close();
        return (int) id;
    }
}
