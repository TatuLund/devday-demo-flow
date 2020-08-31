package com.vaadin.devday.demo.views;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.vaadin.componentfactory.enhancedcrud.BinderCrudEditor;
import com.vaadin.componentfactory.enhancedcrud.Crud;
import com.vaadin.componentfactory.enhancedcrud.CrudEditor;
import com.vaadin.componentfactory.enhancedcrud.CrudFilter;
import com.vaadin.componentfactory.enhancedcrud.CrudVariant;
import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.toList;

@Route(value = CrudView.ROUTE, layout = MainLayout.class)
@PageTitle(CrudView.TITLE)
public class CrudView extends VerticalLayout {
    public static final String ROUTE = "crud";
    public static final String TITLE = "Crud";

    public CrudView() {
    	setSizeFull();
        Crud<Person> crud = new Crud<>(Person.class, createPersonEditor());
        PersonDataProvider dataProvider = new PersonDataProvider();

        crud.setDataProvider(dataProvider);

        // Use cancelEdit in PreSaveEvent
        // PreSaveEvent is dispatched before editor writes the bean
        crud.addPreSaveListener(e -> {
        	BinderCrudEditor<Person> crudEditor = (BinderCrudEditor<Person>) crud.getEditor();
        	TextField textField = (TextField) crudEditor.getBinder().getFields().findFirst().get();
        	if (textField.getValue().equals("noname")) {
        		crud.cancelSave();
	        }
        });
        crud.addSaveListener(e -> {
            dataProvider.persist(e.getItem());
        });
        crud.addDeleteListener(e -> dataProvider.delete(e.getItem()));

        // Prefill new item
        crud.addNewListener(e -> {
        	e.getItem().setFirstName("noname");
        	crud.getEditor().setItem(e.getItem());
        });
        crud.getGrid().removeColumnByKey("id");
        crud.addThemeVariants(CrudVariant.NO_BORDER);

        // Toggle display of toolbar
        Button hideToolbar = new Button("Hide toolbar");
        hideToolbar.addClickListener(buttonClickEvent -> crud.setToolbarVisible(!crud.isToolbarVisible()));

        // Set the whole Grid and Editor to be read only, in this mode
        // dialog is also not editable and show only close button
        Button readOnly = new Button("Read only");
        readOnly.addClickListener(buttonClickEvent -> {
            crud.setReadOnly(!crud.isReadOnly());
        });

        // There are getters for the buttons, so that they can be modified
        // individually
        Button changeEditorButtons = new Button("Change Editor Buttons");
        changeEditorButtons.addClickListener(buttonClickEvent -> {
            crud.getDelete().setText("Restore");
            crud.getCancel().setText("Quit");
            crud.getSave().getElement().setAttribute("Style", "display: none;");
            crud.getCancel().addClickListener(clickEvent -> crud.setToolbarVisible(!crud.isToolbarVisible()));
        });
        
        // Remove edit column and open editor by click listener
        Button changeEditorOpening = new Button("Change Editor Opening");
        changeEditorOpening.addClickListener(buttonClickEvent -> {
        	crud.setEditOnClick(!crud.isEditOnClick());
        });

        // There are getters for the buttons, so that they can hidden 
        // individually
        Button disableDelete = new Button("Disable Delete");
        disableDelete.addClickListener(buttonClickEvent -> {
            crud.addEditListener(event -> {
                crud.getDelete().setVisible(false);
            });
        });
	    // end-source-example
        add(crud, hideToolbar, readOnly, changeEditorButtons,changeEditorOpening,disableDelete);
    }

    private CrudEditor<Person> createPersonEditor() {
        Binder<Person> binder = new Binder<>(Person.class);
    	
        TextField firstName = new TextField("First name");
        TextField lastName = new TextField("Last name");

        FormLayout form = new FormLayout(firstName, lastName);

        binder.forField(firstName).asRequired().bind(Person::getFirstName, Person::setFirstName);
        binder.forField(lastName).bind(Person::getLastName, Person::setLastName);

        return new BinderCrudEditor<>(binder, form);
    }    

    // Dummy database
    private static final String[] FIRSTS = {"James", "Mary", "John", "Patricia", "Robert", "Jennifer"};
    private static final String[] LASTS = {"Smith", "Johnson", "Williams", "Brown"};

    private static List<Person> createPersonList() {
        return IntStream
                .rangeClosed(1, 50)
                .mapToObj(i -> new Person(i, FIRSTS[i % FIRSTS.length], LASTS[i % LASTS.length]))
                .collect(toList());
    }    

    public static class Person implements Cloneable {
        private Integer id;
        private String firstName = "";
        private String lastName = "";

        /**
         * No-arg constructor required by Crud to be able to instantiate a new bean
         * when the new item button is clicked.
         */
        public Person() {
        }

        public Person(Integer id, String firstName, String lastName) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public Person clone() {
            try {
                return (Person)super.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }

    // Person data provider
    public static class PersonDataProvider extends AbstractBackEndDataProvider<Person, CrudFilter> {

        // A real app should hook up something like JPA
        final List<Person> DATABASE = createPersonList();

        private Consumer<Long> sizeChangeListener;

        @Override
        protected Stream<Person> fetchFromBackEnd(Query<Person, CrudFilter> query) {
            int offset = query.getOffset();
            int limit = query.getLimit();

            Stream<Person> stream = DATABASE.stream();

            if (query.getFilter().isPresent()) {
                stream = stream
                        .filter(predicate(query.getFilter().get()))
                        .sorted(comparator(query.getFilter().get()));
            }

            List<Person> result = new ArrayList<>();
            stream.forEach(person -> result.add(person.clone()));
            return result.stream().skip(offset).limit(limit);
        }

        @Override
        protected int sizeInBackEnd(Query<Person, CrudFilter> query) {
            // For RDBMS just execute a SELECT COUNT(*) ... WHERE query
            long count = fetchFromBackEnd(query).count();

            if (sizeChangeListener != null) {
                sizeChangeListener.accept(count);
            }

            return (int) count;
        }

        void setSizeChangeListener(Consumer<Long> listener) {
            sizeChangeListener = listener;
        }

        private static Predicate<Person> predicate(CrudFilter filter) {
            // For RDBMS just generate a WHERE clause
            return filter.getConstraints().entrySet().stream()
                    .map(constraint -> (Predicate<Person>) person -> {
                        try {
                            Object value = valueOf(constraint.getKey(), person);
                            return value != null && value.toString().toLowerCase()
                                    .contains(constraint.getValue().toLowerCase());
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    })
                    .reduce(Predicate::and)
                    .orElse(e -> true);
        }

        private static Comparator<Person> comparator(CrudFilter filter) {
            // For RDBMS just generate an ORDER BY clause
            return filter.getSortOrders().entrySet().stream()
                    .map(sortClause -> {
                        try {
                            Comparator<Person> comparator
                                    = Comparator.comparing(person ->
                                    (Comparable) valueOf(sortClause.getKey(), person));

                            if (sortClause.getValue() == SortDirection.DESCENDING) {
                                comparator = comparator.reversed();
                            }

                            return comparator;
                        } catch (Exception ex) {
                            return (Comparator<Person>) (o1, o2) -> 0;
                        }
                    })
                    .reduce(Comparator::thenComparing)
                    .orElse((o1, o2) -> 0);
        }

        private static Object valueOf(String fieldName, Person person) {
            try {
                Field field = Person.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(person);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        void persist(Person item) {
            if (item.getId() == null) {
                item.setId(DATABASE
                        .stream()
                        .map(Person::getId)
                        .max(naturalOrder())
                        .orElse(0) + 1);
            }

            final Optional<Person> existingItem = find(item.getId());
            if (existingItem.isPresent()) {
                int position = DATABASE.indexOf(existingItem.get());
                DATABASE.remove(existingItem.get());
                DATABASE.add(position, item);
            } else {
                DATABASE.add(item);
            }
        }

        Optional<Person> find(Integer id) {
            return DATABASE
                    .stream()
                    .filter(entity -> entity.getId().equals(id))
                    .findFirst();
        }

        void delete(Person item) {
            DATABASE.removeIf(entity -> entity.getId().equals(item.getId()));
        }
    }

}

