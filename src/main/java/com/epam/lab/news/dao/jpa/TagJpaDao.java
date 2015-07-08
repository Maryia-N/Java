package com.epam.lab.news.dao.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.epam.lab.news.dao.JpaDao;
import com.epam.lab.news.dao.TagDao;
import com.epam.lab.news.entity.Tag;
import com.epam.lab.news.model.TagCountDto;

@Repository
public class TagJpaDao extends JpaDao<Tag, String> implements TagDao {

	@Override
	public List<TagCountDto> getTagCount() {
		List<TagCountDto> tagCount = entityManager.createNamedQuery(
				"tags_by_count_note", TagCountDto.class).getResultList();
		return tagCount;
	}

}
