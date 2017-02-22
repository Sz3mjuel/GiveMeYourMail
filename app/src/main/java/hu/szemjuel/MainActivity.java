package hu.szemjuel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import hu.szemjuel.givemeyourmail.R;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Player> players = new ArrayList<>();
    static PlayerListAdapter adapter;
    ListView listView;
    final static int TODAY = getDay();

    static DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DBHelper(this);

        getAllData();

        adapter = new PlayerListAdapter(getApplicationContext(), players);

        listView = (ListView) findViewById(R.id.list_players);
        listView.setAdapter(adapter);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return sendEmail(exportAllData());
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return db.deleteData(players.get(position));
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static void addData(Player p){
        db.insertData(p);
    }

    public void getAllData(){
        Cursor res = db.getAllData();
        if(res.getCount() == 0){
            return;
        }
        while(res.moveToNext()){

            GameType.Type TYPE = getType(res);

            String[] s = {res.getString(4), res.getString(5), res.getString(6)};
            int[] playerTime = getPlayerTime(s);

            players.add(new Player(res.getString(1), res.getString(2), TYPE, playerTime[0], playerTime[1], playerTime[2]));
        }
    }

    public String exportAllData(){
        Cursor res = db.getAllData();
        String export = "";
        if(res.getCount() == 0) {
            return "";
        }

        while (res.moveToNext()){
            export = export + res.getString(1)+"; "+res.getString(2)+"; "+res.getString(3)+"; "+res.getString(4)+"; "+res.getString(5)+"; "+res.getString(6) + "\n";
        }
        return export;
    }

    protected boolean sendEmail(String export) {
        Log.i("Send email", "");
        String[] TO = {"samu.zsolt@fuldugo.hu"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "EXPORT");
        emailIntent.putExtra(Intent.EXTRA_TEXT, export);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "");
            return true;
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @NonNull
    private GameType.Type getType(Cursor res) {
        int type = Integer.parseInt(res.getString(3));
        GameType.Type TYPE;
        switch (type){
            case 1:
                TYPE = GameType.Type.TYPE1;
                break;
            case 2:
                TYPE = GameType.Type.TYPE2;
                break;
            case 3:
                TYPE = GameType.Type.TYPE3;
                break;
            case 4:
                TYPE = GameType.Type.TYPE4;
                break;
            default:
                TYPE = GameType.Type.TYPE1;
                break;
        }
        return TYPE;
    }

    private int[] getPlayerTime(String[] playersData) {
        int[] tmpPTD = {0,0,0};

        try{
            tmpPTD[0] = Integer.parseInt(playersData[0]);
            tmpPTD[1] = Integer.parseInt(playersData[1]);
            tmpPTD[2] = Integer.parseInt(playersData[2]);
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

                            String unitedPick = "2"+Integer.toString(etMin.getValue())+Integer.toString(etSec.getValue())+"000";

                            int phone=0;
                            int time=10000;

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

                            if(etName.getText().toString().equals("")){
                                Toast.makeText(getContext(), "A mentés nem sikerült,tölts ki mindent!", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                            else if(etEmail.getText().toString().equals("")){
                                Toast.makeText(getContext(), "A mentés nem sikerült,tölts ki mindent!", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }else {
                                Player tmpPlayer = new Player(etName.getText().toString(), etEmail.getText().toString(), type, phone, time, TODAY);
                                players.add(tmpPlayer);
                                addData(tmpPlayer);

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
                if(dailyWinners.getIsType()[i]) {
                    bestPlayers.add(players.get(dailyWinners.getStartPlayersID()[i]));
                }
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
