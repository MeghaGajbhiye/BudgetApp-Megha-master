package com.example.meghagajbhiye.budgetcare_megha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;



public class Budget extends ActionBarActivity {
    private Spinner year;
    private Spinner month;

    private EditText expense;
    private Button saveBtn;
    private BudgetDA budgetDA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        year = (Spinner) findViewById(R.id.year);
        month = (Spinner) findViewById(R.id.month);
        expense = (EditText) findViewById(R.id.expense);
        saveBtn = (Button) findViewById(R.id.setBudget);

        this.budgetDA = new BudgetDA(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showToast(View v){
        Toast.makeText(Budget.this, "Successfully Saved", Toast.LENGTH_SHORT).show();
    }

    public void onClickSave(View v) {

        try {
            int year = Integer.valueOf((String) this.year.getSelectedItem());
            String month = (String) this.month.getSelectedItem();
            float exp = Float.valueOf(expense.getText().toString());

            if(exp<=0 ){
                Toast.makeText(Budget.this, "Please enter your budgeted Earnings and expense", Toast.LENGTH_SHORT).show();
            }else {
                // add the budget information to  to database
                BudgetB setBudget = budgetDA.saveBudgetInfo(year, month,exp);
                Toast.makeText(Budget.this, "Successfully Saved", Toast.LENGTH_SHORT).show();
                budgetDA.close();
                Intent i;
                i=new Intent(Budget.this,BudgetCareHome.class);
                startActivity(i);
            }

        }catch(Exception ex){
            Toast.makeText(Budget.this, "Error while saving.", Toast.LENGTH_SHORT).show();
        }

    }
}
