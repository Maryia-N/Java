package com.epam.lab.news.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.epam.lab.news.dao.JpaDao;
import com.epam.lab.news.dao.NewsDao;
import com.epam.lab.news.entity.Comment;
import com.epam.lab.news.entity.News;
import com.epam.lab.news.model.NewsFilterDto;
import com.epam.lab.news.util.SortField;

@Repository
public class NewsJpaDao extends JpaDao<News, Long> implements NewsDao {

	@Override
	public News save(News entity) {
		News news = null;
		if (entity.getNewsId() != null) {
			news = entityManager.find(News.class, entity.getNewsId());
		}
		if (news == null) {
			news = new News();
		}

		news.setTitle(entity.getTitle());
		news.setShortText(entity.getShortText());
		news.setFullText(entity.getFullText());
		news.setModificationDate(entity.getModificationDate());
		news.setTags(entity.getTags());
		news.setAuthors(entity.getAuthors());

		return entityManager.merge(news);
	}

	@Override
	public List<News> getNewsByCriteria(NewsFilterDto filter) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<News> criteria = builder.createQuery(News.class);
		Root<News> root = criteria.from(News.class);

		List<Predicate> predicate = new ArrayList<>();
		filter.getSearchData().forEach(
				(k, v) -> predicate.add(builder.isMember(v, root.get(k.getField()))));

		List<Order> order = new ArrayList<>();
		if (filter.getCommentTop() != null) {
			Join<News, Comment> join = root.join(SortField.NEWS_COMMENTS.getField(),
					JoinType.LEFT);
			criteria.groupBy(root.get(SortField.NEWS_ID.getField()),
					root.get(SortField.NEWS_TITLE.getField()),
					root.get(SortField.NEWS_SHORT_TEXT.getField()),
					root.get(SortField.NEWS_FULL_TEXT.getField()),
					root.get(SortField.NEWS_CREATION_DATE.getField()),
					root.get(SortField.NEWS_MODIFICATION_DATE.getField()));
			order.add(builder.desc(builder.count(join.get(SortField.COMMENT_ID.getField()))));
		}
		order.add(builder.desc(root.get(SortField.NEWS_MODIFICATION_DATE.getField())));
		order.add(builder.desc(root.get(SortField.NEWS_ID.getField())));

		createQueryCondition(criteria, predicate.toArray(new Predicate[predicate.size()]));
		createQueryOrder(criteria, order);

		TypedQuery<News> query = entityManager.createQuery(criteria);
		createQueryPagination(query, filter.getPageIndex(), filter.getPageSize());
		return query.getResultList();
	}

	private CriteriaQuery<News> createQueryCondition(CriteriaQuery<News> criteria,
			Predicate... predicate) {
		return (predicate == null) ? criteria : criteria.where(predicate);
	}

	private CriteriaQuery<News> createQueryOrder(CriteriaQuery<News> criteria,
			List<Order> order) {
		return (order == null) ? criteria : criteria.orderBy(order);
	}

	private TypedQuery<News> createQueryPagination(TypedQuery<News> query, int offset,
			int size) {
		query.setFirstResult(offset);
		query.setMaxResults(size);
		return query;
	}

}
