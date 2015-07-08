package com.epam.lab.news.dao;

import java.io.Serializable;
import java.util.List;

/**
 * The interface performs basic C.R.U.D operations with repository
 *
 * @author Maryia_Nabzdorava
 * @param <E>
 *            entity
 * @param <K>
 *            primary key of entity
 */
public interface GenericDao<E, K extends Serializable> {

	/**
	 * Find entity from the repository by the specified primary key
	 *
	 * @param id
	 *            specified primary key
	 * @return existed entity from repository or <code>null</code>, if there is
	 *         no such entity
	 */
	E read(K id);

	/**
	 * Reads data from the repository
	 *
	 * @return collection of all entities or empty collection, if there are no
	 *         existed entities in the repository
	 */
	List<E> readAll();

	/**
	 * Reads data in the repository sorted by position and size
	 *
	 * @param offset
	 *            start position of the first result
	 * @param limit
	 *            maximum size of results to retrieve
	 * @return collection of entities or empty collection, if there are no
	 *         existed entities in the repository
	 */
	List<E> readByLimit(int offset, int limit);

	/**
	 * Create new entity or update entity if it already exists in the repository
	 *
	 * @param entity
	 *            entity
	 * @return the managed entity that the state was merged to
	 */
	E save(E entity);

	/**
	 * Remove the entity from repository by primary key.
	 *
	 * @param id
	 *            primary key
	 */
	void delete(K id);

	/**
	 * Count the total number of records in the repository
	 *
	 * @return number of records
	 */
	Long getCount();
}
