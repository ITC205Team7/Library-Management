import java.util.Scanner;


public class PayFineUI {


	public static enum UIState { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };

	private PayFineControl payFineControl;
	private Scanner input;
	private UIState state;

	
	public PayFineUI(PayFineControl payFineControl) {
		this.payFineControl = payFineControl;
		input = new Scanner(System.in);
		state = UIState.INITIALISED;
		payFineControl.setPayFineUI(this);
	}
	
	//Set state
	public void setState(UIState state) {
		this.state = state;
	}

        //It will check the state and according to state it will display the message
	public void run() {
		output("Pay Fine Use Case UI\n");
		
		while (true) {
			
			switch (state) {
			
			case READY:
				String memStr = input("Swipe member card (press <enter> to cancel): ");
				if (memStr.length() == 0) {
					payFineControl.cancel();
					break;
				}
				try {
					int memberId = Integer.valueOf(memStr).intValue();
					payFineControl.cardSwiped(memberId);
				}
				catch (NumberFormatException e) {
					output("Invalid memberId");
				}
				break;
				
			case PAYING:
				double amount = 0;
				String amtStr = input("Enter amount (<Enter> cancels) : ");
				if (amtStr.length() == 0) {
					payFineControl.cancel();
					break;
				}
				try {
					amount = Double.valueOf(amtStr).doubleValue();
				}
				catch (NumberFormatException e) {}
				if (amount <= 0) {
					output("Amount must be positive");
					break;
				}
				payFineControl.payFine(amount);
				break;
								
			case CANCELLED:
				output("Pay Fine process cancelled");
				return;
			
			case COMPLETED:
				output("Pay Fine process complete");
				return;
			
			default:
				output("Unhandled state");
				throw new RuntimeException("FixBookUI : unhandled state :" + state);			
			
			}		
		}		
	}

	//For taking input from user
	private String input(String prompt) {
		System.out.print(prompt);
		return input.nextLine();
	}	
		
	//For showing output	
	private void output(Object object) {
		System.out.println(object);
	}	
			
        //For displaying output
	public void display(Object object) {
		output(object);
	}


}
