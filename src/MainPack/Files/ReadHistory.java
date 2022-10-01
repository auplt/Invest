package MainPack.Files;

import MainPack.Exception.ReverseException;
import MainPack.Files.Graph.History;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ReadHistory {

    public static List<History> readH(String UrlName) throws IOException {
        List<History> histList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(UrlName));
        String line;

        while ((line = reader.readLine()) != null) {
            ReaderH(histList,line);

        }
        reader.close();
        //System.out.println(histList);
        return histList;
    }

    static void ReaderH (List<History> histList, String line){

        try{
            Scanner scanner;
            int index = 0;
        History history = new History();
        scanner = new Scanner(line);
        scanner.useDelimiter(";");
        while (scanner.hasNext()) {

            String data = scanner.next();
            if (index == 0) {
                history.setName(data);
            } else if (index == 1) {
                try {
                    Date date = new SimpleDateFormat("dd.MM.yyyy").parse(data);
                    history.setDate(date);
                } catch (Exception e) {
                    //System.out.println(e);
                }
            } else if (index == 2)
                history.setCur(MainPack.Files.ReadInitialFiles.Reverse(data));
            else
                System.out.println("Incorrect data::" + data);
            index++;
        }
//        index = 0;
        histList.add(history);
        }
        catch (ReverseException ex){
            System.err.println("Wrong input string format!");
        }
    }
}