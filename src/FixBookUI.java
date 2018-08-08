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
		displayOutput("Fix Book Use Case UI\n");
		
		while (true) {
			
			switch (state) {
			
			case READY:
				String bookString = getInput("Scan Book (<enter> completes): ");
				if (bookString.length() == 0) {
					fixBookControl.scanningComplete();
				}
				else {
					try {
						int bookID = Integer.valueOf(bookString).intValue();
						fixBookControl.bookScanned(bookID);
					}
					catch (NumberFormatException e) {
						displayOutput("Invalid bookId");
					}
				}
				break;	
				
			case FIXING:
				String userAnswer = getInput("Fix Book? (Y/N) : ");
				boolean fix = false;
				if (userAnswer.toUpperCase().equals("Y")) {
					fix = true;
				}
				fixBookControl.fixBook(fix);
				break;
								
			case COMPLETED:
				displayOutput("Fixing process complete");
				return;
			
			default:
				displayOutput("Unhandled state");
				throw new RuntimeException("FixBookUI : unhandled state :" + state);			
			
			}		
		}
		
	}

	
	private String getInput(String prompt) {
		System.out.print(prompt);
		return input.nextLine();
	}	
		
		
	private void displayOutput(Object object) {
		System.out.println(object);
	}
	

	public void display(Object object) {
		displayOutput(object);
	}
	
	
}
