import java.util.Scanner;

abstract class Menu {
    public abstract int display();
}

class MainMenu extends Menu {
    @Override
    public int display() {
        System.out.println("Main Menu:\n1. Editing and publishing\n2. Production of publications\n3.Distribution\n4.Reports");

        int ret = new Scanner(System.in).nextInt();

        if (ret < 1 || ret > 4) {
            System.out.println("You're a bad bad person. Exiting...");
            System.exit(0);
        }

        return ret;
    }
}

class EditingMenu extends Menu {
    @Override
    public int display() {


        return 0;
    }
}

class ProductionMenu extends Menu {
    @Override
    public int display() {
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