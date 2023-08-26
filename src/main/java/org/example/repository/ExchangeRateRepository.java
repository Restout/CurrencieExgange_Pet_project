package org.example.repository;

import org.example.dto.ExchangeRateDTO;
import org.example.exceptions.Validator;
import org.example.model.Currency;
import org.example.model.ExchangeRate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateRepository {
    DataSource dataSource = org.example.DataSource.getDataSource();

    public List<ExchangeRate> getListOfExchangeRates() {
        String query = "SELECT *FROM ExchangeRate " +
                "JOIN Currencies c ON c.ID=BaseCurrencyId " +
                "JOIN Currencies c2 ON c2.ID=TargetCurrencyId;";
        List<ExchangeRate> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                result.add(ExchangeRate
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

    public Optional<ExchangeRate> getExchangeRatByBaseAndTargetCurrencies(String base, String target) {
        String query = "SELECT *FROM ExchangeRate er\n" +
                "JOIN Currencies c ON c.ID=er.BaseCurrencyId\n" +
                "JOIN Currencies c2 ON c2.ID=er.TargetCurrencyId\n" +
                "WHERE c.Code LIKE '" + base + "' AND c2.Code LIKE '" + target + "'; \n";
        Optional<ExchangeRate> result = Optional.empty();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            result = Optional.of(ExchangeRate
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public Optional<ExchangeRate> setNewExchangeRate(ExchangeRateDTO exchangeRateDTO) throws SQLException {
        String query = "INSERT INTO ExchangeRate (BaseCurrencyId,TargetCurrencyId,Rate) VALUES( " +
                "(SELECT ID FROM Currencies WHERE Code LIKE '" + exchangeRateDTO.getBaseCode() + "'), " +
                "(SELECT ID FROM Currencies WHERE Code LIKE '" + exchangeRateDTO.getTargetCode() + "'), "
                + exchangeRateDTO.getRate() + ")";
        Optional<ExchangeRate> result = Optional.empty();

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
            result = getExchangeRatByBaseAndTargetCurrencies(exchangeRateDTO.getBaseCode(), exchangeRateDTO.getTargetCode());
        } catch (SQLException e) {
            Validator.validateException(e.getMessage());
        }
        return result;
    }

    public Optional<ExchangeRate> setNewRateToExistExchangeRate(ExchangeRateDTO exchangeRateDTO) throws SQLException {
        String query = "UPDATE ExchangeRate" +
                " SET Rate=" + exchangeRateDTO.getRate() +
                " WHERE BaseCurrencyId=" +
                "(SELECT Currencies.ID FROM Currencies WHERE CODE LIKE '" + exchangeRateDTO.getBaseCode() + "') " +
                "AND TargetCurrencyId=" +
                "(SELECT Currencies.ID FROM Currencies WHERE CODE LIKE '" + exchangeRateDTO.getTargetCode() + "')";
        Optional<ExchangeRate> result = Optional.empty();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
            result = getExchangeRatByBaseAndTargetCurrencies(exchangeRateDTO.getBaseCode(), exchangeRateDTO.getTargetCode());
        } catch (SQLException e) {
            Validator.validateException(e.getMessage());
        }
        return result;
    }
}
