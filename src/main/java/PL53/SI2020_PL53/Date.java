package PL53.SI2020_PL53;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Random;

public class Date {
	private int day, month, year;

	public Date(int day, int month, int year) {
		this.day = day;
		this.month = month;
		this.year = year;
	}

	public Date(Date other) {
		this.day = other.getDay();
		this.month = other.getMonth();
		this.year = other.getYear();
	}

	public Date() {
		this.setRandom();
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setDate(Date d) {
		this.day = d.getDay();
		this.month = d.getMonth();
		this.year = d.getYear();
	}

	public java.sql.Date toSQL() throws ParseException {
		DateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
		java.util.Date date;

		date = formatter.parse(Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day));
		return new java.sql.Date(date.getTime());

	}

	public void setRandom() {
		RandomDate rd = new RandomDate();
		this.setDate(rd.nextDate());
	}

	public void setRandom(int min_year, int max_year) {
		RandomDate rd = new RandomDate(min_year, max_year);
		this.setDate(rd.nextDate());
	}

	public static Date random(int min_year, int max_year) {
		RandomDate rd = new RandomDate(min_year, max_year);
		return rd.nextDate();
	}

	public static Date random() {
		RandomDate rd = new RandomDate();
		return rd.nextDate();
	}
	
	public static Date now() {
		LocalDate d = LocalDate.now();
		
		return new Date(d.getDayOfMonth(), d.getMonthValue(), d.getYear());
	}

	public static Date parse(java.sql.Date date) {
		LocalDate d = date.toLocalDate();
		
		return new Date(d.getDayOfMonth(), d.getMonthValue(), d.getYear());
	}
	
	@Override
	public String toString() {
		return this.day + "/" + this.month + "/" + this.year;
	}

	public static class RandomDate {
		private final LocalDate minDate;
		private final LocalDate maxDate;
		private final Random random;

		public RandomDate(LocalDate minDate, LocalDate maxDate) {
			this.minDate = minDate;
			this.maxDate = maxDate;
			this.random = new Random();
		}

		public RandomDate() {
			this.minDate = LocalDate.of(2000, 1, 1);
			this.maxDate = LocalDate.of(2050, 1, 1);
			this.random = new Random();
		}

		public RandomDate(int min_year, int max_year) {
			this.minDate = LocalDate.of(min_year, 1, 1);
			this.maxDate = LocalDate.of(max_year, 1, 1);
			this.random = new Random();
		}

		public Date nextDate() {
			int minDay = (int) minDate.toEpochDay();
			int maxDay = (int) maxDate.toEpochDay();
			long randomDay = minDay + random.nextInt(maxDay - minDay);
			LocalDate ld = LocalDate.ofEpochDay(randomDay);

			return new Date(ld.getDayOfMonth(), ld.getMonth().getValue(), ld.getYear());
		}

		@Override
		public String toString() {
			return "RandomDate{" + "maxDate=" + maxDate + ", minDate=" + minDate + '}';
		}
	}

}