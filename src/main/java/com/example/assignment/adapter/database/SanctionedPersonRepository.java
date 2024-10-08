package com.example.assignment.adapter.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SanctionedPersonRepository extends JpaRepository<SanctionedPersonEntity, Long> {

    List<SanctionedNameDetails> findAllSanctionedNameDetailsByIsDeletedFalse();

    List<SanctionedPersonEntity> findAllByIsDeletedFalse();
}
