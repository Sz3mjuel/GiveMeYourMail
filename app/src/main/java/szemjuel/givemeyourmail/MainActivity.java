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

        adapter.clear();

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
        players.add(new Player("Zsolt", "zsolt@gmail.com", GameType.Type.TYPE2, 00112233, 12300, TODAY));
        players.add(new Player("Péter", "gipsz.jakab@gmail.com", GameType.Type.TYPE2, 00112233, 11140, TODAY));
        players.add(new Player("István", "gipsz.jakab@gmail.com", GameType.Type.TYPE3, 00112233, 14330, TODAY));
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
            int [] tmpPlayers = getBestPlayers(players);
            for(int i = 0; i < tmpPlayers.length; i++){
                for(int j = i+1;j < tmpPlayers.length; j++){
                    if(tmpPlayers[j]==tmpPlayers[i])
                        i++;
                }
                    bestPlayers.add(players.get(tmpPlayers[i]));
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

    public static int[] getBestPlayers(ArrayList<Player> players){

        int[] bestPlayersID={0,0,0,0};

        for(int i = 0 ; i < players.size() ; i++){
            GameType.Type type = players.get(i).getmGameType();
            if(type.equals(GameType.Type.TYPE1)){
                if(bestPlayersID[0]==0)
                    bestPlayersID[0]=i;
            }else if(type.equals(GameType.Type.TYPE2)){
                if(bestPlayersID[1]==0)
                    bestPlayersID[1]=i;
            }else if(type.equals(GameType.Type.TYPE3)){
                if(bestPlayersID[2]==0)
                    bestPlayersID[2]=i;
            }else{
                if(bestPlayersID[3]==0)
                    bestPlayersID[3]=i;
            }
        }
        for(int i = 0; i < players.size() ; i++){
            GameType.Type type = players.get(i).getmGameType();
            if(type.equals(GameType.Type.TYPE1)){
                if(players.get(bestPlayersID[0]).getmTime()>=players.get(i).getmTime())
                    bestPlayersID[0]=i;
            }else if(type.equals(GameType.Type.TYPE2)){
                if(players.get(bestPlayersID[1]).getmTime()>=players.get(i).getmTime())
                    bestPlayersID[1]=i;
            }else if(type.equals(GameType.Type.TYPE3)){
                if(players.get(bestPlayersID[2]).getmTime()>=players.get(i).getmTime())
                    bestPlayersID[2]=i;
            }else{
                if(players.get(bestPlayersID[3]).getmTime()>=players.get(i).getmTime())
                    bestPlayersID[3]=i;
            }
        }
        return bestPlayersID;
    }

    protected static int getDay(){
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }
}
