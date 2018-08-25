import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class Loan implements Serializable {
	
	public enum LoanState { CURRENT, OVER_DUE, DISCHARGED };
	
	private int loanId;
	private Book loanedBook;
	private Member loanedMember;
	private Date loanDueDate;
	private LoanState state;

	
	public Loan(int loanId, Book book, Member member, Date dueDate) {
		this.loanId = loanId;
		this.loanedBook = book;
		this.loanedMember = member;
		this.loanDueDate = dueDate;
		this.state = LoanState.CURRENT;
	}


	/**
	 * Set loan state to over due if the due date of the loan pass the current date.
	 *
	 * @return void
	 */
	public void checkOverDue() {
		if (state == LoanState.CURRENT &&
			Calendar.getInstance().getDate().after(loanDueDate)) {
			this.state = LoanState.OVER_DUE;
		}
	}


    /**
     * Check whether the loan is over due or not.
     *
     * @return boolean
     */
	public boolean isOverDue() {
		return state == LoanState.OVER_DUE;
	}


    /**
     * get loan id.
     *
     * @return int
     */
	public int getId() {
		return loanId;
	}


    /**
     * get due date of loan.
     *
     * @return Date
     */
	public Date getDueDate() {
		return loanDueDate;
	}


    /**
     * Display details of loan.
     *
     * @return String
     */
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		StringBuilder sb = new StringBuilder();
		sb.append("Loan:  ").append(loanId).append("\n")
		  .append("  Borrower ").append(loanedMember.getId()).append(" : ")
		  .append(loanedMember.getLastName()).append(", ").append(loanedMember.getFirstName()).append("\n")
		  .append("  Book ").append(loanedBook.getBookID()).append(" : " )
		  .append(loanedBook.getTitle()).append("\n")
		  .append("  DueDate: ").append(dateFormat.format(loanDueDate)).append("\n")
		  .append("  State: ").append(state);		
		return sb.toString();
	}


    /**
     * Return person who loan the book.
     *
     * @return Member
     */
	public Member getLoanedMember() {
		return loanedMember;
	}


    /**
     * return the book is being loaned.
     *
     * @return Book
     */
	public Book getLoanedBook() {
		return loanedBook;
	}


    /**
     * Set the loan state to discharged.
     *
     * @return void
     */
	public void Loan() {
		state = LoanState.DISCHARGED;
	}

}
