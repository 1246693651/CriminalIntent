package cn.hnist.pany.criminalintent.database;

import org.litepal.crud.LitePalSupport;

import java.util.Date;
import java.util.UUID;


public class CrimeBean extends LitePalSupport {
    private String mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    public UUID getId() {
        return UUID.fromString(mId);
    }

    public void setId(UUID id) {
        mId = id.toString();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
