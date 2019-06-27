package com.vaadin.devday.demo.views;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;

@Route(value = GridView.ROUTE, layout = MainLayout.class)
@PageTitle(GridView.TITLE)
public class GridView extends SplitLayout {
	public static final String ROUTE = "grid";
	public static final String TITLE = "Grid";

    private Grid<MonthlyExpense> expensesGrid;
    private TextField limit;
    private int index = 0;
    private FormLayout form = null;
	NumberField expenseField = new NumberField();
    MonthlyExpense currentExpense;
    
    public GridView() {
    	VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        Label label = new Label(TITLE);
        label.addClassName("title-label");
        content.add(label);
        
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
        content.add(tools);
        
        content.add(expensesGrid);
        content.expand(expensesGrid);

        this.setSizeFull();
        this.setOrientation(Orientation.VERTICAL);
        this.addToPrimary(content);
        form = createForm();
        this.addToSecondary(form);
        this.setSplitterPosition(80);
        initalizeAndPopulateGrid(expensesGrid);
    }

	private FormLayout createForm() {
		FormLayout form = new FormLayout();
    	TimePicker timePicker = new TimePicker();
    	TextField nameField = new TextField();
    	DatePicker datePicker = new DatePicker();
		timePicker.setWidth("100%");
		datePicker.setWidth("100%");
		nameField.setWidth("100%");
		form.setSizeFull();
		form.addFormItem(nameField,"Name: ").getElement().setAttribute("colspan", "2");
		form.addFormItem(datePicker,"Birth date: ");
		form.addFormItem(timePicker,"Birth time: ");
		form.addFormItem(expenseField, "Expenses");
		expenseField.setStep(1d);
		expenseField.setHasControls(true);
		expenseField.setSuffixComponent(new Span("EUR"));
		return form;
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
		grid.addColumn(new ComponentRenderer<Checkbox,MonthlyExpense>(expense ->  {
			Checkbox check = new Checkbox();
			check.setEnabled(false);
			grid.addSelectionListener(event -> {
				if (event.getAllSelectedItems().contains(expense)) {
					System.out.println("Selected "+expense.toString());
					check.setValue(true);
				} else {
					check.setValue(false);
				}
			});
			check.addValueChangeListener(event -> {
				if (event.isFromClient()) {
					System.out.println("Check box clicked");
					grid.select(expense);
				}
			});
			return check;		
		})).setWidth("50px").setHeader("Select");
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        List<MonthlyExpense> data = getData();
        grid.setItems(data);
        grid.addSelectionListener(event -> {
        	event.getFirstSelectedItem().ifPresent(expense -> {
        		expenseField.setValue(expense.getExpenses());
        		currentExpense = expense;
        	});
        });
        expenseField.addValueChangeListener(event -> {
        	if (event.isFromClient() && currentExpense != null) {
        		currentExpense.setExpenses(event.getValue());
        		grid.getDataProvider().refreshItem(currentExpense);
        	}
        });
        //        grid.addItemClickListener(event -> {
//        	getUI().ifPresent(ui -> ui.navigate(MainView.ROUTE+"/scroll"));
//        });        
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
    private Double getExpenses() {
        return Math.floor((Math.random() * 1000) % 500 + 300);
    }

    private int getMonthlyExpenseLimit() {
        if (limit.getValue() == null || limit.getValue().isEmpty()) {
            return 100000;
        }
        return Integer.parseInt(limit.getValue());
    }
    
}
