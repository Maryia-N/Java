package com.epam.lab.news.dao;

import java.util.List;

import com.epam.lab.news.entity.Comment;

/**
 * The interface {@code CommentDao} contains a collection of abstract methods
 * that describes action with {@link Comment}.
 *
 * @author Maryia_Nabzdorava
 */
public interface CommentDao extends GenericDao<Comment, Long> {

	/**
	 * Reads comments in the repository sorted by the unique identifier of the
	 * news, position and size
	 *
	 * @param newsId
	 *            unique identifier of the news
	 * @param offset
	 *            start position of the first result
	 * @param size
	 *            maximum size of results to retrieve
	 * @return collection of {@link Comment} or empty collection, if there are
	 *         no existed comments in the repository
	 */
	List<Comment> getCommentsByNewsId(Long newsId, int offset, int size);

}