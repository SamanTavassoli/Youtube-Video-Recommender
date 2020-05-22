import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("Type Command:");
            String input = scanner.nextLine();

            if (input.equals("run")) {
                run();
            }

            if (input.equals("exit")) {
                isRunning = false;
            }
        }
    }


    public static void run() {

    }
}
