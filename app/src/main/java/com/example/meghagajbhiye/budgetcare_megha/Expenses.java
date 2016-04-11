package com.example.meghagajbhiye.budgetcare_megha;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Expenses extends FragmentActivity {
    Date currentDate = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String DateToString = dateFormat.format(currentDate);
    EditText eDate;
    private EditText eAmount;

    private Spinner categorySpinner;
    //private Button saveBtn;
    private TransactionDA transactionDA;
   // ImageButton addBut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        eDate = (EditText) findViewById(R.id.date);
        eDate.setText(DateToString);
        //addBut= (ImageButton) findViewById(R.id.imageButton2);
        categorySpinner = (Spinner) findViewById(R.id.categoryspinner);
        eAmount =(EditText) findViewById(R.id.amount);
        //noteText = (EditText) findViewById(R.id.notes);
        //saveBtn = (Button) findViewById(R.id.savebutton);

        this.transactionDA=new TransactionDA(this);

        fillCategorySpinner();
    }


    private void fillCategorySpinner()

    {
        CategoryDA mCategoryDA = new CategoryDA(this);
        ArrayList<Category> categoryList =(ArrayList) mCategoryDA.getAllCategories();


        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this,
                android.R.layout.simple_spinner_item, categoryList);

        categorySpinner.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cash_out, menu);
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

    public void showAddCategory(View v){
        Intent i;
        i=new Intent(Expenses.this,AddCategory.class);
        startActivity(i);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
        //newFragment.show(getSupportFragmentManager(), "datePicker");
        //setNewDate();

    }

    public void populateSetDate(int year, int month, int day) {
        eDate = (EditText) findViewById(R.id.date);
        eDate.setText(year + "-" + (month + 1) + "-" + day);
    }


    //Method which run when save button is pressed
    //Insert data to the database
    public void onClickSave(View v) {

        try {
            final Editable date = eDate.getText();


            final String category = ((Category) categorySpinner.getSelectedItem()).getCategory();
            //Editable amount = (Float) eAmount.getText();
            final float amount = Float.valueOf(eAmount.getText().toString());
           // final Editable notes = noteText.getText();
            if(amount<=0){
                Toast.makeText(Expenses.this, "Please enter the amount", Toast.LENGTH_SHORT).show();
            }else {

                float remainingBudget = ((BudgetApplication)getApplication()).getRemainingBudget();
                if(amount > remainingBudget)
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage(getString(R.string.title_exceeded_budget));

                    alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            addExpense(date.toString(), category, amount);
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else{
                    addExpense(date.toString(),category,amount);
                }

            }

        }catch(Exception ex){
            Toast.makeText(Expenses.this, "Error while saving.", Toast.LENGTH_SHORT).show();
        }

    }

    private void addExpense(String date,String category, float amount) {
        // add the transaction to database
        Transaction createdTransaction = transactionDA.createTransaction(date, "expense", category, amount);
        Toast.makeText(Expenses.this, "Successfully Saved", Toast.LENGTH_SHORT).show();
        transactionDA.close();
        Intent i;
        i=new Intent(Expenses.this,BudgetCareHome.class);
        startActivity(i);
    }

    public void showToast(View v){
        float amount=0;
        amount = Float.valueOf(eAmount.getText().toString());
        if(amount<=0){
            Toast.makeText(Expenses.this, "Please enter the amount", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(Expenses.this, "Successfully Saved", Toast.LENGTH_LONG).show();
            Intent i;
            i=new Intent(Expenses.this,BudgetCareHome.class);
            startActivity(i);
        }
    }

    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        public DatePickerFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            populateSetDate(year,month,day);
        }


    }



}
