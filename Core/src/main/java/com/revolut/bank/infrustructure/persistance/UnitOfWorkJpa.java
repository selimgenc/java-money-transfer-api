package com.revolut.bank.infrustructure.persistance;

import com.revolut.bank.application.UnitOfWork;
import com.revolut.bank.domain.handling.DomainException;
import com.revolut.bank.domain.shared.DomainEntity;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnitOfWorkJpa implements UnitOfWork {
    private static final Logger logger = Logger.getLogger(UnitOfWorkJpa.class.getName());

    private Map<Register, List<DomainEntity>> context = new HashMap<>();
    private final EntityManager em;

    @Inject
    public UnitOfWorkJpa(final Provider<EntityManager> em) {
        this.em = em.get();

    }

    @Override
    public void registerNew(DomainEntity entity) {
        register(entity, Register.NEW);
    }

    @Override
    public void registerModified(DomainEntity entity) {
        register(entity, Register.UPDATE);
    }

    @Override
    public void registerDeleted(DomainEntity entity) {
        register(entity, Register.DELETE);
    }

    @Override
    public void beginTransaction() {
        if(!em.getTransaction().isActive()){
            em.getTransaction().begin();
        }
    }

    @Override
    public void commit() throws DomainException {
        try{
            for (Register register : context.keySet()) {
                switch (register){
                    case NEW:
                        for (DomainEntity entity : context.get(register)) {
                            em.persist(entity);
                        }
                        break;
                    case UPDATE:
                        for (DomainEntity entity : context.get(register)) {
                            em.merge(entity);
                        }
                        break;
                    case DELETE:
                        for (DomainEntity entity : context.get(register)) {
                            em.remove(entity);
                        }
                        break;
                }
            }
            em.getTransaction().commit();
        }catch (Exception ex){
            logger.log(Level.SEVERE, "Transactional Commit failed", ex);
            em.getTransaction().rollback();
            throw new DomainException("account.transfer.commitFailed");
        }

        context.clear();
        logger.log(Level.FINEST, "Commit Finished");
    }


    private void register(DomainEntity entity, Register type) {
        List<DomainEntity> entities = context.get(type);
        if (entities == null) {
            entities = new LinkedList<>();
        }
        entities.add(entity);
        context.put(type, entities);
        logger.log(Level.FINEST, "Registering {1} for {2} in context. ", new String[]{entity.toString(), type.name()});
    }


}
