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
        double actual = kvittering.getSamletBeloeb();
        assertEquals(expected,actual,0);
    }
    @Test
public void testParsePantBeløbFail(){
        Kvittering kvittering = new Kvittering(1,2,4,1,"15.00","24-05-2025");
     double expected = 10.98;
     double actual = kvittering.getSamletBeloeb();
        assertEquals(expected,actual,0.01);
}
}