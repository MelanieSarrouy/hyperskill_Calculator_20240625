package calculator;

public class Command {

    public static boolean commandResponse(String input) {
        switch (input) {
            case "/help":
                System.out.println("The program calculates the result of the operation.");
                return false;
            case "/exit":
                System.out.println("Bye!");
                return true;
            default:
                System.out.println("Unknown command");
                return false;
        }
    }
}

