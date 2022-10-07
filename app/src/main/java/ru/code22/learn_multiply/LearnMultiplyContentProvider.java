package ru.code22.learn_multiply;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
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
