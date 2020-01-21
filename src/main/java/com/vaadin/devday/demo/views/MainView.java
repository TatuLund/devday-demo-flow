package com.vaadin.devday.demo.views;

import java.util.List;

import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouteData;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.RouteRegistry;

@Route(value = MainView.ROUTE, layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class MainView extends VerticalLayout implements HasUrlParameter<String>, HasDynamicTitle, BeforeEnterObserver {

	public static final String ROUTE = "layouts";
	public static final String TITLE = "Layouts";

	private final HorizontalLayout layout;
	private Div navigation;
	private Div content;
	private boolean scrollable;

	public MainView() {
		setPadding(false);
		setSpacing(false);
		setDefaultHorizontalComponentAlignment(Alignment.STRETCH);

		final Div header = new Div();
		header.getStyle().set("flexShrink", "0");
		header.setText("This is the header. My height is 150 pixels");
		header.setClassName("header");
		header.setHeight("150px");

		layout = new HorizontalLayout();
		layout.setHeight("100px");
		layout.setSpacing(false);
		createTextLayout();

		final Div footer = new Div();
		footer.getStyle().set("flexShrink", "0");
		footer.setText("This is the footer area. My height is 100 pixels");
		footer.setClassName("footer");
		footer.setHeight("100px");

		add(new Button("Scroll", event -> {
			getUI().get().navigate("layouts/scroll");
		}));
		
		add(header);
		add(layout);
		add(footer);
		expand(layout);
	}

	private void createTextLayout() {
		navigation = new Div();
		navigation.setClassName("navigation");
		navigation.setWidth("25%");
		navigation.getElement().getStyle().set("flex-shrink", "0");
		navigation.setText("This is the navigation area. My width is 25% of the window.");

		content = new Div();
		content.getStyle().set("display", "flex");
		content.setText("This is the content area");
		content.setClassName("content");
		content.getStyle().set("alignContent", "start");
		content.getStyle().set("overflow-y", "auto");
		
		layout.add(navigation, content);
		layout.expand(content);
		layout.setDefaultVerticalComponentAlignment(Alignment.STRETCH);
	}

	private Div createBlock() {
		final Div button = new Div();
		button.getStyle().set("background", "var(--lumo-tint-10pct)");
		button.setHeight("200px");
		button.setWidth("200px");
		button.getStyle().set("margin", "2px");
		// Add scroll bar to Div if TextArea overgrows
		button.getElement().getStyle().set("overflow", "auto");
		TextArea area = new TextArea("Block");
		area.setWidthFull();
		button.add(area);
		return button;
	}


	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
		if ("scroll".equals(parameter)) {
			updateUIForScroll();
		}		
	}

	private void updateUIForScroll() {
		final Button add = new Button("Add", e -> {
			content.add(createBlock());
		});
		navigation.setText(null);
		content.setText(null);

		navigation.add(add);
		scrollable = true;
		
		makeContentScrollable();
	}

	private void makeContentScrollable() {
		content.getStyle().set("flexWrap", "wrap");
		content.getStyle().set("overflowY", "auto");
	}

	@Override
	public String getPageTitle() {
		if (scrollable)
			return "Scroll";
		else
			return TITLE;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
        RouteRegistry registry = event.getUI().getRouter().getRegistry();
        Class<? extends Component> target = (Class<? extends Component>) event.getNavigationTarget();
        String path = null; 
		for (RouteData route : registry.getRegisteredRoutes()) {
        	if (route.getNavigationTarget() == target) path = route.getUrl();        			
        }
        System.out.println("Path: "+path);
        List<Class<? extends RouterLayout>> layouts = registry.getRouteLayouts(path, target);
        layouts.forEach(layout -> System.out.println(layout.toString()));
	}

}