package szemjuel.givemeyourmail;

public class Player implements GameType{

    private String mName;
    private String mEmail;
    private Type mGameType;
    private int mPhone;
    private int mTime;
    private int mDay;

    public Player(String mName, String mEmail, Type mGameType, int mPhone, int mTime, int mDay) {
        this.mName = mName;
        this.mEmail = mEmail;
        this.mGameType = mGameType;
        this.mPhone = mPhone;
        this.mTime = mTime;
        this.mDay = mDay;
    }

    public int getmPhone() {
        return mPhone;
    }

    public void setmPhone(int mPhone) {
        this.mPhone = mPhone;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public Type getmGameType() {
        return mGameType;
    }

    public void setmGameType(Type mGameType) {
        this.mGameType = mGameType;
    }

    public int getmTime() {
        return mTime;
    }

    public void setmTime(int mTime) {
        this.mTime = mTime;
    }

    public int getmDay() {
        return mDay;
    }

    public void setmDay(int mDay) {
        this.mDay = mDay;
    }

    @Override
    public String toString() {
        return mName + ';' + mEmail + ';' + mGameType + ';' + mPhone + ';' + mTime + ';' + mDay + '\0';
    }
}
