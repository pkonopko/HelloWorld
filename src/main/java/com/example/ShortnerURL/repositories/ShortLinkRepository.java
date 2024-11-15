package com.example.ShortnerURL.repositories;

import com.example.ShortnerURL.models.dto.DeleteShortLinkResultDTO;
import com.example.ShortnerURL.models.entity.ShortLinkEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShortLinkRepository extends JpaRepository<ShortLinkEntity, Long> {
    boolean existsByShortLinkCode(String shortLinkCode);
     List<ShortLinkEntity> findByShortLinkCode(String shortLinkCode);
     //void deleteByShortLinkCode (String shortLinkCode);
     @Modifying
     @Transactional
     @Query("DELETE FROM ShortLinkEntity s WHERE s.shortLinkCode = :shortLinkCode")
     int deleteByShortLinkCode(String shortLinkCode);}
