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

import com.epam.lab.news.entity.Tag;
import com.epam.lab.news.model.TagCountDto;
import com.epam.lab.news.service.TagService;

@RestController
@RequestMapping("/tags")
public class TagController {

	@Autowired
	private TagService tagService;

	@RequestMapping(method = POST)
	public ResponseEntity<Tag> createTag(@RequestBody @Valid Tag tag) {
		Tag entity = tagService.save(tag);
		return new ResponseEntity<>(entity,
				(entity == null) ? HttpStatus.UNPROCESSABLE_ENTITY
						: HttpStatus.CREATED);
	}

	@RequestMapping(method = GET)
	public List<Tag> showTag() {
		List<Tag> tag = tagService.readAll();
		return tag;
	}

	@RequestMapping(value = "/{id}", method = GET)
	public ResponseEntity<Tag> showTagById(
			@PathVariable("id") String tagId,
			@RequestParam(value = "hideFields", required = false) String[] fields) {
		Tag tag = tagService.read(tagId, fields);
		return new ResponseEntity<>(tag, (tag == null) ? HttpStatus.NOT_FOUND
				: HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = PUT)
	public ResponseEntity<Tag> updateTag(@RequestBody @Valid Tag tag) {
		Tag entity = tagService.update(tag);
		return new ResponseEntity<>(entity,
				(entity == null) ? HttpStatus.NOT_FOUND : HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = DELETE)
	public void deleteTag(@PathVariable("id") String tagId) {
		tagService.delete(tagId);
	}

	@RequestMapping(method = GET, params = { "count" })
	public List<TagCountDto> getTagCount() {
		return tagService.getTagCount();
	}
}
