package edu.school21.repositories;

import edu.school21.models.Product;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductsRepositoryJdbcImpl implements ProductsRepository {
    DataSource dataSource;

    ProductsRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Product> findAll() {
        ArrayList<Product> allProducts = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from product;");
            while (rs.next()) {
                allProducts.add(new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return allProducts;
    }

    @Override
    public Optional<Product> findById(Long id) {
        Optional<Product> product = Optional.empty();

        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from product where id = " + id);
            if (rs.next()) {
                product = Optional.of(new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return product;
    }

    @Override
    public void update(Product product) {
        String preparedQuery = "update product set name = ?, price = ? where id = ?;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(preparedQuery)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setLong(3, product.getId());

            if (preparedStatement.executeUpdate() < 1) {
                throw new RuntimeException("Not updated: {" + product + "}");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Product product) {
        String preparedQuery = "insert into product (name, price) values (?, ?);";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(preparedQuery)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());

            if (preparedStatement.executeUpdate() != 1) {
                throw new RuntimeException("Not inserted: {" + product + "}");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            int affectedRows = connection.createStatement().executeUpdate("delete from product where id = " + id);
            if (affectedRows == 0) {
                throw new RuntimeException("Not deleted entity with id = " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
