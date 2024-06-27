package com.anxstra.repositories;

import com.anxstra.connection.ConnectionPool;
import com.anxstra.entities.Prescription;
import com.anxstra.exceptions.DatabaseAccessException;
import com.anxstra.mappers.PrescriptionMapper;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.anxstra.constants.CommonConstant.FIRST_PARAM_POSITION;
import static com.anxstra.constants.CommonConstant.SQL_EXCEPTION_MESSAGE;
import static com.anxstra.constants.PrescriptionQueryConstants.DELETE_QUERY;
import static com.anxstra.constants.PrescriptionQueryConstants.GET_ALL_QUERY;
import static com.anxstra.constants.PrescriptionQueryConstants.GET_BY_ID_QUERY;
import static com.anxstra.constants.PrescriptionQueryConstants.SAVE_QUERY;
import static com.anxstra.constants.PrescriptionQueryConstants.UPDATE_QUERY;

public class PrescriptionRepository implements CRUDRepository<Integer, Prescription> {

    private final ConnectionPool connectionPool;

    private final PrescriptionMapper rowMapper;

    public PrescriptionRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.rowMapper = new PrescriptionMapper();
    }

    @Override
    public Optional<Prescription> getById(Integer id) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID_QUERY)) {
            statement.setInt(FIRST_PARAM_POSITION, id);
            ResultSet resultSet = statement.executeQuery();
            Optional<Prescription> prescription = Optional.empty();
            if (resultSet.next()) {
                prescription = Optional.of(rowMapper.mapRow(resultSet));
            }
            return prescription;
        } catch (SQLException e) {
            throw new DatabaseAccessException(SQL_EXCEPTION_MESSAGE.formatted(e.getMessage()));
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<Prescription> getAll() {
        Connection connection = connectionPool.getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_QUERY);
            List<Prescription> prescriptions = new ArrayList<>();
            while (resultSet.next()) {
                prescriptions.add(rowMapper.mapRow(resultSet));
            }
            return prescriptions;
        } catch (SQLException e) {
            throw new DatabaseAccessException(SQL_EXCEPTION_MESSAGE.formatted(e.getMessage()));
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void save(Prescription entity) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement(statement, entity, false);
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                entity.setId(resultSet.getInt(FIRST_PARAM_POSITION));
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException(SQL_EXCEPTION_MESSAGE.formatted(e.getMessage()));
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void update(Prescription entity) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            prepareStatement(statement, entity, true);
            statement.execute();
        } catch (SQLException e) {
            throw new DatabaseAccessException(SQL_EXCEPTION_MESSAGE.formatted(e.getMessage()));
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void deleteById(Integer id) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(FIRST_PARAM_POSITION, id);
            statement.execute();
        } catch (SQLException e) {
            throw new DatabaseAccessException(SQL_EXCEPTION_MESSAGE.formatted(e.getMessage()));
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private void prepareStatement(PreparedStatement statement, Prescription prescription, boolean isUpdate) throws SQLException {
        int paramPos = FIRST_PARAM_POSITION;
        statement.setInt(paramPos++, prescription.getDoctorId());
        statement.setInt(paramPos++, prescription.getPatientId());
        statement.setString(paramPos++, prescription.getDescription());
        statement.setDate(paramPos++, Date.valueOf(prescription.getIssueDate()));
        statement.setDate(paramPos++, Date.valueOf(prescription.getExpirationDate()));
        if (isUpdate) {
            statement.setInt(paramPos, prescription.getId());
        }
    }
}
