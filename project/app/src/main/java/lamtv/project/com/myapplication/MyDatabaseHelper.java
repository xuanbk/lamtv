package lamtv.project.com.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lamtv.project.com.myapplication.Object.Interested;
import lamtv.project.com.myapplication.Object.Translate;
import lamtv.project.com.myapplication.fragment.SearchFragment;

/**
 * Created by VS9 X64Bit on 23/12/2016.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";


    // Phiên bản
    private static final int DATABASE_VERSION = 1;


    // Tên cơ sở dữ liệu.
    private static final String DATABASE_NAME = "Translate_Manager";


    // Tên bảng: Like.
    private static final String TABLE_TRANSLATE = "Translate";

    private static final String COLUMN_TRANSLATE_ID = "translate_id";
    private static final String COLUMN_EN = "en";
    private static final String COLUMN_VI = "vi";
    private static final String COLUMN_ISEnglish = "isenglish";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    // Tạo các bảng.
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script tạo bảng.
        String script = "CREATE TABLE " + TABLE_TRANSLATE + "("
                + COLUMN_TRANSLATE_ID + " INTEGER PRIMARY KEY, " + COLUMN_EN + " TEXT, " + COLUMN_VI
                + " TEXT," + COLUMN_ISEnglish + " INTEGER DEFAULT 0" + ")";
        // Chạy lệnh tạo bảng.
        db.execSQL(script);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");

        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSLATE);


        // Và tạo lại.
        onCreate(db);
    }

    // Nếu trong bảng Note chưa có dữ liệu,
    // Trèn vào mặc định 2 bản ghi.
      /*public void createDefaultNotesIfNeed()  {
        int count = this.getNotesCount();
        if(count ==0 ) {
          *//* Interested   note1 = new Interested("Firstly see Android ListView");
            Interested note2 = new Interested("Learning Android SQLite");*//*
            String travel_id = "-KXeITgVkLSi5HLw9bY-";
            this.addNote(travel_id);
            //this.addNote(note2);
        }
    }
*/

    public void addNote(String en ,String vi,boolean isenglish) {
        Log.i(TAG, "MyDatabaseHelper.addNote ... " + en);
        int flag = (isenglish)? 1 : 0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EN,en);
        values.put(COLUMN_VI,vi);
        values.put(COLUMN_ISEnglish,flag);
        //values.put(COLUMN_NOTE_CONTENT, interested.getNoteContent());


        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_TRANSLATE, null, values);


        // Đóng kết nối database.
        db.close();
    }


   /* public Interested getNote(int id) {
        Log.i(TAG, "MyDatabaseHelper.getNote ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LIKE, new String[]{COLUMN_LIKE_ID,
                        COLUMN_TRAVEL_ID_LIKE}, COLUMN_LIKE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Interested interested = new Interested(Integer.parseInt(cursor.getString(0)),cursor.getString(1));
        // return note
        return interested;
    }*/


    public ArrayList<Translate> getAllNotes() {
        Log.i(TAG, "MyDatabaseHelper.getAllNotes ... ");

        ArrayList<Translate> translateList = new ArrayList<Translate>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRANSLATE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Translate translate = new Translate();
                translate.setTranslate_id(Integer.parseInt(cursor.getString(0)));
                translate.setEn(cursor.getString(1));
                translate.setVi(cursor.getString(2));
                ///String str = (cursor.getString(cursor.getColumnIndex("isenglish")));
                boolean flag1 = cursor.getInt(cursor.getColumnIndex("isenglish")) > 0;
                translate.setEnglish(flag1);

                // Thêm vào danh sách.
                translateList.add(translate);
            } while (cursor.moveToNext());
        }

        // return note list
        return translateList;
    }

    public void deleteNote(int  translate_id) {
        Log.i(TAG, "MyDatabaseHelper.updateNote ... " + translate_id);

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSLATE, COLUMN_TRANSLATE_ID+ " = ?",
                new String[]{String.valueOf(translate_id)});
        db.close();
    }
    public void  delteALL(){
        Log.i(TAG, "MyDatabaseHelper.deleteall ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DELETE FROM " + TABLE_TRANSLATE);
        db.close();
    }
}