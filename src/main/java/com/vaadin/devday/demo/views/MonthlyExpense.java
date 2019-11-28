package com.vaadin.devday.demo.views;

public class MonthlyExpense {
	private String month;
	private Double expenses;
	private int year;
	private boolean checked;
	
	public MonthlyExpense(String month, int year, Double expenses) {
		setMonth(month);
		setExpenses(expenses);
		setYear(year);
		checked = false;
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

	public String getStyles() {
		int color = (year - 2000) % 10;
		String styles = "color:#"+color+"f"+color;
		return styles;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public boolean isChecked() {
		return checked;
	}
}
