package com.vaadin.devday.demo.views;

import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@Route(value = SplitLayoutView.ROUTE, layout = MainLayout.class)
@PageTitle(SplitLayoutView.TITLE)
public class SplitLayoutView extends SplitLayout {
    public static final String ROUTE = "split";
    public static final String TITLE = "Split layout";    
	private Registration listener;
	private boolean isNarrow = false;
	private boolean columnChartsVisible = true;
    
    public SplitLayoutView(){
        setSizeFull();
         
        SplitLayout innerLayout = new SplitLayout();
        innerLayout.setOrientation(Orientation.VERTICAL);
        innerLayout.addToPrimary(ChartUtil.getPieChart("2010"));
        innerLayout.addToSecondary(ChartUtil.getPieChart("2018"));
        innerLayout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        innerLayout.addSplitterDragendListener(event -> {
        	// Charts do not reflow automatically in all cases even when wrapped in Div
        	getUI().ifPresent(ui -> ui.getPage().executeJavaScript("Array.from(window.document.getElementsByTagName('vaadin-chart')).forEach( el => el.__reflow());"));        	        	
        });
        innerLayout.setSplitterPosition(50);
        
        addToPrimary(ChartUtil.getColumnChart());
        addSplitterDragendListener(event -> {
// Waiting https://github.com/vaadin/vaadin-split-layout-flow/issues/47 to be fixed
//        	if (isNarrow) {
//        		if (columnChartsVisible) {
//        			getPrimaryComponent().getElement().getStyle().set("flex","0%"); 
//        			columnChartsVisible  = false;
//        		} else {
//        			getPrimaryComponent().getElement().getStyle().set("flex","100%"); 
//        			columnChartsVisible = true;
//        		}
//        	}
        	getUI().ifPresent(ui -> ui.getPage().executeJavaScript("Array.from(window.document.getElementsByTagName('vaadin-chart')).forEach( el => el.__reflow());"));
        });
        addToSecondary(innerLayout);
        setSplitterPosition(50);
    }
   
    public String getAddress() {
    	return (String) UI.getCurrent().getSession().getAttribute("hostAddress");
    }
 
    @Override
    protected void onAttach(AttachEvent attachEvent) {
    	super.onAttach(attachEvent);
    	System.out.println("UI: "+attachEvent.getUI().toString());
    	System.out.println("Comp: "+this.toString());
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
        	System.out.println("Browser resize: "+this.toString());
        	if (event.getWidth() < 800) {
        		// Getting restore position would be nice https://github.com/vaadin/vaadin-split-layout-flow/issues/50
        		setSplitterPosition(0);
        		isNarrow = true;
        	} else {
        		if (isNarrow) {
        			setSplitterPosition(50);
        			isNarrow = false;
        		}
        	}
        }));
    	System.out.println("onAttach");
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
    	// It is needed to remove the BrowserWindowResizeListener during detach
    	// in order not to bloat browser resources
    	listener.remove();
    	super.onDetach(detachEvent);
    	System.out.println("onDetach");
    }
    
    
}
