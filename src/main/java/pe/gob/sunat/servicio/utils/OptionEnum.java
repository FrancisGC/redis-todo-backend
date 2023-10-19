package pe.gob.sunat.servicio.utils;

public enum OptionEnum {
    FRECUENTE(0),
    TAREA_DIARIA(1)
    ;

    final Integer value; 
    OptionEnum(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
