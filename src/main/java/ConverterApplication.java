import Controller.ConvertController;
import Model.Rule;
import au.com.bytecode.opencsv.CSVReader;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class ConverterApplication {
    public static void main(String[] args) throws IOException {
        int serverPort = 8888;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        String path = args[0];
        //String path = new File("").getAbsolutePath() +"\\src\\main\\resources\\file.csv";
        File file = new File(path);
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file), "UTF-8"), ',' , '"' , 0);
        ArrayList<Rule> rules = new ArrayList<>();
        String []line;
        while ((line = reader.readNext()) != null) {
            if(line.length>=2) {
                Rule rule = new Rule(line[0], line[1], Double.parseDouble(line[2]));
                rules.add(rule);
                System.out.println(rule);
            }
        }
        server.createContext("/convert", new ConvertController(rules));
        server.setExecutor(null);
        server.start();
    }
}
