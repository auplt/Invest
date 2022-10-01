package MainPack;

import MainPack.Files.Graph.CurrencyGraph;
import MainPack.Files.Graph.History;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Portfolio {
    public Currency[] CurN = new Currency[5];
    public List<History> histList = new ArrayList<>();
    public static String[] names = {"USD", "EUR", "GBP", "CHF", "CNY"};
    public float portfolioAbProf = 0.0f;
    public float portfolioReProf = 0.0f;

    boolean repetitionCheck(String name) {
        boolean flag = true;
        for (int i = 0; i < this.CurN.length; i++) {
            if (this.CurN[i] != null && this.CurN[i].name.equals(name)) {
                flag = false;
                break;
            }
        }

        return flag;
    }

    public void addCurrency(Currency currency) {
        if (this.repetitionCheck(currency.name)) {
            for (int i = 0; i < CurN.length; i++) {
                if (this.CurN[i] == null) {
                    this.CurN[i] = currency;
                    break;
                }
            }
        }
    }

    void addHistList(String HistLink) throws IOException {
        this.histList = MainPack.Files.ReadHistory.readH(HistLink);
    }

    float portInitialization() {
        float sum = 0.0f;
        for (int i = 0; i < this.CurN.length; i++) {
            sum = sum + this.CurN[i].quantity * this.CurN[i].curList.get(this.CurN[i].curList.size() - 1).cur;
        }
        return sum;
    }

    void Profit(String StartDate, String EndDate) {
        int i = 0;
        while (i < this.histList.size() + 1) {
            try {
                Date StDate = new SimpleDateFormat("dd.MM.yyyy").parse(StartDate);
                Date EnDate = new SimpleDateFormat("dd.MM.yyyy").parse(EndDate);
                if ((this.histList.get(i).date.getTime() >= StDate.getTime()) &&
                        (this.histList.get(i).date.getTime() <= EnDate.getTime())) {
                    int number = CurrencyCheck(this.histList.get(i).name);
                    this.CurN[number].operationCheck(this.histList, i);

                } else System.out.println("Date out of range: " + this.histList.get(i).date);
            } catch (Exception e) {
                //System.out.println(e);
            }
            i++;
        }


        float portfolioSellSum = 0.0f;
        float portfolioBuySum = 0.0f;

        for (int j = 0; j < 5; j++) {
            if (this.CurN[j] != null) {
                this.CurN[j].profitCalculate();
                portfolioSellSum = portfolioSellSum + this.CurN[j].SellSum;
                portfolioBuySum = portfolioBuySum + this.CurN[j].BuySum;
            }
        }
        if (portfolioBuySum != 0) {
            this.portfolioAbProf = portfolioSellSum - portfolioBuySum;
            this.portfolioReProf = this.portfolioAbProf * 100 / portfolioBuySum;
        }
    }

    int CurrencyCheck(String name) {

        int number = -1;
        for (int j = 0; j < 5; j++) {
            if (CurN[j].name.equals(name)) {
                number = j;
                break;
            }
        }
        return number;
    }

}

class Currency {
    public String name;
    public float quantity;
    public List<CurrencyGraph> curList;
    public float BuySum;
    public float SellSum;
    public float AbsolutProfit;
    public float RelativeProfit;


    public Currency(String name, float quantity, List<CurrencyGraph> curList) {
        this.name = name;
        this.quantity = quantity;
        this.curList = curList;
    }


    public void initialization() {
        if (this.curList.size()<1){
            this.AbsolutProfit=0.0f;
            this.RelativeProfit=0.0f;
        }
        else {
            this.BuySum = (this.curList.get(this.curList.size() - 1).cur) * this.quantity;
            this.SellSum = 0;
            this.AbsolutProfit = (this.SellSum - this.BuySum);
            if (this.BuySum != 0) {
                this.RelativeProfit = ((this.SellSum - this.BuySum) / this.BuySum) * 100;
            }
        }
    }

    public void profitCalculate() {
        this.SellSum = this.SellSum + (this.curList.get(0).cur) * this.quantity;
        this.AbsolutProfit = this.SellSum - this.BuySum;
        if (this.BuySum != 0) {
            this.RelativeProfit = ((this.SellSum - this.BuySum) / this.BuySum) * 100;
        }
    }

    public void operationCheck(List<History> histList, int i) {

        if (histList.get(i).cur > 0) {
            buying(histList.get(i).date, histList.get(i).cur);
        }
        if (histList.get(i).cur < 0) {
            selling(histList.get(i).date, histList.get(i).cur);
        }

    }

    public void buying(Date date, float sum) {
        int index = searchList(date);
        this.BuySum = this.BuySum + sum * this.curList.get(index).cur;
        this.quantity = this.quantity + sum;
    }

    public void selling(Date date, float sum) {
        int index = searchList(date);
        if (this.quantity >= Math.abs(sum)) {
            this.quantity = this.quantity + sum;
            this.SellSum = this.SellSum - sum * this.curList.get(index).cur;
        } else {
            System.out.println("You can sell only: " + this.quantity + " " + this.name + " on " + date);
            this.SellSum = this.SellSum + this.quantity * this.curList.get(index).cur;
            this.quantity = 0;

        }
    }

    public int searchList(Date date) {
        int rez = -1;
        for (int i = 0; i < this.curList.size() + 1; i++) {
            if (this.curList.get(i).date.equals(date)) {
                rez = i;
                break;
            }

        }
        return rez;
    }

}

