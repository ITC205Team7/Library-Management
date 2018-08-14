import java.util.Scanner;


public class ReturnBookUI {

	public static enum UIstate { INITIALISED, READY, INSPECTING, COMPLETED };

	private ReturnBookControl control;
	private Scanner input;
	private UIstate state;

	
	public ReturnBookUI(ReturnBookControl control) {
		this.control = control;
		input = new Scanner(System.in);
		state = UIstate.INITIALISED;
		control.setUI(this);
	}


	public void run() {		
		output("Return Book Use Case UI\n");
		
		while (true) {
			
			switch (state) {
			
			case INITIALISED:
				break;
				
			case READY:
				String bookString = input("Scan Book (<enter> completes): ");
				if (bookString.length() == 0) {
					control.scanningComplete();
				}
				else {
					try {
						int bookID = Integer.valueOf(bookString).intValue();
						control.bookScanned(bookID);
					}
					catch (NumberFormatException e) {
						output("Invalid bookID");
					}					
				}
				break;				
				
			case INSPECTING:
				String answer = input("Is book Damaged? (Y/N): ");
				boolean isDamaged = false;
				if (answer.toUpperCase().equals("Y")) {
					isDamaged = true;
				}
				control.dischargeLoan(isDamaged);
			
			case COMPLETED:
				output("Return processing complete");
				return;
			
			default:
				output("Unhandled state");
				throw new RuntimeException("ReturnBookUI : unhandled state :" + state);			
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

	
	public void setState(UIstate state) {
		this.state = state;
	}

	
}
