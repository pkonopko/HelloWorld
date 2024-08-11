package com.example.ShortnerURL.repositories;

import com.example.ShortnerURL.models.entity.ShortLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortLinkRepository extends JpaRepository<ShortLinkEntity, Long> {
    boolean existsByShortLinkCode(String shortLinkCode);
}
