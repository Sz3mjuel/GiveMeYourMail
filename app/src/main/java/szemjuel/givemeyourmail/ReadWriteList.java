package szemjuel.givemeyourmail;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static android.R.attr.type;

public class ReadWriteList {

    private ArrayList<Player> players = new ArrayList<>();

    public void save(String fileName) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
        for (Player player : players)
            pw.println(player);
        pw.close();
    }
}
