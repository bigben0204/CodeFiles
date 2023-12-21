package org.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JdbcExecutor implements java.io.Closeable {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL_PATTERN
            = "jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=utf-8";

    private Connection connection;
    private boolean isInitSuccessful = false;
    private Properties config;
    private Options options;

    public JdbcExecutor(Properties config, Options options) {
        this.config = config;
        this.options = options;

        try {
            init();
        } catch (Exception e) {
            System.err.println("failed to init connection: ");
            e.printStackTrace();
        }
    }

    public boolean execute(String[] sqls) throws SQLException {
        if (!isInitSuccessful) {
            System.err.println("failed to init connection and sqls is not executed");
            return false;
        }

        boolean ret = false;
        if (options.isQuery()) {
            executeQuery(sqls);
        } else if (options.isUpdate()) {
            executeUpdate(sqls);
        } else {
            System.err.println("sql type is not query or update");
            ret = false;
        }
        return ret;
    }

    private void init() throws Exception {
        String dbUrl = String.format(config.getProperty("DB_URL_PATTERN", DB_URL_PATTERN), options.getAddress(),
                options.getDb());

        Properties properties = new Properties();
        properties.setProperty("user", options.getUser());
        properties.setProperty("password", options.getPasswd());

        Class.forName(config.getProperty("JDBC_DRIVER", JDBC_DRIVER));
        this.connection = DriverManager.getConnection(dbUrl, properties);
        isInitSuccessful = true;
    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeQuery(String[] sqls) throws SQLException {
        System.out.println(String.format("conn url: %s", connection.getMetaData().getURL()));
        // Query
        String sql = sqls[0];
        try (Statement stmtQuery = connection.createStatement()) {
            ResultSet resultSet = stmtQuery.executeQuery(sql);

            if (options.isPrintLog()) {
                System.out.println(String.format("sql: %s, result:", sql));
                while (resultSet.next()) {
                    System.out.println(resultSet.getString(1));
                }
            }
        }
    }

    private void executeUpdate(String[] sqls) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // 添加多条SQL语句到批处理中
            int index = 1;
            for (String sql : sqls) {
                if (options.isPrintLog()) {
                    System.out.println(String.format("sql%d: %s", index++, sql));
                }
                statement.addBatch(sql);
            }

            // 执行批处理中的所有语句
            int[] results = statement.executeBatch();

            if (options.isPrintLog()) {
                // 检查每个语句的执行结果
                for (int i = 0; i < results.length; i++) {
                    if (results[i] == Statement.SUCCESS_NO_INFO) {
                        System.out.println("sql " + (i + 1) + " executed SUCCESS_NO_INFO");
                    } else if (results[i] == Statement.EXECUTE_FAILED) {
                        System.out.println("sql " + (i + 1) + " executed EXECUTE_FAILED");
                    } else {
                        System.out.println("sql " + (i + 1) + " executed result: " + results[i]);
                    }
                }
            }
        }
    }
}
