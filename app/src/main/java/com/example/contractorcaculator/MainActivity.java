package com.example.contractorcaculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    final Double TAX_RATE = 0.05;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Limit data entry to 5 digits before and 2 digits after decimal
        EditText editLabor = findViewById(R.id.editLabor);
        editLabor.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        EditText editMaterials = findViewById(R.id.editMaterials);
        editMaterials.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});

        // Clear the initial text for the calculation results
        TextView textSubTotalAmount = findViewById(R.id.textSubTotalAmount);
        textSubTotalAmount.setText("");
        TextView textTaxAmount = findViewById(R.id.textTaxAmount);
        textTaxAmount.setText("");
        TextView textTotalAmount = findViewById(R.id.textTotalAmount);
        textTotalAmount.setText("");

        // Initialize the calculation button
        initButtonCalculate();
    }

    private void initButtonCalculate() {
        Button buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the labor amount (default to zero if an empty string)
                EditText editLabor = findViewById(R.id.editLabor);
                Double laborAmount = editLabor.getText().toString().isEmpty() ? 0: Double.parseDouble(editLabor.getText().toString());

                // Get the materials amount (default to zero if an empty string)
                EditText editMaterials = findViewById(R.id.editMaterials);
                Double materialsAmount = editMaterials.getText().toString().isEmpty() ? 0: Double.parseDouble(editMaterials.getText().toString());

                // Calculate the tax and total amounts
                Double subTotalAmount = laborAmount + materialsAmount;
                Double taxAmount = subTotalAmount * TAX_RATE;
                Double totalAmount = subTotalAmount + taxAmount;

                // Display the results
                Resources res = getResources();
                String displaySubTotalAmount = res.getString(R.string.dollar_amount, subTotalAmount);
                TextView textSubTotalAmount = findViewById(R.id.textSubTotalAmount);
                textSubTotalAmount.setText(displaySubTotalAmount);

                String displayTaxAmount = res.getString(R.string.dollar_amount, taxAmount);
                TextView textTaxAmount = findViewById(R.id.textTaxAmount);
                textTaxAmount.setText(displayTaxAmount);

                String displayTotalAmount = res.getString(R.string.dollar_amount, totalAmount);
                TextView textTotalAmount = findViewById(R.id.textTotalAmount);
                textTotalAmount.setText(displayTotalAmount);

            }
        });
    }

    // Limit decimal input from: https://www.tutorialspoint.com/how-to-limit-decimal-places-in-android-edittext
    class DecimalDigitsInputFilter implements InputFilter {
        private Pattern mPattern;
        DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }

}