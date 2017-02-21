package hu.szemjuel;

/**
 * Created by Máté Anna on 2017.02.17..
 */

public class DailyWinners {

    int[] startPlayersID;
    boolean[] isType;

    public DailyWinners() {
    }

    public int[] getStartPlayersID() {
        return startPlayersID;
    }

    public boolean[] getIsType() {
        return isType;
    }

    public void setStartPlayersID(int[] startPlayersID) {
        this.startPlayersID = startPlayersID;
    }

    public void setIsType(boolean[] isType) {
        this.isType = isType;
    }
}
