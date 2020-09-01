package com.haulmont.testtask.dao;

import com.haulmont.testtask.utils.DbUtils;
import com.haulmont.testtask.model.Priority;
import com.haulmont.testtask.model.Recipe;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecipeDAO implements AbstractDAO<Recipe> {
    private static final String CREATE_RECIPES_TABLE =
            "create table recipe (\r\n" +
                    "  id  bigint primary key,\r\n" +
                    "  description varchar(20),\r\n" +
                    "  patient_id bigint,\r\n" +
                    "  doctor_id bigint,\r\n" +
                    "  creationDate timestamp,\r\n" +
                    "  validity timestamp,\r\n" +
                    "  priority varchar(20)\r\n" +
                    "  );";

    private static final String ALTER_RECIPES_TABLE1 ="alter table recipe " +
            "add FOREIGN KEY (patient_id) REFERENCES patients(id)";

    private static final String ALTER_RECIPES_TABLE2 ="alter table recipe " +
            "add FOREIGN KEY (doctor_id) REFERENCES doctor (id)";

    private static final String INSERT_RECIPES_SQL = "INSERT INTO RECIPE" +
            "  (id, description, patient_id, doctor_id, creationDate, validity, priority) VALUES " +
            " (?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_RECIPE_SQL  = "delete from recipe where id = ?";

    private static final String UPDATE_RECIPE_SQL = "update recipe set description=?, patient_id=?, doctor_id=?, creationDate=?, validity=?, priority=? where id = ?";

    private static final String GET_ALL_RECIPES = "select id, description, patient_id, doctor_id, creationDate, validity, priority from recipe";

    private static final String GET_FILTERED_PATIENT = "select id, description, patient_id, doctor_id, creationDate, validity, priority from recipe where patient_id= ?";
    private static final String GET_FILTERED_PRIORITY = "select id, description, patient_id, doctor_id, creationDate, validity, priority from recipe where priority = ?";
    private static final String GET_FILTERED_DESCRIPRION = "select id, description, patient_id, doctor_id, creationDate, validity, priority from recipe where description like CONCAT('%',?,'%')";

    public void add(Recipe recipe) {
        try (Connection connection = DbUtils.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_RECIPES_SQL)) {
            connection.setAutoCommit(true);

            preparedStatement.setLong(1, recipe.getId());
            preparedStatement.setString(2, recipe.getDescription());
            preparedStatement.setLong(3, recipe.getPatient());
            preparedStatement.setLong(4, recipe.getDoctor());
            preparedStatement.setDate(5, new java.sql.Date(recipe.getCreationDate().getTime()));
            preparedStatement.setDate(6, new java.sql.Date(recipe.getValidity().getTime()));
            preparedStatement.setString(7, recipe.getPriority().toString());

            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            DbUtils.printSQLException(e);
        }
    }

    public void delete(Long id) {
        try (Connection connection = DbUtils.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(DELETE_RECIPE_SQL)) {
            connection.setAutoCommit(true);

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            DbUtils.printSQLException(e);
        }
    }

    public void update(Recipe recipe) {
        try (Connection connection = DbUtils.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_RECIPE_SQL)) {
            connection.setAutoCommit(true);


            preparedStatement.setString(1, recipe.getDescription());
            preparedStatement.setLong(2, recipe.getPatient());
            preparedStatement.setLong(3, recipe.getDoctor());
            preparedStatement.setDate(4, new java.sql.Date(recipe.getCreationDate().getTime()));
            preparedStatement.setDate(5, new java.sql.Date(recipe.getValidity().getTime()));
            preparedStatement.setString(6, recipe.getPriority().toString());
            preparedStatement.setLong(7, recipe.getId());


            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            DbUtils.printSQLException(e);
        }
    }


    public void createTable() {
        try (Connection connection = DbUtils.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_RECIPES_TABLE);
        } catch (SQLException e) {
            DbUtils.printSQLException(e);
        }
    }

    public List<Recipe> getFilteredRecipeByIdPatient(long filterId){
        List<Recipe> recipes = new ArrayList<>();
        try (Connection connection = DbUtils.getConnection();PreparedStatement preparedStatement = connection.prepareStatement(GET_FILTERED_PATIENT)) {
            preparedStatement.setLong(1, filterId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                String description = rs.getString("description");
                long patientId = rs.getLong("patient_id");
                long doctorId = rs.getLong("doctor_id");
                Date creationDate = rs.getDate("creationDate");
                Date validity = rs.getDate("validity");
                String priority = rs.getString("priority");

                Recipe recipe = new Recipe(id, description, patientId, doctorId, creationDate, validity, Priority.valueOf(priority));
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }
    public List<Recipe> getFilteredRecipeByPriority(String filterPriority){
        List<Recipe> recipes = new ArrayList<>();
        try (Connection connection = DbUtils.getConnection();PreparedStatement preparedStatement = connection.prepareStatement(GET_FILTERED_PRIORITY)) {
            preparedStatement.setString(1, filterPriority);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                String description = rs.getString("description");
                long patientId = rs.getLong("patient_id");
                long doctorId = rs.getLong("doctor_id");
                Date creationDate = rs.getDate("creationDate");
                Date validity = rs.getDate("validity");
                String priority = rs.getString("priority");

                Recipe recipe = new Recipe(id, description, patientId, doctorId, creationDate, validity, Priority.valueOf(priority));
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }
    public List<Recipe> getFilteredRecipetByDescription(String filterDescription){
        List<Recipe> recipes = new ArrayList<>();
        try (Connection connection = DbUtils.getConnection();PreparedStatement preparedStatement = connection.prepareStatement(GET_FILTERED_DESCRIPRION)) {
            preparedStatement.setString(1, filterDescription);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                String description = rs.getString("description");
                long patientId = rs.getLong("patient_id");
                long doctorId = rs.getLong("doctor_id");
                Date creationDate = rs.getDate("creationDate");
                Date validity = rs.getDate("validity");
                String priority = rs.getString("priority");

                Recipe recipe = new Recipe(id, description, patientId, doctorId, creationDate, validity, Priority.valueOf(priority));
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    public void alterTable() {
        try (Connection connection = DbUtils.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(ALTER_RECIPES_TABLE1);

            statement.execute(ALTER_RECIPES_TABLE2);
        } catch (SQLException e) {
            DbUtils.printSQLException(e);
        }
    }

    public List<Recipe> getAll() {
        List<Recipe> recipes = new ArrayList<>();
        try (Connection connection = DbUtils.getConnection();PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_RECIPES)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("id");
                String description = rs.getString("description");
                long patientId = rs.getLong("patient_id");
                long doctorId = rs.getLong("doctor_id");
                Date creationDate = rs.getDate("creationDate");
                Date validity = rs.getDate("validity");
                String priority = rs.getString("priority");

                Recipe recipe = new Recipe(id, description, patientId, doctorId, creationDate, validity, Priority.valueOf(priority));
                recipes.add(recipe);
            }

        } catch (SQLException e) {
            DbUtils.printSQLException(e);
        }
        return recipes;
    }
}
