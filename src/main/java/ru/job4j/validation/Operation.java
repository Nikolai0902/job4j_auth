package ru.job4j.validation;

/**
 * Перечень классов маркеров,
 * для указания ситуаций, для которых нужно делать валидацию.
 */
public class Operation {

    public interface OnCreate { }

    public interface OnUpdate { }

    public interface OnDelete { }

}
