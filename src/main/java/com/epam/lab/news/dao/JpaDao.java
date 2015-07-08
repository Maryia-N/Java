package com.epam.lab.news.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public abstract class JpaDao<E, K extends Serializable> implements
GenericDao<E, K> {

	private final Class<E> persistentClass;

	@SuppressWarnings("unchecked")
	public JpaDao() {
		this.persistentClass = (Class<E>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public E save(E entity) {
		E entityUpdated = entityManager.merge(entity);
		return entityUpdated;
	}

	@Override
	public E read(K id) {
		return entityManager.find(persistentClass, id);
	}

	@Override
	public List<E> readAll() {
		return entityManager.createQuery(getQuery()).getResultList();
	}

	@Override
	public void delete(K id) {
		E entity = entityManager.find(persistentClass, id);
		if (entity != null) {
			entityManager.remove(entity);
		}
	}

	@Override
	public Long getCount() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		query.select(builder.count(query.from(persistentClass)));
		return entityManager.createQuery(query).getSingleResult();
	}

	@Override
	public List<E> readByLimit(int offset, int limit) {
		TypedQuery<E> query = entityManager.createQuery(getQuery());
		query.setFirstResult(offset);
		query.setMaxResults(limit);
		return query.getResultList();
	}

	private CriteriaQuery<E> getQuery() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> criteria = builder.createQuery(persistentClass);
		criteria.from(persistentClass);
		return criteria;
	}

}
