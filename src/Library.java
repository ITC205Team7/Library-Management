import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Library implements Serializable {
	
	private static final String LIBRARY_FILE = "library.obj";
	private static final int LOAN_LIMIT = 2;
	private static final int LOAN_PERIOD = 2;
	private static final double FINE_PER_DAY = 1.0;
	private static final double MAX_FINES_OWED = 5.0;
	private static final double DAMAGE_FEE = 2.0;
	
	private static Library self;
	private int bookId;
	private int memberId;
	private int loanId;
	private Date loadDate;
	
	private Map<Integer, Book> catalog;
	private Map<Integer, Member> members;
	private Map<Integer, Loan> loans;
	private Map<Integer, Loan> currentLoans;
	private Map<Integer, Book> damagedBooks;
	

	private Library() {
		catalog = new HashMap<>();
		members = new HashMap<>();
		loans = new HashMap<>();
		currentLoans = new HashMap<>();
		damagedBooks = new HashMap<>();
		bookId = 1;
		memberId = 1;
		loanId = 1;
	}

	
	public static synchronized Library INSTANCE() {
		if (self == null) {
			Path path = Paths.get(LIBRARY_FILE);			
			if (Files.exists(path)) {	
				try (ObjectInputStream lof = new ObjectInputStream(new FileInputStream(LIBRARY_FILE));) {
			    
					self = (Library) lof.readObject();
					Calendar.getInstance().setDate(self.loadDate);
					lof.close();
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			else self = new Library();
		}
		return self;
	}

	
	public static synchronized void SAVE() {
		if (self != null) {
			self.loadDate = Calendar.getInstance().getDate();
			try (ObjectOutputStream lof = new ObjectOutputStream(new FileOutputStream(LIBRARY_FILE));) {
				lof.writeObject(self);
				lof.flush();
				lof.close();	
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	
	public int BookID() {
		return bookId;
	}
	
	
	public int MemberID() {
		return memberId;
	}
	
	
	private int nextBookID() {
		return bookId++;
	}

	
	private int nextMemberID() {
		return memberId++;
	}

	
	private int nextLoanID() {
		return loanId++;
	}

	
	public List<Member> Members() {
		return new ArrayList<Member>(members.values());
	}


	public List<Book> Books() {
		return new ArrayList<Book>(catalog.values());
	}


	public List<Loan> CurrentLoans() {
		return new ArrayList<Loan>(currentLoans.values());
	}


	public Member NewMember(String lastName, String firstName, String email, int phoneNo) {
		Member member = new Member(lastName, firstName, email, phoneNo, nextMemberID());
		members.put(member.getId(), member);		
		return member;
	}

	
	public Book Add_book(String author, String title, String callNo) {
		Book book = new Book(author, title, callNo, nextBookID());
		catalog.put(book.bookID(), book);
		return book;
	}

	
	public Member getMember(int memberId) {
		if (members.containsKey(memberId)) 
			return members.get(memberId);
		return null;
	}

	
	public Book Book(int bookId) {
		if (catalog.containsKey(bookId)) 
			return catalog.get(bookId);		
		return null;
	}

	
	public int loanLimit() {
		return LOAN_LIMIT;
	}

	
	public boolean memberCanBorrow(Member member) {
		if (member.getNumberOfCurrentLoans() == LOAN_LIMIT ) 
			return false;
				
		if (member.getFinesOwed() >= MAX_FINES_OWED) 
			return false;
				
		for (Loan loan : member.getLoans())
			if (loan.isOverDue())
				return false;
			
		return true;
	}

	
	public int loansRemainingForMember(Member member) {
		return LOAN_LIMIT - member.getNumberOfCurrentLoans();
	}

	
	public Loan issueLoan(Book book, Member member) {
		Date dueDate = Calendar.getInstance().getDueDate(LOAN_PERIOD);
		Loan loan = new Loan(nextLoanID(), book, member, dueDate);
		member.takeOutLoan(loan);
		book.isBorrowed();
		loans.put(loan.getId(), loan);
		currentLoans.put(book.bookID(), loan);
		return loan;
	}
	
	
	public Loan getLoanByBookId(int bookId) {
		if (currentLoans.containsKey(bookId)) {
			return currentLoans.get(bookId);
		}
		return null;
	}

	
	public double calculateOverDueFine(Loan loan) {
		if (loan.isOverDue()) {
			long daysOverDue = Calendar.getInstance().getDaysDifference(loan.getDueDate());
			double fine = daysOverDue * FINE_PER_DAY;
			return fine;
		}
		return 0.0;		
	}


	public void dischargeLoan(Loan currentLoan, boolean isDamaged) {
		Member member = currentLoan.getLoanedMember();
		Book book  = currentLoan.getLoanedBook();

		double overDueFine = calculateOverDueFine(currentLoan);
		member.addFine(overDueFine);

		member.dischargeLoan(currentLoan);
		book.isReturned(isDamaged);
		if (isDamaged) {
			member.addFine(DAMAGE_FEE);
			damagedBooks.put(book.bookID(), book);
		}
		currentLoan.Loan();
		currentLoans.remove(book.bookID());
	}


	public void checkCurrentLoans() {
		for (Loan loan : currentLoans.values()) {
			loan.checkOverDue();
		}		
	}


	public void repairBook(Book currentBook) {
		if (damagedBooks.containsKey(currentBook.bookID())) {
			currentBook.isRepaired();
			damagedBooks.remove(currentBook.bookID());
		}
		else {
			throw new RuntimeException("Library: repairBook: book is not damaged");
		}
		
	}
	
	
}
