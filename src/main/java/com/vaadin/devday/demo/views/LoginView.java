package com.vaadin.devday.demo.views;

import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.InitialPageSettings.WrapMode;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

//@HtmlImport("styles.html")
@Theme(value = Lumo.class, variant = Lumo.DARK)
@Route(value = LoginView.ROUTE)
@PageTitle(LoginView.TITLE)
public class LoginView extends VerticalLayout implements PageConfigurator {
	public static final String ROUTE = "login";
	public static final String TITLE = "Login";
	
	public LoginView() {
		setSizeFull();
		LoginForm form = new LoginForm();
		form.addLoginListener(event -> {
			if (event.getUsername().equals("user") && event.getPassword().equals("user")) {
				String route = (String) VaadinSession.getCurrent().getAttribute("intendedRoute");
				VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
				VaadinSession.getCurrent().setAttribute("loggedIn", true);
				if (route != null && !route.equals(LoginView.ROUTE)) {
					System.out.println("Navigating back to: "+route);
					UI.getCurrent().navigate(route);
				} else {
					UI.getCurrent().navigate(MainView.ROUTE);
				}
			}
		});
		add(form);
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		form.addForgotPasswordListener(event -> {
			Notification.show("Username: user, Passoword: user", 2000, Position.MIDDLE);
		});
	}


	@Override
	public void configurePage(InitialPageSettings settings) {
		settings.addInlineFromFile("./test.css", WrapMode.STYLESHEET);		
	}
}
