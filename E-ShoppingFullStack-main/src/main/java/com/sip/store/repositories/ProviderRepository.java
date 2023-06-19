package com.sip.store.repositories;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sip.store.entities.Article;
import com.sip.store.entities.Provider;

@Repository
public interface ProviderRepository extends CrudRepository<Provider, Long> {
	@Query("FROM Article a WHERE a.provider.id = ?1")
    List<Article> findArticlesByProvider(long id);
	
	@Query("FROM Provider p WHERE p.name = ?1")
	List<Provider> findProviderByName(String name);

}
