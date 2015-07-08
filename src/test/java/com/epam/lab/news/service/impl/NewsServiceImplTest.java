package com.epam.lab.news.service.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.lab.news.dao.NewsDao;
import com.epam.lab.news.entity.News;
import com.epam.lab.news.model.NewsFilterDto;

@RunWith(MockitoJUnitRunner.class)
public class NewsServiceImplTest {

	@InjectMocks
	private NewsServiceImpl newsService;

	@Mock
	private NewsDao newsDao;

	@Mock
	private News news;

	@Mock
	private List<News> newsList;

	private final Long value = 1L;

	@Test
	public void getNewsById() {
		Mockito.when(newsDao.read(value)).thenReturn(news);
		Assert.assertEquals(news, newsService.read(value));
		Mockito.verify(newsDao).read(value);
	}

	@Test
	public void saveNews() {
		Mockito.when(newsDao.save(news)).thenReturn(news);
		Assert.assertEquals(news, newsService.save(news));
		Mockito.verify(newsDao).save(news);
	}

	@Test
	public void updateNews() {
		Mockito.when(newsDao.read(news.getNewsId())).thenReturn(news);
		Mockito.when(newsDao.save(news)).thenReturn(news);
		Assert.assertEquals(news, newsService.update(news));
		Mockito.verify(newsDao).read(news.getNewsId());
		Mockito.verify(newsDao).save(news);
	}

	@Test
	public void updateNotExistedNews() {
		Mockito.when(newsDao.read(news.getNewsId())).thenReturn(null);
		Assert.assertNull(newsService.update(news));
		Mockito.verify(newsDao).read(news.getNewsId());
		Mockito.verify(newsDao, Mockito.never()).save(news);
	}

	@Test
	public void getNewsCount() {
		Mockito.when(newsDao.getCount()).thenReturn(value);
		Assert.assertEquals(value, newsService.getCommonCount());
		Mockito.verify(newsDao).getCount();
	}

	@Test
	public void getNewsByLimit() {
		int offset = 0;
		int size = 4;
		Mockito.when(newsDao.readByLimit(offset, size)).thenReturn(newsList);
		Assert.assertEquals(newsList, newsService.readByLimit(offset, size));
		Mockito.verify(newsDao).readByLimit(offset, size);
	}

	@Test
	public void getNewsAll() {
		Mockito.when(newsDao.readAll()).thenReturn(newsList);
		Assert.assertEquals(newsList, newsService.readAll());
		Mockito.verify(newsDao).readAll();
	}

	@Test
	public void getNewByFilter() {
		Mockito.when(
				newsDao.getNewsByCriteria(Matchers.any(NewsFilterDto.class)))
				.thenReturn(newsList);
		Assert.assertEquals(newsList,
				newsService.getNewsByCriteria(0, 5, 5, 5L, "Test"));
		Mockito.verify(newsDao).getNewsByCriteria(
				Matchers.any(NewsFilterDto.class));
	}

	@Test
	public void deleteNews() {
		Mockito.doNothing().when(newsDao).delete(Matchers.anyLong());
		newsService.delete(Matchers.anyLong());
		Mockito.verify(newsDao).delete(Matchers.anyLong());
	}
}
