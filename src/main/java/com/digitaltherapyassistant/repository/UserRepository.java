package com.digitaltherapyassistant.repository;

import com.digitaltherapyassistant.entity.TrustedContact;
import com.digitaltherapyassistant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    List<TrustedContact> getTrustedContacts(UUID userId);
}