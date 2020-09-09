package cc.yao01.rabbit.generator.config.dbloader;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author 姚华成
 * @date 2017-10-19
 */
public interface ConnectionFactory {
	/**
	 * Should return a connection to the database in use for this context. The
	 * generator will call this method only one time for each context. The
	 * generator will close the connection.
	 * 
	 * @return
	 * @throws SQLException
	 */
	Connection getConnection() throws SQLException;

	/**
	 * Adds properties for this instance from any properties configured in the
	 * ConnectionFactory.
	 * 
	 * This method will be called before any of the get methods.
	 * 
	 * @param properties
	 *            All properties from the configuration
	 */
	void addConfigurationProperties(Properties properties);
}
