package de.uniluebeck.itm.mdcf.remote.noisetracker.server;

import java.util.List;

public interface Repository<T> {

	T findById(Object id);

    T update(T object);

    void persist(T object);

    void remove(T object);

    List<T> findAll();
}
