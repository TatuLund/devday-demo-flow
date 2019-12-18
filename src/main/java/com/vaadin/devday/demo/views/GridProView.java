package com.vaadin.devday.demo.views;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.LocalDateToDateConverter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = GridProView.ROUTE, layout = MainLayout.class)
@PageTitle(GridProView.TITLE)
public class GridProView extends VerticalLayout {
	public static final String ROUTE = "gridpro";
	public static final String TITLE = "GridPro";

	public class Person {
		private String name;
		private String email;
		private Integer age;
		private Date date;
		private Boolean subscriber;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer age) {
			this.age = age;
		}
		public Boolean isSubscriber() {
			return subscriber;
		}
		public void setSubscriber(Boolean subscriber) {
			this.subscriber = subscriber;
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
	}
	
	public GridProView() {

		GridPro<Person> grid = new GridPro<>();
//			1 TextField
		grid.addEditColumn(Person::getName)
		.text((item, newValue) ->
		item.setName(newValue))
		.setHeader("Name (editable)");

//			2 TextField
		grid.addEditColumn(Person::getAge)
		.text((item, newValue) ->
		item.setName(newValue))
		.setHeader("Age (editable)");;

//		2 TextField
		DatePicker datePicker = new DatePicker();
		LocalDateToDateConverter converter = new LocalDateToDateConverter();
		grid.addEditColumn(Person::getDate)
			.custom(datePicker, (item, newValue) -> {				
				Result<Date> result = converter.convertToModel(newValue, new ValueContext((Component) datePicker, datePicker));
				result.ifOk(date -> item.setDate(date));	
			})
			.setHeader("Date (editable)");		
		
//			3 Combox
		List<String> optionsList = new ArrayList<>();
		optionsList.add("bla-bla@vaadin.com");
		optionsList.add("bla-bla@gmail.com");
		optionsList.add("super-mail@gmail.com");
		grid.addEditColumn(Person::getEmail)
		.select((item, newValue) ->
		item.setEmail(newValue), optionsList)
		.setHeader("Email (editable)");

//			4 checkbox
		grid.addEditColumn(Person::isSubscriber)
		.checkbox((item, newValue) ->
		item.setSubscriber(newValue))
		.setHeader("Subscriber (editable)");

		grid.setItems(createItems());


		add(grid);
		}

		private Collection<Person> createItems() {
		Person p = new Person();
		p.setName("Dinesh");
		p.setAge(12);
		return Arrays.asList(p);
		}	
	
}
