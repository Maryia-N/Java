package com.epam.lab.news.service;

import java.util.List;

import com.epam.lab.news.entity.Author;
import com.epam.lab.news.model.AuthorCountDto;

/**
 * The interface contains a collection of abstract methods that describes action
 * with {@link Author} and {@link AuthorCountDto}
 *
 * @author Maryia_Nabzdorava
 *
 */
public interface AuthorService extends GenericService<Author, Long> {

	/**
	 * Reads data, and counts how many news belongs to the author
	 *
	 * @return collection of {@link AuthorCountDto} or empty collection, if
	 *         there are no existed authors in the repository
	 */
	List<AuthorCountDto> getAuthorCount();

}