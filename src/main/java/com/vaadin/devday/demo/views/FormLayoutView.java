package com.vaadin.devday.demo.views;

import java.util.Optional;

import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.Binding;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = FormLayoutView.ROUTE, layout = MainLayout.class)
@PageTitle(FormLayoutView.TITLE)
public class FormLayoutView extends VerticalLayout {
    public static final String ROUTE = "form";
    public static final String TITLE = "Form Layout";

    public class Person {
    	private String title;
    	private String firstName = "ewpotpoqwuteoiuwotiuowituoiwutoiuwtoiuwotu";
    	private String lastName;
    	private String email;
    	private String password;
    	private String phone;
    	private Boolean doNotCall;
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
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public Boolean isDoNotCall() {
			return doNotCall;
		}
		public void setDoNotCall(Boolean doNotCall) {
			this.doNotCall = doNotCall;
		} 
    }

    // If you want to use bindInstanceFields(this);
    ComboBox<String> title = new ComboBox<>();
    TextField firstName = new TextField();
    TextField lastName = new TextField();
    TextField phone = new TextField();
    TextField email = new TextField();
    PasswordField password = new PasswordField();
    PasswordField repeatPassword = new PasswordField();

    Button saveButton = new Button("Save");
    
    public FormLayoutView(){
        setSizeFull();
        
        FormLayout formLayout = new FormLayout();
        title.setItems("Mr","Mrs","Miss");
        title.setWidth("100%");
        formLayout.getElement().appendChild(ElementFactory.createBr());
                
        firstName.setWidth("100%");
        formLayout.addFormItem(firstName, "First Name");

        lastName.setWidth("100%");
        formLayout.addFormItem(lastName, "Last Name");

        email.setWidth("100%");
        formLayout.addFormItem(email, "Email").getElement().setAttribute("colspan", "2");
        
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
        Binder<Person> binder = new Binder<>(Person.class);
        // https://github.com/vaadin/vaadin-form-layout-flow/issues/59
        binder.forField(firstName)
    	.asRequired()
        	.withValidator(pw -> pw.length() < 21, "Max 20 chars")
        	.bind("firstName");
        binder.forField(lastName).asRequired().bind("lastName");
        binder.forField(email).asRequired().bind("email");
        binder.forField(password)
        	.asRequired()
        	.withValidator(pw -> pw.length() > 7, "Use at least 8 characters for password")
        	.withValidator(pw -> pw.equals(repeatPassword.getValue()) , "Passwords do not match")
        	.bind("password");
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

        add(formLayout,createTools());
    }
    
    HorizontalLayout createTools() {
		HorizontalLayout tools = new HorizontalLayout();
        saveButton.setEnabled(false);
		Button cancelButton = new Button("cancel");
		tools.add(cancelButton,saveButton);
		saveButton.getStyle().set("margin-left", "auto");
		tools.setWidth("100%");
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		saveButton.addClickListener(event -> {
			Notification.show("Saved!",
	                2000, Position.MIDDLE);
		});
		cancelButton.addClickListener(event -> {
		});
		return tools;
    }
}
