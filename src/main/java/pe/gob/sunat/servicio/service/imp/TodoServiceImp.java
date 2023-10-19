package pe.gob.sunat.servicio.service.imp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import pe.gob.sunat.servicio.entity.Todo;
import pe.gob.sunat.servicio.repository.mysql.ITodoRepository;
import pe.gob.sunat.servicio.service.ITodoService;
import pe.gob.sunat.servicio.utils.OptionEnum;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class TodoServiceImp implements ITodoService {

    @Autowired
    private ITodoRepository repository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Todo> findAll() {
        List<Todo> todos = repository.findAll();
        String pattern = "todo*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null) {
            for (String k: keys) {
                Map<Object, Object> cachedValue = redisTemplate.opsForHash().entries(k);
                Integer frec = (int) cachedValue.get("frecuencia");
                if (frec.equals(OptionEnum.TAREA_DIARIA.getValue())) {
                    Todo todo = new Todo();
                    todo.setId(cachedValue.get("id").toString());
                    todo.setTarea(cachedValue.get("tarea").toString());
                    todo.setFrecuencia((int) cachedValue.get("frecuencia"));
                    todo.setCompletado((boolean) cachedValue.get("completado"));
                    todos.add(todo);
                }
            }
        }
        return todos;
    }

    @Override
    public Optional<Todo> findById(String id) {
        ObjectMapper objectMapper = new ObjectMapper();
        String key = "todo:" + id;

        Map<Object, Object> cachedValue = redisTemplate.opsForHash().entries(key);

        if (!cachedValue.isEmpty()) {
            Todo todo = new Todo();
            todo.setId(cachedValue.get("id").toString());
            todo.setTarea(cachedValue.get("tarea").toString());
            todo.setFrecuencia((int) cachedValue.get("frecuencia"));
            todo.setCompletado((boolean) cachedValue.get("completado"));
            return Optional.of(todo);
        } else {
            Optional<Todo> valueFromDatabase = repository.findById(id);

            if (valueFromDatabase.isPresent()) {
                Map<String, Object> mapObj = objectMapper.convertValue(valueFromDatabase.get(), Map.class);
                redisTemplate.opsForHash().putAll(key, mapObj);
            }

            return valueFromDatabase;
        }
    }

    @Override
    public void save(Todo todo) {
        String key = "todo:" + todo.getId();
        ObjectMapper objectMapper = new ObjectMapper();
        if (todo.getFrecuencia().equals(OptionEnum.FRECUENTE.getValue())) {
            repository.save(todo);
        }

        Map<String, Object> mapObj = objectMapper.convertValue(todo, Map.class);
        redisTemplate.opsForHash().putAll(key, mapObj);
    }

    @Override
    public void deleteById(String id) {
        String key = "todo:" + id;
        Map<Object, Object> cachedValue = redisTemplate.opsForHash().entries(key);

        if (!cachedValue.isEmpty()) {
            Integer frec = (int) cachedValue.get("frecuencia");
            if (frec.equals(OptionEnum.FRECUENTE.getValue())) {
                repository.deleteById(id);
            }
            for(Object k : redisTemplate.opsForHash().keys(key)) {
                redisTemplate.opsForHash().delete(key, k.toString());
            }
        }
    }
}
