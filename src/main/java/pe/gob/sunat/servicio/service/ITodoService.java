package pe.gob.sunat.servicio.service;

import pe.gob.sunat.servicio.entity.Todo;

import java.util.Optional;

public interface ITodoService {
    public Iterable<Todo> findAll();
    public Optional<Todo> findById(String id);
    public void save(Todo todo);
    public void deleteById(String id);
}
