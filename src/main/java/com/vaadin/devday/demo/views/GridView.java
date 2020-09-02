package com.vaadin.devday.demo.views;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.GeneratedVaadinRadioButton;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.componentfactory.Popup;
import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.HasItemsAndComponents.ItemComponent;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;

@Route(value = GridView.ROUTE, layout = MainLayout.class)
@PageTitle(GridView.TITLE)
public class GridView extends SplitLayout  {
	public static final String ROUTE = "grid";
	public static final String TITLE = "Grid";

    private Grid<MonthlyExpense> expensesGrid;
    private TextField limit;
    private int index = 0;
    private FormLayout form = null;
	NumberField expenseField = new NumberField();
    MonthlyExpense currentExpense;
	private Popup popup;
	private boolean openMenu;
	private String firstName;
	private String lastName;
    
    public GridView() {
    	VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        Label label = new Label(TITLE);
        label.addClassName("title-label");
        content.add(label);
        content.getThemeList().add(Lumo.DARK);
        expensesGrid = new Grid<>();
        limit = createLimitTextField();
		HorizontalLayout tools = new HorizontalLayout();
		Button print = new Button("Print");
		print.addClickListener(event -> {
			this.getElement().executeJs("window.print();");
		});
        tools.add(print,limit);

        HorizontalLayout buttons = createToolButtons(content);

		tools.setWidth("100%");
   		tools.add(buttons);
        content.add(tools);
        
        Button columnButton = createColumnButton(content);
        
        Div div = new Div();
        div.setSizeFull();
        expensesGrid.setSizeFull();
        div.add(expensesGrid,columnButton);
        columnButton.getStyle().set("position", "absolute");
        columnButton.getStyle().set("top","96px");
        columnButton.getStyle().set("right","30px");
        content.add(div);        
        content.expand(div);
        tools.getElement().callJsFunction("scrollIntoView");
        
        this.setSizeFull();
        this.setOrientation(Orientation.VERTICAL);
        this.addToPrimary(content);
        form = createForm();
        this.addToSecondary(form);
        this.setSplitterPosition(80);
        initalizeAndPopulateGrid(expensesGrid);
    }

	private HorizontalLayout createToolButtons(VerticalLayout content) {
		Span indexLabel = new Span("2000");

//        createColumnButton(content);
        
        // Buttons to add/decrease the year
        Button upBtn = new Button();
        upBtn.setIcon(new Icon(VaadinIcon.ARROW_UP));
        Button downBtn = new Button();
        downBtn.setIcon(new Icon(VaadinIcon.ARROW_DOWN));
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.getStyle().set("margin-left", "auto");
   		upBtn.addClickListener(event -> {
   			index = index - 12;
   			if (index < 0) index = 0;
        	expensesGrid.scrollToIndex(index); // scrollTo(expensesGrid,index);
        	indexLabel.setText(Integer.toString(2000+(index/12)));
        });
   		downBtn.addClickListener(event -> {
   			index = index + 12;
   			if (index > 12*19) index = 0;
        	expensesGrid.scrollToIndex(index); // scrollTo(expensesGrid,index);
        	indexLabel.setText(Integer.toString(2000+(index/12)));
        });
   		indexLabel.addClickListener(event ->{
        	expensesGrid.scrollToIndex(index); // scrollTo(expensesGrid,index);
   		});
		buttons.add(indexLabel,upBtn,downBtn);
		return buttons;
	}

	private Button createColumnButton(VerticalLayout content) {
		// Create button and Popup (later populated by column selector)
        Button popButton = new Button();
        popButton.setIcon(VaadinIcon.MENU.create());
        popButton.setId("columnsbutton");
        popup = new Popup();
        popup.setFor("columnsbutton");
        content.add(popup);
        popButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        popButton.getElement().addEventListener("mouseover",  event -> popup.setOpened(true));
        return popButton;
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
        limit.setMaxLength(3);
//        limit.getElement().setAttribute("aria-required", "false");
        return limit;
    }

    // Not needed anymore as API is added in never Vaadin 14 versions
    @Deprecated
    public static void scrollTo(Grid<?> grid, int index) {
        ListDataProvider<MonthlyExpense> ldp =  (ListDataProvider) grid.getDataProvider();
        if (ldp.getFilter() == null) UI.getCurrent().getPage().executeJavaScript("$0._scrollToIndex(" + index + ")", grid.getElement());
    }
    
    private String getStyles() {
    	return "background:white";
    }
    
    private void initalizeAndPopulateGrid(Grid<MonthlyExpense> grid) {
    	grid.addClassName("my-grid");
		grid.addColumn(new ComponentRenderer<Radio,MonthlyExpense>(expense ->  {
			Radio check = new Radio();
			check.setValue(grid.asSingleSelect().getValue() == expense);
			check.addClickListener(event -> {
				if (event.isFromClient()) {
					System.out.println("Check box clicked");
					expense.setChecked(check.getValue());
				}
				if (grid.getSelectedItems().contains(expense)) {
					grid.deselect(expense);
				} else {
					grid.select(expense);
				}
			});
			return check;
			
		})).setWidth("50px").setFlexGrow(0).setKey("select").setHeader("Select");
    	grid.addColumn(TemplateRenderer.<MonthlyExpense>of("<div style$=\"[[item.styles]]\">[[item.expenses]]</div>")
    			.withProperty("styles", MonthlyExpense::getStyles)
    			.withProperty("expenses", MonthlyExpense::getYear)
    	).setSortable(true).setAutoWidth(true).setHeader("Year").setKey("year").setId("year-column");
    	addYearSelectorMenuToColumnHeader(grid);
    	GridContextMenu<MonthlyExpense> menu = new GridContextMenu<>(grid);
    	menu.setOpenOnClick(true);
    	populateGridContextMenu(grid,menu);
//    	menu.getElement().setProperty("selector", "[part~=\"body-cell\"]");
        grid.addColumn(MonthlyExpense::getMonth).setSortable(true).setHeader("Month").setKey("month").setId("month-column");
    	addMonthFilterMenuToColumnHeader(grid); 
        NumberField numberField = new NumberField();
        numberField.setWidth("100%");
        numberField.setMin(0);
        numberField.setMax(1000);
        numberField.setPreventInvalidInput(true);
        numberField.getElement().setAttribute("theme", "text-field-blue");
        grid.addColumn(MonthlyExpense::getExpenses).setResizable(true).setKey("expenses").setHeader("Expenses").setClassNameGenerator(monthlyExpense -> monthlyExpense.getExpenses() >= getMonthlyExpenseLimit() ? "warning-grid-cell" : "green-grid-cell").setEditorComponent(numberField);
        grid.addItemDoubleClickListener(event -> {
        	grid.getEditor().editItem(event.getItem());        	
        });
        grid.select(null);
        grid.addItemClickListener(event -> {
        	if (grid.getEditor().isOpen()) grid.getEditor().closeEditor();
        });
        Binder<MonthlyExpense> binder = new Binder<>();
        binder.forField(numberField).bind(MonthlyExpense::getExpenses,MonthlyExpense::setExpenses);
        grid.getEditor().setBinder(binder);
        grid.focus();
        
		grid.asSingleSelect().addValueChangeListener(event -> {
			if (!grid.getEditor().isOpen()) {
				System.out.println("Editor open");
				grid.getDataProvider().refreshAll();
			}
		});
        grid.getEditor().addCloseListener(event -> {
        	System.out.println(event.getItem().getExpenses());
        });
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
        grid.getElement().setAttribute("theme", "text-field-blue");
        grid.recalculateColumnWidths();
        grid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS);
        addColumnSelectorMenu(grid);

//        grid.setItemDetailsRenderer(
//                TemplateRenderer.<MonthlyExpense>of("<div style=\"border: 1px solid gray; padding: 10px; width: 100%; box-sizing: border-box;\" inner-h-t-m-l=\"[[item.html]]\"></div>")
//                        .withProperty("html", o ->
//                                Stream.of(2, 3, 4, 5, 7, 8, 9).map(i -> "Value " + i + ": " + o.getYear()).collect(Collectors.joining("<br>"))
//                        )
//        );
        grid.getElement().executeJs("return this.getBoundingClientRect().width;").then(String.class, value -> System.out.println(value));
        grid.getElement().executeJs("return this.getBoundingClientRect().height;").then(String.class, value -> System.out.println(value));
    }

	private void addColumnSelectorMenu(Grid<MonthlyExpense> grid) {
		// Add column selector to Popup        
        VerticalLayout popDiv = new VerticalLayout();
        for (Column<MonthlyExpense> column : grid.getColumns()) {
        	Checkbox check = new Checkbox(column.getKey());
			check.getElement().setAttribute("theme", "swapped");
        	check.getStyle().set("border", "white 1px solid");
        	check.getStyle().set("border-radius", "5px");
        	check.getStyle().set("width", "200px");
        	check.setValue(true);
        	check.addValueChangeListener(event -> {
        		column.setVisible(event.getValue());
        	});
        	popDiv.add(check);
        }
        popup.add(popDiv);
        popup.getElement().executeJs("this.$.popupOverlay.addEventListener('vaadin-overlay-close', () => $0.$server.popupClosed())",getElement());
	}

	@ClientCallable
	public void popupClosed() {
		System.out.println("Popup closed");
	}

	private void populateGridContextMenu(Grid<MonthlyExpense> grid, GridContextMenu<MonthlyExpense> menu) {
		GridMenuItem<MonthlyExpense> menuItem = menu.addItem("Item", event -> {
			event.getItem().ifPresent(item -> Notification.show("This is "+item.getYear()+"/"+item.getMonth(),3000,Position.TOP_START).addThemeVariants(NotificationVariant.LUMO_CONTRAST));   			
		});
		menu.setDynamicContentHandler(item -> {			
			Button button = new Button(VaadinIcon.DOWNLOAD.create());
			menu.addItem(button);
			menuItem.setText("Download");
			return true;
		});
   		menu.addGridContextMenuOpenedListener(event -> {
   			if (event.getColumnId() == null) return;
//   			if (!(event.getColumnId().get().equals("year-column"))) {
//   				menu.close();
//   			}
   			event.getColumnId().ifPresent(c -> { if (!c.equals("year-column")) menu.close(); }); 
   		});
   		Icon icon = VaadinIcon.CLOSE_BIG.create();
	}

	private void addYearSelectorMenuToColumnHeader(Grid<MonthlyExpense> grid) {
    	Div div = new Div();
    	div.setSizeFull();
    	div.add(new Text("Year"));
    	grid.getHeaderRows().get(0).getCell(grid.getColumnByKey("year")).setComponent(div);
    	ContextMenu menu = new ContextMenu(div);
    	populateContextMenu(grid, menu);
	}

	private void addMonthFilterMenuToColumnHeader(Grid<MonthlyExpense> grid) {
    	Div div = new Div();
    	div.setSizeFull();
    	div.add(new Text("Month"));
    	grid.getHeaderRows().get(0).getCell(grid.getColumnByKey("month")).setComponent(div);
    	ContextMenu menu = new ContextMenu(div);
    	populateFilterMenu(grid, menu);
	}

	private void populateFilterMenu(Grid<MonthlyExpense> grid, ContextMenu menu) {
		menu.addItem("All months", event -> {
	        ListDataProvider<MonthlyExpense> ldp =  (ListDataProvider) grid.getDataProvider();
	        ldp.clearFilters();
		});
		String[] months = new java.text.DateFormatSymbols().getMonths();
		for (String month : months) {
    		menu.addItem(month, event -> {
    	        ListDataProvider<MonthlyExpense> ldp =  (ListDataProvider) grid.getDataProvider();
    	        ldp.clearFilters();
    	        ldp.setFilter(item -> item.getMonth().equals(month));
    		});
    	}
	}


	private void populateContextMenu(Grid<MonthlyExpense> grid, ContextMenu menu) {
		for (int i=0;i<10;i++) {
    		final int index = i; 
    		menu.addItem("200"+i, event -> {
    			scrollTo(grid,index*12);
    		});
    	}
    	for (int i=10;i<20;i++) {
    		final int index = i; 
    		menu.addItem("20"+i, event -> {
    			scrollTo(grid,index*12);
    		});
    	}
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

    @NpmPackage(value = "@vaadin/vaadin-radio-button", version = "1.2.4")
    class Radio<T> extends GeneratedVaadinRadioButton<Radio<T>>
            implements ItemComponent<T>, HasComponents {
 	
        Radio() {
        }

        public void setValue(boolean value) {
        	getElement().setProperty("checked", value);
        }

        public boolean getValue() {
        	return getElement().getProperty("checked").equals("true");
        }
        
        
        @Override
        public T getItem() {
            return null;
        }

    }
    
}
