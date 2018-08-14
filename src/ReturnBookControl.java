public class ReturnBookControl {

	private ReturnBookUI ui;
	private enum Controlstate{ INITIALISED, READY, INSPECTING };
	private Controlstate state;
	
	private library library;
	private loan currentLoan;
	

	public ReturnBookControl() {
		this.library = library.INSTANCE();
		state = Controlstate.INITIALISED;
	}
	
	
	public void setUI(ReturnBookUI ui) {
		if (!state.equals(Controlstate.INITIALISED)) {
			throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
		}	
		this.ui = ui;
		ui.setState(ReturnBookUI.UIstate.READY);
		state = Controlstate.READY;
	}


	public void bookScanned(int bookID) {
		if (!state.equals(Controlstate.READY)) {
			throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
		}	
		book currentBook = library.Book(bookID);
		
		if (currentBook == null) {
			ui.display("Invalid Book Id");
			return;
		}
		if (!currentBook.On_loan()) {
			ui.display("Book has not been borrowed");
			return;
		}		
		currentLoan = library.getLoanByBookId(bookID);
		double OverDueFine = 0.0;
		if (currentLoan.isOverDue()) {
			OverDueFine = library.calculateOverDueFine(currentLoan);
		}
		ui.display("Inspecting");
		ui.display(currentBook.toString());
		ui.display(currentLoan.toString());
		
		if (currentLoan.isOverDue()) {
			ui.display(String.format("\nOverdue fine : $%.2f", OverDueFine));
		}
		ui.setState(ReturnBookUI.UIstate.INSPECTING);
		state = Controlstate.INSPECTING;
	}


	public void scanningComplete() {
		if (!state.equals(Controlstate.READY)) {
			throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
		}	
		ui.setState(ReturnBookUI.UIstate.COMPLETED);
	}


	public void dischargeLoan(boolean isDamaged) {
		if (!state.equals(Controlstate.INSPECTING)) {
			throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
		}	
		library.dischargeLoan(currentLoan, isDamaged);
		currentLoan = null;
		ui.setState(ReturnBookUI.UIstate.READY);
		state = Controlstate.READY;
	}


}
