package szemjuel.givemeyourmail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayerListAdapter extends ArrayAdapter<Player> {

    public PlayerListAdapter(Context context, ArrayList<Player> players) {
        super(context, 0, players);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Player player = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_player, parent, false);
            }
            TextView tvName = (TextView) convertView.findViewById(R.id.txtName);
            TextView tvEmail = (TextView) convertView.findViewById(R.id.txtEmail);
            TextView tvTime = (TextView) convertView.findViewById(R.id.txtTime);
            TextView tvType = (TextView) convertView.findViewById(R.id.txtGameType);

            GameType.Type tmpType = null;

            try {
                tmpType = player.getmGameType();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            String type;
            if (tmpType.equals(GameType.Type.TYPE1)) {
                type = "1";
                tvType.setBackgroundResource(R.color.colorBlue);
            } else if (tmpType.equals(GameType.Type.TYPE2)) {
                type = "2";
                tvType.setBackgroundResource(R.color.colorGreen);
            } else if (tmpType.equals(GameType.Type.TYPE3)) {
                type = "3";
                tvType.setBackgroundResource(R.color.colorOrange);
            } else {
                type = "4";
                tvType.setBackgroundResource(R.color.colorBrown);
            }

            String time = String.format("%c:%s", Integer.toString(player.getmTime()).charAt(1), Integer.toString(player.getmTime()).substring(2, 4));

            tvName.setText(player.getmName());
            tvEmail.setText(player.getmEmail());
            tvTime.setText(time);
            tvType.setText(type);

        return convertView;
    }
}
