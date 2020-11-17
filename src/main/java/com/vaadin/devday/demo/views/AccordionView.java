package com.vaadin.devday.demo.views;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details.OpenedChangeEvent;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = AccordionView.ROUTE, layout = MainLayout.class)
@PageTitle(AccordionView.TITLE)
public class AccordionView extends VerticalLayout {
    public static final String ROUTE = "accordion";
    public static final String TITLE = "Accordion";

    public AccordionView() {
        setSizeFull();
        ProgressBar progressBar = new ProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setMax(100);
        progressBar.setMin(0);
        progressBar.setValue(25);

        BiConsumer<OpenedChangeEvent, Integer> activateProgress = (event,
                progress) -> {

            Style style = event.getSource().getSummary().getElement()
                    .getStyle();
            if (event.isOpened()) {
                style.set("color", "var(--lumo-primary-color)");
                progressBar.setValue(progress);
            } else {
                style.remove("color");
            }
        };

        // SUMMARY FACTORY
        BiFunction<Integer, String, Component> summaryFactory = (index,
                title) -> {
            Div summary = new Div();

            Div indexDiv = new Div();
            indexDiv.setText(String.valueOf(index));
            indexDiv.setHeight("25px");
            indexDiv.setWidth("25px");
            indexDiv.getStyle().set("border-radius", "50%");
            indexDiv.getStyle().set("border-style", "dashed");
            indexDiv.getStyle().set("border-width", "2px");
            indexDiv.getStyle().set("display", "inline-block");
            indexDiv.getStyle().set("font-weight", "bold");
            indexDiv.getStyle().set("line-height", "25px");
            indexDiv.getStyle().set("margin-inline-end", "10px");
            indexDiv.getStyle().set("text-align", "center");
            indexDiv.getStyle().set("vertical-align", "middle");

            summary.add(indexDiv);
            summary.add(new Span(title));

            return summary;
        };

        // BEGIN ACCORDION
        Accordion accordion = new Accordion();

        // ACCOUNT INFORMATION
        AccordionPanel accountInfo = new AccordionPanel();
        accountInfo.setSummary(summaryFactory.apply(1, "Account information"));
        accountInfo.addOpenedChangeListener(
                event -> activateProgress.accept(event, 25));

        FormLayout accountForm = new FormLayout();

        TextField emailField = new TextField();
        emailField.setLabel("Email");
        accountForm.add(emailField);

        TextField handleField = new TextField();
        handleField.setLabel("Handle");
        accountForm.add(handleField);

        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("Password");
        accountForm.add(passwordField);

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setLabel("Confirm password");
        accountForm.add(confirmPasswordField);

        accountInfo.setContent(accountForm);
        accordion.add(accountInfo);

        // PROFILE INFORMATION
        AccordionPanel profileInfo = new AccordionPanel();
        profileInfo.setSummary(summaryFactory.apply(2, "Profile information"));
        profileInfo.addOpenedChangeListener(
                event -> activateProgress.accept(event, 50));

        FormLayout profileInfoForm = new FormLayout();
        profileInfoForm.addFormItem(new TextField(), "First name");
        profileInfoForm.addFormItem(new TextField(), "Last name");
        RadioButtonGroup<String> languageGroup = new RadioButtonGroup<>();
        languageGroup.setItems("English", "Finnish");
        profileInfoForm.addFormItem(languageGroup, "Language");
        profileInfoForm.addFormItem(new DatePicker(), "Date of birth");

        profileInfo.setContent(profileInfoForm);
        accordion.add(profileInfo);

        // TOPICS OF INTEREST
        AccordionPanel topicsOfInterest = new AccordionPanel();
        topicsOfInterest
                .setSummary(summaryFactory.apply(3, "Topics of interest"));
        topicsOfInterest.addOpenedChangeListener(
                event -> activateProgress.accept(event, 75));

        FormLayout topicsForm = new FormLayout();
        topicsForm.add(new Checkbox("Culture"));
        topicsForm.add(new Checkbox("Environment"));
        topicsForm.add(new Checkbox("Fashion"));
        topicsForm.add(new Checkbox("Finance"));
        topicsForm.add(new Checkbox("Food", true));
        topicsForm.add(new Checkbox("Politics"));
        topicsForm.add(new Checkbox("Sports"));
        topicsForm.add(new Checkbox("Technology", true));

        topicsOfInterest.setContent(topicsForm);
        accordion.add(topicsOfInterest);

        // TERMS AND CONDITIONS
        AccordionPanel conditions = new AccordionPanel();
        conditions.setSummary(summaryFactory.apply(4, "Terms and conditions"));
        conditions.addOpenedChangeListener(
                event -> activateProgress.accept(event, 100));

        Paragraph paragraph = new Paragraph();
        paragraph.setText("After all has been said and done, I agree that "
                + "my data shall be safely stored for the sole purpose of "
                + "my ultimate enjoyment.");

        Button submit = new Button("Sign up");
        submit.setEnabled(false);
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submit.addClickListener(e -> Notification.show("Complete! \uD83D\uDC4D",
                4000, Position.BOTTOM_END));

        Checkbox consent = new Checkbox("I agree");
        consent.addValueChangeListener(e -> submit.setEnabled(e.getValue()));

        Div termsDetails = new Div();
        termsDetails.getStyle().set("display", "flex");
        termsDetails.getStyle().set("flex-wrap", "wrap");
        termsDetails.getStyle().set("justify-content", "space-between");
        termsDetails.add(paragraph, consent, submit);

        conditions.setContent(termsDetails);
        accordion.add(conditions);

        accordion.setWidth("100%");
        add(accordion);
        add(progressBar);
    }

}
