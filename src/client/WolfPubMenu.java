
import java.util.Scanner;

public class WolfPubMenu {

    //Initializing Scanner object for reading from input
    private static Scanner sc = new Scanner(System.in);

    public static int menuManager() {
        int opt = -1;
        boolean check = false;

        System.out.println("Select a number from the list of options: ");
        System.out.println("0 All Publications");
        System.out.println("1 All Books");
        System.out.println("2 All Periodic Publications");
        System.out.println("3 All Staff");
        System.out.println("4 All Authors");
        System.out.println("5 All Editors");
        System.out.println("6 All distributors");
        System.out.println("7 All Orders");
        System.out.print("Your option ---> ");

        System.out.println(" Add staff member");
        System.out.println(" Update staff information");
        System.out.println(" Delete staff member");
        System.out.println(" Enter new distributor");

        /*
         * Check if the next item is an integer, if it is not show menu again asking for an option.
         * If it is an integer, check if it matches any existed option.
         *
         */
        while (check == false){
            if (!sc.hasNextInt()){
                System.out.println("Select a number from the list of options: ");
                System.out.println("0 All Publications");
                System.out.println("1 All Books");
                System.out.println("2 All Periodic Publications");
                System.out.println("3 All Staff");
                System.out.println("4 All Authors");
                System.out.println("5 All Editors");
                System.out.println("6 All distributors");
                System.out.println("7 All Orders");
                System.out.print("Your option ---> ");
                sc.next()
            } else if (sc.hasNextInt()){
                opt = Integer.parseInt(sc.next());
                if (opt >= 0 & opt <= 7) {
                    check = true;
                }
                else {
                    System.out.println("Select a number from the list of options: ");
                    System.out.println("0 All Publications");
                    System.out.println("1 All Books");
                    System.out.println("2 All Periodic Publications");
                    System.out.println("3 All Staff");
                    System.out.println("4 All Authors");
                    System.out.println("5 All Editors");
                    System.out.println("6 All distributors");
                    System.out.println("7 All Orders");
                    System.out.print("Your option ---> ");
                }
            }

        }
        return opt;

    }

    public static int loginMenu() {
        int opt = -1;
        /*
         * Check if input value is an integer. If it is an integer, login to the system.
         * If it is not an integer, error message is printed. Prompt a user for an option.
         */

        while (opt < 0){
            System.out.println("Enter 99 to exit ");

            // extra option for billing staff ??

            System.out.println("Enter your login ID (1111 for Manager, 2222 for Author/Editor, 3333 for Distributor, 4444 for Billing Staff): ");
            while (!sc.hasNextInt()){
                System.out.println("Invalid input");
                System.out.println("Enter 99 to exit ");
                System.out.println("Enter your login ID: ");
                sc.next();
            }
            opt=sc.nextInt();
        }
        return opt;
    }



}