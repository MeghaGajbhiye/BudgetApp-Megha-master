package com.example.meghagajbhiye.budgetcare_megha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class BudgetCareHome extends ActionBarActivity {

    Button income;
    Button expense;
    ImageButton settings;
    ImageButton status;
    private TransactionDA transactionDA;
    private TextView balanceText;
    ImageButton viewOverviewButton;
    ImageButton viewBudgetButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_care_home);
        income = (Button) findViewById(R.id.bIncome);
       expense = (Button) findViewById(R.id.bExpense);
        settings = (ImageButton) findViewById(R.id.settingsButton);
        status = (ImageButton) findViewById(R.id.chartsButton);
        viewOverviewButton = (ImageButton) findViewById(R.id.overviewButton);
        viewBudgetButton = (ImageButton) findViewById(R.id.budgetButton);
        balanceText = (TextView) findViewById(R.id.balance);


        this.transactionDA=new TransactionDA(this);

        CurrentBalance();
    }

    public void income(View v){
        Intent i;
        i=new Intent(BudgetCareHome.this,Earnings.class);
        startActivity(i);
    }

    public void expenditure(View v) {
        Intent i;
        i = new Intent(BudgetCareHome.this, Expenses.class);
        startActivity(i);
    }

    public void settingsOn(View v) {
        Intent i;
        i = new Intent(BudgetCareHome.this, Settings.class);
        startActivity(i);
    }
    public void exit(View v) {
        finish();
    }

    public void viewOverview(View v) {
        Intent i;
        i = new Intent(BudgetCareHome.this, Overview.class);
        startActivity(i);
    }

    public void setBudget(View v) {
        Intent i;
        i = new Intent(BudgetCareHome.this, Budget.class);
        startActivity(i);
    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget_care_home, menu);
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

    //Method to show table of transaction history
    private void CurrentBalance() {

        float currentBalance= (float) 0.0;

        List<Transaction> transactionList =new ArrayList<Transaction>();
        transactionList=transactionDA.getAllTransactions();
        int length=transactionList.size();

        // for loop to calculate balance amount
        for (int i = 0; i < length; i++) {
            if(transactionList.get(i).getIncomeorexpense().equalsIgnoreCase("Earnings")){
                currentBalance+=transactionList.get(i).getAmount();
            }
            else if(transactionList.get(i).getIncomeorexpense().equalsIgnoreCase("expense")){
                currentBalance-=transactionList.get(i).getAmount();
            }

        }

        String textB=currentBalance+"";
        balanceText.setText(textB);

        BudgetDA budgetDA = new BudgetDA(this);

        float budget = budgetDA.getBudgetForMonth(2016,"april");


        budgetDA.close();
        ((BudgetApplication)getApplication()).setRemainingBudget(budget);


    }
}
