import java.util.List;
import java.util.Scanner;

public class StringUtilities {
    public static int numericalSelect(String prompt, int lowerBound, int upperBound, Scanner input) {
        if (lowerBound > upperBound) {
            System.out.println("Invalid range, lists need to have at least one element");
            return lowerBound - 1;
        }
        int result = -1;
        boolean error;
        do {
            error = false;
            System.out.print(prompt + ": ");
            try {
                result = input.nextInt();
                if (result < lowerBound || result > upperBound) {
                    error = true;
                    System.out.println("Invalid input, please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input, please try again.");
                error = true;
                input.nextLine();
            }
        } while (error);
        return result;
    }

    public static String getUserInput(String prompt, Scanner input) {
        System.out.print(prompt + ": ");
        String output = input.next();
        input.nextLine();
        return output;
    }

    public static void optionList(List<? extends Listable> options) {
        int i = 1;
        for (Listable current: options) {
            System.out.println("(" + i + ") " + current.getName());
            i++;
        }
    }

    public static int optionListSelector(List<? extends Listable> options, String prompt, Scanner input) {
        optionList(options);
        return numericalSelect(prompt, 1, options.size(), input);
    }
}
