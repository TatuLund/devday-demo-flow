package com.vaadin.devday.demo;

import com.vaadin.devday.demo.views.LoginView;
import com.vaadin.devday.demo.views.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.server.CustomizedSystemMessages;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;

import java.time.Instant;

import javax.servlet.http.Cookie;

public class InitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        System.out.println("Service init");
        serviceInitEvent.getSource().getDeploymentConfiguration().isXsrfProtectionEnabled();        
        serviceInitEvent.getSource().addUIInitListener(event -> {
            UI ui = event.getUI();
            if (ui.getSession().getUIs().size() > 1) {
                Notification.show(
                        "You have this application running on antoher tab",
                        2000, Position.MIDDLE);
            }
            System.out.println("New UI instantiated. UI id # " + ui.getUIId()
                    + " " + Instant.now() + " " + ui.toString());
            ui.addBeforeEnterListener(beforeEvent -> {
                Boolean loggedIn = (Boolean) VaadinSession.getCurrent()
                        .getAttribute("loggedIn");
                String route = beforeEvent.getLocation().getPath();
                if (!route.equals(LoginView.ROUTE)) {
                    if (loggedIn == null || !loggedIn) {
                        System.out.println("Not logged in: " + route);
                        VaadinSession.getCurrent().setAttribute("intendedRoute",
                                route);
                        beforeEvent.rerouteTo(LoginView.ROUTE);
                    }
                } else if (loggedIn != null && loggedIn) {
                    beforeEvent.forwardTo(MainView.ROUTE);
                }
            });
            VaadinSession session = ui.getSession();
            setErrorHandler(session, ui);

        });
        serviceInitEvent.getSource().addSessionInitListener(event -> {
            event.getSession().getSession().setMaxInactiveInterval(600);
            System.out.println("Session init");
        });
    }

    private void setErrorHandler(VaadinSession session, UI ui) {
        session.setErrorHandler(error -> {
            ui.access(() -> {
                ConfirmDialog confirmDialog = new ConfirmDialog("Error",
                        "Internal Error: " + error.getThrowable().getMessage(),
                        "Do Something", confirmFire -> {
                        });
                String trace = "";
                for (StackTraceElement s : error.getThrowable()
                        .getStackTrace()) {
                    trace = trace + "  at " + s.getClassName() + ".java line "
                            + s.getLineNumber() + " " + s.getMethodName()
                            + "\n";
                }
                confirmDialog.add(new Pre(trace));
                confirmDialog.setWidth("500px");
                confirmDialog.setHeight("500px");
                confirmDialog.open();
                error.getThrowable().printStackTrace();
            });
        });
    }

}
