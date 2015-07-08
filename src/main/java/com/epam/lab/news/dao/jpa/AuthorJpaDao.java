package com.epam.lab.news.dao.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.epam.lab.news.dao.AuthorDao;
import com.epam.lab.news.dao.JpaDao;
import com.epam.lab.news.entity.Author;
import com.epam.lab.news.model.AuthorCountDto;

@Repository
public class AuthorJpaDao extends JpaDao<Author, Long> implements AuthorDao {

	@Override
	public List<AuthorCountDto> getAuthorCount() {
		List<AuthorCountDto> authorCount = entityManager.createNamedQuery(
				"authors_by_count_note", AuthorCountDto.class).getResultList();
		return authorCount;
	}

}
