package com.vaadin.devday.demo.views;

import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
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
public class LoginView extends VerticalLayout implements PageConfigurator, AfterNavigationObserver {
    public static final String ROUTE = "login";
    public static final String TITLE = "Login";
    LoginOverlay form = new LoginOverlay();

    public LoginView() {
        setSizeFull();
        form.addLoginListener(event -> {
            String user = event.getUsername();
            String pass = event.getPassword();
            validateAndNavigate(user, pass);
        });
        form.setOpened(true);
//        form.addAttachListener(event -> {
            form.getElement().executeJs("$0.$.vaadinLoginForm.$.vaadinLoginPassword.revealButtonHidden=true;", form.getElement());            
//        });
        add(form);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        form.addForgotPasswordListener(event -> {
            Notification.show("Username: user, Passoword: user", 2000,
                    Position.MIDDLE);
        });
    }

    private void validateAndNavigate(String user, String pass) {
        if (user.equals("user")
                && pass.equals("user")) {
            String route = (String) VaadinSession.getCurrent()
                    .getAttribute("intendedRoute");
            VaadinService
                    .reinitializeSession(VaadinService.getCurrentRequest());
            VaadinSession.getCurrent().setAttribute("loggedIn", true);
            form.setOpened(false);
            if (route != null && !route.equals(LoginView.ROUTE)) {
                System.out.println("Navigating back to: " + route);
                UI.getCurrent().navigate(route);
            } else {
                UI.getCurrent().navigate(MainView.ROUTE);
            }
        }
    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        settings.addInlineFromFile("./test.css", WrapMode.STYLESHEET);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        QueryParameters params = event.getLocation().getQueryParameters();
        if (params != null && params.getParameters() != null && !params.getParameters().isEmpty()) {
            String user = params.getParameters().get("user") != null ? params.getParameters().get("user").get(0) : "";
            String pass = params.getParameters().get("pass") != null ? params.getParameters().get("pass").get(0) : "";
            validateAndNavigate(user, pass);
        }
        
    }
}
