package com.haulmont.testtask.dao;

import com.haulmont.testtask.utils.DbUtils;
import com.haulmont.testtask.model.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DoctorDAO implements AbstractDAO<Doctor>{

    private static final String CREATE_DOCTOR_TABLE =
            "create table doctor (\r\n" +
                    "  id  bigint primary key,\r\n" +
                    "  name varchar(20),\r\n" +
                    "  surname varchar(20),\r\n" +
                    "  lastname varchar(20),\r\n" +
                    "  specialization varchar(20)\r\n" +
                    "  );";

    private static final String INSERT_DOCTOR_SQL = "INSERT INTO Doctor" +
            "  (id, name, surname, lastname, specialization) VALUES " +
            " (?, ?, ?, ?, ?)";
    private static final String DELETE_DOCTOR_SQL = "delete from Doctor where id = ?";

    private static final String UPDATE_DOCTOR_SQL = "update doctor set name=?, surname=?, lastname=?, specialization=? where id = ?";

    private static final String GET_ALL_DOCTORS = "select id, name, surname, lastname, specialization from Doctor";

    private static final String GET_STATISCIKS_DOCTOR = "Select count(*) as recipe_count, doctor.name from doctor,recipe where doctor.id =recipe.doctor_id group by doctor.id";

    public void add(Doctor doctor) {
        try (Connection connection = DbUtils.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DOCTOR_SQL)) {
            connection.setAutoCommit(true);

            preparedStatement.setLong(1, doctor.getId());
            preparedStatement.setString(2, doctor.getName());
            preparedStatement.setString(3, doctor.getSurname());
            preparedStatement.setString(4, doctor.getLastname());
            preparedStatement.setString(5, doctor.getSpecialization());

            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            DbUtils.printSQLException(e);
        }
    }

    public void delete(Long id) {
        try (Connection connection = DbUtils.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(DELETE_DOCTOR_SQL)) {
            connection.setAutoCommit(true);

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            DbUtils.printSQLException(e);
        }
    }

    public void update(Doctor doctor) {
        try (Connection connection = DbUtils.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DOCTOR_SQL)) {
            connection.setAutoCommit(true);

            preparedStatement.setString(1, doctor.getName());
            preparedStatement.setString(2, doctor.getSurname());
            preparedStatement.setString(3, doctor.getLastname());
            preparedStatement.setString(4, doctor.getSpecialization());
            preparedStatement.setLong(5, doctor.getId());

            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            DbUtils.printSQLException(e);
        }
    }

    public HashMap<String,String> getStatistic(){
        HashMap<String,String> countDoctorRecipe = new HashMap<>();
        try (Connection connection = DbUtils.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(GET_STATISCIKS_DOCTOR)){
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("name")+" "+rs.getString("recipe_count"));
                countDoctorRecipe.put(rs.getString("name"),rs.getString("recipe_count"));
            }

            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return countDoctorRecipe;
    }
    public void createTable() {
        System.out.println("createTable started");
        try (Connection connection = DbUtils.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_DOCTOR_TABLE);
        } catch (SQLException e) {
            DbUtils.printSQLException(e);
        }
    }

    public List<Doctor> getAll() {
        List<Doctor> doctors = new ArrayList<>();
        try (Connection connection = DbUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_DOCTORS)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String lastname = rs.getString("lastname");
                String specialization = rs.getString("specialization");

                Doctor doctor = new Doctor(id, name, surname, lastname, specialization);
                doctors.add(doctor);
            }

        } catch (SQLException e) {
            DbUtils.printSQLException(e);
        }
        return doctors;
    }
}
