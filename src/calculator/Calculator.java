package calculator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

    private final HashMap<String, BigInteger> map;

    public Calculator(HashMap<String, BigInteger> map) {
        this.map = map;
    }

    private List<String> splitElements(String input) {
        List<String> elements = new ArrayList<>();
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+|\\d+|[+-]+|[*\\/\\^]");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            elements.add(matcher.group());
        }
        return elements;
    }

    private boolean isOperator(String str) {
        return str.matches("[+-]+|[*\\/\\^]");
    }

    public boolean isPlus(String str) {
        return str.chars().filter(c -> c == '-').count() % 2 == 0;
    }

    public BigInteger getValue(String str) throws Exception {
        if (Utils.isVariableValid(str)) {
            BigInteger value = map.get(str);
            if (value == null) {
                throw new Exception("Unknown variable");
            }
            return value;
        } else {
            try {
                return new BigInteger(str);
            } catch (NumberFormatException e) {
                throw new Exception("Invalid assignment");
            }
        }
    }

    public BigInteger getResult(String input) throws Exception {
        List<String> elements = splitElements(input);
        BigInteger result = BigInteger.ZERO;
        boolean plus = true;
        while (elements.indexOf("^") > -1) {
            elements = getNewList(elements, "^");
        }
        while (elements.indexOf("/") > -1) {
            elements = getNewList(elements, "/");
        }
        while (elements.indexOf("*") > -1) {
            elements = getNewList(elements, "*");
        }
        for (String element : elements) {
            if (isOperator(element)) {
                plus = isPlus(element);
            } else {
                if (plus) {
                    result = result.add(getValue(element));
                } else {
                    result = result.subtract(getValue(element));
                }
            }
        }
        return result;
    }

    private List<String> getNewList(List<String> newList, String str) throws Exception {
        int index = newList.indexOf(str);
        BigInteger firstValue = getValue(newList.get(index - 1));
        BigInteger secondValue = getValue(newList.get(index + 1));
        BigInteger result = switch (str) {
            case "^" -> firstValue.pow(secondValue.intValueExact());
            case "/" -> firstValue.divide(secondValue);
            case "*" -> firstValue.multiply(secondValue);
            default -> throw new Exception("Invalid operator");
        };        newList.set(index - 1, String.valueOf(result));
        newList.remove(index);
        newList.remove(index);
        return newList;
    }
}