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
import lamtv.project.com.myapplication.fragment.SearchFragment;

/**
 * Created by VS9 X64Bit on 23/12/2016.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";


    // Phiên bản
    private static final int DATABASE_VERSION = 1;


    // Tên cơ sở dữ liệu.
    private static final String DATABASE_NAME = "Like_Manager";


    // Tên bảng: Like.
    private static final String TABLE_LIKE = "like";

    private static final String COLUMN_TRAVEL_ID_LIKE = "trave_like_id";
    private static final String COLUMN_LIKE_ID = "like_id";
    // private static final String COLUMN_NOTE_CONTENT = "Note_Content";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    // Tạo các bảng.
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script tạo bảng.
        String script = "CREATE TABLE " + TABLE_LIKE + "("
                + COLUMN_LIKE_ID + " INTEGER PRIMARY KEY " + COLUMN_TRAVEL_ID_LIKE + " TEXT," + ")";
        // Chạy lệnh tạo bảng.
        db.execSQL(script);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");

        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIKE);


        // Và tạo lại.
        onCreate(db);
    }

    // Nếu trong bảng Note chưa có dữ liệu,
    // Trèn vào mặc định 2 bản ghi.
      public void createDefaultNotesIfNeed()  {
        int count = this.getNotesCount();
        if(count ==0 ) {
          /* Interested   note1 = new Interested("Firstly see Android ListView");
            Interested note2 = new Interested("Learning Android SQLite");*/
            String travel_id = "-KXeITgVkLSi5HLw9bY-";
            this.addNote(travel_id);
            //this.addNote(note2);
        }
    }


    public void addNote(String travel_id) {
        Log.i(TAG, "MyDatabaseHelper.addNote ... " + travel_id);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TRAVEL_ID_LIKE, travel_id);
        //values.put(COLUMN_NOTE_CONTENT, interested.getNoteContent());


        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_LIKE, null, values);


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


    public List<Interested> getAllNotes() {
        Log.i(TAG, "MyDatabaseHelper.getAllNotes ... ");

        List<Interested> interestedList = new ArrayList<Interested>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LIKE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Interested interested = new Interested();
                interested.setLike_id(Integer.parseInt(cursor.getString(0)));
                interested.setTrave_like_id(cursor.getString(1));


                // Thêm vào danh sách.
                interestedList.add(interested);
            } while (cursor.moveToNext());
        }

        // return note list
        return interestedList;
    }

    public int getNotesCount() {
        Log.i(TAG, "MyDatabaseHelper.getNotesCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_LIKE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }


/*    public int updateNote(Note note) {
        Log.i(TAG, "MyDatabaseHelper.updateNote ... "  + note.getNoteTitle());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getNoteTitle());
        values.put(COLUMN_NOTE_CONTENT, note.getNoteContent());

        // updating row
        return db.update(TABLE_NOTE, values, COLUMN_NOTE_ID + " = ?",
                new String[]{String.valueOf(note.getNoteId())});
    }*/

    public void deleteNote(String travel_id) {
        Log.i(TAG, "MyDatabaseHelper.updateNote ... " + travel_id);

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LIKE, COLUMN_TRAVEL_ID_LIKE+ " = ?",
                new String[]{String.valueOf(travel_id)});
        db.close();
    }
}