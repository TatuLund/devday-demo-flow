package com.vaadin.devday.demo.views;

import java.io.Serializable;
import java.util.LinkedHashMap;

import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = AbsoluteLayoutView.ROUTE, layout = MainLayout.class)
@PageTitle(AbsoluteLayoutView.TITLE)
public class AbsoluteLayoutView extends SplitLayout {
    public static final String ROUTE = "absolute";
    public static final String TITLE = "Absolute Layout";
    private Label addressLabel1 = new Label();
    private Label addressLabel2 = new Label();
    private Label addressLabel3 = new Label();

    public AbsoluteLayoutView() {
        setSizeFull();
        AbsoluteLayout first = new AbsoluteLayout();
        first.addTopLeft(new Label("First content component"), 20, 20);
        first.addBottomRight(addressLabel1, 20, 20);
        AbsoluteLayout second = new AbsoluteLayout();
        second.addTopLeft(new Label("Second content component"), 20, 20);
        second.addBottomRight(addressLabel2, 20, 20);
        AbsoluteLayout third = new AbsoluteLayout();
        third.addTopLeft(new Label("Third content component"), 20, 20);
        third.addBottomRight(addressLabel3, 20, 20);

        SplitLayout innerLayout = new SplitLayout();
        innerLayout.setOrientation(Orientation.VERTICAL);
        innerLayout.addToPrimary(second);
        innerLayout.addToSecondary(third);
        innerLayout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);

        addToPrimary(first);
        addToSecondary(innerLayout);

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        addressLabel1.setText(getAddress());
        addressLabel2.setText(getAddress());
        addressLabel3.setText(getAddress());
    }

    public String getAddress() {
        if (!getUI().isPresent())
            return "";
        return (String) getUI().get().getSession().getAttribute("hostAddress");
    }



}
