package com.elvin66283.firstjavacrud;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    private final PersonRepository repository;
    private final Grid<Person> grid = new Grid<>(Person.class, false);
    private final TextField name = new TextField("Name");
    private final TextField email = new TextField("Email");
    private Person selectedPerson;

    public MainView(PersonRepository repository) {
        this.repository = repository;

        add("Person Management - Java CRUD");

        grid.addColumn(Person::getId).setHeader("ID");
        grid.addColumn(Person::getName).setHeader("Name");
        grid.addColumn(Person::getEmail).setHeader("Email");

        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedPerson = event.getValue();
            if (selectedPerson != null) {
                name.setValue(selectedPerson.getName());
                email.setValue(selectedPerson.getEmail());
            }
        });

        Button save = new Button("Save", event -> savePerson());
        Button delete = new Button("Delete", event -> deletePerson());
        Button clear = new Button("Clear", event -> clearForm());

        add(new HorizontalLayout(name, email));
        add(new HorizontalLayout(save, delete, clear));
        add(grid);

        refreshGrid();
    }

    private void savePerson() {
        if (name.isEmpty() || email.isEmpty()) {
            Notification.show("Please enter name and email");
            return;
        }

        Person person = selectedPerson == null ? new Person() : selectedPerson;
        person.setName(name.getValue());
        person.setEmail(email.getValue());

        repository.save(person);
        clearForm();
        refreshGrid();
        Notification.show("Saved successfully!");
    }

    private void deletePerson() {
        if (selectedPerson != null) {
            repository.delete(selectedPerson);
            clearForm();
            refreshGrid();
            Notification.show("Deleted successfully!");
        }
    }

    private void clearForm() {
        selectedPerson = null;
        name.clear();
        email.clear();
        grid.deselectAll();
    }

    private void refreshGrid() {
        grid.setItems(repository.findAll());
    }
}