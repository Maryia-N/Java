package com.epam.lab.news.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.lab.news.dao.TagDao;
import com.epam.lab.news.entity.News;
import com.epam.lab.news.entity.Tag;
import com.epam.lab.news.model.TagCountDto;
import com.epam.lab.news.service.NewsService;
import com.epam.lab.news.service.TagService;

@Service
public class TagServiceImpl implements TagService {

	@Autowired
	private TagDao tagDao;

	@Autowired
	private NewsService newsService;

	@Transactional(readOnly = true)
	@Override
	public Tag read(String id, String... fieldsToExclude) {
		Tag tag = tagDao.read(id);
		if (tag != null) {
			boolean addNews = true;
			if (null != fieldsToExclude) {
				for (String field : fieldsToExclude) {
					if ("news".equals(field)) {
						addNews = false;
						break;
					}
				}
			}
			if (addNews) {
				tag.getNews().size();
			}
		}
		return tag;
	}

	@Override
	public List<Tag> readAll() {
		return tagDao.readAll();
	}

	@Override
	public List<Tag> readByLimit(int offset, int limit) {
		return tagDao.readByLimit(offset, limit);
	}

	@Transactional
	@Override
	public Tag save(Tag entity) {
		deleteNoExistedNews(entity.getNews());
		return (read(entity.getTagName(), "news") == null) ? tagDao
				.save(entity) : null;
	}

	@Transactional
	@Override
	public Tag update(Tag entity) {
		deleteNoExistedNews(entity.getNews());
		return (read(entity.getTagName(), "news") != null) ? tagDao
				.save(entity) : null;
	}

	@Transactional
	@Override
	public void delete(String id) {
		tagDao.delete(id);
	}

	@Override
	public Long getCommonCount() {
		return tagDao.getCount();
	}

	@Override
	public List<TagCountDto> getTagCount() {
		return tagDao.getTagCount();
	}

	private Set<News> deleteNoExistedNews(Set<News> news) {
		if (news != null) {
			Iterator<News> iterator = news.iterator();
			while (iterator.hasNext()) {
				News i = iterator.next();
				if (newsService.read(i.getNewsId(), "tags", "authors",
						"comments") == null) {
					iterator.remove();
				}
			}
		}
		return news;
	}

}
