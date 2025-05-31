package repository;

import java.util.List;

public interface CRUDInterface<T> {
    T create(T obj);
    T findById(int id);
    List<T> getAll();
    void delete(int id);
}
