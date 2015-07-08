package com.epam.lab.news.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.lab.news.entity.News;
import com.epam.lab.news.service.NewsService;

@RestController
@RequestMapping("/news")
public class NewsController {

	private static final Logger LOG = LoggerFactory
			.getLogger(NewsController.class);

	@Autowired
	private NewsService newsService;

	@RequestMapping(method = POST)
	public ResponseEntity<News> createNews(@RequestBody @Valid News news) {
		NewsController.LOG.debug("Start create new news!");
		news.setNewsId(null);
		return new ResponseEntity<>(newsService.save(news), HttpStatus.CREATED);
	}

	@RequestMapping(method = GET)
	public List<News> getNewsByLimit(
			@RequestParam(defaultValue = "0", value = "offset", required = false) Integer offset,
			@RequestParam(defaultValue = "4", value = "size", required = false) Integer size,
			@RequestParam(value = "top", required = false) Integer top,
			@RequestParam(value = "authorId", required = false) Long authorId,
			@RequestParam(value = "tagName", required = false) String tagName) {
		return newsService.getNewsByCriteria(offset, size, top, authorId,
				tagName);
	}

	@RequestMapping(value = "/{id}", method = GET)
	public ResponseEntity<News> showNewsById(
			@PathVariable("id") Long newsId,
			@RequestParam(value = "hideFields", required = false) String[] fields) {
		NewsController.LOG.debug("Show news by id= " + newsId);
		News news = newsService.read(newsId, fields);
		return new ResponseEntity<>(news, (news == null) ? HttpStatus.NOT_FOUND
				: HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = PUT)
	public ResponseEntity<News> updateNews(@PathVariable("id") Long newsId,
			@RequestBody @Valid News news) {
		NewsController.LOG.debug("Start update news!");
		news.setNewsId(newsId);
		news = newsService.update(news);
		return new ResponseEntity<>(news, (news == null) ? HttpStatus.NOT_FOUND
				: HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = DELETE)
	public void deleteNews(@PathVariable("id") Long newsId) {
		NewsController.LOG.debug("Start delete news!");
		newsService.delete(newsId);
	}
}
