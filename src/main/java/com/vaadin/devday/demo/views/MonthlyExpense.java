package com.vaadin.devday.demo.views;

public class MonthlyExpense {
	private String month;
	private Double expenses;
	private int year;
	
	public MonthlyExpense(String month, int year, Double expenses) {
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

	public Double getExpenses() {
		return expenses;
	}

	public void setExpenses(Double expenses) {
		this.expenses = expenses;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
}
