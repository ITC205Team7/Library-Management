import java.io.Serializable;


@SuppressWarnings("serial")
public class Book implements Serializable {
	
	private String title;
	private String author;
	private String callNo;
	private int ID;
	
	private enum State { AVAILABLE, ON_LOAN, DAMAGED, RESERVED };
	private State state;
	
	
	public Book(String author, String title, String callNo, int bookID) {
		this.author = author;
		this.title = title;
		this.callNo = callNo;
		this.ID = bookID;
		this.state = State.AVAILABLE;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Book: ").append(ID).append("\n")
		  .append("  Title:  ").append(title).append("\n")
		  .append("  Author: ").append(author).append("\n")
		  .append("  CallNo: ").append(callNo).append("\n")
		  .append("  State:  ").append(state);
		
		return sb.toString();
	}

	public Integer getBookID() {
		return ID;
	}

	public String getTitle() {
		return title;
	}


	
	public boolean isAvailable() {
		return state == State.AVAILABLE;
	}

	
	public boolean isOnLoan() {
		return state == State.ON_LOAN;
	}

	
	public boolean isDamaged() {
		return state == State.DAMAGED;
	}

	
	public void isBorrowed() {
		if (state.equals(State.AVAILABLE)) {
			state = State.ON_LOAN;
		}
		else {
			throw new RuntimeException(String.format("Book: cannot borrow while book is in state: %s", state));
		}
		
	}


	public void isReturned(boolean DAMAGED) {
		if (state.equals(State.ON_LOAN)) {
			if (DAMAGED) {
				state = State.DAMAGED;
			}
			else {
				state = State.AVAILABLE;
			}
		}
		else {
			throw new RuntimeException(String.format("Book: cannot Return while book is in state: %s", state));
		}		
	}

	
	public void isRepaired() {
		if (state.equals(State.DAMAGED)) {
			state = State.AVAILABLE;
		}
		else {
			throw new RuntimeException(String.format("Book: cannot repair while book is in state: %s", state));
		}
	}


}
