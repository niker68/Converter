package Controller;

import Model.Expression;
import Model.Rule;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class ConvertController implements HttpHandler {
    String htmlResponse;
    ArrayList<Rule> rules;
    String from;
    String to;
    public ConvertController(ArrayList<Rule> rules) {
        this.rules = rules;
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if ("POST".equals(httpExchange.getRequestMethod())) {
            try (final BufferedReader in = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()))) {
                String inputLine;
                final StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                JSONObject json = new JSONObject(content.toString());
                from = json.getString("from").replaceAll(" ", "");
                to = json.getString("to").replaceAll(" ", "");
                Expression exp0 = new Expression(from, rules);
                Expression exp1 = new Expression(to, rules);
                Expression expstart0 = new Expression(from, rules);
                Expression expstart1 = new Expression(to, rules);
                exp0.allUnitsToMin();
                exp0.deleteSimilarUnits();
                exp1.allUnitsToMin();
                exp1.deleteSimilarUnits();
                Double result = exp0.value / exp1.value;
                ArrayList<String> exp0UnitsNum = new ArrayList<>();
                ArrayList<String> exp1UnitsNum = new ArrayList<>();
                ArrayList<String> exp0UnitsDenom = new ArrayList<>();
                ArrayList<String> exp1UnitsDenom = new ArrayList<>();
                for (int i = 0; i < exp0.getArrNum().length; i++) {
                    exp0UnitsNum.add(exp0.getArrNum()[i]);
                }
                for (int i = 0; i < exp1.getArrNum().length; i++) {
                    exp1UnitsNum.add(exp1.getArrNum()[i]);
                }
                if (exp0.getArrDenom() != null) {
                    for (int i = 0; i < exp0.getArrDenom().length; i++) {
                        exp0UnitsDenom.add(exp0.getArrDenom()[i]);
                    }
                }
                if (exp1.getArrDenom() != null) {
                    for (int i = 0; i < exp1.getArrDenom().length; i++) {
                        exp1UnitsDenom.add(exp1.getArrDenom()[i]);
                    }
                }
                boolean conversationIsPossible = true;
                for (String unit0 : exp0UnitsNum
                ) {
                    for (String unit1 : exp1UnitsNum
                    ) {
                        if (unit0.equals(unit1)) {
                            exp0UnitsNum.set(exp0UnitsNum.indexOf(unit0), "");
                            exp1UnitsNum.set(exp1UnitsNum.indexOf(unit1), "");
                        }
                    }
                }

                for (String unit0 : exp0UnitsDenom
                ) {
                    for (String unit1 : exp1UnitsDenom
                    ) {
                        if (unit0.equals(unit1)) {
                            exp0UnitsDenom.set(exp0UnitsDenom.indexOf(unit0), "");
                            exp1UnitsDenom.set(exp1UnitsDenom.indexOf(unit1), "");
                        }
                    }
                }

                for (String unit0 : exp0UnitsNum
                ) {
                    if (!unit0.equals("")) {
                        conversationIsPossible = false;
                    }
                }

                for (String unit1 : exp1UnitsNum
                ) {
                    if (!unit1.equals("")) {
                        conversationIsPossible = false;
                    }
                }

                for (String unit0 : exp0UnitsDenom
                ) {
                    if (!unit0.equals("")) {
                        conversationIsPossible = false;
                    }
                }

                for (String unit1 : exp1UnitsDenom
                ) {
                    if (!unit1.equals("")) {
                        conversationIsPossible = false;
                    }
                }

                if ((!(exp0.isAllUnitsIsFind()))) {
                    unitNotFound400(httpExchange);
                } else if ((!(exp1.isAllUnitsIsFind())) & (!(to.equals("")))) {
                    unitNotFound400(httpExchange);
                } else if (!conversationIsPossible) {
                    notFound404(httpExchange);
                } else {
                    ok200(httpExchange, result);
                }
            }
        }
        else if ("GET".equals(httpExchange.getRequestMethod())) {
            String path = new File("").getAbsolutePath() +"\\src\\main\\webapp\\WEB-INF\\pages\\index.html";
            StringBuilder string = new StringBuilder();
            try(FileReader reader = new FileReader(path))
            {
                char[] buf = new char[256];
                int c;
                while((c = reader.read(buf))>0){

                    if(c < 256){
                        buf = Arrays.copyOf(buf, c);
                    }
                    string.append(buf);
                }
            }
            catch(IOException ex){

                System.out.println(ex.getMessage());
            }


            htmlResponse = string.toString();
            System.out.println(string.toString());
            httpExchange.getResponseHeaders().add("Content-Type","text/html; charset=utf-8");
            httpExchange.sendResponseHeaders(200, htmlResponse.length());

            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(htmlResponse.toString().getBytes());
            outputStream.flush();
            outputStream.close();
        }


    }
    private void unitNotFound400(HttpExchange httpExchange) throws IOException {
            OutputStream outputStream = httpExchange.getResponseBody();
            htmlResponse = "Unknown units of measure";
            httpExchange.sendResponseHeaders(400, htmlResponse.length());
            outputStream.write(htmlResponse.toString().getBytes());
            outputStream.flush();
            outputStream.close();
    }
    private void ok200(HttpExchange httpExchange, Double response) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        htmlResponse = String.format("%.15f", response);
        httpExchange.sendResponseHeaders(200, htmlResponse.length());
        outputStream.write(htmlResponse.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }
    private void notFound404(HttpExchange httpExchange) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        htmlResponse = "Conversion is not possible";
        httpExchange.sendResponseHeaders(404, htmlResponse.length());
        outputStream.write(htmlResponse.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }
}

