package com.epam.lab.news.dao;

import java.util.List;

import com.epam.lab.news.entity.News;
import com.epam.lab.news.model.NewsFilterDto;

/**
 * The interface {@code NewsDao} contains a collection of abstract methods that
 * describes action with {@link News}.
 *
 * @author Maryia_Nabzdorava
 */
public interface NewsDao extends GenericDao<News, Long> {

	/**
	 * Reads news sorted by filter
	 *
	 * @param filter
	 *            filter for sorting news
	 * @return sorted collection of {@link News} or empty collection, if there
	 *         are no records satisfying conditions in the repository
	 */
	List<News> getNewsByCriteria(NewsFilterDto filter);

}
