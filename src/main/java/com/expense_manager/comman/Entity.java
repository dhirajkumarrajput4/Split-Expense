package com.expense_manager.comman;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Objects;

@MappedSuperclass
public class Entity implements IdProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_gen")
    protected Long id = null;

    @Column(name = "crated")
    protected OffsetDateTime created = OffsetDateTime.now();

    @Column(name = "modified")
    protected OffsetDateTime modified = OffsetDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return getId() != null ? getId().equals(entity.getId()) : entity.getId() == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created, modified);
    }

    public Long getId() {
        return id;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public OffsetDateTime getModified() {
        return modified;
    }

    public String getTag() {
        return getClass().getSimpleName() + "-" + getId();
    }

    public String getLockKey() {
        return getClass().getName() + "-" + getId();
    }
}
