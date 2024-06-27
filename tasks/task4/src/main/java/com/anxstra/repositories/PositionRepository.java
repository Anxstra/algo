package com.anxstra.repositories;

import com.anxstra.connection.ConnectionPool;
import com.anxstra.entities.Position;
import com.anxstra.exceptions.DatabaseAccessException;
import com.anxstra.mappers.PositionMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.anxstra.constants.CommonConstant.FIRST_PARAM_POSITION;
import static com.anxstra.constants.CommonConstant.SQL_EXCEPTION_MESSAGE;
import static com.anxstra.constants.PositionQueryConstants.DELETE_QUERY;
import static com.anxstra.constants.PositionQueryConstants.GET_ALL_QUERY;
import static com.anxstra.constants.PositionQueryConstants.GET_BY_ID_QUERY;
import static com.anxstra.constants.PositionQueryConstants.SAVE_QUERY;
import static com.anxstra.constants.PositionQueryConstants.UPDATE_QUERY;

public class PositionRepository implements CRUDRepository<Integer, Position> {

    private final ConnectionPool connectionPool;

    private final PositionMapper rowMapper;

    public PositionRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.rowMapper = new PositionMapper();
    }

    @Override
    public Optional<Position> getById(Integer id) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID_QUERY)) {
            statement.setInt(FIRST_PARAM_POSITION, id);
            ResultSet resultSet = statement.executeQuery();
            Optional<Position> position = Optional.empty();
            if (resultSet.next()) {
                position = Optional.of(rowMapper.mapRow(resultSet));
            }
            return position;
        } catch (SQLException e) {
            throw new DatabaseAccessException(SQL_EXCEPTION_MESSAGE.formatted(e.getMessage()));
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<Position> getAll() {
        Connection connection = connectionPool.getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_QUERY);
            List<Position> positions = new ArrayList<>();
            while (resultSet.next()) {
                positions.add(rowMapper.mapRow(resultSet));
            }
            return positions;
        } catch (SQLException e) {
            throw new DatabaseAccessException(SQL_EXCEPTION_MESSAGE.formatted(e.getMessage()));
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void save(Position entity) {
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
    public void update(Position entity) {
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

    private void prepareStatement(PreparedStatement statement, Position position, boolean isUpdate) throws SQLException {
        int paramPos = FIRST_PARAM_POSITION;
        statement.setString(paramPos++, position.getName());
        statement.setBigDecimal(paramPos++, position.getSalary());
        if (isUpdate) {
            statement.setInt(paramPos, position.getId());
        }
    }
}
