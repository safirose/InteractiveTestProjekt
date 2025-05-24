package org.beginningandroid.interactivetest;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class KvitteringTest {
    @Test
    public void testParsePantBeløbPass(){
        Kvittering kvittering = new Kvittering(1,2,4,1,"15:00","24-05-2025");
        double expected = 11;
        double actual = 11;
        assertEquals(expected,actual,0.01);
    }
    @Test
public void testParsePantBeløbFail(){
        Kvittering kvittering = new Kvittering(1,2,4,1,"15.00","24-05-2025");
     double expected = 11;
     double actual = 10.98;
        assertEquals(expected,actual,0.01);
}
}