package ru.code22.learn_multiply;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class LearnMultiplyContentProvider extends ContentProvider {

    private static final String LOG_TAG = "learnMultiplyLogs";

    // БД
    public static final String DB_NAME = "data.db";
    public static final int DB_VERSION = 1;

    // Таблица
    private static final String TESTS_TABLE = "tests";
    private static final String QUESTIONS_TABLE = "questions";
    private static final String ANSWERS_TABLE = "answers";
    private static final String SEANCES_TABLE = "seances";

    // Uri
    // authority
    public static final String AUTHORITY = "ru.code22.providers.learnmultiply";
    // path
    private static final String TESTS_PATH = "tests";
    private static final String QUESTIONS_PATH = "questions";
    private static final String ANSWERS_PATH = "answers";
    private static final String SEANCES_PATH = "seances";

    // Общий Uri
    public static final Uri TESTS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + TESTS_PATH);
    public static final Uri QUESTIONS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + QUESTIONS_PATH);
    public static final Uri ANSWERS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + ANSWERS_PATH);
    public static final Uri SEANCES_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + SEANCES_PATH);

    private static final UriMatcher sUriMatcher;

    private static final int URI_TESTS = 1;
    private static final int URI_TESTS_ID = 2;
    private static final int URI_QUESTIONS = 3;
    private static final int URI_QUESTIONS_ID = 4;
    private static final int URI_ANSWERS = 5;
    private static final int URI_ANSWERS_ID = 6;
    private static final int URI_SEANCES = 7;
    private static final int URI_SEANCES_ID = 8;


    private static HashMap<String, String> testsProjectionMap;
    private static HashMap<String, String> questionsProjectionMap;
    private static HashMap<String, String> answersProjectionMap;
    private static HashMap<String, String> seancesProjectionMap;



    @SuppressLint("Override")
    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context)
        {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            db.execSQL("PRAGMA encoding = \"UTF-8\"");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, " --- onCreate database --- ");
            db.execSQL("PRAGMA encoding = \"UTF-8\"");

            db.execSQL("create table tests (" +
                    "_id integer primary key autoincrement, " +
                    "begin_datetime text," +
                    "end_datetime text," +
                    "mistakes integer" +
                    ");");


            db.execSQL("create table questions (" +
                    "_id integer primary key autoincrement, " +
                    "test_id integer," +
                    "a integer," +
                    "b integer" +
                    ");");

            db.execSQL("create table answers (" +
                    "_id integer primary key autoincrement, " +
                    "test_id integer," +
                    "datetime text," +
                    "c integer" +
                    ");");

            db.execSQL("create table seances (" +
                    "incoming text null,"+
                    "outgoing text null"+
                    ");");

            db.execSQL("insert into seances(incoming) values ('CREATED')");


        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    @Override
    public boolean onCreate() {

        dbHelper = new DBHelper(getContext());

        return false;
    }

    private DBHelper dbHelper;

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        boolean bAppendIdToSelection=false;
        String appendIdPreffix="";

        switch (sUriMatcher.match(uri)) {
            case URI_TESTS_ID:
                bAppendIdToSelection = true;
            case URI_TESTS:
                qb.setTables(TESTS_TABLE);
                qb.setProjectionMap(testsProjectionMap);
                break;
            case URI_QUESTIONS_ID:
                bAppendIdToSelection = true;
            case URI_QUESTIONS:
                qb.setTables(QUESTIONS_TABLE);
                qb.setProjectionMap(questionsProjectionMap);
                break;
            case URI_ANSWERS_ID:
                bAppendIdToSelection = true;
            case URI_ANSWERS:
                qb.setTables(ANSWERS_TABLE);
                qb.setProjectionMap(answersProjectionMap);
                break;
            case URI_SEANCES_ID:
                bAppendIdToSelection = true;
            case URI_SEANCES:
                qb.setTables(SEANCES_TABLE);
                qb.setProjectionMap(seancesProjectionMap);
                break;


            default:
                throw new IllegalArgumentException("Unknown URI (for select) " + uri);
        }

        if (bAppendIdToSelection) {
            if (selection==null||selection.isEmpty())
                selection = appendIdPreffix+"_id = " + uri.getLastPathSegment();
            else
                selection = selection + " and "+appendIdPreffix+"_id = " + uri.getLastPathSegment();
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        // просим ContentResolver уведомлять этот курсор
        // об изменениях данных в CONTACT_CONTENT_URI
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case URI_TESTS:
            case URI_QUESTIONS:
            case URI_ANSWERS:
            case URI_SEANCES:{
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                long rowID = db.insert(TESTS_TABLE, null, values);
                Uri resultUri = ContentUris.withAppendedId(TESTS_CONTENT_URI, rowID);
                // уведомляем ContentResolver, что данные по адресу resultUri изменились
                getContext().getContentResolver().notifyChange(resultUri, null);
                return resultUri;
            }
            default:
                throw new IllegalArgumentException("Unknown URI (for insert) " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, TESTS_PATH, URI_TESTS);
        sUriMatcher.addURI(AUTHORITY, TESTS_PATH + "/#", URI_TESTS_ID);
        sUriMatcher.addURI(AUTHORITY, QUESTIONS_PATH, URI_QUESTIONS);
        sUriMatcher.addURI(AUTHORITY, QUESTIONS_PATH + "/#", URI_QUESTIONS_ID);
        sUriMatcher.addURI(AUTHORITY, ANSWERS_PATH, URI_ANSWERS);
        sUriMatcher.addURI(AUTHORITY, ANSWERS_PATH + "/#", URI_ANSWERS_ID);
        sUriMatcher.addURI(AUTHORITY, SEANCES_PATH, URI_SEANCES);
        sUriMatcher.addURI(AUTHORITY, SEANCES_PATH + "/#", URI_SEANCES_ID);


        testsProjectionMap = new HashMap<String, String>();
        testsProjectionMap.put("_id", "_id");
        testsProjectionMap.put("begin_datetime", "begin_datetime");
        testsProjectionMap.put("end_datetime", "end_datetime");
        testsProjectionMap.put("mistakes", "mistakes");


        questionsProjectionMap = new HashMap<String, String>();
        questionsProjectionMap.put("_id", "_id");
        questionsProjectionMap.put("test_id", "test_id");
        questionsProjectionMap.put("a", "a");
        questionsProjectionMap.put("b", "b");

        answersProjectionMap = new HashMap<String, String>();
        answersProjectionMap.put("_id", "_id");
        answersProjectionMap.put("test_id", "test_id");
        answersProjectionMap.put("datetime", "datetime");
        answersProjectionMap.put("c", "c");

        seancesProjectionMap = new HashMap<String, String>();
        seancesProjectionMap.put("incoming", "incoming");
        seancesProjectionMap.put("outgoing", "outgoing");


    }
}
