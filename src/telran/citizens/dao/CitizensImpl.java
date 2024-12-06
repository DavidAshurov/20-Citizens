package telran.citizens.dao;

import telran.citizens.model.Person;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class CitizensImpl implements Citizens{
    private List<Person> idList;
    private List<Person> lastNameList;
    private List<Person> ageList;
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
        this.idList = new ArrayList<>();
        this.lastNameList = new ArrayList<>();
        this.ageList = new ArrayList<>();
    }

    public CitizensImpl(List<Person> citizens) {
        this.idList = new ArrayList<>(citizens);
        this.lastNameList = new ArrayList<>(citizens);
        this.ageList = new ArrayList<>(citizens);
        Collections.sort(this.idList);
        Collections.sort(this.lastNameList,lastNameComparator);
        Collections.sort(this.ageList,ageComparator);
    }

    @Override
    public boolean add(Person person) {
        if (person == null || find(person.getId()) != null) {
            return false;
        }
        int index = Collections.binarySearch(idList,person);
        index = -index - 1;
        idList.add(index,person);
        index = Collections.binarySearch(lastNameList,person,lastNameComparator);
        index = -index - 1;
        lastNameList.add(index,person);
        index = Collections.binarySearch(ageList,person,ageComparator);
        index = -index - 1;
        ageList.add(index,person);
        size++;
        return true;
    }

    @Override
    public boolean remove(int id) {
        Person removed = find(id);
        if (removed == null) {
            return false;
        }
        idList.remove(removed);
        lastNameList.remove(removed);
        ageList.remove(removed);
        size--;
        return true;
    }

    @Override
    public Person find(int id) {
        Person pattern = new Person(id,null,null,null);
        int index = Collections.binarySearch(idList,pattern);
        return index >= 0 ? idList.get(index) : null;
    }

    @Override
    public Iterable<Person> find(int minAge, int maxAge) {
        Person pattern = new Person(Integer.MIN_VALUE,null,null, LocalDate.now().minusYears(minAge));
        int minIndex = Collections.binarySearch(ageList,pattern,ageComparator);
        minIndex = -minIndex - 1;
        pattern = new Person(Integer.MAX_VALUE,null,null, LocalDate.now().minusYears(maxAge));
        int maxIndex = Collections.binarySearch(ageList,pattern,ageComparator);
        maxIndex = -maxIndex - 1;
        return new ArrayList<>(ageList.subList(minIndex,maxIndex));
    }

    @Override
    public Iterable<Person> find(String lastName) {
        Person pattern = new Person(Integer.MIN_VALUE,null,lastName, null);
        int minIndex = Collections.binarySearch(lastNameList,pattern,lastNameComparator);
        minIndex = -minIndex - 1;
        pattern = new Person(Integer.MAX_VALUE,null,lastName, null);
        int maxIndex = Collections.binarySearch(lastNameList,pattern,lastNameComparator);
        maxIndex = -maxIndex - 1;
        return new ArrayList<>(lastNameList.subList(minIndex,maxIndex));
    }

    @Override
    public Iterable<Person> getAllPersonsSortedById() {
        return new ArrayList<>(idList);
    }

    @Override
    public Iterable<Person> getAllPersonsSortedByAge() {
        return new ArrayList<>(ageList);
    }

    @Override
    public Iterable<Person> getAllPersonsSortedByLastName() {
        return new ArrayList<>(lastNameList);
    }

    @Override
    public int size() {
        return size;
    }
}
