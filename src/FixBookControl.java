public class FixBookControl {
	
	private FixBookUI fixBookUI;
	private enum ControlState { INITIALISED, READY, FIXING };
	private ControlState state;
	
	private library library;
	private book currentBook;


	public FixBookControl() {
		this.library = library.INSTANCE();
		state = ControlState.INITIALISED;
	}
	
	
	public void setUI(FixBookUI fixBookUI) {
		if (!state.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
		}	
		this.fixBookUI = fixBookUI;
		fixBookUI.setState(FixBookUI.UIState.READY);
		state = ControlState.READY;
	}


	public void bookScanned(int bookID) {
		if (!state.equals(ControlState.READY)) {
			throw new RuntimeException("FixBookControl: cannot call bookScanned except in READY state");
		}	
		currentBook = library.Book(bookID);
		
		if (currentBook == null) {
			fixBookUI.display("Invalid bookId");
			return;
		}
		if (!currentBook.Damaged()) {
			fixBookUI.display("\"Book has not been damaged");
			return;
		}
		fixBookUI.display(currentBook.toString());
		fixBookUI.setState(FixBookUI.UIState.FIXING);
		state = ControlState.FIXING;
	}


	public void fixBook(boolean fix) {
		if (!state.equals(ControlState.FIXING)) {
			throw new RuntimeException("FixBookControl: cannot call fixBook except in FIXING state");
		}	
		if (fix) {
			library.repairBook(currentBook);
		}
		currentBook = null;
		fixBookUI.setState(FixBookUI.UIState.READY);
		state = ControlState.READY;
	}

	
	public void scanningComplete() {
		if (!state.equals(ControlState.READY)) {
			throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
		}
		fixBookUI.setState(FixBookUI.UIState.COMPLETED);
	}






}
