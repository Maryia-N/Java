package com.epam.lab.news.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.lab.news.dao.CommentDao;
import com.epam.lab.news.entity.Comment;
import com.epam.lab.news.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentDao commentDao;

	@Override
	public Comment read(Long id, String... fieldsToExclude) {
		return commentDao.read(id);
	}

	@Override
	public List<Comment> readAll() {
		return commentDao.readAll();
	}

	@Override
	public List<Comment> readByLimit(int offset, int limit) {
		return commentDao.readByLimit((offset > 0) ? offset : 0,
				(limit > 0) ? limit : 0);
	}

	@Transactional
	@Override
	public Comment save(Comment entity) {
		return commentDao.save(entity);
	}

	@Transactional
	@Override
	public Comment update(Comment entity) {
		return (read(entity.getCommentId()) != null) ? save(entity) : null;
	}

	@Transactional
	@Override
	public void delete(Long id) {
		commentDao.delete(id);
	}

	@Override
	public Long getCommonCount() {
		return commentDao.getCount();
	}

	@Override
	public List<Comment> getCommentsByNewsId(Long newsId, int offset, int size) {
		return commentDao.getCommentsByNewsId(newsId,
				(offset > 0) ? offset : 0, (size > 0) ? size : 0);
	}

}
