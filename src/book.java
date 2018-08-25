import java.io.Serializable;


@SuppressWarnings("serial")
public class Book implements Serializable {
	
	private String Title;
	private String Author;
	private String CallNo;
	private int ID;
	
	private enum State { AVAILABLE, ON_LOAN, DAMAGED, RESERVED };
	private State state;
	
	
	public Book(String author, String title, String callNo, int bookID) {
		this.Author = author;
		this.Title = title;
		this.CallNo = callNo;
		this.ID = bookID;
		this.state = State.AVAILABLE;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Book: ").append(ID).append("\n")
		  .append("  Title:  ").append(Title).append("\n")
		  .append("  Author: ").append(Author).append("\n")
		  .append("  CallNo: ").append(CallNo).append("\n")
		  .append("  State:  ").append(state);
		
		return sb.toString();
	}

	public Integer bookID() {
		return ID;
	}

	public String Title() {
		return Title;
	}


	
	public boolean isAvailable() {
		return state == State.AVAILABLE;
	}

	
	public boolean ON_LOAN() {
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
