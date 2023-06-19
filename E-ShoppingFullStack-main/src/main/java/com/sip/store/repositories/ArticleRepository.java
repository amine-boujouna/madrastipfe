package com.sip.store.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sip.store.entities.Article;

public interface ArticleRepository  extends CrudRepository<Article, Long> {
	@Query("FROM Article a WHERE a.inventoryQuantity = 0")
    List<Article> findProductSoldOut();
	
	@Query("FROM Article a WHERE a.label = ?1")
	List<Article> findProductByLabel(String label);
	
	
}
