package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the plus (+) button is clicked.
     *
     * @param view to display
     */
    public void increment(View view) {

        if (quantity < 100) {
            quantity++;
        } else {
            //show an error message as a toast
            Toast.makeText(this, getString(R.string.toast_100, quantity), Toast.LENGTH_SHORT).show();
        }

        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus (-) button is clicked.
     *
     * @param view to display
     */
    public void decrement(View view) {

        if (quantity > 1) {
            quantity--;
        } else {
            //show an error message as a toast
            Toast.makeText(this, getString(R.string.toast_001, quantity), Toast.LENGTH_SHORT).show();
        }

        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     * Checks if the user wants: whipped cream, chocolate
     * Calls calculatePrice() method to set final price
     * Calls sendEmail() method to send email request
     */
    public void submitOrder(View view) {

        // Check name in name_edit EditText
        EditText nameField = findViewById(R.id.name_field);
        String userName = nameField.getText().toString();

        // Check the status of whipped_cream_checkbox CheckView
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        // Check the status of chocolate_checkbox CheckView
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        // Call CalculatePrice method
        int price = calculatePrice(hasWhippedCream, hasChocolate);

        // Send Email
        String appName = getResources().getString(R.string.app_name);       // Get App name
        String emailSubject = getString(R.string.email_subject, appName, userName);           // Sets subject
        String emailText = createOrderSummary(userName, price, hasWhippedCream, hasChocolate);  // Sets text
        composeEmail(emailSubject, emailText);
    }

    /**
     * Create summary of the order
     *
     * @param price           of the order
     * @param hasWhippedCream is whether or not the user wants whipped cream
     * @param hasChocolate    is whether or not the user wants chocolate
     * @param userName        is the name the user wrote
     * @return priceMessage text summary
     */
    private String createOrderSummary(String userName, int price, boolean hasWhippedCream, boolean hasChocolate) {
        return getString(R.string.order_summary_name, userName)
                + "\n" + getString(R.string.order_summary_quantity, quantity)
                + "\n" + getString(R.string.order_summary_whipped_cream, hasWhippedCream)
                + "\n" + getString(R.string.order_summary_chocolate, hasChocolate)
                + "\n" + getString(R.string.order_summary_price, NumberFormat.getCurrencyInstance().format(price))
                + "\n" + getString(R.string.thank_you);
    }

    /**
     * Calculates the price of the order based on the current quantity.
     *
     * @param hasWhippedCream is whether or not the user wants whipped cream
     * @param hasChocolate    is whether or not the user wants chocolate
     * @return the price
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {

        int pricePerCup = 5;

        // add 1€ if the user wants whipped cream
        if (hasWhippedCream) {
            pricePerCup += 1;
        }

        // add 2€ if the user wants chocolate
        if (hasChocolate) {
            pricePerCup += 2;
        }

        // calculate the total order price multiplying by quantity
        return pricePerCup * quantity;
    }

    /**
     * This method displays the given quantity value on the screen.
     *
     * @param numberOfCoffees in order
     */

    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText(getString(R.string.number_of_coffees, numberOfCoffees));
    }

    /**
     * This method uses an intent to send email.
     *
     * @param emailSubject of the order contains string userName
     * @param emailText    contains createOrderSummary() method output
     */

    public void composeEmail(String emailSubject, String emailText) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:eumenides.3@gmail.com")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        intent.putExtra(Intent.EXTRA_TEXT, emailText);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}