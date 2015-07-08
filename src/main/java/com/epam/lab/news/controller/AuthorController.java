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

import com.epam.lab.news.entity.Author;
import com.epam.lab.news.model.AuthorCountDto;
import com.epam.lab.news.service.AuthorService;

@RestController
@RequestMapping("/authors")
public class AuthorController {

	@Autowired
	private AuthorService authorService;

	@RequestMapping(method = POST)
	public ResponseEntity<Author> createAuthor(@RequestBody @Valid Author author) {
		author.setAuthorId(null);
		return new ResponseEntity<>(authorService.save(author),
				HttpStatus.CREATED);
	}

	@RequestMapping(method = GET)
	public List<Author> showAuthor() {
		List<Author> author = authorService.readAll();
		return author;
	}

	@RequestMapping(value = "/{id}", method = GET)
	public ResponseEntity<Author> showAuthorById(
			@PathVariable("id") Long authorId,
			@RequestParam(value = "hideFields", required = false) String[] fields) {
		Author author = authorService.read(authorId, fields);
		return new ResponseEntity<>(author,
				(author == null) ? HttpStatus.NOT_FOUND : HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = PUT)
	public ResponseEntity<Author> updateAuthor(
			@PathVariable("id") Long authorId, @RequestBody @Valid Author author) {
		author.setAuthorId(authorId);
		author = authorService.update(author);
		return new ResponseEntity<>(author,
				(author == null) ? HttpStatus.NOT_FOUND : HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = DELETE)
	public void deleteAuthor(@PathVariable("id") Long authorId) {
		authorService.delete(authorId);
	}

	@RequestMapping(method = GET, params = { "count" })
	public List<AuthorCountDto> getAuthorCount() {
		return authorService.getAuthorCount();
	}
}
