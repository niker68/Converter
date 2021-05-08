package Application;

import Controller.ConvertController;
import Model.Rule;
import au.com.bytecode.opencsv.CSVReader;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.ArrayList;

public class ConverterApplication {
    public static void main(String[] args) throws IOException {
        int serverPort = 8888;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        //String path = args[0];
        String path = "data/rules.txt";
        System.out.println(path);
        File file = new File(path);
        //CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file), "UTF-8"), ',' , '"' , 0);
        ArrayList<Rule> rules = new ArrayList<>();
        FileReader fr = new FileReader(file);
        //������� BufferedReader � ������������� FileReader ��� ����������� ����������
        BufferedReader reader = new BufferedReader(fr);


        String str= reader.readLine();
        String []line = str.split(",");
        while (!str.equals("")) {
            // ��������� ��������� ������ � �����
            str = reader.readLine();
            line = str.split(",");
            if (line.length >= 2) {
                Rule rule = new Rule(line[0], line[1], Double.parseDouble(line[2]));
                rules.add(rule);
                System.out.println(rule);
            }
        }

       /* while ((line = reader.readLine() != null) {
            if(line.length>=2) {
                Rule rule = new Rule(line[0], line[1], Double.parseDouble(line[2]));
                rules.add(rule);
                System.out.println(rule);
            }
        }*/
        server.createContext("/convert", new ConvertController(rules));
        server.setExecutor(null);
        server.start();
    }
}
