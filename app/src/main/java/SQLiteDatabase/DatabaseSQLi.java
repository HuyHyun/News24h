package SQLiteDatabase;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import androidx.annotation.Nullable;
import Entiny.PostEntiny;



public class DatabaseSQLi extends SQLiteOpenHelper {
    public DatabaseSQLi(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void QueryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public Cursor GetData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }


    public void INSERT_Data(String tenbang, PostEntiny postEntiny) {
        try {
            SQLiteDatabase database = getWritableDatabase();
            String sql = "INSERT INTO '" + tenbang + "' VALUES(null,?,?,?,?,?,?)";
            SQLiteStatement statement = database.compileStatement(sql);
            statement.clearBindings();
            statement.bindLong(1, postEntiny.getId());
            statement.bindString(2, postEntiny.getTitle());
            statement.bindString(3, postEntiny.getSource());
            statement.bindString(4, postEntiny.getThumb());
            statement.bindLong(5, postEntiny.getCategory());
            statement.bindString(6, postEntiny.getDate());
            statement.executeInsert();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
