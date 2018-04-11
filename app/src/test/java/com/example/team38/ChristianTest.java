package com.example.team38;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Test to make sure that shelters compare as equal when they should
 */
public class ChristianTest {
    /**
     * @throws Exception When Something goes wrong
     */
    @Test
    public void homeless_shelter_equals_is_correct() throws Exception {
        Object notAShelter = new Object();
        HashMap<String, Object> shelter_dictionary = new HashMap<>();
        shelter_dictionary.put("id", 0L);
        shelter_dictionary.put("name", "a");
        shelter_dictionary.put("capacity", 100L);
        shelter_dictionary.put("allowed", "men");
        shelter_dictionary.put("latitude", 20.0);
        shelter_dictionary.put("longitude", 30.0);
        shelter_dictionary.put("address", "SomeAddress");
        shelter_dictionary.put("services", "bed");
        shelter_dictionary.put("phone", "555-5555");
        HomelessShelter a = new HomelessShelter(shelter_dictionary);

        //if(o instanceof HomelessShelter) {
            assertEquals(a, new HomelessShelter(shelter_dictionary)); //All equal
            shelter_dictionary.put("id", 1L);
            assertNotEquals(a, new HomelessShelter(shelter_dictionary)); //id unequal
            shelter_dictionary.put("id", 0L);

            shelter_dictionary.put("name", "b");
            assertNotEquals(a, new HomelessShelter(shelter_dictionary)); //name unequal
            shelter_dictionary.put("name", "a");

            shelter_dictionary.put("latitude", 80.0);
            assertNotEquals(a, new HomelessShelter(shelter_dictionary)); //latitude unequal
            shelter_dictionary.put("latitude", 20.0);

            shelter_dictionary.put("longitude", 90.0);
            assertNotEquals(a, new HomelessShelter(shelter_dictionary)); //longitude unequal
            shelter_dictionary.put("longitude", 30.0);

            shelter_dictionary.put("address", "OtherAddress");
            assertNotEquals(a, new HomelessShelter(shelter_dictionary)); //address unequal
            shelter_dictionary.put("address", "SomeAddress");

            shelter_dictionary.put("services", "something");
            assertNotEquals(a, new HomelessShelter(shelter_dictionary)); //shelterType unequal
            shelter_dictionary.put("services", "bed");

            shelter_dictionary.put("phone", "666-6666");
            assertNotEquals(a, new HomelessShelter(shelter_dictionary)); //phone unequal
            shelter_dictionary.put("phone", "555-5555");

            //All booleans in return statement have been individually tested.

        //} else {
        assertNotEquals(a, notAShelter);
        //}

    }
    /*
    @Override
    public boolean equals(Object o) {
        if(o instanceof HomelessShelter) {
            HomelessShelter s = (HomelessShelter) o;
            return (id == s.id) && name.equals(s.name) && (latitude == s.latitude)
                    && (longitude == s.longitude) && (address.equals(s.address)) &&
                    (shelterType.equals(s.shelterType)) && (phoneNumber.equals(s.phoneNumber));
        }
        return false;
    }
    */
}
