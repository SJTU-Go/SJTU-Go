package org.sjtugo.api.DAO;

import org.sjtugo.api.entity.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceRepository extends JpaRepository<Preference,Integer> {
}

