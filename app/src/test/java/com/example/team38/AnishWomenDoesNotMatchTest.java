package com.example.team38;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Test to make sure that ShelterSearch fails when looking specifically for women
 * and the given shelter does not allow women
 */
public class AnishWomenDoesNotMatchTest {

    /**
     * see above
     * @throws Exception when something goes wrong
     */
    @Test
    public void testWomenDoesNotMatch() throws Exception {
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

        // failOnWomen == "should fail because we're looking for method and it's not there"
        shelter.allowed = "men";
        assertTrue(ShelterSearch.failOnWomen(shelter, true));
        assertFalse(ShelterSearch.failOnWomen(shelter, false));

        shelter.allowed = "women";
        assertFalse(ShelterSearch.failOnWomen(shelter, true));
        assertFalse(ShelterSearch.failOnWomen(shelter, false));
    }
}
