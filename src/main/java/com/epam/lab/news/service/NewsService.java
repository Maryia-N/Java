package com.epam.lab.news.service;

import java.util.List;

import com.epam.lab.news.entity.News;

/**
 * The interface contains a collection of abstract methods that describes action
 * with {@link News}.
 *
 * @author Maryia_Nabzdorava
 */

public interface NewsService extends GenericService<News, Long> {

	/**
	 * Reads news sorted by parameters
	 *
	 * @param offset
	 *            start position of the first result
	 * @param size
	 *            maximum size of results to retrieve
	 * @param commentTop
	 *            number of news that should be sorted according to the most
	 *            commented. If this option is specified, the pagination is
	 *            ignored
	 * @param authorId
	 *            primary key of authors
	 * @param tagName
	 *            tag name
	 * @return sorted collection of {@link News} or empty collection, if there
	 *         are no records satisfying conditions in the repository
	 */
	List<News> getNewsByCriteria(Integer offset, Integer size,
			Integer commentTop, Long authorId, String tagName);

	void testTransactionForDelete(Long newsId, boolean isException);

}
