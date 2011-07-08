package de.uniluebeck.itm.mdcf.remote.locationtracker.server;

import java.util.List;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

@Transactional
public abstract class AbstractRepository<T> implements Repository<T> {

	private final Class<T> persistentClass;
	
	@Inject
	private Provider<EntityManager> entitiyManager;

    public AbstractRepository(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }
    
    @Override
    public void persist(T t) {
    	entitiyManager.get().persist(t);
    }

    @Override
    public T update(T t) {
        return entitiyManager.get().merge(t);
    }

    @Override
    public void remove(T t) {
    	entitiyManager.get().remove(t);
    }

    @Override
    public T findById(Object id) {
        return entitiyManager.get().find(this.persistentClass, id);
    }
    
    @Override
    public List<T> findAll() {
    	return entitiyManager.get().createQuery(
                String.format("SELECT x FROM %s x", persistentClass.getName()), persistentClass).getResultList();
    }
    
    public EntityManager getEntitiyManager() {
		return entitiyManager.get();
	}
}
