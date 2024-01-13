package ru.netology.qa.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLHelper {
    private static final QueryRunner runner = new QueryRunner();

    private SQLHelper() {
    }

    @SneakyThrows
    private static Connection getConn() {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @SneakyThrows
    public static void clearDB() {
        var conn = getConn();
        runner.execute(conn, "DELETE FROM credit_request_entity");
        runner.execute(conn, "DELETE FROM order_entity");
        runner.execute(conn, "DELETE FROM payment_entity");
    }

    @SneakyThrows
    public static String getCardPaymentStatus() {
        var requestSQL = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        var conn = getConn();
        return runner.query(conn, requestSQL, new ScalarHandler<String>());
    }

    @SneakyThrows
    public static boolean checkEmptyDB() {
        var requestSQL = "SELECT * FROM payment_entity";
        var conn = getConn();
        ResultSetHandler<Boolean> resultSetHandler = rs -> !rs.next();

        return runner.query(conn, requestSQL, resultSetHandler);
    }
}
