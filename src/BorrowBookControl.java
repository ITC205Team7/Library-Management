import java.util.ArrayList;
import java.util.List;

public class BorrowBookControl {
	
	private BorrowBookUI borrowBookUI;
	
	private library libraryObj;
	private member currentMember;
	private enum CONTROL_STATE { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };
	private CONTROL_STATE state;
	
	private List<book> listPendingBooks;
	private List<Loan> listLoans;
	private book currentBook;
	
	
	public BorrowBookControl() {
		this.libraryObj = libraryObj.INSTANCE();
		state = CONTROL_STATE.INITIALISED;
	}
	

	public void setUI(BorrowBookUI ui) {
		if (!state.equals(CONTROL_STATE.INITIALISED)) {
			throw new RuntimeException("BorrowBookControl: cannot call setUI except in INITIALISED state");
		}
		this.borrowBookUI = ui;
		ui.setState(BorrowBookUI.UI_STATE.READY);
		state = CONTROL_STATE.READY;		
	}


	/**
     * Display and set UI state according the status of entered memberId.
     *
     * @param memberId - valid member id to get member information
     * @return void
     */
	public void swipeMemberCard(int memberId) {
		if (!state.equals(CONTROL_STATE.READY)) {
			throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");
		}
		currentMember = libraryObj.getMember(memberId);
		if (currentMember == null) {
			borrowBookUI.display("Invalid memberId");
			return;
		}
		if (libraryObj.memberCanBorrow(currentMember)) {
			listPendingBooks = new ArrayList<>();
			borrowBookUI.setState(BorrowBookUI.UI_STATE.SCANNING);
			state = CONTROL_STATE.SCANNING;
		}
		else {
			borrowBookUI.display("Member cannot borrow at this time");
			borrowBookUI.setState(BorrowBookUI.UI_STATE.RESTRICTED);
		}
	}


    /**
     * Display book status based on the entered book id.
     *
     * @param bookId - valid book id to load book information
     * @return void
     */
	public void scanBook(int bookId) {
		currentBook = null;
		if (!state.equals(CONTROL_STATE.SCANNING)) {
			throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
		}
		currentBook = libraryObj.Book(bookId);
		if (currentBook == null) {
			borrowBookUI.display("Invalid bookId");
			return;
		}
		if (!currentBook.Available()) {
			borrowBookUI.display("Book cannot be borrowed");
			return;
		}
		listPendingBooks.add(currentBook);
		for (book pendingBook : listPendingBooks) {
			borrowBookUI.display(pendingBook.toString());
		}
		if (libraryObj.loansRemainingForMember(currentMember) - listPendingBooks.size() == 0) {
			borrowBookUI.display("Loan limit reached");
			completeBorrowBooks();
		}
	}


	/**
	 * Complete borrowing books.
	 *
	 * @return void
	 */
	public void completeBorrowBooks() {
		if (listPendingBooks.size() == 0) {
			cancelBorrowBooks();
		}
		else {
			borrowBookUI.display("\nFinal Borrowing List");
			for (book b : listPendingBooks) {
				borrowBookUI.display(b.toString());
			}
			listLoans = new ArrayList<Loan>();
			borrowBookUI.setState(BorrowBookUI.UI_STATE.FINALISING);
			state = CONTROL_STATE.FINALISING;
		}
	}


    /**
     * Commit and display loans for current member.
     *
     * @return void
     */
	public void commitLoans() {
		if (!state.equals(CONTROL_STATE.FINALISING)) {
			throw new RuntimeException("BorrowBookControl: cannot call commitLoans except in FINALISING state");
		}	
		for (book pendingBook : listPendingBooks) {
            Loan loan = libraryObj.issueLoan(pendingBook, currentMember);
			listLoans.add(loan);
		}
		borrowBookUI.display("Completed Loan Slip");
		for (Loan loan : listLoans) {
			borrowBookUI.display(loan.toString());
		}
		borrowBookUI.setState(BorrowBookUI.UI_STATE.COMPLETED);
		state = CONTROL_STATE.COMPLETED;
	}


    /**
     * Cancel change state of the borrow book borrowBookUI.
     *
     * @return void
     */
	public void cancelBorrowBooks() {
		borrowBookUI.setState(BorrowBookUI.UI_STATE.CANCELLED);
		state = CONTROL_STATE.CANCELLED;
	}
	
	
}
