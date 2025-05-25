package org.beginningandroid.interactivetest;

import static org.junit.Assert.assertEquals;
import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SaldoActivityTest {
    private MyDatabaseHelper db;

    @Before
    public void setUp(){
        Context context = ApplicationProvider.getApplicationContext();
        db = new MyDatabaseHelper(context);
    }
@Test
    public void testSaldoCalculation(){
    db.insertKvittering(new Kvittering(1,4,3,5,"12:24","25-05-2025"));
    db.insertKvittering(new Kvittering(2,8,4,2,"12:26","25-05-2025"));
    double saldo = db.beregnTotalSaldo();
    assertEquals(43.5,saldo,0.01);

}
}