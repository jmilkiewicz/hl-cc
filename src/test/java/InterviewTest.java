import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class InterviewTest {

    private static Stream<Person> interviewTask(List<String> sourceUserList) {
        final Stream<Person> personStream = sourceUserList.stream()
                .flatMap(
                        l -> Arrays.stream(l.split(" "))
                                .map(lp -> {
                                    final String[] split = lp.split(",");
                                    return new Person(split[0], Integer.valueOf(split[1]));
                                })
                );

        return personStream;
    }


    private record DuplicatesResult(String name, Integer occurences) {
    }

    ;

    private static List<DuplicatesResult> min3Duplicates(List<String> sourceUserList) {
        final Stream<Person> personStream = interviewTask(sourceUserList);

        final Map<String, List<Person>> groupedByName = personStream.collect(Collectors.groupingBy(p -> p.name));

        final List<DuplicatesResult> result = groupedByName.entrySet()
                .stream()
                .filter(pair -> pair.getValue()
                        .size() >= 3)
                .map(pair -> new DuplicatesResult(pair.getKey(), pair.getValue()
                        .size()))
                .toList();


        return result;
    }


    private record Person(String name, Integer age, UUID id) {
        public Person(String name, Integer age) {
            this(name, age, UUID.randomUUID());
        }

        public static Person empty = new Person(null, null, UUID.randomUUID());

        public Person withName(String name) {
            return new Person(name, this.age, this.id);
        }

        public Person withAge(Integer age) {
            return new Person(this.name, age, this.id);
        }
    }

    public static class InterviewTask {


        private static void interviewTask(List<String> sourceUserList) {
            final Stream<Person> personStream = sourceUserList.stream()
                    .flatMap(
                            l -> Arrays.stream(l.split(" "))
                                    .map(lp -> {
                                        final String[] split = l.split(",");
                                        return new Person(split[0], Integer.valueOf(split[1]));
                                    })
                    );
        }

        static List<String> input = List.of(
                "KAROLINA,22 ANNA,32 KAROLINA,41",
                "KAROLINA,26 ANNA,19 ANNA,35",
                "MAGDA,29 MAGDA,24 EWA,31",
                "KAROLINA,40 NATALIA,25 EWA,38",
                "KATARZYNA,22 KAROLINA,30 JOANNA,35",
                "ANNA,28 PAULINA,24 MAGDA,37");


        // TODO
        // 1. Create class Person that has name, age and ID properties (data types are up to you)
        // 2. Use parameter 'sourceUserList' to create and display collection of Persons. Ensure that each Person has unique ID
        // 3. Display names with number of occurrences that have at least 3 occurrences in sourceUserList
        // 4. <Optional> - Write builder for Person class (without using any plugins or annotations)


    }

    @Test
    void canCreatePerson() {
        final Person adam = new Person("Adam", 22);

        assertThat(adam.name, is("Adam"));
        assertThat(adam.age, is(22));
        assertThat(adam.id, notNullValue());

    }


    @Test
    void canCreatePersonViaBuilder() {
        final Person prototype = Person.empty.withName("Adam");

        final Person adultAdam = prototype.withAge(22);
        final Person underAgeAdam = prototype.withAge(12);

        assertThat(adultAdam.name, is("Adam"));
        assertThat(adultAdam.age, is(22));


        assertThat(underAgeAdam.name, is(prototype.name));
        assertThat(underAgeAdam.age, is(12));


        assertThat(prototype.name, is("Adam"));
        assertThat(prototype.age, Matchers.nullValue());
        assertThat(prototype.id, notNullValue());

    }

    @Test
    void canParseToPersons() {


        final Stream<Person> personStream = interviewTask(List.of("KAROLINA,22 ANNA,32", "KAROLINA,26 ZUZIA,35"));

        final List<Person> persons = personStream.toList();


        //Hamcrest hasProperty does not work for records
        assertThat(persons, containsInAnyOrder(
                        both(hasProperty("name", is("KAROLINA"))).and(hasProperty("age", is(22))),
                        both(hasProperty("name", is("ANNA"))).and(hasProperty("age", is(32))),
                        both(hasProperty("name", is("KAROLINA"))).and(hasProperty("age", is(26))),
                        both(hasProperty("name", is("ZUZIA"))).and(hasProperty("age", is(35)))

                )
        );

    }

    @Test
    void canFindDuplicates() {

        final List<DuplicatesResult> duplicatesResults = min3Duplicates(List.of("KAROLINA,22 ANNA,32 KAROLINA,41 KAROLINA,21"));

        assertThat(duplicatesResults, containsInAnyOrder(new DuplicatesResult("KAROLINA", 3)));

    }
}
