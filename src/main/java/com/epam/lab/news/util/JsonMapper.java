package com.epam.lab.news.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

@Component
public class JsonMapper extends ObjectMapper {

	private static final long serialVersionUID = -3131980955975958812L;

	public JsonMapper() {
		Hibernate4Module hm = new Hibernate4Module();
		registerModule(hm);
		setSerializationInclusion(Include.NON_EMPTY);
	}
}
