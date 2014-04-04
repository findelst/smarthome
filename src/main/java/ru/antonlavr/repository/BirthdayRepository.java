package ru.antonlavr.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.antonlavr.model.Birthday;

@Repository
public interface BirthdayRepository extends CrudRepository<Birthday, Long> {

}
