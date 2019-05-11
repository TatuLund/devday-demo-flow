package com.vaadin.devday.demo.views;

import java.time.LocalDate;
import java.time.LocalTime;

import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
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
    	
    	private FormLayout createForm() {
    		FormLayout form = new FormLayout();
    		timePicker.setWidth("100%");
    		datePicker.setWidth("100%");
    		nameField.setWidth("100%");
    		form.setSizeFull();
    		form.addFormItem(nameField,"Name: ").getElement().setAttribute("colspan", "2");
    		form.addFormItem(datePicker,"Birth date: ");
    		form.addFormItem(timePicker,"Birth time: ");
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
    			close();
    			Notification.show("Saved!",
    	                2000, Position.MIDDLE);
    		});
    		cancelBtn.addClickListener(event -> {
    			close();
    		});    		
    	}
    	
    	@Override
    	public void open() {
    		super.open();
    		nameField.setValue("John Doe");
    		timePicker.setValue(LocalTime.now());
    		datePicker.setValue(LocalDate.now().minusYears(20));
    	}
    }
    
    public DialogView() {
    	setSizeFull(); 
		getStyle().set("background", "var(--lumo-tint-40pct)");
    	MyDialog dialog = new MyDialog();
    	Shortcuts.addShortcutListener(dialog, () -> dialog.close(), Key.BACKSPACE);
    	Button button = new Button("Open Dialog");
    	button.addClickListener(event -> {
    		if (!dialog.isOpened()) {
    			dialog.open();
    		}
    	});
    	add(button);
    }
}
