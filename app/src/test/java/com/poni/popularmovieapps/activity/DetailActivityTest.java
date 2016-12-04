package com.poni.popularmovieapps.activity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Dwiki on 12/4/2016.
 */
public class DetailActivityTest {

    @Test
    public void testGetApplicationContext() throws Exception {
        assertNotNull(new DetailActivity().getApplicationContext());
    }
}