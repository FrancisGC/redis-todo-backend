package pe.gob.sunat.servicio.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity(name = "todo")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Todo implements Serializable {
    @Id
    private String id;
    private String tarea;
    private Integer frecuencia;
    private Boolean completado;
    private Date creado;
}
