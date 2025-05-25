package org.beginningandroid.interactivetest;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MyDatabaseHelperTest {
    @Test
    public void testInsertAndRetreivePant(){
        Context context = ApplicationProvider.getApplicationContext();
        MyDatabaseHelper db = new MyDatabaseHelper(context);
        Kvittering kv = new Kvittering();
        db.insertKvittering(kv);
        double saldo = db.beregnTotalSaldo();
        assertEquals(43.5,43.5,0.01);
    }

}