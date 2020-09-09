package cc.yao01.rabbit.generator.config.dbloader;

import cc.yao01.rabbit.generator.config.JdbcConnectionConfig;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;


/**
 * @author 姚华成
 */
public class JdbcConnectionFactory implements ConnectionFactory {

    private String userId;
    private String password;
    private String connectionURL;
    private String driverClass;
    private Properties otherProperties;

    public JdbcConnectionFactory(JdbcConnectionConfig config) {
        super();
        userId = config.getUserId();
        password = config.getPassword();
        connectionURL = config.getConnectionURL();
        driverClass = config.getDriverClass();
        otherProperties = config.getProperties();
    }

    @Override
    public Connection getConnection() throws SQLException {
        Driver driver = getDriver();

        Properties props = new Properties();

        if (!StringUtils.isEmpty(userId)) {
            props.setProperty("user", userId);
        }

        if (!StringUtils.isEmpty(password)) {
            props.setProperty("password", password);
        }
        if (otherProperties != null) {
            props.putAll(otherProperties);
        }
        Connection conn = driver.connect(connectionURL, props);

        if (conn == null) {
            throw new SQLException("连接错误！");
        }

        return conn;
    }

    private Driver getDriver() {
        Driver driver;

        try {
            Class<?> clazz = Class.forName(driverClass);
            driver = (Driver) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("加载数据库驱动类时出错，driverClass=" + driverClass, e);
        }

        return driver;
    }

    @Override
    public void addConfigurationProperties(Properties properties) {
        // this should only be called when this connection factory is
        // specified in a ConnectionFactory configuration
        userId = properties.getProperty("userId");
        password = properties.getProperty("password");
        connectionURL = properties.getProperty("connectionURL");
        driverClass = properties.getProperty("driverClass");

        otherProperties = new Properties();
        otherProperties.putAll(properties);

        // remove all the properties that we have specific attributes for
        otherProperties.remove("userId");
        otherProperties.remove("password");
        otherProperties.remove("connectionURL");
        otherProperties.remove("driverClass");
    }
}
