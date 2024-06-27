package com.anxstra.repositories;

import com.anxstra.connection.ConnectionPool;
import com.anxstra.entities.AppointmentType;
import com.anxstra.exceptions.DatabaseAccessException;
import com.anxstra.mappers.AppointmentTypeMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.anxstra.constants.AppointmentTypeQueryConstants.DELETE_QUERY;
import static com.anxstra.constants.AppointmentTypeQueryConstants.GET_ALL_QUERY;
import static com.anxstra.constants.AppointmentTypeQueryConstants.GET_BY_ID_QUERY;
import static com.anxstra.constants.AppointmentTypeQueryConstants.SAVE_QUERY;
import static com.anxstra.constants.AppointmentTypeQueryConstants.UPDATE_QUERY;
import static com.anxstra.constants.CommonConstant.FIRST_PARAM_POSITION;
import static com.anxstra.constants.CommonConstant.SQL_EXCEPTION_MESSAGE;

public class AppointmentTypeRepository implements CRUDRepository<Integer, AppointmentType> {

    private final ConnectionPool connectionPool;

    private final AppointmentTypeMapper rowMapper;

    public AppointmentTypeRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.rowMapper = new AppointmentTypeMapper();
    }

    @Override
    public Optional<AppointmentType> getById(Integer id) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID_QUERY)) {
            statement.setInt(FIRST_PARAM_POSITION, id);
            ResultSet resultSet = statement.executeQuery();
            Optional<AppointmentType> appointmentType = Optional.empty();
            if (resultSet.next()) {
                appointmentType = Optional.of(rowMapper.mapRow(resultSet));
            }
            return appointmentType;
        } catch (SQLException e) {
            throw new DatabaseAccessException(SQL_EXCEPTION_MESSAGE.formatted(e.getMessage()));
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<AppointmentType> getAll() {
        Connection connection = connectionPool.getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_QUERY);
            List<AppointmentType> appointmentTypes = new ArrayList<>();
            while (resultSet.next()) {
                appointmentTypes.add(rowMapper.mapRow(resultSet));
            }
            return appointmentTypes;
        } catch (SQLException e) {
            throw new DatabaseAccessException(SQL_EXCEPTION_MESSAGE.formatted(e.getMessage()));
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void save(AppointmentType entity) {
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
    public void update(AppointmentType entity) {
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

    private void prepareStatement(PreparedStatement statement, AppointmentType appointmentType, boolean isUpdate) throws SQLException {
        int paramPos = FIRST_PARAM_POSITION;
        statement.setString(paramPos++, appointmentType.getName());
        if (isUpdate) {
            statement.setInt(paramPos, appointmentType.getId());
        }
    }
}
