package com.haulmont.testtask.dao;

import com.haulmont.testtask.utils.DbUtils;
import com.haulmont.testtask.model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO implements AbstractDAO<Patient> {

    private static final String CREATE_PATIENT_TABLE =
            "create table patients (\r\n" +
                    "  id  bigint primary key,\r\n" +
                    "  name varchar(20),\r\n" +
                    "  surname varchar(20),\r\n" +
                    "  lastname varchar(20),\r\n" +
                    "  phone varchar(20)\r\n" +
                    "  );";

    private static final String INSERT_PATIENT_SQL = "INSERT INTO patients" +
            "  (id, name, surname, lastname, phone) VALUES " +
            " (?, ?, ?, ?, ?)";

    private static final String GET_ALL_PATIENTS = "select id, name, surname, lastname, phone from patients";
    private static final String DELETE_PATIENT_SQL = "delete from patients where id = ?";
    private static final String UPDATE_PATIENT_SQL = "update patients set name=?,surname=?,lastname=?, phone=? where id = ?";

    public void add(Patient patient) {
        try (Connection connection = DbUtils.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PATIENT_SQL)) {
            connection.setAutoCommit(true);

            preparedStatement.setLong(1, patient.getId());
            preparedStatement.setString(2, patient.getName());
            preparedStatement.setString(3, patient.getSurname());
            preparedStatement.setString(4, patient.getLastname());
            preparedStatement.setString(5, patient.getPhone());

            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            DbUtils.printSQLException(e);
        }
    }

    public void update(Patient patient) {
        try (Connection connection = DbUtils.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PATIENT_SQL)) {
            connection.setAutoCommit(true);

            preparedStatement.setString(1, patient.getName());
            preparedStatement.setString(2, patient.getSurname());
            preparedStatement.setString(3, patient.getLastname());
            preparedStatement.setString(4, patient.getPhone());
            preparedStatement.setLong(5, patient.getId());

            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            DbUtils.printSQLException(e);
        }
    }

    public void delete(Long id) {
        try (Connection connection = DbUtils.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PATIENT_SQL)) {
            connection.setAutoCommit(true);

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            DbUtils.printSQLException(e);
        }
    }

    public void createTable() {
        System.out.println("createTable started");
        try (Connection connection = DbUtils.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_PATIENT_TABLE);
        } catch (SQLException e) {
            DbUtils.printSQLException(e);
        }
    }

    public List<Patient> getAll() {
        List<Patient> patients = new ArrayList<>();
        try (Connection connection = DbUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_PATIENTS)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String lastname = rs.getString("lastname");
                String phone = rs.getString("phone");

                Patient patient = new Patient(id, name, surname, lastname, phone);
                patients.add(patient);
            }

        } catch (SQLException e) {
            DbUtils.printSQLException(e);
        }
        return patients;
    }
}
