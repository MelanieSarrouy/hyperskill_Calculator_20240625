package calculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static boolean isVariableValid(String str) {
        Pattern pattern  = Pattern.compile("^[a-zA-Z]+$"); // get variable
        Matcher matcher  = pattern.matcher(str);
        return matcher.matches();
    }

}