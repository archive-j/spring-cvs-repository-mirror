/*
 * The Spring Framework is published under the terms
 * of the Apache Software License.
 */

package org.springframework.jdbc.core;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.InvalidDataAccessApiUsageException;

/**
 * Helper class that can efficiently create multiple CallableStatementCreator
 * objects with different parameters based on a SQL statement and a single
 * set of parameter declarations.
 * @author Rod Johnson
 * @author Thomas Risberg
 */
public class CallableStatementCreatorFactory { 

	/**
	 * List of SqlParameter objects. May not be null.
	 */
	private List declaredParameters = new LinkedList();

	/** The Sql call string, which won't change when the parameters change. */
	private String callString;

	/**
	 * Create a new factory. Will need to add parameters
	 * via the addParameter() method or have no parameters
	 */
	public CallableStatementCreatorFactory(String callString) {
		this(callString, new LinkedList());
	}

	/**
	 * Create a new factory with sql and the given parameters
	 * @param sql SQL
	 * @param declaredParameters list of SqlParameter objects
	 */
	public CallableStatementCreatorFactory(String callString, List declaredParameters) {
		this.callString = callString;
		this.declaredParameters = declaredParameters;
	}

	/**
	 * Add a new declared parameter
	 * Order of parameter addition is significant
	 */
	public void addParameter(SqlParameter p) {
		declaredParameters.add(p);
	}
		
	/**
	 * Return a new CallableStatementCreator instance given this parameters.
	 * @param params List of parameters. May be null.
	 */
	public CallableStatementCreator newCallableStatementCreator(Map inParams) {
		return new CallableStatementCreatorImpl(inParams != null ? inParams : new HashMap());
	}


	/**
	 * CallableStatementCreator implementation returned by this class.
	 */
	private class CallableStatementCreatorImpl implements CallableStatementCreator {

		private Map inParameters;
		
		/**
		 * @param params list of SqlParameter objects. May not be null
		 */
		private CallableStatementCreatorImpl(final Map inParams) {
			this.inParameters = inParams;
		}
		
		public CallableStatement createCallableStatement(Connection conn) throws SQLException {

			CallableStatement cs = conn.prepareCall(callString);

			int sqlColIndx = 1;
			for (int i = 0; i < declaredParameters.size(); i++) {
				SqlParameter p = (SqlParameter) CallableStatementCreatorFactory.this.declaredParameters.get(i);
				if (!inParameters.containsKey(p.getName()) && !(p instanceof SqlOutParameter) && !(p instanceof SqlReturnResultSet))
					throw new InvalidDataAccessApiUsageException("Required input parameter '" + p.getName() + "' is missing");
				// The value may still be null
				Object in = inParameters.get(p.getName());
				if (!(p instanceof SqlOutParameter) && !(p instanceof SqlReturnResultSet)) {
					// Input parameters must be supplied
					if (in != null)
						cs.setObject(sqlColIndx, in, p.getSqlType());
					else
						cs.setNull(sqlColIndx, p.getSqlType());
				}
				else {
					// It's an output parameter. Skip SqlReturnResultSet parameters
					// It need not (but may be) supplied by the caller.
					if (p instanceof SqlOutParameter) {
						if (p.getTypeName() != null) {
							cs.registerOutParameter(sqlColIndx, p.getSqlType(), p.getTypeName());
						}
						else {
							cs.registerOutParameter(sqlColIndx, p.getSqlType());
						}
						if (in != null) {
							cs.setObject(sqlColIndx, in, p.getSqlType());
						}
					}
				}
				if (!(p instanceof SqlReturnResultSet)) {
					sqlColIndx++;
				}
			}

			return cs;
		}

		public String toString() {
			StringBuffer sbuf = new StringBuffer("CallableStatementCreatorFactory.CallableStatementCreatorImpl: sql={" + callString + "}: params={");
			sbuf.append(inParameters.toString());
			return sbuf.toString() + "}";
		}
	}

}
