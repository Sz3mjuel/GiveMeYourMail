package szemjuel.givemeyourmail;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Player> players = new ArrayList<>();
    PlayerListAdapter adapter;
    ListView listView;
    final static int TODAY = getDay();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        fillList();

        adapter = new PlayerListAdapter(getApplicationContext(), players);

        listView = (ListView) findViewById(R.id.list_players);
        listView.setAdapter(adapter);

        //adapter.clear();

        Button btnNewPlayer = (Button) findViewById(R.id.btnNewPlayer);
        btnNewPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fm = new NewPlayerDialog();
                fm.show(getSupportFragmentManager(), "newPlayer");
                adapter.notifyDataSetChanged();
            }
        });
        Button btnBestPlayers = (Button) findViewById(R.id.btnBestPlayer);
        btnBestPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fm = new DailyBestDialog();
                fm.show(getSupportFragmentManager(), "dailyBest");
            }
        });
    }

    private void fillList() {
        players.add(new Player("Gipsz Jakab", "gipsz.jakab@gmail.com", GameType.Type.TYPE1, 00112233, 10300, TODAY));
        players.add(new Player("Daniella", "lpnanni@gmail.com", GameType.Type.TYPE1, 00112233, 10200, TODAY));
        players.add(new Player("Zsolt", "zsolt@gmail.com", GameType.Type.TYPE2, 00112233, 12300, 10));
        players.add(new Player("Péter", "gipsz.jakab@gmail.com", GameType.Type.TYPE2, 00112233, 11140, TODAY));
        //players.add(new Player("István", "gipsz.jakab@gmail.com", GameType.Type.TYPE3, 00112233, 14330, TODAY));
        players.add(new Player("László", "gipsz.jakab@gmail.com", GameType.Type.TYPE4, 00112233, 12230, TODAY));
    }

    public static class NewPlayerDialog extends DialogFragment{

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_newplayer, null);

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            final NumberPicker etMin = (NumberPicker) view.findViewById(R.id.minutePicker);
            final NumberPicker etSec = (NumberPicker) view.findViewById(R.id.secondPicker);

            etMin.setMinValue(0);
            etMin.setMaxValue(4);
            etSec.setMinValue(0);
            etSec.setMaxValue(59);

            builder.setView(view)
                    .setPositiveButton("Mentés", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText etName = (EditText) getDialog().findViewById(R.id.etName);
                            EditText etEmail = (EditText) getDialog().findViewById(R.id.etEmail);
                            EditText etPhone = (EditText) getDialog().findViewById(R.id.etPhone);

                            RadioGroup radioGroup = (RadioGroup) getDialog().findViewById(R.id.btnRadioGroup);

                            String unitedPick = "1"+Integer.toString(etMin.getValue())+Integer.toString(etSec.getValue())+"0";

                            int phone, time;

                            phone = Integer.parseInt(etPhone.getText().toString());
                            time = Integer.parseInt(unitedPick);

                            GameType.Type type;
                            int radioButtonID = radioGroup.getCheckedRadioButtonId();
                            RadioButton radioButton = (RadioButton) getDialog().findViewById(radioButtonID);

                            if(radioButton.getText().equals("Type1")){
                                type = GameType.Type.TYPE1;
                            }else if(radioButton.getText().equals("Type2")){
                                type = GameType.Type.TYPE2;
                            }else if(radioButton.getText().equals("Type3")){
                                type = GameType.Type.TYPE3;
                            }else{
                                type = GameType.Type.TYPE4;
                            }
                            players.add(new Player(etName.getText().toString(), etEmail.getText().toString(), type, phone, time, TODAY));
                        }
                    })
                    .setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            return builder.create();
        }
    }

    public static class DailyBestDialog extends DialogFragment{
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_bestplayers, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            ArrayList<Player> bestPlayers = new ArrayList<>();

            DailyWinners dailyWinners = getBestPlayers(players);

            for(int i = 0; i < dailyWinners.getStartPlayersID().length; i++){
                if(dailyWinners.getIsType()[i] == true)
                    bestPlayers.add(players.get(dailyWinners.getStartPlayersID()[i]));
            }

            PlayerListAdapter adapter = new PlayerListAdapter(getContext(), bestPlayers);

            ListView listView = (ListView) view.findViewById(R.id.bestPlayersList);
            listView.setAdapter(adapter);

            builder.setView(view);
            builder.setPositiveButton("Rendben", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            return builder.create();
        }
    }

    public static DailyWinners getBestPlayers(ArrayList<Player> players){

        DailyWinners dailyWinners = new DailyWinners();

        int[] startPlayersID={60000,60000,60000,60000};
        boolean[] isType = {false, false, false, false};

        for(int i = 0 ; i < players.size() ; i++){
            GameType.Type type = players.get(i).getmGameType();
            if(type.equals(GameType.Type.TYPE1) && players.get(i).getmDay() == TODAY){
                isType[0]=true;
                if(startPlayersID[0]==60000){
                    startPlayersID[0]=i;
                }else {
                    if (players.get(startPlayersID[0]).getmTime() < players.get(i).getmTime())
                        startPlayersID[0] = i;
                }

            }else if(type.equals(GameType.Type.TYPE2) && players.get(i).getmDay() == TODAY){
                isType[1]=true;
                if(startPlayersID[1]==60000){
                    startPlayersID[1]=i;
                }else {
                    if (players.get(startPlayersID[1]).getmTime() < players.get(i).getmTime())
                        startPlayersID[1] = i;
                }
            }else if(type.equals(GameType.Type.TYPE3) && players.get(i).getmDay() == TODAY){
                isType[2]=true;
                if(startPlayersID[2]==60000){
                    startPlayersID[2]=i;
                }else {
                    if (players.get(startPlayersID[2]).getmTime() < players.get(i).getmTime())
                        startPlayersID[2] = i;
                }
            }else if(players.get(i).getmDay() == TODAY){
                isType[3]=true;
                if(startPlayersID[3]==60000){
                    startPlayersID[3]=i;
                }else {
                    if (players.get(startPlayersID[3]).getmTime() < players.get(i).getmTime())
                        startPlayersID[3] = i;
                }
            }
        }
        dailyWinners.setIsType(isType);
        dailyWinners.setStartPlayersID(startPlayersID);

        return dailyWinners;
    }

    protected static int getDay(){
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }
}
