package com.example.fmcore.chiho.repository;

import com.example.fmcore.chiho.entity.ChiHoRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChiHoRepository extends JpaRepository<ChiHoRequest, String> {
    Optional<ChiHoRequest> findBySignature(String signature);
}