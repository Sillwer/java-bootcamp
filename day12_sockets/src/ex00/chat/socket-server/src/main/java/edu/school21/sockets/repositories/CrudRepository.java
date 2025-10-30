package edu.school21.sockets.repositories;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    void save(T entity);

    Optional<T> findById(Long id);

    List<T> findAll();

    void update(T entity);

    void delete(Long id);
}
