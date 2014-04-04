package ru.antonlavr.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.antonlavr.model.PhoneBalance;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PhoneBalanceRepository extends CrudRepository<PhoneBalance, Long> {
    List<PhoneBalance> findByPhoneNumberAndBalance(String phoneNumber, BigDecimal balance);
    List<PhoneBalance> findByPhoneNumberAndCurrent(String phoneNumber, Boolean current);
    List<PhoneBalance> findByCurrent(Boolean current);
}
