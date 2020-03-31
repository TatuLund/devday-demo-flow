package com.vaadin.devday.demo.views;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BindingValidationStatus.Status;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = FormLayoutView.ROUTE, layout = MainLayout.class)
@PageTitle(FormLayoutView.TITLE)
public class FormLayoutView extends VerticalLayout implements BeforeLeaveObserver {
    public static final String ROUTE = "form";
    public static final String TITLE = "Form Layout";

    private Binder<Person> binder;
    private ConfirmDialog confirmDialog;
    
    public class MyException extends RuntimeException {
    	public MyException() {
    		super("Test exception");    		
    	}
    }
    
    public class Person {
    	private String title;
    	private String firstName = "ewpotpoqwuteoiuwotiuowituoiwutoiuwtoiuwotu";
    	private String lastName;
    	private String email;
    	private String password;
    	private BigDecimal phone;
    	private Boolean doNotCall;
		private List<String> options;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public BigDecimal getPhone() {
			return phone;
		}
		public void setPhone(BigDecimal phone) {
			this.phone = phone;
		}
		public Boolean isDoNotCall() {
			return doNotCall;
		}
		public void setDoNotCall(Boolean doNotCall) {
			this.doNotCall = doNotCall;
		}
		public List<String> getOptions() {
			return options;
		}
		public void setOptions(List<String> options) {
			this.options = options;
		}
    }

    // If you want to use bindInstanceFields(this);
    ComboBox<String> title = new ComboBox<>();
    TextField firstName = new TextField();
    TextField lastName = new TextField();
    TextField phone = new TextField();
    TextArea email = new TextArea();
    PasswordField password = new PasswordField();
    PasswordField repeatPassword = new PasswordField();

    Button saveButton = new Button("Save");
  
    public FormLayoutView(){
        setSizeFull();
        
        FormLayout formLayout = new FormLayout();
        title.setItems("Mr","Mrs","Miss");
        title.setWidth("100px");
		title.getElement().setAttribute("theme", "underline titlefont widepopup");
		title.addValueChangeListener(event -> {
			if (event.getValue().equals("Operations")) {
				
			}
		});
		title.getElement().getStyle().set("--lumo-icons-checkmark", "T");
        formLayout.addFormItem(title, "Title");
       
        firstName.setWidth("100%");
        formLayout.addFormItem(firstName, "First Name");

        lastName.setWidth("100%");
        formLayout.addFormItem(lastName, "Last Name");
        lastName.setPlaceholder("Last name");
//        lastName.setEnabled(false);

        email.setWidth("100%");
        formLayout.addFormItem(email, "Email").getElement().setAttribute("colspan", "2");
        email.setValueChangeMode(ValueChangeMode.EAGER);
        phone.setEnabled(false);
        
        FlexLayout phoneLayout = new FlexLayout();
        phoneLayout.setAlignItems(Alignment.END);
        phoneLayout.setWidth("100%");

        Checkbox doNotCall = new Checkbox("Do not call");
        phoneLayout.add(phone, doNotCall);
        phoneLayout.expand(phone);
        FormLayout.FormItem item = formLayout.addFormItem(phoneLayout, "Phone");
        item.getElement().setAttribute("colspan", "2");

        password.setWidth("100%");
        formLayout.addFormItem(password, "Password");

        formLayout.getElement().appendChild(ElementFactory.createBr());

        repeatPassword.setWidth("100%");
        formLayout.addFormItem(repeatPassword, "Repeat Password");
        
        Person person = new Person();
        binder = new Binder<>(Person.class);
        // https://github.com/vaadin/vaadin-form-layout-flow/issues/59
        binder.forField(firstName)
    	.asRequired()
        	.withValidator(new StringLengthValidator("Max 20 chars",5,20))
        	.bind("firstName");
        binder.forField(lastName).asRequired().bind("lastName");
        binder.forField(email).withValidator(new EmailValidator("Not valid")).asRequired().bind("email");
        binder.forField(password)
        	.asRequired()
        	.withValidator(pw -> pw.length() > 7, "Use at least 8 characters for password")
        	.withValidator(pw -> pw.equals(repeatPassword.getValue()) && !pw.isEmpty(), "Passwords do not match")
        	.withValidationStatusHandler(handler -> {
        		if (handler.getStatus().equals(Status.ERROR)) {
        			Notification.show(handler.getMessage().get()+" "+handler.getField().getValue().toString());
        		}
        	})
        	.bind("password");
        binder.forField(phone)
        	.withNullRepresentation("null")
        	.withConverter(new StringToBigDecimalConverter("Must input number")).bind(Person::getPhone,Person::setPhone);
        binder.bindInstanceFields(this);
        binder.setBean(person);
        // setting bean does not trigger validators, calling validate explicitly will bring
        // invalid fields visible
        binder.validate();
        
        // repeatPassword is not bound and does not trigger validations
        repeatPassword.addValueChangeListener(event -> {
        	binder.validate();
        });
        
        // https://github.com/vaadin/flow/issues/4988
        // Because of the above issue we need to use isValid()
        binder.addStatusChangeListener(event -> saveButton.setEnabled(binder.isValid()));

        RadioButtonGroup<String> group = new RadioButtonGroup<>();
        group.setItems("foo", "bar", "baz");
		group.getElement().setAttribute("theme", "button-spread");
		group.getStyle().set("width", "100%");
		group.addValueChangeListener(event -> {
			if (event.isFromClient()) 
				throw new MyException();
		});		
		group.setValue("bar");
        add(formLayout,createTools(),group);
        
    }

    public Tabs getData() {
		Optional<Component> parent = getParent();
		Tabs menu = null;
		while (parent.isPresent()) { 
			Component p = parent.get();
			if (p instanceof MainLayout) {
				MainLayout main = (MainLayout) p;
				menu = main.hello();
			}			
			parent = p.getParent();
		}
		return menu;
    }
    
    @Override
    public void onAttach(AttachEvent event) {
		Optional<Component> parent = getParent();
		while (parent.isPresent()) { 
			Component p = parent.get();
			if (p instanceof MainLayout) {
				MainLayout main = (MainLayout) p;
				main.hello();
			}			
			parent = p.getParent();
		}
    }
    
    HorizontalLayout createTools() {
		HorizontalLayout tools = new HorizontalLayout();
        saveButton.setEnabled(false);
		Button cancelButton = new Button("cancel");
		tools.add(cancelButton,saveButton);
		saveButton.getStyle().set("margin-left", "auto");
		tools.setWidth("100%");
		saveButton.focus();
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		saveButton.addClickListener(event -> {
			Notification.show("Saved!",
	                2000, Position.MIDDLE);
		});
		cancelButton.addClickListener(event -> {
		});
		return tools;
    }

	@Override
	public void beforeLeave(BeforeLeaveEvent event) {
		if (!binder.isValid()) {
			ContinueNavigationAction action = event.postpone(); // Save navigation action for later
			confirmDialog = new ConfirmDialog("Form is not validated",
		    	        "Do you want to save or discard your changes before navigating away?",
		    	        "Save", e -> {
		    	        	confirmDialog.close();
		    	        	Notification.show("Saved", 2000, Position.MIDDLE);
		    	        	action.proceed();}, 
		    	        "Discard", e -> {
		    	        	confirmDialog.close();
		    	        	action.proceed();},
		    	        "Cancel", e -> {
		    	        	confirmDialog.close();		    	        	
		    	        	});		      
		    	confirmDialog.open();
		    }		
	}

}
