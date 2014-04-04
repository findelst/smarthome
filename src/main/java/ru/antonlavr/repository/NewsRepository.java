package ru.antonlavr.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.antonlavr.model.News;

@Repository
public interface NewsRepository extends CrudRepository<News, Long> {

}
