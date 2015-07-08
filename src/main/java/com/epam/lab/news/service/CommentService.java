package com.epam.lab.news.service;

import java.util.List;

import com.epam.lab.news.entity.Comment;

/**
 * The interface contains a collection of abstract methods that describes action
 * with {@link Comment}.
 *
 * @author Maryia_Nabzdorava
 */
public interface CommentService extends GenericService<Comment, Long> {

	List<Comment> getCommentsByNewsId(Long newsId, int offset, int size);

}
