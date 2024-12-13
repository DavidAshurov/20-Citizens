package telran.citizens.dao;

import telran.citizens.model.Person;

import java.time.LocalDate;
import java.util.*;

public class CitizensImpl implements Citizens{
    private TreeSet<Person> idSet;
    private TreeSet<Person> lastNameSet;
    private TreeSet<Person> ageSet;
    private int size;
    private static final Comparator<Person> ageComparator = (p1, p2) -> {
        int res = Integer.compare(p1.getAge(),p2.getAge());
        return res != 0 ? res : Integer.compare(p1.getId(),p2.getId());
    };
    private static final Comparator<Person> lastNameComparator = (p1,p2) -> {
        int res = p1.getLastName().compareTo(p2.getLastName());
        return res != 0 ? res : Integer.compare(p1.getId(),p2.getId());
    };

    public CitizensImpl() {
        this.idSet = new TreeSet<>();
        this.lastNameSet = new TreeSet<>(lastNameComparator);
        this.ageSet = new TreeSet<>(ageComparator);
    }

    public CitizensImpl(List<Person> citizens) {
        this.idSet = new TreeSet<>(citizens);
        this.lastNameSet = new TreeSet<>(lastNameComparator);
        this.ageSet = new TreeSet<>(ageComparator);
        this.lastNameSet.addAll(citizens);
        this.ageSet.addAll(citizens);
        size = this.idSet.size();
    }

    //O(n) -> O(log(n))
    @Override
    public boolean add(Person person) {
        if (person == null || find(person.getId()) != null) {
            return false;
        }
        idSet.add(person);
        lastNameSet.add(person);
        ageSet.add(person);
        size++;
        return true;
    }

    //O(n) -> O(log(n))
    @Override
    public boolean remove(int id) {
        Person removed = find(id);
        if (removed == null) {
            return false;
        }
        idSet.remove(removed);
        lastNameSet.remove(removed);
        ageSet.remove(removed);
        size--;
        return true;
    }

    //O(log(n))
    @Override
    public Person find(int id) {
        Person pattern = new Person(id,null,null,null);
        Person res = idSet.floor(pattern);
        return (res != null && res.getId() == id) ? res : null;
    }

    //O(log(n))
    @Override
    public Iterable<Person> find(int minAge, int maxAge) {
        Person leftPattern = new Person(Integer.MIN_VALUE,null,null, LocalDate.now().minusYears(minAge));
        Person rightPattern = new Person(Integer.MAX_VALUE,null,null, LocalDate.now().minusYears(maxAge));
        return new TreeSet<>(ageSet.subSet(leftPattern,true,rightPattern,true));
    }

    //O(log(n))
    @Override
    public Iterable<Person> find(String lastName) {
        Person leftPattern = new Person(Integer.MIN_VALUE,null,lastName, null);
        Person rightPattern = new Person(Integer.MAX_VALUE,null,lastName, null);
        return new TreeSet<>(lastNameSet.subSet(leftPattern,true,rightPattern,true));
    }

    //O(1)
    @Override
    public Iterable<Person> getAllPersonsSortedById() {
        return new TreeSet<>(idSet);
    }

    //O(1)
    @Override
    public Iterable<Person> getAllPersonsSortedByAge() {
        return new TreeSet<>(ageSet);
    }

    //O(1)
    @Override
    public Iterable<Person> getAllPersonsSortedByLastName() {
        return new TreeSet<>(lastNameSet);
    }

    //O(1)
    @Override
    public int size() {
        return size;
    }
}
