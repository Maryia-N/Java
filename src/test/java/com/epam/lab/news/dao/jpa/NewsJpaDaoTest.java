package com.epam.lab.news.dao.jpa;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.lab.news.entity.News;
import com.epam.lab.news.model.NewsFilterDto;
import com.epam.lab.news.util.SortField;

@RunWith(MockitoJUnitRunner.class)
public class NewsJpaDaoTest {

	@InjectMocks
	private NewsJpaDao newsDao;

	@Mock
	private EntityManager entityManager;

	@Mock
	private List<News> newsList;

	@Mock
	private CriteriaBuilder builder;

	@Mock
	private CriteriaQuery<News> criteria;

	@Mock
	private TypedQuery<News> query;

	@Mock
	private Root<News> root;

	@Mock
	private Expression<News> expression;

	@Mock
	private Predicate predicate;

	@Mock
	private Order order;

	@Mock
	private Path<Object> path;

	private final Long value = 1L;

	private News news;

	@Before
	public void befoteTest() {
		news = new News();
		news.setNewsId(1L);
		news.setTitle("Test");
		news.setShortText("Test");
		news.setFullText("Text");
		Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(builder);
		Mockito.doNothing().when(entityManager).remove(news);
	}

	@Test
	public void getNewsById() {
		Mockito.when(entityManager.find(News.class, value)).thenReturn(news);
		Assert.assertEquals(news, newsDao.read(value));
		Mockito.verify(entityManager).find(News.class, value);
	}

	@Test
	public void saveExistedNews() {
		News existedNews = Mockito.mock(News.class);
		Mockito.when(entityManager.find(News.class, news.getNewsId())).thenReturn(
				existedNews);
		Mockito.when(entityManager.merge(existedNews)).thenReturn(existedNews);
		Assert.assertEquals(existedNews, newsDao.save(news));
		Mockito.verify(entityManager).merge(existedNews);
		Mockito.verify(entityManager).merge(existedNews);
	}

	public void saveNoExistedNews() {
		News newNews = new News();
		newNews.setNewsId(news.getNewsId());
		newNews.setTitle(news.getTitle());
		newNews.setShortText(news.getShortText());
		newNews.setFullText(news.getFullText());
		Mockito.when(entityManager.merge(newNews)).thenReturn(newNews);
		Assert.assertEquals(newNews, newsDao.save(news));
		Mockito.verify(entityManager, Mockito.never()).find(News.class, news.getNewsId());
		Mockito.verify(entityManager).merge(newNews);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getNewsCount() {
		CriteriaQuery<Long> cr = Mockito.mock(CriteriaQuery.class);
		Root<News> root = Mockito.mock(Root.class);
		Expression<Long> exp = Mockito.mock(Expression.class);
		TypedQuery<Long> tq = Mockito.mock(TypedQuery.class);
		Long value = 1L;

		Mockito.when(builder.createQuery(Long.class)).thenReturn(cr);
		Mockito.when(cr.from(News.class)).thenReturn(root);
		Mockito.when(builder.count(root)).thenReturn(exp);
		Mockito.when(cr.select(exp)).thenReturn(cr);
		Mockito.when(entityManager.createQuery(cr)).thenReturn(tq);
		Mockito.when(tq.getSingleResult()).thenReturn(value);

		Assert.assertEquals(value, newsDao.getCount());

		Mockito.verify(entityManager).getCriteriaBuilder();
		Mockito.verify(builder).createQuery(Long.class);
		Mockito.verify(cr).from(News.class);
		Mockito.verify(builder).count(root);
		Mockito.verify(cr).select(exp);
		Mockito.verify(entityManager).createQuery(cr);
		Mockito.verify(tq).getSingleResult();
	}

	@Test
	public void getNewsAll() {
		Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(builder);
		Mockito.when(builder.createQuery(News.class)).thenReturn(criteria);
		Mockito.when(criteria.from(News.class)).thenReturn(root);
		Mockito.when(entityManager.createQuery(criteria)).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(newsList);

		Assert.assertEquals(newsList, newsDao.readByLimit(0, 4));

		Mockito.verify(entityManager).getCriteriaBuilder();
		Mockito.verify(builder).createQuery(News.class);
		Mockito.verify(criteria).from(News.class);
		Mockito.verify(entityManager).createQuery(criteria);
		Mockito.verify(query).getResultList();
	}

	@Test
	public void getNewsByFilterPagination() {
		NewsFilterDto filter = new NewsFilterDto(new HashMap<SortField, Object>());
		filter.setPageIndex(0);
		filter.setPageSize(4);

		Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(builder);
		Mockito.when(builder.createQuery(News.class)).thenReturn(criteria);
		Mockito.when(criteria.from(News.class)).thenReturn(root);

		Mockito.when(root.get(Matchers.anyString())).thenReturn(path);
		Mockito.when(builder.desc(path)).thenReturn(order);
		Mockito.when(criteria.orderBy(order)).thenReturn(criteria);

		Mockito.when(entityManager.createQuery(criteria)).thenReturn(query);
		Mockito.when(query.setFirstResult(filter.getPageIndex())).thenReturn(query);
		Mockito.when(query.setMaxResults(filter.getPageSize())).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(newsList);

		Assert.assertEquals(newsList, newsDao.getNewsByCriteria(filter));

		Mockito.verify(entityManager).getCriteriaBuilder();
		Mockito.verify(builder).createQuery(News.class);
		Mockito.verify(criteria).from(News.class);

		Mockito.verify(entityManager).createQuery(criteria);
		Mockito.verify(query).setFirstResult(filter.getPageIndex());
		Mockito.verify(query).setMaxResults(filter.getPageSize());
		Mockito.verify(query).getResultList();
	}

	@Test
	public void deleteExistedNews() {
		Mockito.when(entityManager.find(News.class, value)).thenReturn(news);
		newsDao.delete(value);
		Mockito.verify(entityManager).find(News.class, value);
		Mockito.verify(entityManager).remove(news);
	}

	@Test
	public void deleteNoExistedNews() {
		Mockito.when(entityManager.find(News.class, value)).thenReturn(null);
		newsDao.delete(value);
		Mockito.verify(entityManager).find(News.class, value);
		Mockito.verify(entityManager, Mockito.never()).remove(news);
	}
}
