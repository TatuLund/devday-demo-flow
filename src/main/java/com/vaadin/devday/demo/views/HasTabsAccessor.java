package com.vaadin.devday.demo.views;

import java.util.Optional;

import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.tabs.Tabs;

public interface HasTabsAccessor {

    public default Tabs getTabs(Component component) {
        Optional<Component> parent = component.getParent();
        Tabs menu = null;
        while (parent.isPresent()) {
            Component p = parent.get();
            if (p instanceof MainLayout) {
                MainLayout main = (MainLayout) p;
                menu = main.getMenu();
            }
            parent = p.getParent();
        }
        return menu;
    }
}
