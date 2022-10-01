package MainPack;

import MainPack.Files.ReadInitialFiles;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Interface extends JFrame {


    public static void main(String[] args) {
        new Interface();
    }

    public Interface() {

        Portfolio portfolio = new Portfolio();
        JFrame jFrame = new JFrame("CuRrEnCy");
        jFrame.setSize(800, 600);
        jFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                winClosing();
            }
        });
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        int x = jFrame.getWidth();
        int y = jFrame.getHeight();

        JPanel jPanel = new JPanel();
        jPanel.setBounds(0, 0, x, y);
        jPanel.setLayout(null);
        jPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
        jPanel.setBackground(new Color(246, 244, 244, 255));

        TitledBorder jPanelBorder = new TitledBorder(new TitledBorder(""),
                "\u00a9 Dolgov Alexander 2020", TitledBorder.CENTER, TitledBorder.BOTTOM);
        jPanelBorder.setTitleFont(new Font("Arial", Font.PLAIN, 12));
        jPanelBorder.setBorder(new LineBorder(Color.gray, 2));
        jPanel.setBorder(jPanelBorder);

        JTextArea jTextArea = new JTextArea(10, 10);
        jTextArea.setEditable(false);

        JScrollPane jScrollPaneText = new JScrollPane(jTextArea);
        jScrollPaneText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPaneText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        borderCustomize(jScrollPaneText, "Доходность портфеля");
        jScrollPaneText.setBounds(new Rectangle(x / 2 + 10, 10, x / 2 - 50, x / 2 - 100));
        jPanel.add(jScrollPaneText);


        JTextArea jTextAreaMistake = new JTextArea(10, 10);
        PrintStream out = new PrintStream(new TextAreaOutputStream(jTextAreaMistake));
        System.setOut(out);
        System.setErr(out);

        jTextAreaMistake.setEditable(true);
        JScrollPane jScrollPaneMistake = new JScrollPane(jTextAreaMistake);
        jScrollPaneMistake.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPaneMistake.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        borderCustomize(jScrollPaneMistake, "Проблемы");
        jScrollPaneMistake.setBounds(new Rectangle(20, 320, 740, 190));
        jPanel.add(jScrollPaneMistake);

        int widthText = 80;
        int heightText = 20;
        int heightSpace = 10;
        int xText = 80;
        int yText = 40;
        int xLabel = 20;
        int widthLabel = 40;

        JTextField jStartDate = new JTextField();
        jStartDate.setBounds(3 * xText / 2, 20, widthText, heightText);
        jPanel.add(jStartDate);
        jStartDate.setEditable(true);
        JTextField jEndDate = new JTextField();
        jEndDate.setBounds(3 * xText / 2 + 3 * widthText / 2 + widthText - 17, 20, widthText, heightText);
        jPanel.add(jEndDate);
        jEndDate.setEditable(true);

        JLabel labelStartDate = new JLabel("Начало периода");
        labelStartDate.setBounds(xLabel, 20, 3 * widthText / 2, heightText);
        labelCustomize(labelStartDate);
        jPanel.add(labelStartDate);
        JLabel labelEndDate = new JLabel("Конец периода");
        labelEndDate.setBounds(3 * xText / 2 + widthText + heightSpace, 20, 3 * widthText / 2, heightText);
        labelCustomize(labelEndDate);
        jPanel.add(labelEndDate);

        JTextField[] jTextField = new JTextField[5];
        JLabel[] jLabel = new JLabel[5];
        for (int i = 0; i < jLabel.length; i++) {
            jLabel[i] = new JLabel();
            final int y1 = yText + (i + 1) * (heightText + heightSpace);
            jLabel[i].setBounds(xLabel, y1, widthLabel, heightText);
            labelCustomize(jLabel[i]);
            jLabel[i].setText(Portfolio.names[i]);
            jPanel.add(jLabel[i]);
            jTextField[i] = new JTextField();
            jTextField[i].setText("");
            jTextField[i].setBounds(xText, y1, widthText, heightText);
            jPanel.add(jTextField[i]);
            jTextField[i].setEditable(true);

        }

        JLabel jFileLabel = new JLabel("<html>Добавить файл с<br>историей операций</html");
        jFileLabel.setBounds(x / 4, y / 6, 2 * widthText, 2 * heightText);
        labelCustomize(jFileLabel);
        jPanel.add(jFileLabel);

        JButton jFileButton = new JButton("Ввод имени файла");
        jFileButton.setBounds(x / 4, y / 6 + 2 * heightText, 3 * widthText / 2, heightText);
        buttomCustomize(jFileButton);
        jPanel.add(jFileButton);

        jFileButton.addActionListener(e -> {
            JPanel panel = new JPanel();
            JLabel fileLabel = new JLabel("Введите имя файла");
            labelCustomize(fileLabel);
            JTextField textField = new JTextField(10);
            panel.add(fileLabel);
            panel.add(textField);
            JButton agree = new JButton("  Ввод  ");
            buttomCustomize(agree);
            final String[] fileName = {null};
            agree.addActionListener(e1 -> {
                fileName[0] = textField.getText();
                File file = new File(fileName[0]);
                if (!file.exists()) {
                    dataError("Файл не найден");
                } else {
                    try {
                        portfolio.addHistList(fileName[0]);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                JOptionPane.getRootFrame().dispose();
                //String HistLink = "D:\\WORK\\OOA&P\\Project\\HistoryTest.csv";
            });
            JButton disagree = new JButton("  Отмена  ");
            buttomCustomize(disagree);
            disagree.addActionListener(e12 -> {
                textField.setText(null);
                JOptionPane.getRootFrame().dispose();
            });

            JButton[] buttons = {agree, disagree};

            JOptionPane.showOptionDialog(null, panel, "Добавление файла",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);
        });

        int widthButton = 80;
        int heightButton = 20;
        int widthSpace = 40;
        JButton InPut = new JButton("Ввод");
        InPut.setBounds(x / 4 - 35, y / 2 - 40, widthButton, heightButton);
        buttomCustomize(InPut);
        jPanel.add(InPut);

        JButton Abort = new JButton("Выход");
        Abort.setBounds(x - widthButton - widthSpace, y - heightButton - 3 * widthSpace / 2,
                widthButton, heightButton);
        jPanel.add(Abort);
        buttomCustomize(Abort);
        ActionListener preExitActionListener = new PreExitActionListener();
        Abort.addActionListener(preExitActionListener);

        InPut.addActionListener(ae -> {
            boolean flag = true;
            boolean flagcur = true;
            String StartDate = jStartDate.getText();
            String EndDate = jEndDate.getText();

            Date StDate = null;
            Date EnDate = null;
            Date StrDate = null;
            try {
                if (StartDate.length() > 1) {
                    StDate = new SimpleDateFormat("dd.MM.yyyy").parse(StartDate);
                }
                if (EndDate.length() > 1) {
                    EnDate = new SimpleDateFormat("dd.MM.yyyy").parse(EndDate);
                }
                StrDate = new SimpleDateFormat("dd.MM.yyyy").parse("01.12.1992");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            float[] CurQua = new float[5];
            if (StDate == null) {
                dataError("Заполните дату начала периода");
                flag = false;
            } else if (EnDate == null) {
                dataError("Заполните дату окончания периода");
                flag = false;
            } else if (StDate.getTime() < StrDate.getTime()) {
                dataError("Начало периода не может быть раньше 01.12.1992");
                flag = false;
            } else if (StDate.getTime() > System.currentTimeMillis()) {
                dataError("Дата начала периода не может быть больше текущей");
                flag = false;
            } else if (EnDate.getTime() > System.currentTimeMillis()) {
                dataError("Дата окончания периода не может быть больше текущей");
                flag = false;
            } else if (StDate.getTime() >= EnDate.getTime()) {//flag = true;
                dataError("Дата начала периода не может быть меньше или равна дате окончания периода");
                flag = false;
            } else {
                for (int i = 0; i < 5; i++) {
                    String currencyName = jTextField[i].getText();
                    float currencyValue = Reverse(currencyName);
                    if (currencyValue < 0) {
                        dataError("Сумма " + Portfolio.names[i] + " не может быть отрицательной");
                        flagcur = false;
                        break;

                    } else {
                        CurQua[i] = currencyValue;
                    }
                }
            }
            if (flag && flagcur) {
                //boolean[] usedcur = {false, false, false, false, false};
                if (connectionCheck()) {
                    for (int j = 0; j < 5; j++) {
                        String UrlName = "http://mfd.ru/currency/export/mfdexport.csv?currencyCodes=" +
                                Portfolio.names[j] + "&from=" + StartDate + "&till=" + EndDate;
                        try {
                            portfolio.CurN[j] = new Currency(Portfolio.names[j], CurQua[j],
                                    ReadInitialFiles.readIF(UrlName));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        portfolio.CurN[j].initialization();
                        portfolio.addCurrency(portfolio.CurN[j]);

                    }

                    jTextArea.append("Период: " + StartDate + " \u2014 " + EndDate + "\n" + "Состав портфеля\n");
                    for (int i = 0; i < portfolio.CurN.length; i++) {
                        if (portfolio.CurN[i].quantity > 0) {
                            //usedcur[i] = true;
                            jTextArea.append("   " + portfolio.CurN[i].name + " " + portfolio.CurN[i].quantity + "\n");
                        }
                    }
                    jTextArea.append("Стоимость портфеля на начало периода " + String.format("%.2f",
                            portfolio.portInitialization()) + " \u20BD\n");
                    portfolio.Profit(StartDate, EndDate);

                    float portsum = 0.0f;
                    for (int i = 0; i < 5; i++) {
                        if (portfolio.CurN[i].SellSum != 0) {
                            jTextArea.append(portfolio.CurN[i].name + "\n");
                            jTextArea.append("   Абсолютная прибыль: " + String.format("%.2f",
                                    portfolio.CurN[i].AbsolutProfit) + " \u20BD\n");
                            jTextArea.append("   Относительная прибыль: " + String.format("%.2f",
                                    portfolio.CurN[i].RelativeProfit) + "%" + "\n");
                            portsum = portsum + Math.abs(portfolio.CurN[i].SellSum);
                        }
                    }
                    jTextArea.append("Итого\n");
                    jTextArea.append("   Стоимость портфеля на конец периода: " + String.format("%.2f",
                            portsum) + " \u20BD\n");
                    jTextArea.append("   Абсолютная прибыль портфеля: " + String.format("%.2f",
                            portfolio.portfolioAbProf) + " \u20BD\n");
                    jTextArea.append("   Относительная прибыль портфеля: " + String.format("%.2f",
                            portfolio.portfolioReProf) + "%\n");
                    jTextArea.append("******************************************************************");
                } else {
                    System.err.println("No Internet connection");
                }
            }

        });

        JButton Clean = new JButton("Отчистка");
        Clean.setBounds(x - 2 * (widthButton + 2 * widthSpace / 3), y - heightButton - 3 * widthSpace / 2,
                widthButton, heightButton);
        jPanel.add(Clean);
        buttomCustomize(Clean);

        Clean.addActionListener(e -> {
            JButton agree = new JButton("   Да   ");
            buttomCustomize(agree);
            agree.addActionListener(e13 -> {
                for (int i = 0; i < 5; i++) {
                    jTextField[i].setText(null);
                }
                jTextArea.setText(null);
                jTextAreaMistake.setText(null);
                jStartDate.setText(null);
                jEndDate.setText(null);
                JOptionPane.getRootFrame().dispose();
            });

            JButton disagree = new JButton("   Нет   ");
            buttomCustomize(disagree);
            disagree.addActionListener(new ResumeActionListener());

            JButton[] buttons = {agree, disagree};
            JLabel cleanLabel = new JLabel("Вы действительно хотите отчистить введенные данные?");
            labelCustomize(cleanLabel);
            JOptionPane.showOptionDialog(null, cleanLabel, "Отчистка данных",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE, null, buttons, buttons[0]);
        });

        jFrame.setLocationRelativeTo(null);
        jFrame.add(jPanel);
        jFrame.setVisible(true);


    }


    static class PreExitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            winClosing();
        }
    }

    static class ResumeActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.getRootFrame().dispose();
        }
    }

    static class ExitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.exit(0);
        }
    }


    static void buttomCustomize(JButton jButton) {
        jButton.setFont(new Font("", Font.PLAIN, 12));
        jButton.setForeground(new Color(0, 0, 0));
        jButton.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
        jButton.setBackground(new Color(232, 232, 232));
    }

    static void labelCustomize(JLabel jLabel) {
        jLabel.setFont(new Font("", Font.PLAIN, 12));
        jLabel.setForeground(new Color(0, 0, 0));
    }

    static void borderCustomize(JScrollPane jScrollPane, String name) {
        jScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.gray, 2), name, 1, 0,
                new Font("Arial", Font.PLAIN, 12)));
    }

    static void dataError(String errorName) {
        JButton Agree = new JButton("   Ок   ");
        buttomCustomize(Agree);
        JLabel dateErrorLabel = new JLabel(errorName);
        labelCustomize(dateErrorLabel);
        Agree.addActionListener(new ResumeActionListener());
        JButton[] buttons = {Agree};
        JOptionPane.showOptionDialog(null, dateErrorLabel, "Error",
                JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[0]);
    }

    public static float Reverse(String str) {
        float rev = 0;
        try {
            if (str.length() < 1) {
                rev = 0.0f;
            } else {
                String newstr = str.replace(',', '.');
                rev = Float.parseFloat(newstr);
            }
        } catch (NumberFormatException e) {
            System.err.println("Wrong input string format!");
        }
        return rev;
    }

    public static void winClosing() {
        JButton agree = new JButton("   Да   ");
        buttomCustomize(agree);
        agree.addActionListener(new ExitActionListener());
        JButton disagree = new JButton("   Нет   ");
        buttomCustomize(disagree);
        disagree.addActionListener(new ResumeActionListener());

        JButton[] buttons = {agree, disagree};
        JLabel exitLabel = new JLabel("Закрыть окно?");
        labelCustomize(exitLabel);
        JOptionPane.showOptionDialog(null, exitLabel, "Подтверждение выхода",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE, null, buttons, buttons[0]);
    }

    static boolean connectionCheck() {
        boolean inetcon;
        try {
            final URL url = new URL("http://mfd.ru");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            inetcon = true;
        } catch (Exception e) {
            inetcon = false;
        }
        return inetcon;
    }
}

class TextAreaOutputStream extends OutputStream {
    private JTextArea textControl;

    public TextAreaOutputStream(JTextArea control) {
        textControl = control;
    }

    public void write(int in) {
        textControl.append(String.valueOf((char) in));
    }
}

