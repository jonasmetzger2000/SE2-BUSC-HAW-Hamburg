package de.haw_hamburg.mensamatch.domain.criteria.service;

import de.haw_hamburg.mensamatch.domain.criteria.CriteriaRepository;
import de.haw_hamburg.mensamatch.domain.criteria.model.CriteriaSelection;
import de.haw_hamburg.mensamatch.domain.criteria.model.Criterum;
import de.haw_hamburg.mensamatch.domain.user.UserRepository;
import de.haw_hamburg.mensamatch.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CriteriaService {

    private final UserRepository userRepository;
    private final CriteriaRepository criteriaRepository;

    public boolean addNewIncludeCriteria(String username, Set<String> criteria) {
        final Optional<User> optionalUser = userRepository.findUser(username);
        if (optionalUser.isPresent()) {
            final User user = optionalUser.get();
            final CriteriaSelection criteriaForUser = criteriaRepository.getCriteriaForUser(user.getId());
            try {
                final Set<Criterum> collect = criteria.stream().map(Criterum::valueOf).collect(Collectors.toSet());
                final boolean added = criteriaForUser.getInclude().addAll(collect);
                return added && criteriaRepository.updateCriteria(criteriaForUser);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return false;
    }

    public boolean addNewExcludeCriteria(String username, Set<String> criteria) {
        final Optional<User> optionalUser = userRepository.findUser(username);
        if (optionalUser.isPresent()) {
            final User user = optionalUser.get();
            final CriteriaSelection criteriaForUser = criteriaRepository.getCriteriaForUser(user.getId());
            try {
                final Set<Criterum> collect = criteria.stream().map(Criterum::valueOf).collect(Collectors.toSet());
                final boolean added = criteriaForUser.getExclude().addAll(collect);
                return added && criteriaRepository.updateCriteria(criteriaForUser);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return false;
    }

    public boolean removeIncludeCriteria(String username, Set<String> criteria) {
        final Optional<User> optionalUser = userRepository.findUser(username);
        if (optionalUser.isPresent()) {
            final User user = optionalUser.get();
            final CriteriaSelection criteriaForUser = criteriaRepository.getCriteriaForUser(user.getId());
            try {
                final Set<Criterum> collect = criteria.stream().map(Criterum::valueOf).collect(Collectors.toSet());
                final boolean removed = criteriaForUser.getInclude().removeAll(collect);
                return removed && criteriaRepository.updateCriteria(criteriaForUser);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return false;
    }

    public boolean removeExcludeCriteria(String username, Set<String> criteria) {
        final Optional<User> optionalUser = userRepository.findUser(username);
        if (optionalUser.isPresent()) {
            final User user = optionalUser.get();
            final CriteriaSelection criteriaForUser = criteriaRepository.getCriteriaForUser(user.getId());
            try {
                final Set<Criterum> collect = criteria.stream().map(Criterum::valueOf).collect(Collectors.toSet());
                final boolean removed = criteriaForUser.getExclude().removeAll(collect);
                return removed && criteriaRepository.updateCriteria(criteriaForUser);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return false;
    }

    public Set<Criterum> getIncludeCriteria(String username) {
        final Optional<User> optionalUser = userRepository.findUser(username);
        if (optionalUser.isPresent()) {
            final User user = optionalUser.get();
            return criteriaRepository.getCriteriaForUser(user.getId()).getInclude();
        }
        return new HashSet<>();
    }

    public Set<Criterum> getExcludeCriteria(String username) {
        final Optional<User> optionalUser = userRepository.findUser(username);
        if (optionalUser.isPresent()) {
            final User user = optionalUser.get();
            return criteriaRepository.getCriteriaForUser(user.getId()).getExclude();
        }
        return new HashSet<>();
    }

    public Set<Criterum> availableCriterias() {
        return Set.of(Criterum.values());
    }
}
