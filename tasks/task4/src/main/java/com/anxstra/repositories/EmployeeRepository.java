package com.anxstra.repositories;

import com.anxstra.connection.ConnectionPool;
import com.anxstra.entities.Employee;
import com.anxstra.exceptions.DatabaseAccessException;
import com.anxstra.mappers.EmployeeMapper;

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
import static com.anxstra.constants.EmployeeQueryConstants.DELETE_QUERY;
import static com.anxstra.constants.EmployeeQueryConstants.GET_ALL_QUERY;
import static com.anxstra.constants.EmployeeQueryConstants.GET_BY_ID_QUERY;
import static com.anxstra.constants.EmployeeQueryConstants.SAVE_QUERY;
import static com.anxstra.constants.EmployeeQueryConstants.UPDATE_QUERY;

public class EmployeeRepository implements CRUDRepository<Integer, Employee> {

    private final ConnectionPool connectionPool;

    private final EmployeeMapper rowMapper;

    public EmployeeRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.rowMapper = new EmployeeMapper();
    }

    @Override
    public Optional<Employee> getById(Integer id) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID_QUERY)) {
            statement.setInt(FIRST_PARAM_POSITION, id);
            ResultSet resultSet = statement.executeQuery();
            Optional<Employee> employee = Optional.empty();
            if (resultSet.next()) {
                employee = Optional.of(rowMapper.mapRow(resultSet));
            }
            return employee;
        } catch (SQLException e) {
            throw new DatabaseAccessException(SQL_EXCEPTION_MESSAGE.formatted(e.getMessage()));
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<Employee> getAll() {
        Connection connection = connectionPool.getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_QUERY);
            List<Employee> employees = new ArrayList<>();
            while (resultSet.next()) {
                employees.add(rowMapper.mapRow(resultSet));
            }
            return employees;
        } catch (SQLException e) {
            throw new DatabaseAccessException(SQL_EXCEPTION_MESSAGE.formatted(e.getMessage()));
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void save(Employee entity) {
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
    public void update(Employee entity) {
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

    private void prepareStatement(PreparedStatement statement, Employee employee, boolean isUpdate) throws SQLException {
        int paramPos = FIRST_PARAM_POSITION;
        statement.setString(paramPos++, employee.getFio());
        statement.setString(paramPos++, employee.getPhoneNumber());
        statement.setInt(paramPos++, employee.getPositionId());
        statement.setDate(paramPos++, Date.valueOf(employee.getHireDate()));
        if (isUpdate) {
            statement.setInt(paramPos, employee.getId());
        }
    }
}
