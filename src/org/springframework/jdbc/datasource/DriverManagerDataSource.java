/*
 * The Spring Framework is published under the terms
 * of the Apache Software License.
 */
 
package org.springframework.jdbc.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Implementation of SmartDataSource that configures a plain old JDBC Driver
 * via bean properties, and returns a new connection every time.
 * 
 * <p>Useful for test or standalone environments outside of a J2EE container, either
 * as a DataSource bean in a respective ApplicationContext, or in conjunction with a
 * simple JNDI environment. Pool-assuming Connection.close() calls will simply
 * close the connection, so any DataSource-aware persistence code should work.
 *
 * <p>In a J2EE container, it is recommended to use a JNDI DataSource provided by
 * the container. Such a DataSource can be exported as a DataSource bean in an
 * ApplicationContext via JndiObjectFactoryBean, for seamless switching to and from
 * a local DataSource bean like this class.
 *
 * <p>If you need a "real" connection pool outside of a container, consider
 * <a href="http://jakarta.apache.org/commons/dbcp">Apache's Jakarta Commons DBCP</a>.
 * Its BasicDataSource is a full connection pool bean, supporting the same basic
 * properties as this class + specific settings. It can be used as a replacement for
 * an instance of this class just by exchanging the class name of the bean definition
 * to "org.apache.commons.dbcp.BasicDataSource".
 *
 * @author Juergen Hoeller
 * @since 14.03.2003
 * @version $Id$
 * @see org.springframework.jndi.support.SimpleNamingContextBuilder
 * @see org.springframework.jndi.JndiObjectFactoryBean
 */
public class DriverManagerDataSource extends AbstractDataSource implements SmartDataSource {

	private String driverClassName = "";

	private String url = "";

	private String username = "";

	private String password = "";

	/**
	 * Constructor for bean-style configuration.
	 */
	public DriverManagerDataSource() {
	}

	/**
	 * Create a new SingleConnectionDataSource with the given standard
	 * DriverManager parameters.
	 */
	public DriverManagerDataSource(String driverClassName, String url, String username, String password)
	    throws CannotGetJdbcConnectionException {
		setDriverClassName(driverClassName);
		setUrl(url);
		setUsername(username);
		setPassword(password);
	}

	public void setDriverClassName(String driverClassName) throws CannotGetJdbcConnectionException {
		this.driverClassName = driverClassName;
		try {
			Class.forName(this.driverClassName);
			logger.info("Loaded JDBC driver: " + this.driverClassName);
		}
		catch (ClassNotFoundException ex) {
			throw new CannotGetJdbcConnectionException("Cannot load JDBC driver class '" + this.driverClassName + "'", ex);
		}
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * This DataSource returns a new connection every time: Close it when returning one to the "pool".
	 */
	public boolean shouldClose(Connection conn) {
		return true;
	}

	public Connection getConnection() throws SQLException {
		return getConnectionFromDriverManager();
	}

	public Connection getConnection(String username, String password) throws SQLException {
		return getConnectionFromDriverManager(this.url, username, password);
	}
	
	/**
	 * Get a new Connection from DriverManager, with the connection
	 * properties of this DataSource.
	 */
	protected Connection getConnectionFromDriverManager() throws SQLException {
		return getConnectionFromDriverManager(this.url, this.username, this.password);
	}

	/**
	 * Getting a connection using the nasty static from DriverManager is extracted
	 * into a protected method to allow for easy unit testing.
	 */
	protected Connection getConnectionFromDriverManager(String url, String username, String password) throws SQLException {
		logger.info("Creating new JDBC connection to [" + url + "]");
		return DriverManager.getConnection(url, username, password);
	}

}
