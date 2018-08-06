import java.util.ArrayList;
import java.util.List;

public class BorrowBookControl {
	
	private BorrowBookUI ui;
	
	private library libraryObj;
	private member currentMember;
	private enum CONTROL_STATE { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };
	private CONTROL_STATE state;
	
	private List<book> listPendingBooks;
	private List<loan> listLoans;
	private book currentBook;
	
	
	public BorrowBookControl() {
		this.libraryObj = libraryObj.INSTANCE();
		state = CONTROL_STATE.INITIALISED;
	}
	

	public void setUI(BorrowBookUI ui) {
		if (!state.equals(CONTROL_STATE.INITIALISED)) {
			throw new RuntimeException("BorrowBookControl: cannot call setUI except in INITIALISED state");
		}
		this.ui = ui;
		ui.setState(BorrowBookUI.UI_STATE.READY);
		state = CONTROL_STATE.READY;		
	}

		
	public void swipe(int memberId) {
		if (!state.equals(CONTROL_STATE.READY)) {
			throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");
		}
		currentMember = libraryObj.getMember(memberId);
		if (currentMember == null) {
			ui.display("Invalid memberId");
			return;
		}
		if (libraryObj.memberCanBorrow(currentMember)) {
			listPendingBooks = new ArrayList<>();
			ui.setState(BorrowBookUI.UI_STATE.SCANNING);
			state = CONTROL_STATE.SCANNING;
		}
		else {
			ui.display("Member cannot borrow at this time");
			ui.setState(BorrowBookUI.UI_STATE.RESTRICTED);
		}
	}
	
	
	public void scan(int bookId) {
		currentBook = null;
		if (!state.equals(CONTROL_STATE.SCANNING)) {
			throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
		}
		currentBook = libraryObj.Book(bookId);
		if (currentBook == null) {
			ui.display("Invalid bookId");
			return;
		}
		if (!currentBook.Available()) {
			ui.display("Book cannot be borrowed");
			return;
		}
		listPendingBooks.add(currentBook);
		for (book B : listPendingBooks) {
			ui.display(B.toString());
		}
		if (libraryObj.loansRemainingForMember(currentMember) - listPendingBooks.size() == 0) {
			ui.display("Loan limit reached");
			complete();
		}
	}
	
	
	public void complete() {
		if (listPendingBooks.size() == 0) {
			cancel();
		}
		else {
			ui.display("\nFinal Borrowing List");
			for (book b : listPendingBooks) {
				ui.display(b.toString());
			}
			listLoans = new ArrayList<loan>();
			ui.setState(BorrowBookUI.UI_STATE.FINALISING);
			state = CONTROL_STATE.FINALISING;
		}
	}


	public void commitLoans() {
		if (!state.equals(CONTROL_STATE.FINALISING)) {
			throw new RuntimeException("BorrowBookControl: cannot call commitLoans except in FINALISING state");
		}	
		for (book b : listPendingBooks) {
			loan loan = libraryObj.issueLoan(b, currentMember);
			listLoans.add(loan);
		}
		ui.display("Completed Loan Slip");
		for (loan loan : listLoans) {
			ui.display(loan.toString());
		}
		ui.setState(BorrowBookUI.UI_STATE.COMPLETED);
		state = CONTROL_STATE.COMPLETED;
	}

	
	public void cancel() {
		ui.setState(BorrowBookUI.UI_STATE.CANCELLED);
		state = CONTROL_STATE.CANCELLED;
	}
	
	
}
