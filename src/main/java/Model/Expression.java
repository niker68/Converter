package Model;

import com.sun.net.httpserver.HttpExchange;
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.util.ArrayList;

public class Expression {
    public Double value = 1.0;
    public String[]arrNumDenom =null;
    public String[]arrNum =null;
    public String[]arrDenom =null;
    public ArrayList<Rule> rules;
    boolean allUnitsIsFind;
    public Expression(String expression,ArrayList<Rule> rules) throws IOException {
        this.rules = rules;
        if(expression.contains("/")){
            arrNumDenom = expression.split("/");
            arrNum = arrNumDenom[0].split("\\*");

            arrDenom = arrNumDenom[1].split("\\*");
        } else {
            arrNum = expression.split("\\*");
        }
        checkAllUnits();
    }
    public void allUnitsToMin(){
        for (int i = 0; i<arrNum.length;i++) {
            boolean isFind = true;
                while (isFind){
                    isFind = false;
                for (Rule rule : rules) {
                    if (arrNum[i].equals(rule.getS())&(rule.getValue() > 1.0)) {
                            value = value * rule.getValue();
                            arrNum[i] = rule.getT();
                            isFind = true;
                    } else if (arrNum[i].equals(rule.getT())&((1.0 / rule.getValue()) > 1.0)) {
                            value = value / rule.getValue();
                            arrNum[i] = rule.getS();
                            isFind = true;
                    }
                }
            }
        }
        if(arrDenom!=null) {
            for (int i = 0; i < arrDenom.length; i++) {
                boolean isFind = true;
                while (isFind) {
                    isFind = false;
                    for (Rule rule : rules) {
                        if (arrDenom[i].equals(rule.getS())) {
                            if (rule.getValue() > 1.0) {
                                //System.out.println(arrDenom[i] + " " + value.toString());
                                value = value / rule.getValue();
                                arrDenom[i] = rule.getT();
                                //System.out.println(arrDenom[i] + " " + value);
                                isFind = true;
                            }
                        } else if (arrDenom[i].equals(rule.getT())) {
                            if ((1.0 / rule.getValue()) > 1.0) {
                                //System.out.println(arrDenom[i] + " " + value);
                                value = value * rule.getValue();
                                arrDenom[i] = rule.getS();
                                //System.out.println(arrDenom[i] + " " + value.toString());
                                isFind = true;
                            }
                        }
                    }
                }
            }
        }

    }
    public void deleteSimilarUnits(){
        if(arrDenom!=null) {
            for (int i = 0; i < arrNum.length; i++) {
                for (int j = 0; j < arrDenom.length; j++) {
                    if (arrNum[i].equals(arrDenom[j])){
                        arrNum[i]="";
                        arrDenom[j]="";
                    }
                }
            }
        }
    }
    public boolean checkAllUnits() throws IOException {
        allUnitsIsFind = true;
        for (int i = 0; i < arrNum.length; i++) {
           if(!findUnitOfMeasure(arrNum[i])){
               allUnitsIsFind = false;
           }
        }
        if (arrDenom != null) {
            for (int i = 0; i < arrDenom.length; i++) {
                if(!findUnitOfMeasure(arrDenom[i])){
                    allUnitsIsFind = false;
                }
            }
        }
        return allUnitsIsFind;
    }
    private boolean findUnitOfMeasure(String unit) throws IOException {
        boolean isFind = false;
        for (Rule rule : rules) {
            if (unit.equals(rule.getS()) | unit.equals(rule.getT())) {
                isFind = true;
            }
        }
        if (isFind) {
            return true;
        } else {
            if (!isFind) {
            } return false;
        }
    }
    public boolean isAllUnitsIsFind() {
        return allUnitsIsFind;
    }
    public String[] getArrDenom() {
        return arrDenom;
    }
    @Override
    public String toString() {
        StringBuilder tostr= new StringBuilder(value +"*");
        for (int i = 0; i < arrNum.length; i++){
            if (!arrNum[i].equals("")){
            tostr.append(arrNum[i]);
            tostr.append("*");}
        }
        tostr.deleteCharAt(tostr.lastIndexOf("*"));
        if (arrDenom!=null){
            tostr.append("/");
            for (int i = 0; i < arrDenom.length; i++){
                if (!arrDenom[i].equals("")) {
                    tostr.append(arrDenom[i]);
                    tostr.append("*");
                }
            }
            tostr.deleteCharAt(tostr.lastIndexOf("*"));
        }
        return tostr.toString();
    }
    public String[] getArrNum() {
        return arrNum;
    }
}

