package com.softserve.itacademy.todolist.service.impl;

import com.softserve.itacademy.todolist.exception.NullEntityReferenceException;
import com.softserve.itacademy.todolist.model.State;
import com.softserve.itacademy.todolist.repository.StateRepository;
import com.softserve.itacademy.todolist.service.StateService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;

    public StateServiceImpl(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public State create(State state) {
        if (state != null) {
            return stateRepository.save(state);
        }
        throw new NullEntityReferenceException("State cannot be 'null'");
    }

    @Override
    public State readById(long id) {
        return stateRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("State with id " + id + " not found"));
    }

    @Override
    public State update(State state) {
        if (state != null) {
            readById(state.getId());
            return stateRepository.save(state);
        }
        throw new NullEntityReferenceException("State cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        State state = readById(id);
        stateRepository.delete(state);
    }

    @Override
    public List<State> getAll() {
        return stateRepository.findByOrderByIdAsc();
    }

    @Override
    public State getByName(String name) {
        Optional<State> optional = Optional.ofNullable(stateRepository.findByName(name));
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new EntityNotFoundException("State with name '" + name + "' not found");
    }
}
