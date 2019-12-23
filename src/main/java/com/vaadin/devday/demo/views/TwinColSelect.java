package com.vaadin.devday.demo.views;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.HasDataProvider;
import com.vaadin.flow.data.binder.HasItemsAndComponents;
import com.vaadin.flow.data.provider.DataChangeEvent;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.KeyMapper;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.selection.MultiSelect;
import com.vaadin.flow.data.selection.MultiSelectionEvent;
import com.vaadin.flow.data.selection.MultiSelectionListener;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.shared.Registration;

@Tag("div")
public class TwinColSelect<T> extends AbstractField<TwinColSelect<T>,Set<T>> implements HasItemsAndComponents<T>,
    HasSize, HasValidation, MultiSelect<TwinColSelect<T>, T>, HasDataProvider<T> {

    private static final String VALUE = "value";

    private final KeyMapper<T> keyMapper = new KeyMapper<>(this::getItemId);
    
    private SerializablePredicate<T> itemEnabledProvider = item -> isEnabled();
    
	private VerticalLayout list1 = new VerticalLayout();
	private VerticalLayout list2 = new VerticalLayout();
	private String errorMessage = "Validation error";
	private Div errorLabel = new Div();
	private DataProvider<T, ?> dataProvider; 

    private ItemLabelGenerator<T> itemLabelGenerator = String::valueOf;
	
	final static String LIST_BORDER = "1px var(--lumo-primary-color) solid";
	final static String LIST_BORDER_RADIUS = "var(--lumo-border-radius)";
	final static String LIST_BORDER_ERROR = "1px var(--lumo-error-color) solid";
	final static String LIST_BACKGROUND_ERROR = "var(--lumo-error-color-10pct)";
	final static String LIST_BACKGROUND = "var(--lumo-contrast-10pct)";

    private Registration dataProviderListenerRegistration;
	
    private static class CheckBoxItem<T> extends Checkbox
    	implements ItemComponent<T> {

    	private final T item;

		private CheckBoxItem(String id, T item) {
    		this.item = item;
    		getElement().setProperty(VALUE, id);
		}

		@Override
		public T getItem() {
			return item;
		}

    }
    
	public TwinColSelect() {
		super(null);
		getElement().getStyle().set("display", "flex");
		getElement().getStyle().set("flex-direction", "column");
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		setErrorLabelStyles();
		setSizeFull();
		setupList(list1);
		setupList(list2);
		Button allButton = new Button(VaadinIcon.ANGLE_DOUBLE_RIGHT.create());
		allButton.addClickListener(event -> {
			list1.getChildren().forEach(comp -> {
				Checkbox checkbox = (Checkbox) comp;
				list1.remove(checkbox);
				list2.add(checkbox);
			});
			setValue(getSelectedItems());
		});
		Button addButton = new Button(VaadinIcon.ANGLE_RIGHT.create());
		addButton.addClickListener(event -> {
			moveItems(list1,list2);
		});
		Button removeButton = new Button(VaadinIcon.ANGLE_LEFT.create());
		removeButton.addClickListener(event -> {
			moveItems(list2,list1);
		});
		Button clearButton = new Button(VaadinIcon.TRASH.create());
		clearButton.addClickListener(event -> {
			list2.getChildren().forEach(comp -> {
				Checkbox checkbox = (Checkbox) comp;
				list2.remove(checkbox);
				list1.add(checkbox);
			});
			setValue(getSelectedItems());
		});
		VerticalLayout buttons = new VerticalLayout();
		buttons.setWidth("50px");
		buttons.setHeight("100%");
		buttons.setJustifyContentMode(JustifyContentMode.CENTER);
		buttons.add(allButton,addButton,removeButton, clearButton);
		layout.setFlexGrow(1, list1,list2);
		layout.add(list1,buttons,list2);
		add(layout,errorLabel);
	}

	private void moveItems(VerticalLayout list1, VerticalLayout list2) {
		list1.getChildren().forEach(comp -> {
			Checkbox checkbox = (Checkbox) comp;
			if (checkbox.getValue()) {
				list1.remove(checkbox);
				list2.add(checkbox);
			}
		});
		setValue(getSelectedItems());
	}

	private void setupList(VerticalLayout list) {
		list.getStyle().set("overflow-y", "auto");
		list.setSizeFull();
		list.getStyle().set("border", LIST_BORDER);
		list.getStyle().set("border-radius",LIST_BORDER_RADIUS);
		list.getStyle().set("background", LIST_BACKGROUND);
	}

	private void setErrorLabelStyles() {
		errorLabel.getStyle().set("color", "var(--lumo-error-text-color)");
		errorLabel.getStyle().set("font-size", "var(--lumo-font-size-xs)");
		errorLabel.getStyle().set("line-height", "var(--lumo-line-height-xs)");
		errorLabel.getStyle().set("will-change", "max-height");
		errorLabel.getStyle().set("transition", "0.4s max-height");
		errorLabel.getStyle().set("max-height", "5em");
		errorLabel.getStyle().set("align-self", "flex-end");
	}

    private void reset(boolean refresh) {
        keyMapper.removeAll();
        list1.removeAll();
        if (!refresh) clear();
        getDataProvider().fetch(new Query<>()).map(this::createCheckBox)
                .forEach(checkbox -> {
                	if (!this.getSelectedCheckboxItems().anyMatch(selected -> checkbox.getItem().equals(selected.getItem()))) {
                		list1.add(checkbox);
                	} else {
                		
                	}
                });
    }
	
    public void setItemLabelGenerator(
            ItemLabelGenerator<T> itemLabelGenerator) {
        Objects.requireNonNull(itemLabelGenerator,
                "The item label generator can not be null");
        this.itemLabelGenerator = itemLabelGenerator;
        reset(true);
    }
	
    public void setItemEnabledProvider(
            SerializablePredicate<T> itemEnabledProvider) {
        this.itemEnabledProvider = Objects.requireNonNull(itemEnabledProvider);
        refreshCheckboxes();
    }
    
    public ItemLabelGenerator<T> getItemLabelGenerator() {
        return itemLabelGenerator;
    }
    
    public SerializablePredicate<T> getItemEnabledProvider() {
        return itemEnabledProvider;
    }

    private Stream<CheckBoxItem<T>> getCheckboxItems() {
        return list1.getChildren().filter(CheckBoxItem.class::isInstance)
                .map(child -> (CheckBoxItem<T>) child);
    }
    

    private Stream<CheckBoxItem<T>> getSelectedCheckboxItems() {
        return list2.getChildren().filter(CheckBoxItem.class::isInstance)
                .map(child -> (CheckBoxItem<T>) child);
    }

    private void refreshCheckboxes() {
        getCheckboxItems().forEach(this::updateCheckbox);
    }
    
    protected boolean isDisabledBoolean() {
        return getElement().getProperty("disabled", false);
    }

    
    private void updateEnabled(CheckBoxItem<T> checkbox) {
        boolean disabled = isDisabledBoolean()
                || !getItemEnabledProvider().test(checkbox.getItem());
        Serializable rawValue = checkbox.getElement()
                .getPropertyRaw("disabled");
        if (rawValue instanceof Boolean) {
            // convert the boolean value to a String to force update the
            // property value. Otherwise since the provided value is the same as
            // the current one the update don't do anything.
            checkbox.getElement().setProperty("disabled",
                    disabled ? Boolean.TRUE.toString() : null);
        } else {
        	checkbox.setEnabled(!disabled);
        }
    }

    
    private void updateCheckbox(CheckBoxItem<T> checkbox) {
        checkbox.setLabel(getItemLabelGenerator().apply(checkbox.getItem()));
        updateEnabled(checkbox);
    }
	
    private CheckBoxItem<T> createCheckBox(T item) {
        CheckBoxItem<T> checkbox = new CheckBoxItem<>(keyMapper.key(item),
                item);
        updateCheckbox(checkbox);
        return checkbox;
    }
	
	@Override
	public Set<T> getEmptyValue() {
		return new HashSet<T>();
	}

	@Override
	public Element getElement() {
		return super.getElement();
	}

	@Override
	public void setInvalid(boolean invalid) {
		if (invalid) {
			list2.getStyle().set("border", LIST_BORDER_ERROR);
			list2.getStyle().set("background", LIST_BACKGROUND_ERROR);
			errorLabel.setText(errorMessage);
			errorLabel.setVisible(true);
		} else {
			list2.getStyle().set("border", LIST_BORDER);		
			list2.getStyle().set("background", LIST_BACKGROUND);
			errorLabel.setVisible(false);
		}

	}

	@Override 		
	public void setValue(Set<T> value) {
		Objects.requireNonNull(value,
				"Cannot set a null value to checkbox group. "
						+ "Use the clear-method to reset the component's value to an empty set.");	
		super.setValue(value);
		list1.getChildren().forEach(comp -> {
			Checkbox checkbox = (Checkbox) comp;
			if (checkbox.getLabel().equals(value)) {
				list1.remove(checkbox);
				list2.add(checkbox);
			}
		});			
	}

	@Override
	public boolean isInvalid() {
		return false;
	}

	@Override
	public void setDataProvider(DataProvider<T, ?> dataProvider) {
        this.dataProvider = dataProvider;
        reset(true);

        if (dataProviderListenerRegistration != null) {
            dataProviderListenerRegistration.remove();
        }
        dataProviderListenerRegistration = dataProvider
                .addDataProviderListener(event -> {
                    if (event instanceof DataChangeEvent.DataRefreshEvent) {
                        T otherItem = ((DataChangeEvent.DataRefreshEvent<T>) event)
                                .getItem();
                        this.getCheckboxItems()
                                .filter(item -> Objects.equals(
                                        getItemId(item.item),
                                        getItemId(otherItem)))
                                .findFirst().ifPresent(this::updateCheckbox);
                    } else {
                        reset(false);
                    }
                });
	}

	@Override
	public Set<T> getSelectedItems() {
		Set<T> set = new HashSet<T>();

		list2.getChildren().forEach(comp -> { 
			CheckBoxItem<T> checkbox = (CheckBoxItem) comp;
			set.add(checkbox.getItem());
		});
		return set;
	}

	@Override
	public void updateSelection(Set<T> addedItems, Set<T> removedItems) {
		Set<T> value = new HashSet<>(getValue());
		value.addAll(addedItems);
		value.removeAll(removedItems);
		setValue(value);			
	}

	@Override
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;			
	}

	@Override
	public String getErrorMessage() {
		return this.errorMessage;
	}

	@Override
	public Registration addSelectionListener(MultiSelectionListener<TwinColSelect<T>, T> listener) {
		return addValueChangeListener(event -> listener
				.selectionChange(new MultiSelectionEvent<>(this, this,
						event.getOldValue(), event.isFromClient())));
	}

	@Override
	protected void setPresentationValue(Set<T> newPresentationValue) {
		this.setValue(newPresentationValue);	
	}
	
	public DataProvider<T,?> getDataProvider() {
		return dataProvider;
	}

    private Object getItemId(T item) {
        if (getDataProvider() == null) {
            return item;
        }
        return getDataProvider().getId(item);
    }

}
