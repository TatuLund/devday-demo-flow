package com.vaadin.devday.demo.views;

import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = ThemeVariantsView.ROUTE, layout = MainLayout.class)
@PageTitle(ThemeVariantsView.TITLE)
public class ThemeVariantsView extends VerticalLayout {
	public static final String ROUTE = "variants";
	public static final String TITLE = "Theme Variants";

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
        buttons.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        buttons.setItems("One","Two", "Three");
        buttons.addValueChangeListener(event -> {
        	Notification.show(event.getValue()).setPosition(Position.MIDDLE);;
        });
        add(buttons);
	}
}
