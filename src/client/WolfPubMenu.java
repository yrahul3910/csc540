import java.util.Scanner;

abstract class Menu {
    WolfPub runner;
    final String badInput = "You're a bad bad person. Exiting...";

    void checkBadThingsHappen(int ret, int high) {
        if (ret < 1 || ret > high) {
            System.out.println(badInput);
            System.exit(1);
        }
    }

    public Menu() {
        runner = new WolfPub("jdbc:mysql://localhost:3306/setup?serverTimezone=UTC", "root", "password");
    }
    public abstract int display();
}

class MainMenu extends Menu {
    @Override
    public int display() {
        System.out.print("Main Menu:\n1. Editing and publishing\n2. Production of publications\n3.Distribution\n" +
                "4.Reports\nEnter an option: ");

        int ret = new Scanner(System.in).nextInt();
        checkBadThingsHappen(ret, 4);

        return ret;
    }
}

class EditingMenu extends Menu {
    @Override
    public int display() {
        System.out.print("1. Enter basic information on a new publication\n2. Update publication information\n" +
                "3. Assign editor to publication\n4. View information on publications for an editor\n" +
                "5. Add an article\n 6. Delete an article\n7. Add chapter to book\n8. Delete chapter from book\n" +
                "Enter an option: ");

        int option = new Scanner(System.in).nextInt();
        checkBadThingsHappen(option, 8);

        switch (option) {
            case 1:
                runner.enterPublicationInfo();
                break;
            case 2:
                runner.updatePublication();
                break;
            case 3:
                runner.assignEditor();
                break;
            case 4:
                runner.showPublicationEditor();
                break;
            case 5:
                runner.addArticle();
                break;
            case 6:
                runner.deleteArticle();
                break;
            case 7:
                runner.addChapter();
                break;
            case 8:
                runner.deleteChapter();
                break;
        }

        return 0;
    }
}

class ProductionMenu extends Menu {
    @Override
    public int display() {
        System.out.println("1. Enter a new book edition\n2. Enter new publication issue\n3. Update a book edition\n" +
                "4. Update a publication issue\n5. Delete a book edition\n6. Delete a publication issue\n" +
                "7. Enter an article\n8. Enter a chapter\n9. Update article\n10. Update chapter\n" +
                "11. Enter article text\n12. Update article text\n13. Find book by topic\n14. Find book by date\n" +
                "15. Find book by author\n16. Find articles by topic\n17. Find articles by date\n" +
                "18. Find articles by author\n19. Enter payment for staff\n20. Find payment claim date for staff\n");

        int option = new Scanner(System.in).nextInt();
        checkBadThingsHappen(option, 20);

        return 0;
    }
}

class DistributionMenu extends Menu {
    @Override
    public int display() {
        return 0;
    }
}

class ReportsMenu extends Menu {
    @Override
    public int display() {
        return 0;
    }
}

public class WolfPubMenu {
    public static void main(String[] args) {
        Menu[] submenus =  new Menu[4];
        submenus[0] = new EditingMenu();
        submenus[1] = new ProductionMenu();
        submenus[2] = new DistributionMenu();
        submenus[3] = new ReportsMenu();

        MainMenu menu = new MainMenu();
        submenus[menu.display() - 1].display();
    }
}