package src;

public class PayFineControl {
	
	private PayFineUI payFineUI;
	private enum ControlState { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };
	private ControlState state;
	private Library library;
	private Member member;


	public PayFineControl() {
		this.library = library.getInstance();
		state = ControlState.INITIALISED;
	}
	
        //this method will setting PayFineUI
	public void setPayFineUI(PayFineUI payFineUI) {
		if (!state.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
		}	
		this.payFineUI = payFineUI;
		payFineUI.setState(PayFineUI.UIState.READY);
		state = ControlState.READY;		
	}

        //Checking Card with memberId
	public void swipeCard(int memberId) {
		if (!state.equals(ControlState.READY)) {
			throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
		}	
		member = library.getMember(memberId);
		
		if (member == null) {
			payFineUI.display("Invalid Member Id");
			return;
		}
		payFineUI.display(member.toString());
		payFineUI.setState(PayFineUI.UIState.PAYING);
		state = ControlState.PAYING;
	}
	
	//For cancelling
	public void cancel() {
		payFineUI.setState(PayFineUI.UIState.CANCELLED);
		state = ControlState.CANCELLED;
	}

        //For paying fine
	public double payFine(double amount) {
		if (!state.equals(ControlState.PAYING)) {
			throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
		}	
		double change = member.payFine(amount);
		if (change > 0) {
			payFineUI.display(String.format("Change: $%.2f", change));
		}
		payFineUI.display(member.toString());
		payFineUI.setState(PayFineUI.UIState.COMPLETED);
		state = ControlState.COMPLETED;
		return change;
	}
	


}
