package org.springframework.orm.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import net.sf.hibernate.LockMode;
import net.sf.hibernate.type.Type;

import org.springframework.dao.DataAccessException;

/**
 * Interface that specifies a basic set of Hibernate operations.
 * Implemented by HibernateTemplate. Not often used, but a useful option
 * to enhance testability, as it can easily be mocked or stubbed.
 *
 * <p>Provides HibernateTemplate's convenience methods that mirror
 * various Session methods. 
 *
 * @author Juergen Hoeller
 * @since 05.02.2004
 * @see HibernateTemplate
 * @see net.sf.hibernate.Session
 */
public interface HibernateOperations {

	//-------------------------------------------------------------------------
	// Convenience methods for load, save, update, delete
	//-------------------------------------------------------------------------

	/**
	 * Return the persistent instance of the given entity class
	 * with the given identifier, or null if not found.
	 * @param entityClass a persistent class
	 * @param id an identifier of the persistent instance
	 * @return the persistent instance, or null if not found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#get(Class, java.io.Serializable)
	 */
	Object get(final Class entityClass, final Serializable id) throws DataAccessException;

	/**
	 * Return the persistent instance of the given entity class
	 * with the given identifier, or null if not found.
	 * Obtains the specified lock mode if the instance exists.
	 * @param entityClass a persistent class
	 * @param id an identifier of the persistent instance
	 * @return the persistent instance, or null if not found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#get(Class, java.io.Serializable, net.sf.hibernate.LockMode)
	 */
	Object get(final Class entityClass, final Serializable id, final LockMode lockMode)
			throws DataAccessException;

	/**
	 * Return the persistent instance of the given entity class
	 * with the given identifier, throwing an exception if not found.
	 * @param entityClass a persistent class
	 * @param id an identifier of the persistent instance
	 * @return the persistent instance
	 * @throws HibernateObjectRetrievalFailureException if the instance could not be found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#load(Class, java.io.Serializable)
	 */
	Object load(final Class entityClass, final Serializable id) throws DataAccessException;

	/**
	 * Return the persistent instance of the given entity class
	 * with the given identifier, throwing an exception if not found.
	 * Obtains the specified lock mode if the instance exists.
	 * @param entityClass a persistent class
	 * @param id an identifier of the persistent instance
	 * @return the persistent instance
	 * @throws HibernateObjectRetrievalFailureException if the instance could not be found
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#load(Class, java.io.Serializable)
	 */
	Object load(final Class entityClass, final Serializable id, final LockMode lockMode)
			throws DataAccessException;

	/**
	 * Remove the given object from the Session cache.
	 * @param entity the persistent instance to lock
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#evict(Object)
	 */
	void evict(final Object entity) throws DataAccessException;

	/**
	 * Obtain the specified lock level upon the given object, implicitly
	 * checking whether the corresponding database entry still exists
	 * (throwing an OptimisticLockingFailureException if not found).
	 * @param entity the persistent instance to lock
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see HibernateOptimisticLockingFailureException
	 * @see net.sf.hibernate.Session#lock(Object, net.sf.hibernate.LockMode)
	 */
	void lock(final Object entity, final LockMode lockMode) throws DataAccessException;

	/**
	 * Save the given persistent instance.
	 * @param entity the persistent instance to save
	 * @return the generated identifier
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#save(Object)
	 */
	Serializable save(final Object entity) throws DataAccessException;

	/**
	 * Save the given persistent instance with the given identifier.
	 * @param entity the persistent instance to save
	 * @param id the identifier to assign
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#save(Object, java.io.Serializable)
	 */
	void save(final Object entity, final Serializable id) throws DataAccessException;

	/**
	 * Save respectively update the given persistent instance,
	 * according to its ID (matching the configured "unsaved-value"?).
	 * @param entity the persistent instance to save respectively update
	 * (to be associated with the Hibernate Session)
	 * @throws DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#saveOrUpdate(Object)
	 */
	void saveOrUpdate(final Object entity) throws DataAccessException;

	/**
	 * Save respectively update the contents of given persistent object,
	 * according to its ID (matching the configured "unsaved-value"?).
	 * Will copy the contained fields to an already loaded instance
	 * with the same ID, if appropriate.
	 * @param entity the persistent object to save respectively update
	 * (<i>not</i> necessarily to be associated with the Hibernate Session)
	 * @return the actually associated persistent object
	 * (either an already loaded instance with the same ID, or the given object)
	 * @throws DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#saveOrUpdateCopy(Object)
	 */
	public Object saveOrUpdateCopy(final Object entity) throws DataAccessException;

	/**
	 * Update the given persistent instance.
	 * @param entity the persistent instance to update
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#update(Object)
	 */
	void update(final Object entity) throws DataAccessException;

	/**
	 * Update the given persistent instance.
	 * Obtains the specified lock mode if the instance exists, implicitly
	 * checking whether the corresponding database entry still exists
	 * (throwing an OptimisticLockingFailureException if not found).
	 * @param entity the persistent instance to update
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see HibernateOptimisticLockingFailureException
	 * @see net.sf.hibernate.Session#update(Object)
	 */
	void update(final Object entity, final LockMode lockMode) throws DataAccessException;

	/**
	 * Delete the given persistent instance.
	 * @param entity the persistent instance to delete
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#delete(Object)
	 */
	void delete(final Object entity) throws DataAccessException;

	/**
	 * Delete the given persistent instance.
	 * Obtains the specified lock mode if the instance exists, implicitly
	 * checking whether the corresponding database entry still exists
	 * (throwing an OptimisticLockingFailureException if not found).
	 * @param entity the persistent instance to delete
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see HibernateOptimisticLockingFailureException
	 * @see net.sf.hibernate.Session#delete(Object)
	 */
	void delete(final Object entity, final LockMode lockMode) throws DataAccessException;

	/**
	 * Delete all given persistent instances.
	 * This can be combined with any of the find methods to delete by query
	 * in two lines of code, similar to Session's delete by query methods.
	 * @param entities the persistent instances to delete
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#delete(String)
	 */
	void deleteAll(final Collection entities) throws DataAccessException;


	//-------------------------------------------------------------------------
	// Convenience finder methods
	//-------------------------------------------------------------------------

	/**
	 * Execute a query for persistent instances.
	 * @param queryString a query expressed in Hibernate's query language
	 * @return the List of persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#find(String)
	 * @see net.sf.hibernate.Session#createQuery
	 */
	List find(final String queryString) throws DataAccessException;

	/**
	 * Execute a query for persistent instances, binding
	 * one value to a "?" parameter in the query string.
	 * @param queryString a query expressed in Hibernate's query language
	 * @param value the value of the parameter
	 * @return the List of persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#find(String)
	 * @see net.sf.hibernate.Session#createQuery
	 */
	List find(final String queryString, final Object value) throws DataAccessException;

	/**
	 * Execute a query for persistent instances, binding one value
	 * to a "?" parameter of the given type in the query string.
	 * @param queryString a query expressed in Hibernate's query language
	 * @param value the value of the parameter
	 * @param type Hibernate type of the parameter
	 * @return the List of persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#find(String)
	 * @see net.sf.hibernate.Session#createQuery
	 */
	List find(final String queryString, final Object value, final Type type)
			throws DataAccessException;

	/**
	 * Execute a query for persistent instances, binding a
	 * number of values to "?" parameters in the query string.
	 * @param queryString a query expressed in Hibernate's query language
	 * @param values the values of the parameters
	 * @return the List of persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#find(String)
	 * @see net.sf.hibernate.Session#createQuery
	 */
	List find(final String queryString, final Object[] values) throws DataAccessException;

	/**
	 * Execute a query for persistent instances, binding a number of
	 * values to "?" parameters of the given types in the query string.
	 * @param queryString a query expressed in Hibernate's query language
	 * @param values the values of the parameters
	 * @param types Hibernate types of the parameters
	 * @return the List of persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#find(String)
	 * @see net.sf.hibernate.Session#createQuery
	 */
	List find(final String queryString, final Object[] values, final Type[] types)
			throws DataAccessException;

	/**
	 * Execute a query for persistent instances, binding the properties
	 * of the given bean to <i>named</i> parameters in the query string.
	 * @param queryString a query expressed in Hibernate's query language
	 * @param valueBean the values of the parameters
	 * @return the List of persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#find(String)
	 * @see net.sf.hibernate.Session#createQuery
	 * @see net.sf.hibernate.Query#setProperties
	 */
	List findByValueBean(final String queryString, final Object valueBean)
			throws DataAccessException;

	/**
	 * Execute a named query for persistent instances.
	 * A named query is defined in a Hibernate mapping file.
	 * @param queryName the name of a Hibernate query in a mapping file
	 * @return the List of persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#find(String)
	 * @see net.sf.hibernate.Session#getNamedQuery(String)
	 */
	List findByNamedQuery(final String queryName) throws DataAccessException;

	/**
	 * Execute a named query for persistent instances, binding
	 * one value to a "?" parameter in the query string.
	 * A named query is defined in a Hibernate mapping file.
	 * @param queryName the name of a Hibernate query in a mapping file
	 * @return the List of persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#find(String)
	 * @see net.sf.hibernate.Session#getNamedQuery(String)
	 */
	List findByNamedQuery(final String queryName, final Object value)
			throws DataAccessException;

	/**
	 * Execute a named query for persistent instances, binding
	 * one value to a "?" parameter in the query string.
	 * A named query is defined in a Hibernate mapping file.
	 * @param queryName the name of a Hibernate query in a mapping file
	 * @param type Hibernate type of the parameter
	 * @return the List of persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#find(String)
	 * @see net.sf.hibernate.Session#getNamedQuery(String)
	 */
	List findByNamedQuery(final String queryName, final Object value, final Type type)
			throws DataAccessException;

	/**
	 * Execute a named query for persistent instances, binding a
	 * number of values to "?" parameters in the query string.
	 * A named query is defined in a Hibernate mapping file.
	 * @param queryName the name of a Hibernate query in a mapping file
	 * @param values the values of the parameters
	 * @return the List of persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#find(String)
	 * @see net.sf.hibernate.Session#getNamedQuery(String)
	 */
	List findByNamedQuery(final String queryName, final Object[] values)
			throws DataAccessException;

	/**
	 * Execute a named query for persistent instances, binding a
	 * number of values to "?" parameters in the query string.
	 * A named query is defined in a Hibernate mapping file.
	 * @param queryName the name of a Hibernate query in a mapping file
	 * @param values the values of the parameters
	 * @param types Hibernate types of the parameters
	 * @return the List of persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#find(String)
	 * @see net.sf.hibernate.Session#getNamedQuery(String)
	 */
	List findByNamedQuery(final String queryName, final Object[] values, final Type[] types)
			throws DataAccessException;

	/**
	 * Execute a named query for persistent instances, binding the properties
	 * of the given bean to <i>named</i> parameters in the query string.
	 * A named query is defined in a Hibernate mapping file.
	 * @param queryName the name of a Hibernate query in a mapping file
	 * @param valueBean the values of the parameters
	 * @return the List of persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see net.sf.hibernate.Session#find(String)
	 * @see net.sf.hibernate.Session#getNamedQuery(String)
	 * @see net.sf.hibernate.Query#setProperties
	 */
	List findByNamedQueryAndValueBean(final String queryName, final Object valueBean)
			throws DataAccessException;

}
