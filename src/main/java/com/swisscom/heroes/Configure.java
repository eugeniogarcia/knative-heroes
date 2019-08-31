package com.swisscom.heroes;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import com.swisscom.hero.datasource.heroesDS;
import com.swisscom.heroes.filter.HttpFiltro;
import com.swisscom.heroes.filter.RequestContext;

@Configuration
public class Configure {

	private static final int ORDER = 2;

	@Bean
	public heroesDS service() {
		return new heroesDS();
	}

	@Bean
	@RequestScope
	public RequestContext requestContext() {
		return new RequestContext();
	}

	@Bean
	public FilterRegistrationBean<HttpFiltro> trackingFilterRegistrar(final RequestContext oceRequestContext) {
		final FilterRegistrationBean<HttpFiltro> registration = new FilterRegistrationBean<>();
		registration.setFilter(new HttpFiltro(oceRequestContext));
		registration.setOrder(ORDER);
		return registration;
	}
}
