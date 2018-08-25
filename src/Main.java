import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Main {

    private static Scanner INPUT;
    private static Library LIBRARY;
    private static String MENU;
    private static Calendar CALENDAR;
    private static SimpleDateFormat DATEFORMAT;

    /*This method will create a menu and store it in a string and
    return that menu
    */
    private static String getMenu() {
        StringBuilder menu = new StringBuilder();

        menu.append("\nLibrary Main Menu\n\n")
                .append("  M  : add member\n")
                .append("  LM : list members\n")
                .append("\n")
                .append("  B  : add book\n")
                .append("  LB : list books\n")
                .append("  FB : fix books\n")
                .append("\n")
                .append("  L  : take out a loan\n")
                .append("  R  : return a loan\n")
                .append("  LL : list loans\n")
                .append("\n")
                .append("  P  : pay fine\n")
                .append("\n")
                .append("  T  : increment date\n")
                .append("  Q  : quit\n")
                .append("\n")
                .append("Choice : ");

        return menu.toString();
    }

    /*this is main method which is controlling the logic of whole program
    all members will be list
    all books will be list
    and this will check the menu selection
    */
    public static void main(String[] args) {
        try {
            INPUT = new Scanner(System.in);
            LIBRARY = Library.INSTANCE();
            CALENDAR = Calendar.getInstance();
            DATEFORMAT = new SimpleDateFormat("dd/MM/yyyy");

            for (Member member : LIBRARY.Members()) {
                output(member);
            }
            output(" ");
            for (Book book : LIBRARY.Books()) {
                output(book);
            }

            MENU = getMenu();

            boolean check = false;

            while (!check) {

                output("\n" + DATEFORMAT.format(CALENDAR.Date()));
                String c = input(MENU);

                switch (c.toUpperCase()) {

                    case "M":
                        addMember();
                        break;

                    case "LM":
                        listMembers();
                        break;

                    case "B":
                        addBook();
                        break;

                    case "LB":
                        listBooks();
                        break;

                    case "FB":
                        fixBooks();
                        break;

                    case "L":
                        borrowBook();
                        break;

                    case "R":
                        returnBook();
                        break;

                    case "LL":
                        listCurrentLoans();
                        break;

                    case "P":
                        payFine();
                        break;

                    case "T":
                        incrementDate();
                        break;

                    case "Q":
                        check = true;
                        break;

                    default:
                        output("\nInvalid option\n");
                        break;
                }

                Library.SAVE();
            }
        } catch (RuntimeException e) {
            output(e);
        }
        output("\nEnded\n");
    }

    //It will create the instance for PayFineUI
    private static void payFine() {
        new PayFineUI(new PayFineControl()).run();
    }

    //list all the current loans
    private static void listCurrentLoans() {
        output("");
        for (loan loan : LIBRARY.CurrentLoans()) {
            output(loan + "\n");
        }
    }
    
    //list all the books
    private static void listBooks() {
        output("");
        for (Book book : LIBRARY.Books()) {
            output(book + "\n");
        }
    }

    //list all members
    private static void listMembers() {
        output("");
        for (Member member : LIBRARY.Members()) {
            output(member + "\n");
        }
    }

    //It will create the instance for BorrowBookUI
    private static void borrowBook() {
        new BorrowBookUI(new BorrowBookControl()).run();
    }

    //It will create the instance for ReturnBookUI
    private static void returnBook() {
        new ReturnBookUI(new ReturnBookControl()).run();
    }
    
    //It will create the instance for FixBookUI
    private static void fixBooks() {
        new FixBookUI(new FixBookControl()).run();
    }

    //For incrementing date in days
    private static void incrementDate() {
        try {
            int days = Integer.valueOf(input("Enter number of days: ")).intValue();
            CALENDAR.incrementDate(days);
            LIBRARY.checkCurrentLoans();
            output(DATEFORMAT.format(CALENDAR.Date()));

        } catch (NumberFormatException e) {
            output("\nInvalid number of days\n");
        }
    }

    //For adding new book in library
    private static void addBook() {

        String author = input("Enter author: ");
        String title = input("Enter title: ");
        String callNo = input("Enter call number: ");
        Book book = LIBRARY.Add_book(author, title, callNo);
        output("\n" + book + "\n");

    }

    //adding member 
    private static void addMember() {
        try {
            String lastName = input("Enter last name: ");
            String firstName = input("Enter first name: ");
            String email = input("Enter email: ");
            int phoneNo = Integer.valueOf(input("Enter phone number: ")).intValue();
            Member member = LIBRARY.Add_mem(lastName, firstName, email, phoneNo);
            output("\n" + member + "\n");

        } catch (NumberFormatException e) {
            output("\nInvalid phone number\n");
        }

    }

    //taking input from user
    private static String input(String prompt) {
        System.out.print(prompt);
        return INPUT.nextLine();
    }

    //showing output
    private static void output(Object object) {
        System.out.println(object);
    }

}
