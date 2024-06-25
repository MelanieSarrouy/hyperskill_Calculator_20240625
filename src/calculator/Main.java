package calculator;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        HashMap<String, BigInteger> map = new HashMap<>();
        Calculator calculator = new Calculator(map);
        Pattern commandPattern = Pattern.compile("^/.*"); // commands
        Pattern initiatorPattern = Pattern.compile(".*=.*"); // declaration
        Pattern variablePattern = Pattern.compile("^[a-zA-Z0-9]+"); // get variable

        while (true) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;
            String cleanInput = input.replaceAll("\\s+", "");
            Matcher commandMatcher = commandPattern.matcher(cleanInput);
            Matcher initiatorMatcher = initiatorPattern.matcher(cleanInput);
            Matcher variableMatcher = variablePattern.matcher(cleanInput);
            if (commandMatcher.matches()) {
                if (Command.commandResponse(cleanInput)) {
                    break;
                }
            } else if (initiatorMatcher.matches()) {
                handleDeclaration(cleanInput, calculator, map);
            } else if (variableMatcher.matches()) {
                handleVariable(cleanInput, map);
            } else {
                extractComponents(calculator, cleanInput);
            }
        }
        scanner.close();
    }

    private static void handleOperation(Calculator calculator, String cleanInput) {
        try {
            BigInteger result = calculator.getResult(cleanInput);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Invalid expression");
        }
    }

    private static void extractComponents(Calculator calculator, String cleanInput) throws Exception {
        String regex = ".*\\(.*\\).*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cleanInput);
        long occParOpen = cleanInput.chars().filter(c -> c== '(').count();
        long occParClose = cleanInput.chars().filter(c -> c== ')').count();
        if (occParOpen - occParClose != 0) {
            System.out.println("Invalid expression");
            return;
        }
        if (matcher.find()){
            StringBuilder newString = new StringBuilder();
            Deque<Integer> stack = new ArrayDeque<>();
            int start = 0;
            for (int i = 0; i < cleanInput.length(); i++) {
                char c = cleanInput.charAt(i);
                if (c == '(') {
                    stack.push(i);
                } else if (c == ')') {
                    int close = stack.pop();
                    newString.append(cleanInput, start, close);
                    newString.append(calculator.getResult(cleanInput.substring(close + 1, i)));
                    if (i < cleanInput.length() +1) newString.append(cleanInput.substring(i + 1));
                    break;
                }
            }
            extractComponents(calculator, newString.toString());
        } else {
            handleOperation(calculator, cleanInput);
        }
    }

    private static void handleVariable(String cleanInput, HashMap<String, BigInteger> map) {
        if (!Utils.isVariableValid(cleanInput)) {
            System.out.println("Invalid identifier");
            return;
        }
        BigInteger value = map.get(cleanInput);
        if (value == null) {
            System.out.println("Unknown variable");
        } else {
            System.out.println(value);
        }
    }

    private static void handleDeclaration(String cleanInput, Calculator calculator, HashMap<String, BigInteger> map) {
        String[] parts = cleanInput.split("=");
        if (parts.length != 2) {
            System.out.println("Invalid assignment");
            return;
        }
        String identifier = parts[0].trim();
        String assignment = parts[1].trim();
        if (!Utils.isVariableValid(identifier)) {
            System.out.println("Invalid identifier");
            return;
        }
        try {
            BigInteger result = calculator.getResult(assignment);
            map.put(identifier, result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}