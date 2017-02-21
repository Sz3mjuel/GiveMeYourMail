package hu.szemjuel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    InputStream inputStream;

    public CSVReader(InputStream is){
        this.inputStream=is;
    }
    public List<String[]> read(){
        List<String[]> resultList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        try{
            String csvLine;
            while((csvLine = bufferedReader.readLine())!=null){
                String[] row = csvLine.split(";");
                resultList.add(row);
            }
        }catch (IOException ex){
            throw new RuntimeException("Error reading .CSV file: " + ex);
        }finally {
            try{
                inputStream.close();
            }catch (IOException e){
                throw new RuntimeException("Error closing .CSV file: " + e);
            }
        }
        return resultList;
    }
}
