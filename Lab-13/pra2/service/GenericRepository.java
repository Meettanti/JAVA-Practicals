package service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import model.annotation.Id;

public class GenericRepository<T extends Serializable> {
    private final Class<T> entityType;
    private final Field idField;
    private volatile ConcurrentHashMap<Object, T> entities = new ConcurrentHashMap<>();

    public GenericRepository(Class<T> entityType) {
        this.entityType = Objects.requireNonNull(entityType, "entityType");
        idField = findIdField(entityType);
        if (!idField.trySetAccessible()) {
            throw new IllegalArgumentException(
                    "The @Id field is not accessible: " + idField.getName());
        }
    }

    public synchronized T create(T entity) {
        Object id = idOf(entity);
        if (entities.putIfAbsent(id, entity) != null) {
            throw new IllegalArgumentException("Entity already exists for ID: " + id);
        }
        return entity;
    }

    public synchronized void createAll(Collection<? extends T> newEntities) {
        Objects.requireNonNull(newEntities, "newEntities");
        ConcurrentHashMap<Object, T> replacement = new ConcurrentHashMap<>(entities);
        for (T entity : newEntities) {
            Object id = idOf(entity);
            if (replacement.putIfAbsent(id, entity) != null) {
                throw new IllegalArgumentException("Entity already exists for ID: " + id);
            }
        }
        entities = replacement;
    }

    public Optional<T> readById(Object id) {
        if (id == null) {
            throw new IllegalArgumentException("Entity ID cannot be null.");
        }
        return Optional.ofNullable(entities.get(id));
    }

    public synchronized T update(T entity) {
        Object id = idOf(entity);
        if (entities.replace(id, entity) == null) {
            throw new IllegalArgumentException("No entity exists for ID: " + id);
        }
        return entity;
    }

    public synchronized Optional<T> delete(Object id) {
        if (id == null) {
            throw new IllegalArgumentException("Entity ID cannot be null.");
        }
        return Optional.ofNullable(entities.remove(id));
    }

    public List<T> findAll() {
        return List.copyOf(entities.values());
    }

    public final List<T> findByIds(Object... ids) {
        if (ids == null) {
            throw new IllegalArgumentException("ID list cannot be null.");
        }
        List<T> found = new ArrayList<>(ids.length);
        for (Object id : ids) {
            if (id == null) {
                throw new IllegalArgumentException("Entity ID cannot be null.");
            }
            T entity = entities.get(id);
            if (entity != null) {
                found.add(entity);
            }
        }
        return List.copyOf(found);
    }

    public synchronized void replaceAll(Collection<? extends T> replacementEntities) {
        Objects.requireNonNull(replacementEntities, "replacementEntities");
        ConcurrentHashMap<Object, T> replacement = new ConcurrentHashMap<>();
        for (T entity : replacementEntities) {
            Object id = idOf(entity);
            if (replacement.putIfAbsent(id, entity) != null) {
                throw new IllegalArgumentException("Duplicate entity ID: " + id);
            }
        }
        entities = replacement;
    }

    public int size() {
        return entities.size();
    }

    private Object idOf(T entity) {
        Objects.requireNonNull(entity, "entity");
        if (!entityType.isInstance(entity)) {
            throw new IllegalArgumentException(
                    "Expected " + entityType.getName() + " but received "
                            + entity.getClass().getName() + ".");
        }
        try {
            Object id = idField.get(entity);
            if (id == null) {
                throw new IllegalArgumentException("@Id value cannot be null.");
            }
            return id;
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException("Could not read the entity @Id field.", exception);
        }
    }

    private static Field findIdField(Class<?> entityType) {
        Field idField = null;
        for (Class<?> current = entityType; current != null && current != Object.class;
                current = current.getSuperclass()) {
            for (Field field : current.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Id.class)) {
                    continue;
                }
                if (Modifier.isStatic(field.getModifiers())) {
                    throw new IllegalArgumentException("@Id cannot annotate a static field.");
                }
                if (idField != null) {
                    throw new IllegalArgumentException(
                            "Entity type must declare exactly one @Id field.");
                }
                idField = field;
            }
        }
        if (idField == null) {
            throw new IllegalArgumentException(
                    "No @Id field found in " + entityType.getName() + ".");
        }
        return idField;
    }
}
