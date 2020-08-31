package com.vaadin.devday.demo.views;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.IntStream;

import com.vaadin.componentfactory.EnhancedRichTextEditor;
import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.DateRangeValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = DialogView.ROUTE, layout = MainLayout.class)
@PageTitle(DialogView.TITLE)
public class DialogView extends VerticalLayout {
    public static final String ROUTE = "dialog";
    public static final String TITLE = "Dialog";    

    public class MyDialog extends Dialog {
    	TimePicker timePicker = new TimePicker();
    	TextField nameField = new TextField();
    	DatePicker datePicker = new DatePicker();
    	Button saveBtn = new Button("save");
    	Button cancelBtn = new Button("cancel");
    	private Person person;
    	private Binder<Person> binder;
    	
    	public class Person {
    		private String name;
    		private LocalDate birthDate;
    		private LocalTime birthTime;
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public LocalDate getBirthDate() {
				return birthDate;
			}
			public void setBirthDate(LocalDate birthDate) {
				this.birthDate = birthDate;
			}
			public LocalTime getBirthTime() {
				return birthTime;
			}
			public void setBirthTime(LocalTime birthTime) {
				this.birthTime = birthTime;
			}
    		    	
    	}
    	
    	private FormLayout createForm() {
    		FormLayout form = new FormLayout();
    		timePicker.setWidth("100%");
    		timePicker.getElement().setAttribute("theme", "underline");
    		datePicker.setWidth("100%");
    		nameField.setWidth("100%");
    		form.setSizeFull();
    		form.addFormItem(nameField,"Name: ").getElement().setAttribute("colspan", "2");
    		form.addFormItem(datePicker,"Birth date: ");
    		form.addFormItem(timePicker,"Birth time: ");
    		binder = new Binder<Person>();
    		person = new Person();
    		binder.readBean(person);
    		binder.forField(nameField).bind(Person::getName, Person::setName);
    		datePicker.setErrorMessage("Please provide valid date");
    		datePicker.getElement().setAttribute("theme", "weekend-highlight underline");
    		binder.forField(datePicker)
    		.withValidator(new DateRangeValidator("Birth date can't be in future",LocalDate.MIN,LocalDate.now()))
    		.bind(Person::getBirthDate, Person::setBirthDate);
    		binder.forField(timePicker).bind(Person::getBirthTime, Person::setBirthTime);
    		return form;
    	}
    	
    	public MyDialog() {
    		super();
    		setCloseOnOutsideClick(false);
    		HorizontalLayout tools = new HorizontalLayout();
    		tools.add(cancelBtn,saveBtn);
    		saveBtn.getStyle().set("margin-left", "auto");
    		tools.setWidth("100%");
    		saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    		VerticalLayout layout = new VerticalLayout(new H2("Person info"),new Hr(),createForm(),tools);
    		layout.setSizeFull();
    		add(layout);
    		saveBtn.addClickListener(event -> {
    			try {
					binder.writeBean(person);
	    			close();
	    			Notification.show("Saved!",
	    	                2000, Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_SUCCESS); ;
				} catch (ValidationException e) {
	    			Notification.show("Form has validation errors!",
	    	                2000, Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR); ;
				}
    		});
    		cancelBtn.addClickListener(event -> {
    			close();
    		});
    	}
    	
    	@Override
    	public void open() {
    		super.open();
    		nameField.setValue("John Doe");
    		timePicker.setValue(null);
    		datePicker.setValue(LocalDate.now().minusYears(20));
    	}
    }
    
    public DialogView() {
    	setSizeFull(); 
		getStyle().set("background", "var(--lumo-tint-40pct)");
    	MyDialog dialog = new MyDialog();
    	dialog.setResizable(true);
    	dialog.setDraggable(true);
//    	Shortcuts.addShortcutListener(dialog, () -> dialog.close(), Key.BACKSPACE);
    	Button button = new Button("Open Dialog");
    	button.addClickListener(event -> {
    		if (!dialog.isOpened()) {
    			dialog.open();
    		}
    	});
		MyRTE edit = new MyRTE();
		edit.setWidth("500px");
		edit.setHeight("200px");
		Html html = new Html("<div></div>");
		Div div = new Div(html);
		edit.addValueChangeListener(event -> {
			div.removeAll();
			String htmlValue = edit.getHtmlValue();
//			String value = edit.getValue();			
//			String newValue = htmlValue.replace("\t","&nbsp;&nbsp;&nbsp;&nbsp;");
			Html h = new Html("<div>"+htmlValue+"</div>");
			Pre pre = new Pre(htmlValue);
			div.add(h,pre);
		});
    	add(button,edit,div);
        Button gridTest = new Button("Grid");
        gridTest.addClickListener(buttonClickEvent -> {
        	Dialog gridDialog = new Dialog();
			gridDialog.setWidth("1500px");
			gridDialog.setHeight("750px");
            gridDialog.open();
            gridDialog.add("Opening ...");
            setupGrid(gridDialog);
        });
        add(gridTest);
        Html list = new Html("<ul>\r\n" + 
        		"  <li>Coffee</li>\r\n" + 
        		"  <li>Tea</li>\r\n" + 
        		"  <li>Milk</li>\r\n" + 
        		"</ul>");
        add(list);
    }

	private void setupGrid(Dialog gridDialog) {
		GridPro<Object> grid = new GridPro<>();
		grid.appendHeaderRow();
		grid.addThemeNames("my-workaround-theme");
		final HeaderRow row = grid.prependHeaderRow();
		final Grid.Column<Object> col1 = grid.addColumn(x -> "aaaaaaaaaaaaa").setFrozen(true).setHeader("AAA");
		final Grid.Column<Object> col2 = grid.addColumn(x -> "bbbbbbbbbbbbb").setFrozen(true).setHeader("BBB");
		final Grid.Column<Object> col3 = grid.addColumn(x -> "ccccccccccccc").setFrozen(true).setHeader("CCC");
		row.join(col1, col2, col3).setText("Joined");
		for (int i = 0; i < 30; i++) {
		    final int ii = i;
		    grid.addColumn(x -> "aaaaaaaaaaaaa" + ii).setHeader("AAA" + i);
		}
		grid.setItems(IntStream.range(0, 100).mapToObj(i -> i));
		grid.setSelectionMode(Grid.SelectionMode.MULTI);
		grid.setSizeFull();
		gridDialog.removeAll();
		gridDialog.add(grid);
		
	}
    
    public class MyRTE extends EnhancedRichTextEditor {
    	@Override
    	public String getHtmlValue() {
    		return sanitize(this.getHtmlValueString().replace("\t","&nbsp;&nbsp;&nbsp;&nbsp;"));
    	}
        String sanitize(String html) {
            return org.jsoup.Jsoup.clean(html,
                    org.jsoup.safety.Whitelist.basic()
                            .addTags("img", "h1", "h2", "h3", "s")
                            .addAttributes("img", "align", "alt", "height", "src", "title", "width")
                            .addAttributes(":all", "style")
                            .addProtocols("img", "src", "data"));
        }
    }
}
