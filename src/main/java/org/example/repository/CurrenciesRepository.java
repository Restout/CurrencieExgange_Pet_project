package org.example.repository;

import org.example.exceptions.Validator;
import org.example.model.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrenciesRepository implements CrudRepository<Currency> {
    private final String SELECT_ALL = "SELECT * FROM Currencies";
    private final String SELECT_BY_CODE = "SELECT * FROM Currencies WHERE Code LIKE ?";
    private final String INSERT = "INSERT INTO Currencies (ID,Code,Fullname,Sign) VALUES (?,?,?,?)";


    @Override
    public List<Currency> getAll() {
        List<Currency> result = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(SELECT_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(Currency
                        .builder()
                        .id(resultSet.getInt("ID"))
                        .code(resultSet.getString("Code"))
                        .fullName(resultSet.getString("FullName"))
                        .sign(resultSet.getString("Sign"))
                        .build());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }
    public Optional<Currency> getCurrencyByCode(String code) {
        Optional<Currency> result = Optional.empty();
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(SELECT_BY_CODE);
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = Optional.of(Currency
                        .builder()
                        .id(resultSet.getInt("ID"))
                        .code(resultSet.getString("Code"))
                        .fullName(resultSet.getString("FullName"))
                        .sign(resultSet.getString("Sign"))
                        .build());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    @Override
    public void create(Currency currency) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setInt(1, currency.getId());
            preparedStatement.setString(2, currency.getCode());
            preparedStatement.setString(3, currency.getFullName());
            preparedStatement.setString(4, currency.getSign());
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Record inserted successfully");

            }
        } catch (Exception e) {
            Validator.validateException(e.getMessage());
        }

    }
    @Override
    public void update(Currency object) {

    }

    @Override
    public void delete(Currency object) {

    }
}
