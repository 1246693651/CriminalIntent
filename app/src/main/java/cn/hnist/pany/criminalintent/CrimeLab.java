package cn.hnist.pany.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.hnist.pany.criminalintent.database.CrimeBean;


public class CrimeLab {
    private static CrimeLab sCrimeLab;

    //    private List<Crime> mCrimes;
    private Context mContext;
//    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
//        mDatabase = new CrimeBaseHelper(mContext)
//                .getWritableDatabase();
        Connector.getDatabase();
//        mCrimes = new ArrayList<>();
    }

    // 自定义一个将CrimeBean 转化为 Crime的函数
    private Crime toCrime(CrimeBean crimeBean) {
        Crime crime = new Crime(crimeBean.getId());
        crime.setTitle(crimeBean.getTitle());
        crime.setDate(crimeBean.getDate());
        crime.setSolved(crimeBean.isSolved());
        crime.setSuspect(crimeBean.getSuspect());
        crime.setPhone(crimeBean.getPhone());
        return crime;
    }

    // 自定义一个将CrimeBean 转化为 Crime的函数
    private CrimeBean toCrimeBean(Crime c) {
        CrimeBean crime = new CrimeBean();
        crime.setId(c.getId());
        crime.setTitle(c.getTitle());
        crime.setDate(c.getDate());
        crime.setSolved(c.isSolved());
        crime.setSuspect(c.getSuspect());
        crime.setPhone(c.getPhone());
        return crime;
    }

    // 新建陋习
    public void addCrime(Crime c) {
//        mCrimes.add(c);
//        ContentValues values = getContentValues(c);
//        mDatabase.insert(CrimeTable.NAME, null, values);
        toCrimeBean(c).save();
    }

    // 删除陋习
    public void deleteCrime(Crime c) {
//        mCrimes.remove(c);
//        String uuidString = c.getId().toString();
//        mDatabase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});
        LitePal.deleteAll(CrimeBean.class, "mID=?", c.getId().toString());
    }

    // 获取全部陋习
    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();
        List<CrimeBean> crimeBeans = LitePal.findAll(CrimeBean.class);
        for (CrimeBean crimeBean : crimeBeans) {
            crimes.add(toCrime(crimeBean));
        }
        return crimes;
//        List<Crime> crimes = new ArrayList<>();
//        CrimeCursorWrapper cursor = queryCrimes(null, null);
//        try {
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                crimes.add(cursor.getCrime());
//                cursor.moveToNext();
//            }
//        } finally {
//            cursor.close();
//        }
//        return new ArrayList<>();
    }

    // 获取单个陋习
    public Crime getCrime(UUID id) {
        CrimeBean crimeBean = LitePal.where("mId=?", id.toString()).findFirst(CrimeBean.class);
        return toCrime(crimeBean);
        /*for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }*/
//        return null;
//        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?", new String[]{id.toString()});
//        try {
//            if (cursor.getCount() == 0) {
//                return null;
//            }
//            cursor.moveToFirst();
//            return cursor.getCrime();
//        } finally {
//            cursor.close();
//        }\
    }

    public File getPhotoFile(Crime crime) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, crime.getPhotoFilename());
    }

    public void updateCrime(Crime c) {
//        String uuidString = crime.getId().toString();
//        ContentValues values = getContentValues(crime);
//        mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});
        toCrimeBean(c).updateAll("mId=?", c.getId().toString());
        // 强行修复更改check无效的bug
        if (!c.isSolved()) {
            CrimeBean crimeBean = LitePal.where("mId=?", c.getId().toString()).findFirst(CrimeBean.class);
            crimeBean.setSolved(c.isSolved());
            crimeBean.save();
        }
    }

//    //    private Cursor queryCrimes(String whereClause, String[] whereArgs) {
//    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
//        Cursor cursor = mDatabase.query(CrimeTable.NAME,
//                null, // Columns - null selects all columns
//                whereClause, whereArgs,
//                null, // groupBy
//                null, // having
//                null  // orderBy
//        );
////        return cursor;
//        return new CrimeCursorWrapper(cursor);
//    }
//
//    private static ContentValues getContentValues(Crime crime) {
//        ContentValues values = new ContentValues();
//        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
//        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
//        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
//        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
//        return values;
//    }
}

