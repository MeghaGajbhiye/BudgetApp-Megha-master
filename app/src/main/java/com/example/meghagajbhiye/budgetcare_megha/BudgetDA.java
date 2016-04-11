package com.example.meghagajbhiye.budgetcare_megha;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by Upekka on 9/8/2015.
 */
public class BudgetDA {
    public static final String TAG = "BudgetDA";

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private String[] mAllColumns = { DBHelper.COLUMN_YEAR, DBHelper.COLUMN_MONTH,DBHelper.COLUMN_BUDGET_EXPENSE};

    public BudgetDA(Context context) {
        mDbHelper = new DBHelper(context);
        this.mContext = context;
        // open the database
        try {
            open();
        }
        catch(SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public BudgetB saveBudgetInfo(int year,String month,float expense) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_YEAR, year);
        values.put(DBHelper.COLUMN_MONTH, month.toLowerCase());
        values.put(DBHelper.COLUMN_BUDGET_EXPENSE, expense);

        long insertId = mDatabase.insert(DBHelper.TABLE_BUDGET, null, values);
        Cursor cursor = mDatabase.query(DBHelper.TABLE_BUDGET, mAllColumns, null, null, null, null, null);
        cursor.moveToFirst();
        BudgetB newBudget = cursorToBudget(cursor);
        cursor.close();
        return newBudget;
    }

    public float getBudgetForMonth(int year,String month)
    {

        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_BUDGET + " WHERE "
                + DBHelper.COLUMN_YEAR + " = " + year;

        float budget = 0;
        try {
            Cursor cursor = mDatabase.rawQuery(selectQuery, null);

            if (cursor != null)
                cursor.moveToFirst();

            boolean hasFound = true;
            while(cursor != null && !cursor.isLast())
            {
                if(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_MONTH)).equalsIgnoreCase(month))
                {
                    hasFound = true;
                    budget = cursor.getFloat(cursor.getColumnIndex(DBHelper.COLUMN_BUDGET_EXPENSE));
                    break;
                }
            }
            {
                cursor.moveToNext();
            }

        } catch (Exception ex)
        {
            System.out.println("error = "+ex);
        }

        return budget;
    }



   /* public List<BudgetB> getAllBudgetInfo() {
        List<BudgetB> listBudgets = new ArrayList<BudgetB>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_BUDGET,
                mAllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BudgetB budget = cursorToBudget(cursor);
            listBudgets.add(budget);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return listBudgets;
    }
*/

    private BudgetB cursorToBudget(Cursor cursor) {
        BudgetB budget = new BudgetB();
        budget.setYear(cursor.getInt(0));
        budget.setMonth(cursor.getString(1));
        budget.setIncome(cursor.getFloat(2));

        return budget;
    }
}
