import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Calendar {
	
	private static Calendar self;
	private static java.util.Calendar calendarObj;
	
	
	private Calendar() {
		calendarObj = java.util.Calendar.getInstance();
	}


	public static Calendar getInstance() {
		if (self == null) {
			self = new Calendar();
		}
		return self;
	}


    /**
     * Increase the date in calendar object by passing param days.
     * @param days
     * @return void
     */
	public void incrementDate(int days) {
		calendarObj.add(java.util.Calendar.DATE, days);
	}


    /**
     * Set date for calendar object.
     * @param date
     * @return void
     */
	public synchronized void setDate(Date date) {
		try {
			calendarObj.setTime(date);
	        calendarObj.set(java.util.Calendar.HOUR_OF_DAY, 0);
	        calendarObj.set(java.util.Calendar.MINUTE, 0);
	        calendarObj.set(java.util.Calendar.SECOND, 0);
	        calendarObj.set(java.util.Calendar.MILLISECOND, 0);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}


    /**
     * get date
     *
     * @return date
     */
	public synchronized Date Date() {
		try {
	        calendarObj.set(java.util.Calendar.HOUR_OF_DAY, 0);
	        calendarObj.set(java.util.Calendar.MINUTE, 0);
	        calendarObj.set(java.util.Calendar.SECOND, 0);
	        calendarObj.set(java.util.Calendar.MILLISECOND, 0);
			return calendarObj.getTime();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}


    /**
     * Get due date by passing loan period.
     * @param loanPeriod
     * @return due date
     */
	public synchronized Date getDueDate(int loanPeriod) {
		Date now = Date();
		calendarObj.add(java.util.Calendar.DATE, loanPeriod);
		Date dueDate = calendarObj.getTime();
		calendarObj.setTime(now);
		return dueDate;
	}


    /**
     * get the day difference of current date and passing date.
     * @param targetDate
     * @return void
     */
	public synchronized long getDaysDifference(Date targetDate) {
		long diffMillis = Date().getTime() - targetDate.getTime();
	    long diffDays = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);
	    return diffDays;
	}

}
