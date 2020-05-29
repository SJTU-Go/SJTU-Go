package org.sjtugo.api.DAO;

import org.sjtugo.api.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByName(String name);
}
