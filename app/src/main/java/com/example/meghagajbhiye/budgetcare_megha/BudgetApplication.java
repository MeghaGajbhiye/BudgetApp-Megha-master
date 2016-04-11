package com.example.meghagajbhiye.budgetcare_megha;

import android.app.Application;

/**
 * Created by global on 4/10/16.
 */
public class BudgetApplication extends Application
{

    private float remainingBudget;

    protected void setRemainingBudget(float remainingBudget)
    {
        this.remainingBudget = remainingBudget;
    }

    protected float getRemainingBudget()
    {
        return this.remainingBudget;
    }

}
