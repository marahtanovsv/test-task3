package com.haulmont.testtask;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.RecipeDAO;
import com.haulmont.testtask.model.Doctor;
import com.haulmont.testtask.model.Patient;
import com.haulmont.testtask.model.Priority;
import com.haulmont.testtask.model.Recipe;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {


    PatientDAO patientDAO = new PatientDAO();
    DoctorDAO doctorDAO = new DoctorDAO();
    RecipeDAO recipeDAO = new RecipeDAO();

    private Grid<Patient> gridPatient = new Grid<Patient>(Patient.class);
    private Grid<Doctor> gridDoctor = new Grid<Doctor>(Doctor.class);
    private Grid<Recipe> gridRecipe = new Grid<Recipe>(Recipe.class);
    String focus = "Patient";

    @Override
    protected void init(VaadinRequest request) {
        //patientDAO.createTable();
        //doctorDAO.createTable();
        //recipeDAO.createTable();

        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout filterlayout = new HorizontalLayout();
        HorizontalLayout objectLayout = new HorizontalLayout();
        HorizontalLayout infoLayout = new HorizontalLayout();
        VerticalLayout tableLayout = new VerticalLayout();
        VerticalLayout actionButtonLayout = new VerticalLayout();

        layout.addComponent(objectLayout);
        layout.addComponent(filterlayout);
        layout.addComponent(infoLayout);
        filterlayout.setVisible(false);

        infoLayout.addComponent(tableLayout);
        infoLayout.addComponent(actionButtonLayout);

        gridPatient.setItems(patientDAO.getAll());
        gridPatient.setWidth("800");
        tableLayout.addComponent(gridPatient);

        Button parientButton = new Button("Patient");
        Button doctorButton = new Button("Doctor");
        Button recipeButton = new Button("Recipe");

        Button addButton = new Button("Add");
        Button deleteButton = new Button("Delete");
        Button changeButton = new Button("Change");
        Button viewStatisticsButton = new Button("View statistics");
        viewStatisticsButton.setVisible(false);

        Button applyFilterButton = new Button("Apply filter");
        TextField patientTextField = new TextField("Patient");
        ComboBox priorityComboBox = new ComboBox("Priority", Arrays.asList(Priority.values()));
        TextField descriptionTextField = new TextField("Description");

        objectLayout.addComponent(parientButton);
        objectLayout.addComponent(doctorButton);
        objectLayout.addComponent(recipeButton);
        actionButtonLayout.addComponent(addButton);
        actionButtonLayout.addComponent(changeButton);
        actionButtonLayout.addComponent(deleteButton);
        actionButtonLayout.addComponent(viewStatisticsButton);
        filterlayout.addComponent(applyFilterButton);
        filterlayout.addComponent(patientTextField);
        filterlayout.addComponent(priorityComboBox);
        filterlayout.addComponent(descriptionTextField);

        setContent(layout);


        parientButton.addClickListener(click -> {
            gridPatient.setItems(patientDAO.getAll());
            gridPatient.setWidth("800");
            tableLayout.removeAllComponents();
            focus = parientButton.getCaption();
            tableLayout.addComponent(gridPatient);
            viewStatisticsButton.setVisible(false);
            filterlayout.setVisible(false);

        });

        doctorButton.addClickListener(click -> {

            gridDoctor.setItems(doctorDAO.getAll());
            tableLayout.removeAllComponents();
            focus = doctorButton.getCaption();
            tableLayout.addComponent(gridDoctor);
            viewStatisticsButton.setVisible(true);
            filterlayout.setVisible(false);

        });

        recipeButton.addClickListener(click -> {
            gridRecipe.setItems(recipeDAO.getAll());
            gridRecipe.setWidth("1000");
            tableLayout.removeAllComponents();
            focus = recipeButton.getCaption();
            tableLayout.addComponent(gridRecipe);
            viewStatisticsButton.setVisible(false);
            filterlayout.setVisible(true);
        });

        applyFilterButton.addClickListener(click -> {
            tableLayout.removeAllComponents();
            if (patientTextField.isEmpty() & (priorityComboBox.getValue() == null) & descriptionTextField.isEmpty()) {
                gridRecipe.setItems(recipeDAO.getAll());
            } else if (patientTextField.isEmpty() & descriptionTextField.isEmpty()) {
                gridRecipe.setItems(recipeDAO.getFilteredRecipeByPriority(priorityComboBox.getValue().toString()));
            } else if (patientTextField.isEmpty() & priorityComboBox.isEmptySelectionAllowed()) {
                gridRecipe.setItems(recipeDAO.getFilteredRecipetByDescription(descriptionTextField.getValue()));
            } else
                gridRecipe.setItems(recipeDAO.getFilteredRecipeByIdPatient(Long.parseLong(patientTextField.getValue())));
            gridRecipe.setWidth("1000");
            focus = recipeButton.getCaption();
            tableLayout.addComponent(gridRecipe);
            viewStatisticsButton.setVisible(false);
            filterlayout.setVisible(true);
        });


        addButton.addClickListener(click -> {

            switch (focus) {
                case ("Patient"):
                    Window subWindowPatient = new Window("Add patient");
                    subWindowPatient.setModal(true);
                    VerticalLayout addLayoutPatient = new VerticalLayout();
                    VerticalLayout textAreaLayoutPatient = new VerticalLayout();
                    HorizontalLayout buttonLayoutPatient = new HorizontalLayout();
                    addLayoutPatient.addComponent(textAreaLayoutPatient);
                    addLayoutPatient.addComponent(buttonLayoutPatient);
                    subWindowPatient.setContent(addLayoutPatient);

                    TextField nameTextFieldPatient = new TextField("Name");
                    TextField surnameTextFieldPatient = new TextField("Surname");
                    TextField lastnameTextFieldPatient = new TextField("Lastname");
                    TextField phoneTextFieldPatient = new TextField("Phone");

                    textAreaLayoutPatient.addComponent(new Label("Add attribute patient"));
                    textAreaLayoutPatient.addComponent(nameTextFieldPatient);
                    textAreaLayoutPatient.addComponent(surnameTextFieldPatient);
                    textAreaLayoutPatient.addComponent(lastnameTextFieldPatient);
                    textAreaLayoutPatient.addComponent(phoneTextFieldPatient);

                    Button okButtonPatient = new Button("Ok");
                    Button cancelButtonPatient = new Button("Cancel");
                    buttonLayoutPatient.addComponent(okButtonPatient);
                    buttonLayoutPatient.addComponent(cancelButtonPatient);
                    okButtonPatient.addClickListener(click2 -> {
                        List<Patient> patients = patientDAO.getAll();
                        long id;
                        long maxId = 0;
                        String phone = phoneTextFieldPatient.getValue().replaceAll("[\\D]", "");
                        if (phone.length() < 9 || phone.length() > 11) {
                            Window errorTelephoneNumber = new Window("Error");
                            errorTelephoneNumber.setModal(true);
                            addWindow(errorTelephoneNumber);
                            VerticalLayout error = new VerticalLayout(new Label("Incorrect phone number."));
                            errorTelephoneNumber.setContent(error);
                        } else if(nameTextFieldPatient.isEmpty()|surnameTextFieldPatient.isEmpty()|lastnameTextFieldPatient.isEmpty()){
                            Window errorTextArea = new Window("Error");
                            errorTextArea.setModal(true);
                            addWindow(errorTextArea);
                            VerticalLayout error = new VerticalLayout(new Label("All fields must be filled in."));
                            errorTextArea.setContent(error);
                        }
                            else {
                            for (Patient patient : patients) {
                                if (patient.getId() > maxId) maxId = patient.getId();
                            }
                            id = maxId + 1;
                            Patient patient = new Patient(id, nameTextFieldPatient.getValue(),
                                    surnameTextFieldPatient.getValue(), lastnameTextFieldPatient.getValue(),
                                    phoneTextFieldPatient.getValue());
                            patientDAO.add(patient);
                            parientButton.click();
                            subWindowPatient.close();
                        }
                    });

                    cancelButtonPatient.addClickListener(click2 -> {
                        subWindowPatient.close();
                    });
                    subWindowPatient.center();
                    addWindow(subWindowPatient);

                    break;
                case ("Doctor"):

                    Window subWindowDoctor = new Window("Add doctor");
                    subWindowDoctor.setModal(true);
                    VerticalLayout addLayoutDoctor = new VerticalLayout();
                    VerticalLayout textAreaLayoutDoctor = new VerticalLayout();
                    HorizontalLayout buttonLayoutDoctor = new HorizontalLayout();
                    addLayoutDoctor.addComponent(textAreaLayoutDoctor);
                    addLayoutDoctor.addComponent(buttonLayoutDoctor);
                    subWindowDoctor.setContent(addLayoutDoctor);


                    TextField nameTextFieldDoctor = new TextField("Name");
                    TextField surnameTextFieldDoctor = new TextField("Surname");
                    TextField lastnameTextFieldDoctor = new TextField("Lastname");
                    TextField specializationTextFieldDoctor = new TextField("Specialization");

                    textAreaLayoutDoctor.addComponent(new Label("Add attribute doctor"));
                    textAreaLayoutDoctor.addComponent(nameTextFieldDoctor);
                    textAreaLayoutDoctor.addComponent(surnameTextFieldDoctor);
                    textAreaLayoutDoctor.addComponent(lastnameTextFieldDoctor);
                    textAreaLayoutDoctor.addComponent(specializationTextFieldDoctor);

                    Button okButtonDoctor = new Button("Ok");
                    Button cancelButtonDoctor = new Button("Cancel");
                    buttonLayoutDoctor.addComponent(okButtonDoctor);
                    buttonLayoutDoctor.addComponent(cancelButtonDoctor);
                    okButtonDoctor.addClickListener(click2 -> {
                        long id;
                        long maxId = 0;
                        if(nameTextFieldDoctor.isEmpty()|surnameTextFieldDoctor.isEmpty()|lastnameTextFieldDoctor.isEmpty()|specializationTextFieldDoctor.isEmpty()){
                            Window errorTextArea = new Window("Error");
                            errorTextArea.setModal(true);
                            addWindow(errorTextArea);
                            VerticalLayout error = new VerticalLayout(new Label("All fields must be filled in."));
                            errorTextArea.setContent(error);
                        }
                        else {
                            List<Doctor> doctors = doctorDAO.getAll();
                            for (Doctor doctor : doctors) {
                                if (doctor.getId() > maxId) maxId = doctor.getId();
                            }
                            id = maxId + 1;
                            Doctor doctor = new Doctor(id, nameTextFieldDoctor.getValue(), surnameTextFieldDoctor.getValue(),
                                    lastnameTextFieldDoctor.getValue(), specializationTextFieldDoctor.getValue());
                            doctorDAO.add(doctor);
                            doctorButton.click();
                            subWindowDoctor.close();
                        }
                    });

                    cancelButtonDoctor.addClickListener(click2 -> {
                        subWindowDoctor.close();
                    });

                    subWindowDoctor.center();
                    addWindow(subWindowDoctor);


                    break;
                case ("Recipe"):

                    Window subWindowRecipe = new Window("Add recipe");
                    subWindowRecipe.setModal(true);
                    VerticalLayout addLayoutRecipe = new VerticalLayout();
                    VerticalLayout textAreaLayoutRecipe = new VerticalLayout();
                    HorizontalLayout buttonLayoutRecipe = new HorizontalLayout();
                    addLayoutRecipe.addComponent(textAreaLayoutRecipe);
                    addLayoutRecipe.addComponent(buttonLayoutRecipe);
                    subWindowRecipe.setContent(addLayoutRecipe);

                    TextField descriptionTextFieldRecipe = new TextField("description");
                    TextField patientTextFieldRecipe = new TextField("patient_id");
                    TextField doctorTextFieldRecipe = new TextField("doctor_id");
                    DateField creationDateDateFieldRecipe = new DateField("creationDate");
                    DateField validityDateFieldRecipe = new DateField("validity");
                    ComboBox<Priority> priorityComboBoxRecipe = new ComboBox("Priority", Arrays.asList(Priority.values()));

                    textAreaLayoutRecipe.addComponent(new Label("add attribute recipe"));
                    textAreaLayoutRecipe.addComponent(descriptionTextFieldRecipe);
                    textAreaLayoutRecipe.addComponent(patientTextFieldRecipe);
                    textAreaLayoutRecipe.addComponent(doctorTextFieldRecipe);
                    textAreaLayoutRecipe.addComponent(creationDateDateFieldRecipe);
                    textAreaLayoutRecipe.addComponent(validityDateFieldRecipe);
                    textAreaLayoutRecipe.addComponent(priorityComboBoxRecipe);

                    Button okButtonRecipe = new Button("Ok");
                    Button cancelButtonRecipe = new Button("Cancel");
                    buttonLayoutRecipe.addComponent(okButtonRecipe);
                    buttonLayoutRecipe.addComponent(cancelButtonRecipe);
                    okButtonRecipe.addClickListener(click2 -> {
                        long id;
                        long maxId = 0;
                        if(descriptionTextFieldRecipe.isEmpty()|priorityComboBoxRecipe.getValue()==null){
                            Window errorTextArea = new Window("Error");
                            errorTextArea.setModal(true);
                            addWindow(errorTextArea);
                            VerticalLayout error = new VerticalLayout(new Label("All fields must be filled in."));
                            errorTextArea.setContent(error);
                        }
                        else {
                            List<Recipe> recipes = recipeDAO.getAll();
                            for (Recipe recipe : recipes) {
                                if (recipe.getId() > maxId) maxId = recipe.getId();
                            }
                            id = maxId + 1;
                            Recipe recipe = new Recipe(id,
                                    descriptionTextFieldRecipe.getValue(),
                                    Long.parseLong(patientTextFieldRecipe.getValue()),
                                    Long.parseLong(doctorTextFieldRecipe.getValue()),
                                    Date.from(creationDateDateFieldRecipe.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                                    Date.from(validityDateFieldRecipe.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                                    priorityComboBoxRecipe.getValue());
                            recipeDAO.add(recipe);
                            recipeButton.click();
                            subWindowRecipe.close();
                        }
                    });

                    cancelButtonRecipe.addClickListener(click2 -> {
                        subWindowRecipe.close();
                    });

                    subWindowRecipe.center();
                    addWindow(subWindowRecipe);


                    break;
            }
        });

        changeButton.addClickListener(click -> {
            switch (focus) {
                case ("Patient"):
                    Window subWindowPatient = new Window("Change Patient");
                    subWindowPatient.setModal(true);
                    VerticalLayout changeLayoutPatient = new VerticalLayout();
                    VerticalLayout textAreaLayoutPatient = new VerticalLayout();
                    HorizontalLayout buttonLayoutPatient = new HorizontalLayout();
                    changeLayoutPatient.addComponent(textAreaLayoutPatient);
                    changeLayoutPatient.addComponent(buttonLayoutPatient);
                    subWindowPatient.setContent(changeLayoutPatient);


                    Set<Patient> selectedPatient = gridPatient.getSelectedItems();
                    if (!selectedPatient.isEmpty()) {
                        Patient patient = selectedPatient.iterator().next();

                        TextField nameTextFieldPatient = new TextField("Name", patient.getName());
                        TextField surnameTextFieldPatient = new TextField("Surname", patient.getSurname());
                        TextField lastnameTextFieldPatient = new TextField("Lastname", patient.getLastname());
                        TextField phoneTextFieldPatient = new TextField("Phone", patient.getPhone());

                        textAreaLayoutPatient.addComponent(new Label("Change attribute patient"));
                        textAreaLayoutPatient.addComponent(nameTextFieldPatient);
                        textAreaLayoutPatient.addComponent(surnameTextFieldPatient);
                        textAreaLayoutPatient.addComponent(lastnameTextFieldPatient);
                        textAreaLayoutPatient.addComponent(phoneTextFieldPatient);

                        Button okButtonPatient = new Button("Ok");
                        Button cancelButtonPatient = new Button("Cancel");
                        buttonLayoutPatient.addComponent(okButtonPatient);
                        buttonLayoutPatient.addComponent(cancelButtonPatient);
                        okButtonPatient.addClickListener(click2 -> {
                            String phone = phoneTextFieldPatient.getValue().replaceAll("[\\D]", "");
                            if (phone.length() < 9 || phone.length() > 11) {
                                Window errorTelephoneNumber = new Window("Error");
                                errorTelephoneNumber.setModal(true);
                                addWindow(errorTelephoneNumber);
                                VerticalLayout error = new VerticalLayout(new Label("Incorrect phone number."));
                                errorTelephoneNumber.setContent(error);
                            } else if(nameTextFieldPatient.isEmpty()|surnameTextFieldPatient.isEmpty()|lastnameTextFieldPatient.isEmpty()){
                                Window errorTextArea = new Window("Error");
                                errorTextArea.setModal(true);
                                addWindow(errorTextArea);
                                VerticalLayout error = new VerticalLayout(new Label("All fields must be filled in."));
                                errorTextArea.setContent(error);
                            }
                            else {
                                System.out.println("Start patient changing");
                                Patient patientForUpdate = new Patient(patient.getId(), nameTextFieldPatient.getValue(), surnameTextFieldPatient.getValue(),
                                        lastnameTextFieldPatient.getValue(), phoneTextFieldPatient.getValue());
                                patientDAO.update(patientForUpdate);
                                parientButton.click();
                                subWindowPatient.close();
                            }
                        });

                        cancelButtonPatient.addClickListener(click2 -> {
                            subWindowPatient.close();
                        });
                        subWindowPatient.center();

                        addWindow(subWindowPatient);
                    } else {
                        subWindowPatient.setCaption("Error");
                        VerticalLayout errorVerticalLayout = new VerticalLayout();
                        errorVerticalLayout.addComponent(new Label("Don't select change element"));
                        subWindowPatient.setContent(errorVerticalLayout);
                        subWindowPatient.center();
                        addWindow(subWindowPatient);
                    }
                    break;
                case ("Doctor"):
                    Window subWindowDoctor = new Window("Change Doctor");
                    subWindowDoctor.setModal(true);
                    VerticalLayout changeLayoutDoctor = new VerticalLayout();
                    VerticalLayout textAreaLayoutDoctor = new VerticalLayout();
                    HorizontalLayout buttonLayoutDoctor = new HorizontalLayout();
                    changeLayoutDoctor.addComponent(textAreaLayoutDoctor);
                    changeLayoutDoctor.addComponent(buttonLayoutDoctor);
                    subWindowDoctor.setContent(changeLayoutDoctor);


                    Set<Doctor> selectedDoctor = gridDoctor.getSelectedItems();
                    if (!selectedDoctor.isEmpty()) {
                        Doctor doctor = selectedDoctor.iterator().next();

                        TextField nameTextFieldDoctor = new TextField("Name", doctor.getName());
                        TextField surnameTextFieldDoctor = new TextField("Surname", doctor.getSurname());
                        TextField lastnameTextFieldDoctor = new TextField("Lastname", doctor.getLastname());
                        TextField specializationTextFieldDoctor = new TextField("Specialization", doctor.getSpecialization());

                        textAreaLayoutDoctor.addComponent(new Label("Change attribute doctor"));
                        textAreaLayoutDoctor.addComponent(nameTextFieldDoctor);
                        textAreaLayoutDoctor.addComponent(surnameTextFieldDoctor);
                        textAreaLayoutDoctor.addComponent(lastnameTextFieldDoctor);
                        textAreaLayoutDoctor.addComponent(specializationTextFieldDoctor);

                        Button okButtonDoctor = new Button("Ok");
                        Button cancelButtonDoctor = new Button("Cancel");
                        buttonLayoutDoctor.addComponent(okButtonDoctor);
                        buttonLayoutDoctor.addComponent(cancelButtonDoctor);
                        okButtonDoctor.addClickListener(click2 -> {
                            System.out.println("Start doctor changing");
                            if(nameTextFieldDoctor.isEmpty()|surnameTextFieldDoctor.isEmpty()|lastnameTextFieldDoctor.isEmpty()|specializationTextFieldDoctor.isEmpty()){
                                Window errorTextArea = new Window("Error");
                                errorTextArea.setModal(true);
                                addWindow(errorTextArea);
                                VerticalLayout error = new VerticalLayout(new Label("All fields must be filled in."));
                                errorTextArea.setContent(error);
                            }
                            else {
                                Doctor doctorForUpdate = new Doctor(doctor.getId(), nameTextFieldDoctor.getValue(), surnameTextFieldDoctor.getValue(),
                                        lastnameTextFieldDoctor.getValue(), specializationTextFieldDoctor.getValue());
                                doctorDAO.update(doctorForUpdate);
                                doctorButton.click();
                                subWindowDoctor.close();
                            }
                        });

                        cancelButtonDoctor.addClickListener(click2 -> {
                            subWindowDoctor.close();
                        });

                        subWindowDoctor.center();
                        addWindow(subWindowDoctor);
                    } else {
                        subWindowDoctor.setCaption("Error");
                        VerticalLayout errorVerticalLayout = new VerticalLayout();
                        errorVerticalLayout.addComponent(new Label("Don't select change element"));
                        subWindowDoctor.setContent(errorVerticalLayout);
                        subWindowDoctor.center();
                        addWindow(subWindowDoctor);
                    }
                    break;
                case ("Recipe"):
                    Window subWindowRecipe = new Window("Change Recipe");
                    subWindowRecipe.setModal(true);
                    VerticalLayout changeLayoutRecipe = new VerticalLayout();
                    VerticalLayout textAreaLayoutRecipe = new VerticalLayout();
                    HorizontalLayout buttonLayoutRecipe = new HorizontalLayout();
                    changeLayoutRecipe.addComponent(textAreaLayoutRecipe);
                    changeLayoutRecipe.addComponent(buttonLayoutRecipe);
                    subWindowRecipe.setContent(changeLayoutRecipe);


                    Set<Recipe> selectedRecipe = gridRecipe.getSelectedItems();
                    if (!selectedRecipe.isEmpty()) {
                        Recipe recipe = selectedRecipe.iterator().next();
                        TextField descriptionTextFieldRecipe = new TextField("description", recipe.getDescription());
                        TextField patientTextFieldRecipe = new TextField("patient_id", recipe.getPatient().toString());
                        TextField doctorTextFieldRecipe = new TextField("doctor_id", recipe.getDoctor().toString());
                        DateField creationDateDateFieldRecipe = new DateField("creationDate", Instant.ofEpochMilli(recipe.getCreationDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
                        DateField validityDateFieldRecipe = new DateField("validity", Instant.ofEpochMilli(recipe.getValidity().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
                        ComboBox<Priority> priorityComboBoxRecipe = new ComboBox("Priority", Arrays.asList(Priority.values()));
                        priorityComboBoxRecipe.setValue(recipe.getPriority());

                        textAreaLayoutRecipe.addComponent(new Label("Change attribute recipe"));
                        textAreaLayoutRecipe.addComponent(descriptionTextFieldRecipe);
                        textAreaLayoutRecipe.addComponent(patientTextFieldRecipe);
                        textAreaLayoutRecipe.addComponent(doctorTextFieldRecipe);
                        textAreaLayoutRecipe.addComponent(creationDateDateFieldRecipe);
                        textAreaLayoutRecipe.addComponent(validityDateFieldRecipe);
                        textAreaLayoutRecipe.addComponent(priorityComboBoxRecipe);

                        Button okButtonRecipe = new Button("Ok");
                        Button cancelButtonRecipe = new Button("Cancel");
                        buttonLayoutRecipe.addComponent(okButtonRecipe);
                        buttonLayoutRecipe.addComponent(cancelButtonRecipe);
                        okButtonRecipe.addClickListener(click2 -> {
                            System.out.println("Start Recipe changing");
                            if(descriptionTextFieldRecipe.isEmpty()|priorityComboBoxRecipe.getValue()==null){
                                Window errorTextArea = new Window("Error");
                                errorTextArea.setModal(true);
                                addWindow(errorTextArea);
                                VerticalLayout error = new VerticalLayout(new Label("All fields must be filled in."));
                                errorTextArea.setContent(error);
                            }
                            else {
                                Recipe recipeForUpdate = new Recipe(recipe.getId(),
                                        descriptionTextFieldRecipe.getValue(),
                                        Long.parseLong(patientTextFieldRecipe.getValue()),
                                        Long.parseLong(doctorTextFieldRecipe.getValue()),
                                        Date.from(creationDateDateFieldRecipe.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                                        Date.from(validityDateFieldRecipe.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                                        priorityComboBoxRecipe.getValue());
                                recipeDAO.update(recipeForUpdate);
                                recipeButton.click();
                                subWindowRecipe.close();
                            }
                        });

                        cancelButtonRecipe.addClickListener(click2 -> {
                            subWindowRecipe.close();
                        });

                        subWindowRecipe.center();
                        addWindow(subWindowRecipe);
                    } else {
                        subWindowRecipe.setCaption("Error");
                        VerticalLayout errorVerticalLayout = new VerticalLayout();
                        errorVerticalLayout.addComponent(new Label("Don't select change element"));
                        subWindowRecipe.setContent(errorVerticalLayout);
                        subWindowRecipe.center();
                        addWindow(subWindowRecipe);
                    }

                    break;
            }

        });

        deleteButton.addClickListener(click -> {
            switch (focus) {
                case ("Patient"):
                    Window subWindowPatient = new Window("Error delete patient");
                    subWindowPatient.setModal(true);
                    Set<Patient> selectedPatient = gridPatient.getSelectedItems();
                    if (!selectedPatient.isEmpty()) {
                            patientDAO.delete(selectedPatient.iterator().next().getId());
                            parientButton.click();
                    }else{
                        VerticalLayout errorVerticalLayout = new VerticalLayout();
                        errorVerticalLayout.addComponent(new Label("Don't select change element"));
                        subWindowPatient.setContent(errorVerticalLayout);
                        subWindowPatient.center();
                        addWindow(subWindowPatient);
                    }
                    break;
                case ("Doctor"):
                    Window subWindowDoctor = new Window("Error delete doctor");
                    subWindowDoctor.setModal(true);
                    Set<Doctor> selectedDoctor = gridDoctor.getSelectedItems();
                    if (!selectedDoctor.isEmpty()) {
                        doctorDAO.delete(selectedDoctor.iterator().next().getId());
                        doctorButton.click();
                    }
                    else{
                        VerticalLayout errorVerticalLayout = new VerticalLayout();
                        errorVerticalLayout.addComponent(new Label("Don't select change element"));
                        subWindowDoctor.setContent(errorVerticalLayout);
                        subWindowDoctor.center();
                        addWindow(subWindowDoctor);
                    }
                    break;
                case ("Recipe"):
                    Window subWindowRecipe = new Window("Error delete recipe");
                    subWindowRecipe.setModal(true);
                    Set<Recipe> selectedRecipe = gridRecipe.getSelectedItems();
                    if (!selectedRecipe.isEmpty()) {
                        recipeDAO.delete(selectedRecipe.iterator().next().getId());
                        recipeButton.click();
                    }
                    else{
                        VerticalLayout errorVerticalLayout = new VerticalLayout();
                        errorVerticalLayout.addComponent(new Label("Don't select change element"));
                        subWindowRecipe.setContent(errorVerticalLayout);
                        subWindowRecipe.center();
                        addWindow(subWindowRecipe);
                    }
                    break;
            }
        });

        viewStatisticsButton.addClickListener(click -> {
            switch (focus) {
                case ("Patient"):
                    viewStatisticsButton.setVisible(false);
                    break;
                case ("Doctor"):
                    viewStatisticsButton.setVisible(true);
                    HashMap<String, String> statistics = doctorDAO.getStatistic();
                    Window subWindowStatistic = new Window("Statistic");
                    VerticalLayout statisticLayout = new VerticalLayout();
                    subWindowStatistic.setModal(true);
                    subWindowStatistic.setContent(statisticLayout);
                    for (Map.Entry<String, String> entry : statistics.entrySet()) {
                        HorizontalLayout statisticRowLayout = new HorizontalLayout();
                        Label doctorName = new Label("doctor: " + entry.getKey());
                        Label recipeCount = new Label(" count recipes: " + entry.getValue());
                        statisticRowLayout.addComponent(doctorName);
                        statisticRowLayout.addComponent(recipeCount);
                        statisticLayout.addComponent(statisticRowLayout);
                    }

                    doctorButton.click();
                    subWindowStatistic.center();
                    addWindow(subWindowStatistic);
                    break;
                case ("Recipe"):
                    viewStatisticsButton.setVisible(false);
                    break;
            }
        });

    }
}