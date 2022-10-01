package MainPack.Files;

import MainPack.Exception.ReverseException;
import MainPack.Files.Graph.CurrencyGraph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ReadInitialFiles {

    public static List<CurrencyGraph> readIF(String UrlName) throws IOException {
        List<CurrencyGraph> curList = new ArrayList<>();
        URL url = new URL(UrlName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            ReaderIF(curList, line);
        }
        reader.close();
        return curList;
    }

    public static float Reverse(String str) throws ReverseException {
        float rev = 0.0f;
        try {
            String newstr = str.replace(',', '.');
            rev = Float.parseFloat(newstr);
        } catch (NumberFormatException e) {
            throw new ReverseException();
        }
        return rev;
    }

    static void ReaderIF(List<CurrencyGraph> curList, String line) {

        try {
            Scanner scanner;
            int index = 0;
            CurrencyGraph cur = new CurrencyGraph();
            scanner = new Scanner(line);
            scanner.useDelimiter(";");
            while (scanner.hasNext()) {

                String data = scanner.next();
                if (index == 0) {
                    try {
                        Date date = new SimpleDateFormat("dd.MM.yyyy H:mm:ss").parse(data);
                        cur.setDate(date);
                    } catch (Exception e) {
                        //System.out.println(e);
                    }
                } else if (index == 1) {
                    cur.setCur(Reverse(data));
                } else
                    System.err.println("Incorrect data::" + data);
                index++;

            }
            index = 0;
            curList.add(cur);
        } catch (ReverseException ex) {
            System.err.println("Wrong input string format!");
        }

    }
}





