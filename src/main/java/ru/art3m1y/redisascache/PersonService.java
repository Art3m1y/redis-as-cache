package ru.art3m1y.redisascache;

import lombok.SneakyThrows;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "personCache")
public class PersonService {
    private final CacheManager cacheManager;
    private final PersonRepository personRepository;

    public PersonService(CacheManager cacheManager, PersonRepository personRepository) {
        this.cacheManager = cacheManager;
        this.personRepository = personRepository;
    }

    @Cacheable
    @SneakyThrows
    public List<Person> findAll() {
        Thread.sleep(1500);

        return personRepository.findAll();
    }

    @Cacheable()
    @SneakyThrows
    public Person find(String id) {
        Thread.sleep(1500);

        return personRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользователь с таким идентификатором не найден"));
    }

    @CachePut(key = "#person.id")
    @SneakyThrows
    public Person save(Person person) {
        Thread.sleep(1500);

        personRepository.save(person);

        return person;
    }

    @CachePut(key = "#person.id")
    @SneakyThrows
    public Person update(Person person) {
        Thread.sleep(1500);

        Optional<Person> personOptional = personRepository.findById(person.getId());

        if (personOptional.isPresent()) {
            personRepository.save(person);

            return person;
        }

        throw new RuntimeException("Пользователь с таким идентификатором не найден");
    }

    @CacheEvict(key = "#person.id")
    @SneakyThrows
    public void delete(String id) {
        Thread.sleep(1500);

        personRepository.deleteById(id);
    }
}
