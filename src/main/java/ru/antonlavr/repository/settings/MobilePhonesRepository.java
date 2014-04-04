package ru.antonlavr.repository.settings;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.antonlavr.model.settings.MobilePhones;

@Repository
public interface MobilePhonesRepository extends CrudRepository<MobilePhones, Long> {

}
