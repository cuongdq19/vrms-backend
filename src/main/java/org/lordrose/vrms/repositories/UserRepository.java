package org.lordrose.vrms.repositories;

import org.lordrose.vrms.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByPhoneNumberAndPassword(String phoneNum, String password);

    Optional<User> findUserByUsernameAndPassword(String username, String password);

    Optional<User> findUserByPhoneNumber(String phoneNumber);

    List<User> findAllByProviderId (Long providerId);

    Optional<User> findUserByUsername(String username);

    List<User> findAllByProviderIdAndRoleName(Long providerId, String roleName);
}
