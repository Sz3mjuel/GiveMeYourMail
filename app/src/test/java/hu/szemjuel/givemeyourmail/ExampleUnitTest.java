package hu.szemjuel.givemeyourmail;

import org.junit.Test;

import java.util.ArrayList;

import hu.szemjuel.GameType;
import hu.szemjuel.Player;

import static org.junit.Assert.*;
import static hu.szemjuel.MainActivity.getDay;


public class ExampleUnitTest {

    final static int TODAY = getDay();

    @Test
    public void getBestPlayers(){

        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("László", "gipsz.jakab@gmail.com", GameType.Type.TYPE4, 112233, 12130, TODAY));
        players.add(new Player("Daniella", "lpnanni@gmail.com", GameType.Type.TYPE1, 112233, 10200, TODAY));
        //players.add(new Player("Zsolt", "zsolt@gmail.com", GameType.Type.TYPE2, 112233, 12300, TODAY));
        players.add(new Player("István", "gipsz.jakab@gmail.com", GameType.Type.TYPE3, 112233, 14330, TODAY));
        players.add(new Player("Gipsz Jakab", "gipsz.jakab@gmail.com", GameType.Type.TYPE1, 112233, 10300, TODAY));
        players.add(new Player("László", "gipsz.jakab@gmail.com", GameType.Type.TYPE4, 112233, 12230, TODAY));
        //players.add(new Player("Péter", "gipsz.jakab@gmail.com", GameType.Type.TYPE2, 112233, 11140, TODAY));

        int[] startPlayersID={60000,60000,60000,60000};
        boolean[] isType = {false, false, false, false};

        for(int i = 0 ; i < players.size() ; i++){
            GameType.Type type = players.get(i).getmGameType();
            if(type.equals(GameType.Type.TYPE1)){
                isType[0]=true;
                if(startPlayersID[0]==60000){
                    startPlayersID[0]=i;
                }else {
                    if (players.get(startPlayersID[0]).getmTime() < players.get(i).getmTime())
                        startPlayersID[0] = i;
                }

            }else if(type.equals(GameType.Type.TYPE2)){
                isType[1]=true;
                if(startPlayersID[1]==60000){
                    startPlayersID[1]=i;
                }else {
                    if (players.get(startPlayersID[1]).getmTime() < players.get(i).getmTime())
                        startPlayersID[1] = i;
                }
            }else if(type.equals(GameType.Type.TYPE3)){
                isType[2]=true;
                if(startPlayersID[2]==60000){
                    startPlayersID[2]=i;
                }else {
                    if (players.get(startPlayersID[2]).getmTime() < players.get(i).getmTime())
                        startPlayersID[2] = i;
                }
            }else{
                isType[3]=true;
                if(startPlayersID[3]==60000){
                    startPlayersID[3]=i;
                }else {
                    if (players.get(startPlayersID[3]).getmTime() < players.get(i).getmTime())
                        startPlayersID[3] = i;
                }
            }
        }
        //assertEquals(startPlayersID[1], 2);
        assertEquals(startPlayersID[0], 3);
        assertEquals(startPlayersID[2], 2);
        assertEquals(startPlayersID[3], 4);
    }
}