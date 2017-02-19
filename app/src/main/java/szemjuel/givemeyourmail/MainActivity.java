package szemjuel.givemeyourmail;

import android.app.Dialog;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Player> players = new ArrayList<>();
    static PlayerListAdapter adapter;
    ListView listView;
    final static int TODAY = getDay();
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        try {
            file = new File("players.csv");
            if (!file.exists()) {
                file.createNewFile();
                Toast.makeText(getApplicationContext(), "File elkésztve", Toast.LENGTH_LONG).show();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        if(adapter != null) {
            adapter.clear();
        }

        read();

        adapter = new PlayerListAdapter(getApplicationContext(), players);

        listView = (ListView) findViewById(R.id.list_players);
        listView.setAdapter(adapter);

        Button btnNewPlayer = (Button) findViewById(R.id.btnNewPlayer);
        btnNewPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fm = new NewPlayerDialog();
                fm.show(getSupportFragmentManager(), "newPlayer");
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

    private void read() {
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);

            CSVReader csvReader = new CSVReader(inputStream);
            List<String[]> readCSV = csvReader.read();

            for(String [] playersData : readCSV){

                int[] tmpPTD = getPlayerTime(playersData);

                GameType.Type type = getType(playersData[2]);

                players.add(new Player(playersData[0],playersData[1],type,tmpPTD[0], tmpPTD[1], tmpPTD[2]));
            }
            if(readCSV.size() == players.size()){
                Toast.makeText(getApplicationContext(), players.size()+" játékos importálva", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void write()throws IOException{
        CSVWriter writer = new CSVWriter(new FileWriter("players.csv"));

        List<String[]> data = new ArrayList<>();

        for(Player p : players){

            String type = "";
            if(p.getmGameType() == GameType.Type.TYPE1){
                type = "TYPE1";
            }else if(p.getmGameType() == GameType.Type.TYPE2){
                type = "TYPE2";
            }else if(p.getmGameType() == GameType.Type.TYPE3){
                type = "TYPE3";
            }else{
                type = "TYPE4";
            }
            data.add(new String[]{p.getmName(), p.getmEmail(), type, Integer.toString(p.getmPhone()),
                    Integer.toString(p.getmTime()), Integer.toString(p.getmDay())});
        }

        writer.writeAll(data);
        writer.close();
    }


    @Nullable
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

                            int phone=0;
                            int time=500;

                            try {
                                phone = Integer.parseInt(etPhone.getText().toString());
                                time = Integer.parseInt(unitedPick);
                            }catch (NumberFormatException ex){
                                Log.e(getTag(), "NumberFormatException");
                            }

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

                            if(etName.getText().toString().equals("") && etEmail.getText().toString().equals("")){
                                Toast.makeText(getContext(), "A mentés nem sikerült,tölts ki mindent!", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }else {
                                players.add(new Player(etName.getText().toString(), etEmail.getText().toString(), type, phone, time, TODAY));
                                try {
                                    write();
                                }catch (IOException ex){
                                    Log.getStackTraceString(ex);
                                }
                                adapter.notifyDataSetChanged();
                            }
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
