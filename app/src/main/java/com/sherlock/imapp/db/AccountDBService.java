package com.sherlock.imapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sherlock.imapp.entity.AccountVO;

import java.util.ArrayList;
import java.util.List;

/**
 * description 账号数据库
 * Created by Administrator on 2018/6/15 0015.
 */

public class AccountDBService extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    private static AccountDBService accountDBService;
    private static Context context;

    public static void setContext(Context context) {
        AccountDBService.context = context;
        init();
    }

    private static void init(){
        accountDBService = new AccountDBService();
    }

    private AccountDBService(){
        super(context,"account.db",null,1);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        //创建用户表
        db.execSQL("create table if not exists account(id integer primary key,account text not null, pwd text, headPic text, lastLogTime integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public static void upsertAccount(int id, String account, String pwd, String headPic){
        AccountVO po = getAccount(id);
        long lastLogTime = System.currentTimeMillis();
        if (po == null) {
            String sql = "insert into account values(?,?,?,?,?)";
            accountDBService.db.execSQL(sql, new String[]{id+"", account, pwd, headPic, lastLogTime+""});
        } else {
            String sql = "update account set lastLogTime=? where id=?";
            accountDBService.db.execSQL(sql,new String[]{lastLogTime+""});
        }
    }

    public static List<AccountVO> getAccountList(){
        String sql = "select * from account order by lastLogTime desc";
        Cursor cursor = accountDBService.db.rawQuery(sql, new String[]{});

        List<AccountVO> list = new ArrayList<>(cursor.getCount());
        if (cursor.getCount()>0) {
            int idIdx = cursor.getColumnIndex("id");
            int accountIdx = cursor.getColumnIndex("account");
            int pwdIdx = cursor.getColumnIndex("pwd");
            int headPicIdx = cursor.getColumnIndex("headPic");
            while (cursor.moveToNext()) {
                AccountVO po = new AccountVO();
                po.setId(cursor.getInt(idIdx));
                po.setAccount(cursor.getString(accountIdx));
                po.setPwd(cursor.getString(pwdIdx));
                po.setHeadPic(cursor.getString(headPicIdx));
                list.add(po);
            }
        }
        return list;
    }

    /**
     * 查询联系人
     * @param id
     */
    public static AccountVO getAccount(int id){
        String sql = "select * from account where id=?";
        Cursor cursor =accountDBService.db.rawQuery(sql, new String[]{id+""});
        AccountVO po = null;
        if (cursor.getCount()>0) {
            int idIdx = cursor.getColumnIndex("id");
            int accountIdx = cursor.getColumnIndex("account");
            int pwdIdx = cursor.getColumnIndex("pwd");
            int headPicIdx = cursor.getColumnIndex("headPic");
            cursor.moveToNext();
            po = new AccountVO();
            po.setId(cursor.getInt(idIdx));
            po.setAccount(cursor.getString(accountIdx));
            po.setPwd(cursor.getString(pwdIdx));
            po.setHeadPic(cursor.getString(headPicIdx));
        }
        return po;
    }
}
