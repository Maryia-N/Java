package com.epam.lab.news.service;

import java.io.Serializable;
import java.util.List;

/**
 * The interface performs basic C.R.U.D. operations for service layer
 *
 * @author Maryia_Nabzdorava
 * @param <E>
 *            entity
 * @param <K>
 *            primary key
 */
public interface GenericService<E, K extends Serializable> {

	/**
	 * Find entity from the repository by the specified primary key
	 * 
	 * @param id
	 *            primary key
	 * @param fieldsToExclude
	 *            a list of the fields that you want to hide when displayed
	 *            entity
	 * @return existed entity from repository or <code>null</code>, if there is
	 *         no such entity
	 */
	E read(K id, String... fieldsToExclude);

	/**
	 * Reads data from the repository
	 *
	 * @return collection of all entities or empty collection, if there are no
	 *         existed entities in the repository
	 */
	List<E> readAll();

	/**
	 * Reads data sorted by position and size
	 *
	 * @param page
	 *            start position of the first result
	 * @param limit
	 *            maximum size of results to retrieve
	 * @return collection of entities or empty collection, if there are no
	 *         existed entities
	 */
	List<E> readByLimit(int page, int limit);

	/**
	 * Create new record
	 *
	 * @param entity
	 *            entity
	 * @return entity with primary key
	 */
	E save(E entity);

	/**
	 * Update entity
	 *
	 * @param entity
	 *            entity
	 * @return entity or {@code null}, if there are no such entity
	 */
	E update(E entity);

	/**
	 * Remove the entity by primary key
	 *
	 * @param id
	 *            primary key
	 */
	void delete(K id);

	/**
	 * Count the total number of records
	 *
	 * @return number of records
	 */
	Long getCommonCount();
}