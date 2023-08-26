package org.example.repository;

import org.example.DataSource;
import org.example.exceptions.UniqueConstraintException;
import org.example.exceptions.Validator;
import org.example.model.Currency;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrenciesRepository {
    SQLiteDataSource dataSource = DataSource.getDataSource();

    public List<Currency> getCurrenciesList() {

        List<Currency> result = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            String query = "SELECT * FROM Currencies";
            PreparedStatement preparedStatement = con.prepareStatement(query);
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
        Optional<Currency> result;
        try (Connection con = dataSource.getConnection()) {
            String query = "SELECT * FROM Currencies WHERE Code LIKE '" + code + "'";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            result = Optional.of(Currency
                    .builder()
                    .id(resultSet.getInt("ID"))
                    .code(resultSet.getString("Code"))
                    .fullName(resultSet.getString("FullName"))
                    .sign(resultSet.getString("Sign"))
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    public boolean setNewCurrency(Currency currency) throws UniqueConstraintException {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ")
                .append("Currencies ")
                .append("(ID,Code,FullName,Sign)")
                .append("VALUES ")
                .append("(?,?,?,?)");
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
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
        return true;
    }
}
