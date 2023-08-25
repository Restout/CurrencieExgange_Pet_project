package org.example.repository;

import org.example.DataSource;
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

    public Optional<Currency> setNewCurrency(Currency currency) {
        return null;
    }
}
