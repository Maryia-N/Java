package com.epam.lab.news.dao;

import java.util.List;

import com.epam.lab.news.entity.Tag;
import com.epam.lab.news.model.TagCountDto;

/**
 * The interface {@code TagDao} contains a collection of abstract methods that
 * describes action with {@link Tag} and {@link TagCountDto}
 *
 * @author Maryia_Nabzdorava
 *
 */
public interface TagDao extends GenericDao<Tag, String> {

	/**
	 * Reads data, and counts how many news belongs to the tag
	 *
	 * @return collection of {@link TagCountDto} or empty collection, if there
	 *         are no existed tags in the repository
	 */
	List<TagCountDto> getTagCount();

}
