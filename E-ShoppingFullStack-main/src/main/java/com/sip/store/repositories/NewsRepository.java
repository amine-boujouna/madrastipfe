package com.sip.store.repositories;

import org.springframework.data.repository.CrudRepository;

import com.sip.store.entities.News;

public interface NewsRepository  extends CrudRepository<News, Long> {

}
