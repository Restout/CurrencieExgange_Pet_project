package org.example.repository;

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

public class ExchangeRateRepository implements CrudRepository<ExchangeRate> {
    private final String SELECT_ALL = "SELECT *FROM ExchangeRate " +
            "JOIN Currencie c ON c.ID=BaseCurrencyId " +
            "JOIN Currencie c2 ON c2.ID=TargetCurrencyId;";
    private final String SELECT_BY_CODES = "SELECT *FROM ExchangeRate er\n" +
            "JOIN Currencie c ON c.ID=er.BaseCurrencyId\n" +
            "JOIN Currencie c2 ON c2.ID=er.TargetCurrencyId\n" +
            "WHERE c.Code LIKE ? AND c2.Code LIKE ?; \n";
    private final String CREATE = "INSERT INTO ExchangeRate (BaseCurrencyId,TargetCurrencyId,Rate) VALUES( " +
            "(SELECT ID FROM Currencie WHERE Code LIKE ?), " +
            "(SELECT ID FROM Currencie WHERE Code LIKE ?), ?)";
    private final String UPDATE = "UPDATE ExchangeRate" +
            " SET Rate=?" +
            " WHERE BaseCurrencyId=" +
            "(SELECT Currencie.ID FROM Currencie WHERE CODE LIKE ?) " +
            "AND TargetCurrencyId=" +
            "(SELECT Currencie.ID FROM Currencie WHERE CODE LIKE ?)";

    @Override
    public List<ExchangeRate> getAll() {
        List<ExchangeRate> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL);
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

    public Optional<ExchangeRate> getExchangeRateByBaseAndTargetCurrencies(String base, String target) {
        Optional<ExchangeRate> result = Optional.empty();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_CODES);
            preparedStatement.setString(1, base);
            preparedStatement.setString(2, target);
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

    @Override
    public void create(ExchangeRate exchangeRate) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE);
            preparedStatement.setString(1, exchangeRate.getBaseCurrency().getCode());
            preparedStatement.setString(2, exchangeRate.getTargetCurrency().getCode());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Validator.validateException(e.getMessage());
        }
    }

    public void update(ExchangeRate exchangeRate) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setBigDecimal(1, exchangeRate.getRate());
            preparedStatement.setString(2, exchangeRate.getBaseCurrency().getCode());
            preparedStatement.setString(3, exchangeRate.getTargetCurrency().getCode());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Validator.validateException(e.getMessage());
        }
    }

    @Override
    public void delete(ExchangeRate object) {

    }


}
