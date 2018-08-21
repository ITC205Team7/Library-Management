import java.io.Serializable;


@SuppressWarnings("serial")
public class Book implements Serializable {
	
	private String T;
	private String A;
	private String C;
	private int ID;
	
	private enum State { AVAILABLE, Onloan, DAMAGED, RESERVED };
	private State state;
	
	
	public book(String author, String title, String callNo, int ID) {
		this.A = author;
		this.T = title;
		this.C = callNo;
		this.ID = ID;
		this.state = State.AVAILABLE;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Book: ").append(ID).append("\n")
		  .append("  Title:  ").append(T).append("\n")
		  .append("  Author: ").append(A).append("\n")
		  .append("  CallNo: ").append(C).append("\n")
		  .append("  State:  ").append(state);
		
		return sb.toString();
	}

	public Integer ID() {
		return ID;
	}

	public String Title() {
		return T;
	}


	
	public boolean isAvailable() {
		return state == State.AVAILABLE;
	}

	
	public boolean Onloan() {
		return state == State.Onloan;
	}

	
	public boolean isDamaged() {
		return state == State.DAMAGED;
	}

	
	public void isBorrowed() {
		if (state.equals(State.AVAILABLE)) {
			state = State.Onloan;
		}
		else {
			throw new RuntimeException(String.format("Book: cannot borrow while book is in state: %s", state));
		}
		
	}


	public void isReturned(boolean DAMAGED) {
		if (state.equals(State.Onloan)) {
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
