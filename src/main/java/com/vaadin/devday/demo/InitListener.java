package com.vaadin.devday.demo;

import com.vaadin.devday.demo.views.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;

import java.time.Instant;

public class InitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit (ServiceInitEvent serviceInitEvent) {
    	System.out.println("Service init");
        serviceInitEvent.getSource().addUIInitListener(event -> {
        	UI ui = event.getUI();
        	System.out.println("New UI instantiated. UI id # " + ui.getUIId() + " " + Instant.now () + " " + ui.toString());
        	ui.addBeforeEnterListener(beforeEvent -> {
        		Boolean loggedIn = (Boolean) VaadinSession.getCurrent().getAttribute("loggedIn");
    			String route = beforeEvent.getLocation().getPath();
        		if (!route.equals(LoginView.ROUTE)) {        			
        			if (loggedIn == null || !loggedIn) {
        				System.out.println("Not logged in: "+route);
        				VaadinSession.getCurrent().setAttribute("intendedRoute", route);
        				beforeEvent.rerouteTo(LoginView.ROUTE);
        				ui.navigate(LoginView.ROUTE);
        			}
        		}
        	});
        });
        serviceInitEvent.getSource().addSessionInitListener(event -> {
        	System.out.println("Session init");
        	VaadinSession session = event.getSession();
    		session.setErrorHandler(error -> {
            	ConfirmDialog confirmDialog = new ConfirmDialog("Error","Internal Error: "+error.getThrowable().getMessage(), "Do Something", confirmFire -> {});
        		confirmDialog.setWidth("500px");
        		confirmDialog.setHeight("500px");
        		confirmDialog.open();
        	});
        });
    }

}
