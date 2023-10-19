package pe.gob.sunat.servicio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.sunat.servicio.entity.Todo;
import pe.gob.sunat.servicio.service.ITodoService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/todo")
@CrossOrigin(origins = "http://localhost:8080/")
public class TodoController {

    @Autowired
    private ITodoService todoService;

    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(todoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            Optional<Todo> todo = todoService.findById(id);
            if (todo.isPresent()) {
                return ResponseEntity.ok(todo.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> save(@RequestBody Todo todo) {
        try {
            todoService.save(todo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("respuesta","Ok");
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            todoService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("respuesta","Ok");
        return ResponseEntity.ok(respuesta);
    }
}
