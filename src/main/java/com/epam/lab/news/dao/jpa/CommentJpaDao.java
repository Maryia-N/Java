package com.epam.lab.news.dao.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.epam.lab.news.dao.CommentDao;
import com.epam.lab.news.dao.JpaDao;
import com.epam.lab.news.entity.Comment;

@Repository
public class CommentJpaDao extends JpaDao<Comment, Long> implements CommentDao {

	@Override
	public List<Comment> getCommentsByNewsId(Long newsId, int offset, int size) {
		return entityManager
				.createNamedQuery("comments_by_news_id", Comment.class)
				.setParameter("newsId", newsId).setFirstResult(offset)
				.setMaxResults(size).getResultList();
	}

}
