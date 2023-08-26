package org.example.repository;

import org.example.model.Currency;
import org.example.model.ExcnahgeRate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateRepository {
    DataSource dataSource = org.example.DataSource.getDataSource();

    public List<ExcnahgeRate> getListOfExchangeRates() {
        String query = "SELECT *FROM ExchangeRate " +
                "JOIN Currencies c ON c.ID=BaseCurrencyId " +
                "JOIN Currencies c2 ON c2.ID=TargetCurrencyId;";
        List<ExcnahgeRate> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                result.add(ExcnahgeRate
                        .builder()
                        .id(resultSet.getInt(1))
                        .rate(resultSet.getBigDecimal(2))
                        .baseCurrency(Currency
                                .builder()
                                .id(resultSet.getInt(3))
                                .code(resultSet.getString(6))
                                .fullName(resultSet.getString(6))
                                .sign(resultSet.getString(8))
                                .build())
                        .targetCurrency(Currency
                                .builder()
                                .id(resultSet.getInt(4))
                                .code(resultSet.getString(10))
                                .fullName(resultSet.getString(11))
                                .sign(resultSet.getString(12))
                                .build())
                        .build());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
}
