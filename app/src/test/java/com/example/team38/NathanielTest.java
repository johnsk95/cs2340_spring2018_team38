package com.example.team38;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by Nathaniel on 4/16/18.
 * Test to for failOnMen
 */
public class NathanielTest {

    /**
     * see above
     * @throws Exception when something goes wrong
     */
    @Test
    public void testMenDoesNotMatch() throws Exception {
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
        HomelessShelter shelter = new HomelessShelter(shelter_dictionary);

        // failOnMen == false if men should be included in the search
        // true if does not meet the search criteria
        shelter.allowed = "women";
        assertTrue(ShelterSearch.failOnMen(shelter, true));
        assertFalse(ShelterSearch.failOnMen(shelter, false));

        shelter.allowed = "men";
        assertFalse(ShelterSearch.failOnMen(shelter, true));
        assertFalse(ShelterSearch.failOnMen(shelter, false));
    }
}
