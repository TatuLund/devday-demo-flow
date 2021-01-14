package com.vaadin.devday.demo.views;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.dom.Element;

public class PrefixSuffixUtil {

    private static Stream<Element> getElementsInSlot(HasElement target,
            String slot) {
        return target.getElement().getChildren()
                .filter(child -> slot.equals(child.getAttribute("slot")));
    }

    public static void setPrefixComponent(Component target, Component component) {
        clearSlot(target,"prefix");

        if (component != null) {
            component.getElement().setAttribute("slot", "prefix");
            target.getElement().appendChild(component.getElement());
        }
    }

    private static void clearSlot(Component target, String slot) {
        getElementsInSlot(target, "prefix").collect(Collectors.toList())
                .forEach(target.getElement()::removeChild);
    }

    public static  void setSuffixComponent(Component target, Component component) {
        clearSlot(target,"suffix");

        if (component != null) {
            component.getElement().setAttribute("slot", "suffix");
            target.getElement().appendChild(component.getElement());
        }
    }

    private static Component getChildInSlot(HasElement target, String slot) {
        Optional<Element> element = getElementsInSlot(target, slot).findFirst();
        if (element.isPresent()) {
            return element.get().getComponent().get();
        }
        return null;
    }

    public static Component getPrefixComponent(Component target) {
        return getChildInSlot(target, "prefix");
    }

    public static Component getSuffixComponent(Component target) {
        return getChildInSlot(target, "suffix");
    }

}
