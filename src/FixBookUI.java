import java.util.Scanner;


public class FixBookUI {

	public static enum UIState { INITIALISED, READY, FIXING, COMPLETED };

	private FixBookControl fixBookControl;
	private Scanner input;
	private UIState state;

	
	public FixBookUI(FixBookControl fixBookControl) {
		this.fixBookControl = fixBookControl;
		input = new Scanner(System.in);
		state = UIState.INITIALISED;
		fixBookControl.setUI(this);
	}


	public void setState(UIState state) {
		this.state = state;
	}

	
	public void run() {
		output("Fix Book Use Case UI\n");
		
		while (true) {
			
			switch (state) {
			
			case READY:
				String bookStr = input("Scan Book (<enter> completes): ");
				if (bookStr.length() == 0) {
					fixBookControl.scanningComplete();
				}
				else {
					try {
						int bookID = Integer.valueOf(bookStr).intValue();
						fixBookControl.bookScanned(bookID);
					}
					catch (NumberFormatException e) {
						output("Invalid bookId");
					}
				}
				break;	
				
			case FIXING:
				String ans = input("Fix Book? (Y/N) : ");
				boolean fix = false;
				if (ans.toUpperCase().equals("Y")) {
					fix = true;
				}
				fixBookControl.fixBook(fix);
				break;
								
			case COMPLETED:
				output("Fixing process complete");
				return;
			
			default:
				output("Unhandled state");
				throw new RuntimeException("FixBookUI : unhandled state :" + state);			
			
			}		
		}
		
	}

	
	private String input(String prompt) {
		System.out.print(prompt);
		return input.nextLine();
	}	
		
		
	private void output(Object object) {
		System.out.println(object);
	}
	

	public void display(Object object) {
		output(object);
	}
	
	
}
