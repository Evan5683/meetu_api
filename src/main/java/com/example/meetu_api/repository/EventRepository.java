package com.example.meetu_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.meetu_api.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
