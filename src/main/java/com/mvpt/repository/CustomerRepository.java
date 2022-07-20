package com.mvpt.repository;

import com.mvpt.model.Customer;
import com.mvpt.model.dto.CustomerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT NEW com.mvpt.model.dto.CustomerDTO (c.id, c.fullName, c.email, c.phone, c.balance, c.locationRegion) FROM Customer c WHERE c.deleted = false ")
    List<CustomerDTO> findAllCustomerDTOByDeletedIsFalse();

    @Query("SELECT NEW com.mvpt.model.dto.CustomerDTO (c.id, c.fullName, c.email, c.phone, c.balance, c.locationRegion) FROM Customer c WHERE c.id = ?1 ")
    Optional<CustomerDTO> getCustomerDTOById(Long id);

    @Query("SELECT NEW com.mvpt.model.dto.CustomerDTO (c.id, c.fullName, c.email, c.phone, c.balance, c.locationRegion) FROM Customer c WHERE c.email = ?1 AND c.id <> ?2 ")
    Optional<CustomerDTO> findCustomerDTOByEmailAndIdIsNot(String email, Long id);

//    @Query("SELECT NEW com.mvpt.model.dto.CustomerDTO (c.id, c.fullName, c.email, c.phone, c.balance, c.locationRegion) FROM Customer c WHERE c.email = ?1 AND c.id <> ?2 ")
//    Optional<CustomerDTO> getRecipentWithoutSenderById(Long id);

    List<Customer> findAllByDeletedFalse();

    Boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE Customer AS c SET c.balance = c.balance + :transactionAmount WHERE c.id = :id")
    void incrementBalance(@Param("id") Long id, @Param("transactionAmount") BigDecimal transactionAmount);

    @Modifying
    @Query("UPDATE Customer AS c SET c.balance = c.balance - :transactionAmount WHERE c.id = :id")
    void reduceBalance(@Param("id") Long id, @Param("transactionAmount") BigDecimal transactionAmount);

    List<Customer> findAllByIdNot(Long id);
}
