package com.vaadin.devday.demo;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
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
public class MainLayout extends AppLayout implements RouterLayout {

	private FlexLayout childWrapper = new FlexLayout();
    private Tabs menu = new Tabs();

    private Tab createMenuItem(String title, VaadinIcon icon, Class<? extends Component> target) {
        RouterLink link = new RouterLink(null,target);
        if (icon != null) link.add(icon.create());
        link.add(title);
        Tab tab = new Tab();
        tab.add(link);
        return tab;
    }
   
	public MainLayout() {
        Image img = new Image("https://vaadin.com/images/vaadin-logo.svg", "Vaadin Logo");
        img.setHeight("35px");
        menu.setOrientation(Tabs.Orientation.HORIZONTAL);
        Span appName = new Span(img);
        appName.addClassName("hide-on-mobile");
        addToNavbar(true, appName, menu);
        
        getElement().getStyle().set("--vaadin-app-layout-navbar-background", "var(--lumo-tint-30pct)");
       
        menu.add(createMenuItem(AccordionView.TITLE,null,AccordionView.class),
        		createMenuItem(SplitLayoutView.TITLE, null, SplitLayoutView.class),
        		createMenuItem(DialogView.TITLE, null, DialogView.class),
        		createMenuItem(GridView.TITLE, null, GridView.class),
        		createMenuItem(VaadinBoardView.TITLE, null, VaadinBoardView.class),
        		createMenuItem(FormLayoutView.TITLE, null, FormLayoutView.class),
        		createMenuItem(ThemeVariantsView.TITLE, null, ThemeVariantsView.class),
        		createMenuItem(AbsoluteLayoutView.TITLE, null, AbsoluteLayoutView.class),
        		createMenuItem(UploadView.TITLE, null, UploadView.class),
        		createMenuItem(MainView.TITLE, null, MainView.class)
        		
        		);
        
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

	@WebServlet(urlPatterns = {"/myapp/*","/frontend/*"})
	@VaadinServletConfiguration(productionMode = false)
	public static class Servlet extends VaadinServlet {
	}

}
