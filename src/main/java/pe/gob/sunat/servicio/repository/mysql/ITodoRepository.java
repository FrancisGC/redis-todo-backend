package pe.gob.sunat.servicio.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.sunat.servicio.entity.Todo;

public interface ITodoRepository extends JpaRepository<Todo, String> {
}
