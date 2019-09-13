package com.vaadin.devday.demo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.devday.demo.views.AbsoluteLayoutView;
import com.vaadin.devday.demo.views.AccordionView;
import com.vaadin.devday.demo.views.DialogView;
import com.vaadin.devday.demo.views.FormLayoutView;
import com.vaadin.devday.demo.views.GridView;
import com.vaadin.devday.demo.views.MainView;
import com.vaadin.devday.demo.views.SplitLayoutView;
import com.vaadin.devday.demo.views.ThemeVariantsView;
import com.vaadin.devday.demo.views.UploadView;
import com.vaadin.devday.demo.views.VaadinBoardView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletConfiguration;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Push
@PWA(name = "DevDay demo application", shortName = "DevDay")
@Theme(value = Lumo.class, variant = Lumo.DARK)
@HtmlImport("styles.html")
//@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
//@BodySize(height = "100vh", width = "100vw")
public class MainLayout extends AppLayout implements RouterLayout, AfterNavigationObserver {

	private FlexLayout childWrapper = new FlexLayout();
    private AppLayoutMenu menu = createMenu();
    private int count = 0;
    
	public MainLayout() {
        Image img = new Image("https://vaadin.com/images/vaadin-logo.svg", "Vaadin Logo");
        img.setHeight("35px");
        setBranding(img);
        getElement().getStyle().set("--vaadin-app-layout-navbar-background", "var(--lumo-tint-30pct)");

        menu.addMenuItems(new AppLayoutMenuItem(AccordionView.TITLE, AccordionView.ROUTE),
                new AppLayoutMenuItem(SplitLayoutView.TITLE, SplitLayoutView.ROUTE),
                new AppLayoutMenuItem(DialogView.TITLE, DialogView.ROUTE),
                new AppLayoutMenuItem(GridView.TITLE, GridView.ROUTE),
                new AppLayoutMenuItem(VaadinBoardView.TITLE, VaadinBoardView.ROUTE),
                new AppLayoutMenuItem(FormLayoutView.TITLE, FormLayoutView.ROUTE),
                new AppLayoutMenuItem(ThemeVariantsView.TITLE, ThemeVariantsView.ROUTE),
                new AppLayoutMenuItem(AbsoluteLayoutView.TITLE, AbsoluteLayoutView.ROUTE),
                new AppLayoutMenuItem(UploadView.TITLE, UploadView.ROUTE),
                new AppLayoutMenuItem(MainView.TITLE, "scroll"));

        childWrapper.setSizeFull();
        setContent(childWrapper);
    	Shortcuts.addShortcutListener(this, () -> getUI().ifPresent(ui -> ui.navigate(AccordionView.ROUTE)), Key.F1);
    	Shortcuts.addShortcutListener(this, () -> getUI().ifPresent(ui -> ui.navigate(SplitLayoutView.ROUTE)), Key.F2);
    	Shortcuts.addShortcutListener(this, () -> getUI().ifPresent(ui -> ui.navigate(DialogView.ROUTE)), Key.F3);
    	Shortcuts.addShortcutListener(this, () -> getUI().ifPresent(ui -> ui.navigate(GridView.ROUTE)), Key.F4);
    	Shortcuts.addShortcutListener(this, () -> getUI().ifPresent(ui -> ui.navigate(VaadinBoardView.ROUTE)), Key.F5);
    	Shortcuts.addShortcutListener(this, () -> getUI().ifPresent(ui -> ui.navigate(FormLayoutView.ROUTE)), Key.F6);
    	Shortcuts.addShortcutListener(this, () -> getUI().ifPresent(ui -> ui.navigate(ThemeVariantsView.ROUTE)), Key.F7);
    	Shortcuts.addShortcutListener(this, () -> getUI().ifPresent(ui -> ui.navigate(AbsoluteLayoutView.ROUTE)), Key.F8);
    	Shortcuts.addShortcutListener(this, () -> getUI().ifPresent(ui -> ui.navigate(UploadView.ROUTE)), Key.F9);
    	Shortcuts.addShortcutListener(this, () -> getUI().ifPresent(ui -> ui.navigate("")), Key.F12);
	}

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        try {
        	getUI().get().getSession().setAttribute("hostAddress",InetAddress.getLocalHost().getHostAddress().toString());
        	System.out.println(InetAddress.getByName(InetAddress.getLocalHost().getHostAddress()).getHostName());
        } catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
    }	
	
	@Override
	public void showRouterLayoutContent(HasElement content) {
		childWrapper.getElement().appendChild(content.getElement());		
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		menu.getMenuItemTargetingRoute(event.getLocation().getPath()).ifPresent(menuItem -> menu.selectMenuItem(menuItem));
		count++;
	}

	@WebServlet(urlPatterns = {"/myapp/*","/frontend/*"})
	@VaadinServletConfiguration(productionMode = false)
	public static class Servlet extends VaadinServlet {
	}
	
}
