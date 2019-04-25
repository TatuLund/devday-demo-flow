package com.vaadin.devday.demo.views;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.Renderer;

@Route(value = GridView.ROUTE, layout = MainLayout.class)
public class GridView extends VerticalLayout {
	public static final String ROUTE = "grid";
	public static final String TITLE = "Grid";

    private Grid<MonthlyExpense> expensesGrid;
    private TextField limit;
    private int index = 0;
    
    public GridView() {
        setSizeFull();
        Label label = new Label(TITLE);
        label.addClassName("title-label");
        add(label);
        
        expensesGrid = new Grid<>();
        limit = createLimitTextField();
		HorizontalLayout tools = new HorizontalLayout();
        tools.add(limit);

        Span indexLabel = new Span("2000");        
        Button upBtn = new Button();
        upBtn.setIcon(new Icon(VaadinIcon.ARROW_UP));
        Button downBtn = new Button();
        downBtn.setIcon(new Icon(VaadinIcon.ARROW_DOWN));
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.add(indexLabel,upBtn,downBtn);
		buttons.getStyle().set("margin-left", "auto");
   		upBtn.addClickListener(event -> {
   			index = index - 12;
   			if (index < 0) index = 0;
        	scrollTo(expensesGrid,index);
        	indexLabel.setText(Integer.toString(2000+(index/12)));
        });
   		downBtn.addClickListener(event -> {
   			index = index + 12;
   			if (index > 12*19) index = 0;
        	scrollTo(expensesGrid,index);
        	indexLabel.setText(Integer.toString(2000+(index/12)));
        });
   		indexLabel.addClickListener(event ->{
        	scrollTo(expensesGrid,index);
   		});
   		tools.setWidth("100%");
   		tools.add(buttons);
        add(tools);
        
        add(expensesGrid);
        expand(expensesGrid);

        initalizeAndPopulateGrid(expensesGrid);
    }

    private TextField createLimitTextField() {
        TextField limit = new TextField("Limit for monthly expenses");
        limit.addClassName("limit-field");
        limit.addValueChangeListener(event -> expensesGrid.getDataProvider().refreshAll());
        limit.getElement().getStyle().set("--limit-field-color", "#2dd7a4");
        return limit;
    }

    public static void scrollTo(Grid<?> grid, int index) {
        UI.getCurrent().getPage().executeJavaScript("$0._scrollToIndex(" + index + ")", grid.getElement());
    }
    
    private void initalizeAndPopulateGrid(Grid<MonthlyExpense> grid) {
    	grid.addClassName("my-grid");
        grid.addColumn(MonthlyExpense::getYear).setHeader("Year").setKey("year").setId("year-column");
        grid.addColumn(MonthlyExpense::getMonth).setHeader("Month").setKey("month").setId("month-column");

        grid.addColumn(MonthlyExpense::getExpenses).setHeader("Expenses").setClassNameGenerator(monthlyExpense -> monthlyExpense.getExpenses() >= getMonthlyExpenseLimit() ? "warning-grid-cell" : "green-grid-cell");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        List<MonthlyExpense> data = getData();
        grid.setItems(data);
        grid.addItemClickListener(event -> {
        	getUI().ifPresent(ui -> ui.navigate(MainView.ROUTE+"/scroll"));
        });
    }

	private List<MonthlyExpense> getData() {
		String[] monthNames = new java.text.DateFormatSymbols().getMonths();
        List<MonthlyExpense> data = new ArrayList<>();
        for (int year = 2000; year < 2020; year++ ) {
        	for (int month = 0; month < 12; month++) {
            	data.add(new MonthlyExpense(monthNames[month], year, getExpenses()));
        	}
    	}
		return data;
	}

    // Randomize a value between 300 and 800
    private long getExpenses() {
        return Math.round((Math.random() * 1000) % 500 + 300);
    }

    private int getMonthlyExpenseLimit() {
        if (limit.getValue() == null || limit.getValue().isEmpty()) {
            return 100000;
        }
        return Integer.parseInt(limit.getValue());
    }

    
}
