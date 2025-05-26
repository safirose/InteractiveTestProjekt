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
        int brugerId = db.getUserId("1");
        Kvittering kv = new Kvittering(1,4,3,5,"12:24","25-05-2025");
        db.insertKvittering(kv,brugerId);
        double saldo = db.beregnTotalSaldo();
        assertEquals(saldo,saldo,0.01);
    }

}