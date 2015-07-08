package com.epam.lab.news.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.lab.news.entity.Comment;
import com.epam.lab.news.service.CommentService;

@RestController
@RequestMapping("/comments")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@RequestMapping(method = POST)
	public ResponseEntity<Comment> createComment(
			@RequestBody @Valid Comment comment) {
		comment.setCommentId(null);
		return new ResponseEntity<>(commentService.save(comment),
				HttpStatus.CREATED);
	}

	@RequestMapping(method = GET)
	public List<Comment> getCommentByLimit(
			@RequestParam(defaultValue = "0", value = "offset", required = false) Integer offset,
			@RequestParam(defaultValue = "4", value = "size", required = false) Integer size) {
		return commentService.readByLimit(offset, size);
	}

	@RequestMapping(method = GET, params = { "newsId" })
	public List<Comment> getCommentByNewsId(
			@RequestParam(required = true) Long newsId,
			@RequestParam(defaultValue = "0", value = "offset", required = false) Integer offset,
			@RequestParam(defaultValue = "4", value = "size", required = false) Integer size) {
		List<Comment> news = commentService.getCommentsByNewsId(newsId, offset,
				size);
		return news;
	}

	@RequestMapping(value = "/{id}", method = GET)
	public ResponseEntity<Comment> showCommentById(
			@PathVariable("id") Long commentId) {
		Comment comment = commentService.read(commentId);
		return new ResponseEntity<>(comment,
				(comment == null) ? HttpStatus.NOT_FOUND : HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = PUT)
	public ResponseEntity<Comment> updateComment(
			@PathVariable("id") Long commentId,
			@RequestBody @Valid Comment comment) {
		comment.setCommentId(commentId);
		comment = commentService.update(comment);
		return new ResponseEntity<>(comment,
				(comment == null) ? HttpStatus.NOT_FOUND : HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = DELETE)
	public void deleteComment(@PathVariable("id") Long commentId) {
		commentService.delete(commentId);
	}
}
