package com.example.sanad.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Types;
import java.time.Instant;
import java.util.UUID;
@MappedSuperclass
@Data
public class AbstractEntity <T extends AbstractEntity<T>> {
    // This class can be used as a base class for other entities
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    protected UUID id;
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    protected Instant createdAt;
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    protected Instant updatedAt;
}
