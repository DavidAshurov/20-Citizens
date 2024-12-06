package telran.citizens.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import telran.citizens.dao.Citizens;
import telran.citizens.dao.CitizensImpl;
import telran.citizens.model.Person;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CitizensTest {
    private Citizens citizens;
    private Person[] persons;
    private static final Comparator<Person> ageComparator = (p1, p2) -> {
        int res = Integer.compare(p1.getAge(),p2.getAge());
        return res != 0 ? res : Integer.compare(p1.getId(),p2.getId());
    };
    private static final Comparator<Person> lastNameComparator = (p1,p2) -> {
        int res = p1.getLastName().compareTo(p2.getLastName());
        return res != 0 ? res : Integer.compare(p1.getId(),p2.getId());
    };
    @BeforeEach
    void setUp() {
        citizens = new CitizensImpl();
        persons = new Person[5];
        persons[0] = new Person(1,"David","Ashurov", LocalDate.of(2000,10,6));
        persons[1] = new Person(2,"Dari","Ashurov", LocalDate.of(1996,5,11));
        persons[2] = new Person(3,"Dani","Nihaichuck", LocalDate.of(2000,9,30));
        persons[3] = new Person(4,"Ivan","Gershanikov", LocalDate.of(2004,9,27));
        persons[4] = new Person(5,"Ilan","Bondar", LocalDate.of(2000,11,11));
        for (int i = 0; i < persons.length; i++) {
            citizens.add(persons[i]);
        }
    }

    @Test
    void testAdd() {
        assertFalse(citizens.add(null));
        assertFalse(citizens.add(persons[2]));
        assertTrue(citizens.add(new Person(6,"Petya","Sidorov",LocalDate.now().minusYears(10))));
        assertEquals(6,citizens.size());
    }

    @Test
    void testRemove() {
        assertFalse(citizens.remove(6));
        assertTrue(citizens.remove(1));
        assertEquals(4,citizens.size());
    }

    @Test
    void testFind() {
        assertNull(citizens.find(6));
        assertEquals(persons[3],citizens.find(4));
    }

    @Test
    void testFindBetweenAges() {
        List<Person> actual = (List<Person>) citizens.find(20,24);
        Collections.sort(actual,ageComparator);
        Person[] expected = new Person[]{persons[3], persons[0], persons[2], persons[4]};
        int i = 0;
        for (Person p : actual) {
            assertEquals(expected[i],p);
            i++;
        }
        actual = (List<Person>) citizens.find(30,35);
        assertEquals(0,actual.size());
    }

    @Test
    void testFindByLastName() {
        List<Person> actual = (List<Person>) citizens.find("Ashurov");
        Collections.sort(actual,lastNameComparator);
        Person[] expected = new Person[]{persons[0], persons[1]};
        int i = 0;
        for (Person p : actual) {
            assertEquals(expected[i],p);
            i++;
        }
        actual = (List<Person>) citizens.find("Sidorov");
        assertEquals(0,actual.size());
    }

    @Test
    void testGetAllPersonsSortedById() {
        List<Person> actual = (List<Person>) citizens.getAllPersonsSortedById();
        int i = 0;
        for (Person p : actual) {
            assertEquals(persons[i],p);
            i++;
        }
    }

    @Test
    void testGetAllPersonsSortedByAge() {
        List<Person> actual = (List<Person>) citizens.getAllPersonsSortedByAge();
        Arrays.sort(persons,ageComparator);
        int i = 0;
        for (Person p : actual) {
            assertEquals(persons[i],p);
            i++;
        }
    }

    @Test
    void testGetAllPersonsSortedByLastName() {
        List<Person> actual = (List<Person>) citizens.getAllPersonsSortedByLastName();
        Arrays.sort(persons,lastNameComparator);
        int i = 0;
        for (Person p : actual) {
            assertEquals(persons[i],p);
            i++;
        }
    }

    @Test
    void testSize() {
        assertEquals(5,citizens.size());
    }
}