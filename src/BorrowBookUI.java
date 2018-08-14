import java.util.Scanner;


public class BorrowBookUI {
	
	public static enum UI_STATE { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };

	private BorrowBookControl borrowBookControl;
	private Scanner input;
	private UI_STATE state;

	
	public BorrowBookUI(BorrowBookControl control) {
		this.borrowBookControl = control;
		input = new Scanner(System.in);
		state = UI_STATE.INITIALISED;
		control.setUI(this);
	}


    /**
     * Display a prompt and return user entered string.
     *
     * @return void
     */
	private String input(String prompt) {
		System.out.print(prompt);
		return input.nextLine();
	}


    /**
     * Print the object.
     * @param object print the object
     * @return void
     */
	private void output(Object object) {
		System.out.println(object);
	}


    /**
     * Change the state of the UI according the param.
     * @param state the new state for the ui
     * @return void
     */
	public void setState(UI_STATE state) {
		this.state = state;
	}

	
	public void run() {
		output("Borrow Book Use Case UI\n");
		
		while (true) {
			
			switch (state) {			
			
				case CANCELLED:
					output("Borrowing Cancelled");
					return;


				case READY:
					String memStr = input("Swipe member card (press <enter> to cancel): ");
					if (memStr.length() == 0) {
						borrowBookControl.cancelBorrowBooks();
						break;
					}
					try {
						int memberId = Integer.valueOf(memStr).intValue();
						borrowBookControl.swipeMemberCard(memberId);
					}
					catch (NumberFormatException e) {
						output("Invalid Member Id");
					}
					break;


				case RESTRICTED:
					input("Press <any key> to cancel");
					borrowBookControl.cancelBorrowBooks();
					break;


				case SCANNING:
					String bookStr = input("Scan Book (<enter> completes): ");
					if (bookStr.length() == 0) {
						borrowBookControl.completeBorrowBooks();
						break;
					}
					try {
						int bookId = Integer.valueOf(bookStr).intValue();
						borrowBookControl.scanBook(bookId);

					} catch (NumberFormatException e) {
						output("Invalid Book Id");
					}
					break;


				case FINALISING:
					String ans = input("Commit loans? (Y/N): ");
					if (ans.toUpperCase().equals("N")) {
						borrowBookControl.cancelBorrowBooks();

					} else {
						borrowBookControl.commitLoans();
						input("Press <any key> to complete ");
					}
					break;


				case COMPLETED:
					output("Borrowing Completed");
					return;


				default:
					output("Unhandled state");
					throw new RuntimeException("BorrowBookUI : unhandled state :" + state);
			}
		}		
	}


	/**
	 * Display an object.
	 *
	 * @param object Object typed param to display
	 * @return void
	 */
	public void display(Object object) {
		output(object);		
	}


}
