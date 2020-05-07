package com.vaadin.devday.demo.views;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.vaadin.tatu.TwinColSelect;

import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@Route(value = ThemeVariantsView.ROUTE, layout = MainLayout.class)
@PageTitle(ThemeVariantsView.TITLE)
public class ThemeVariantsView extends VerticalLayout {
	public static final String ROUTE = "variants";
	public static final String TITLE = "Theme Variants";
  	int newi=1000;

	public ThemeVariantsView() {
		setSizeFull();
        Label label = new Label(TITLE);
        label.addClassName("title-label");
        add(label);
        
		add(new Span("Buttons"));
		Button primaryButton = new Button("Primary");
		primaryButton.addClassName("left-align");
		primaryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		primaryButton.setWidth("100px");
		Button secondaryButton = new Button("Contrast");
		secondaryButton.addClassName("left-align");
		secondaryButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		secondaryButton.setWidth("100px");
		Button tertiaryButton = new Button("Tertiary");
		tertiaryButton.addClassName("left-align");
		tertiaryButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		tertiaryButton.setWidth("100px");
		add(new HorizontalLayout(primaryButton, secondaryButton, tertiaryButton));


		add(new Span("Radio button group"));
		RadioButtonGroup<String> group = new RadioButtonGroup<>();
		group.setItems("Option1", "Option2", "Option3");
		group.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		add(group);


		add(new Span("Progress Bar"));
		ProgressBar contrastProgressBar = new ProgressBar();
		contrastProgressBar.addThemeVariants(ProgressBarVariant.LUMO_CONTRAST);
		contrastProgressBar.setValue(0.3);

		ProgressBar successProgressBar = new ProgressBar();
		successProgressBar.addThemeVariants(ProgressBarVariant.LUMO_SUCCESS);
		successProgressBar.setValue(0.4);

		ProgressBar errorProgressBar = new ProgressBar();
		errorProgressBar.setValue(0.5);
		errorProgressBar.addThemeVariants(ProgressBarVariant.LUMO_ERROR);
		add(contrastProgressBar, successProgressBar, errorProgressBar);

		add(new Span("TextField"));
		TextField alignCenterTextField = new TextField();
		alignCenterTextField.setValue("Align center");
		alignCenterTextField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
		TextField alignRightTextField = new TextField();
		alignRightTextField.setValue("Align right");
		alignRightTextField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
		NumberField numberField = new NumberField();
		numberField.setPreventInvalidInput(true);
		numberField.setMin(0);
		numberField.setValue(10d);
		numberField.setMax(20);
		// https://github.com/vaadin/vaadin-text-field/issues/349
//		numberField.setHasControls(true);
		numberField.setSuffixComponent(new Span("€"));
		numberField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
		add(new HorizontalLayout(alignCenterTextField, alignRightTextField,numberField));		
		
        RadioButtonGroup<String> buttons = new RadioButtonGroup<>();
        buttons.setRequiredIndicatorVisible(true);
//        buttons.setLabel("Test");
        buttons.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        buttons.setItems("One","Two", "Three");
        buttons.addValueChangeListener(event -> {
        	Notification.show(event.getValue()).setPosition(Position.MIDDLE);;
        });
        add(buttons);

        MenuBar menubar = new MenuBar();
		menubar.getElement().setAttribute("theme", "menu-bar-grow");
        menubar.setWidth("100%");
        MenuItem first = menubar.addItem("First");
        first.getSubMenu().addItem("Sub one");
        first.getSubMenu().addItem("Sub two");
        first.getSubMenu().addItem("Sub three");
        menubar.addItem("Second");
        menubar.addItem("Third");
        MenuItem fourth = menubar.addItem("Fourth");
        fourth.getSubMenu().addItem("Sub one");
        fourth.getSubMenu().addItem("Sub two");
        fourth.getSubMenu().addItem("Sub three");
        menubar.addItem("Fith");
        menubar.addItem("Sixth");

        add(menubar);
        
        TwinColSelect<String> select = new TwinColSelect<>();
//        select.setLabel("Do selection");
//        select.setRequiredIndicatorVisible(true);
      	select.setItems("One","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten");
//        Set<String> set = new HashSet<>();
//        for (Integer i=1;i<101;i++) {
//        	set.add("Item "+i);
//        }
//        select.setItems(set);
        ListDataProvider<String> dp = (ListDataProvider<String>) select.getDataProvider();
      	Set<String> set = dp.getItems().stream().collect(Collectors.toSet());
        dp.setSortComparator((a, b) -> a.compareTo(b));
      	select.setHeight("300px");
      	select.setWidth("500px");
      	Set<String> selection = new HashSet<>();
      	set.forEach(item -> {
      		if (item.contains("o")) {
      			selection.add(item);
      		}      			
      	});
      	select.setValue(selection);
      	select.addSelectionListener(event -> {
      		System.out.println("Value changed");
      		if (event.isFromClient()) {
      			select.getValue().forEach(item -> System.out.println(item + " selected!"));
      		}
      	});      	
      	Button refresh = new Button("Refresh");
      	refresh.addClickListener(event -> {
      		dp.getItems().add("An item "+newi);
      		newi++;
            dp.setSortComparator((a, b) -> a.compareTo(b));
      		dp.refreshAll();
      	});
      	ComboBox<String> combo = new ComboBox<>();
      	combo.setItems("","One","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten");
      	add(select,refresh,combo);
      	combo.addValueChangeListener(event -> {
      		Notification.show(event.getValue());
      	});

	}
	
}
