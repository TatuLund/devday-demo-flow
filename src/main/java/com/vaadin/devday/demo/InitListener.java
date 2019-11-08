package com.vaadin.devday.demo;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;

import java.time.Instant;

public class InitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit (ServiceInitEvent serviceInitEvent) {
    	System.out.println("Service init");
        serviceInitEvent.getSource().addUIInitListener(event -> {
        	UI ui = event.getUI();
        	System.out.println("New UI instantiated. UI id # " + ui.getUIId() + " " + Instant.now () + " " + ui.toString());
        });
    }

}
