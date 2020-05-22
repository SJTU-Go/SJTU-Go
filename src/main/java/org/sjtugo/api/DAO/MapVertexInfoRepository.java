package org.sjtugo.api.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MapVertexInfoRepository extends JpaRepository<MapVertexInfo, Integer> {
    List<Object> findByVertexNameLike(String kw);
}
