package hu.szemjuel.givemeyourmail;

import android.util.Log;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import hu.szemjuel.CSVReader;
import hu.szemjuel.GameType;
import hu.szemjuel.Player;

import static junit.framework.Assert.assertEquals;

public class CSVReaderTest{

    private ArrayList<Player> players = new ArrayList<>();
    private InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("playersTest.csv");

    @Test
    public void CSVReader(){

        CSVReader csvReader = new CSVReader(inputStream);
        List<String[]> readCSV = csvReader.read();



        for(String [] playersData : readCSV){

            int[] tmpPTD = getPlayerTime(playersData);

            GameType.Type type = getType(playersData[2]);

            players.add(new Player(playersData[0],playersData[1],type,tmpPTD[0], tmpPTD[1], tmpPTD[2]));
        }

        try{
            inputStream.close();
        }catch (IOException ex){
            throw new RuntimeException("Error reading .CSV file: " + ex);
        }
        assertEquals(players.size(), );
    }

    private GameType.Type getType(String s) {
        String string = s.substring(s.length()-1);
        int typeInt = Integer.parseInt(string);

        GameType.Type type = null;
        switch (typeInt){
            case 1:
                type = GameType.Type.TYPE1;
                break;
            case 2:
                type = GameType.Type.TYPE2;
                break;
            case 3:
                type = GameType.Type.TYPE3;
                break;
            case 4:
                type = GameType.Type.TYPE4;
                break;
        }
        return type;
    }

    private int[] getPlayerTime(String[] playersData) {
        int[] tmpPTD = {0,0,0};

        try{
            tmpPTD[0] = Integer.parseInt(playersData[3]);
            tmpPTD[1] = Integer.parseInt(playersData[4]);
            tmpPTD[2] = Integer.parseInt(playersData[5]);
        }catch(Exception e){
            Log.e("PraseException", Log.getStackTraceString(e));
        }
        return tmpPTD;
    }
}
