package com.vaadin.devday.demo.views;

public class MonthlyExpense {
	private String month;
	private long expenses;
	private int year;
	
	public MonthlyExpense(String month, int year, long expenses) {
		setMonth(month);
		setExpenses(expenses);
		setYear(year);
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public long getExpenses() {
		return expenses;
	}

	public void setExpenses(long expenses) {
		this.expenses = expenses;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
}
