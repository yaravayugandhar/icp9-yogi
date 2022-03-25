package com.example.class9;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    // declare variables
    private EditText UserName, UserEmailAddress;
    private Spinner userPizzaSize;
    private CheckBox Cheese, Pappers,ExtraCheese,BlackOlives;
    private TextView numberOfPizzas;

    // used CurrentPizzaSize for  pizza size based on spinner value
    private String CurrentPizzaSize;

    // set initial quantity of pizza as 1
    private int CurrentQuantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get user pizza size from xml using findViewById
        userPizzaSize = findViewById(R.id.selextPizzaSize_id);

        // get pizzaSize Array from string.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.pizzaSize, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userPizzaSize.setAdapter(adapter);

        // on item selected listener to select pizza size
        userPizzaSize.setOnItemSelectedListener(this);

        // get the elements from XML using findViewById
        UserName = findViewById(R.id.UserName_id);
        UserEmailAddress = findViewById(R.id.emailAddress_id);
        numberOfPizzas = findViewById(R.id.numberOfPizzas_id);
        Cheese = findViewById(R.id.cheeseTopping_id);
        Pappers = findViewById(R.id.pappersTopping_id);
        ExtraCheese = findViewById(R.id.extraCheeseTopping_id);
        BlackOlives = findViewById(R.id.BlackOlivesTopping_id);

    }

    // set the pizza size based upon user input from spinner value
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        CurrentPizzaSize = parent.getItemAtPosition(position).toString();
    }


    // to avoid error
    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    // send order details as email
    public void sendEmail(String message){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        // to which email address we need to send message is done by below intent
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {this.UserEmailAddress.getText().toString()});

        // set the subject for the email
        intent.putExtra(Intent.EXTRA_SUBJECT,"Pizza Order Details");

        // text in the mail body
        intent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(intent, "Sending mail"));
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There are no email client", Toast.LENGTH_SHORT).show();
        }

    }

    // on clicking the order button
    public void MakeYourOrder(View view){
        String UserNameInput = UserName.getText().toString();
        String UserEmailInput = UserEmailAddress.getText().toString();
        // if user name or email id is empty display the below Toast message
        if(UserNameInput.isEmpty() || UserEmailInput.isEmpty()){
            Toast.makeText(MainActivity.this,"Need Email Id and Name to make order", Toast.LENGTH_SHORT).show();

        }else {

            String message = createOrderDetailsMessage();
            sendEmail(message); // pass the message text to sendEmail which can be used to send as text in email
        }
    }

    // calculatePrice() return the price based on various user inputs
    private double calculatePrice(){

        // initial prize of the regular pizza is set to 10
        double price = 10;

        switch (this.CurrentPizzaSize){
            // decrease the price by 30%
            case "Small":
                price = price * 0.7;
                break;
            // price remains same
            case "Regular":
                price = price * 1;
                break;
            // price is increased by 40%
            case "Large":
                price = price * 1.4;
                break;
            // increase the price by 80%
            case "Extra-Large":
                price = price * 1.8;
                break;
        }

        // based on checked toppings change the price
        if (this.Cheese.isChecked()) // this is checked by default
            price =price + 1;
        if (this.Pappers.isChecked())
            price =price +  1;
        if(this.ExtraCheese.isChecked())
            price =price + 2;
        if(this.BlackOlives.isChecked())
            price=price + 3;

        return price;
    }



    // text to be displayed on clicking OrderDetails

    private String createOrderDetailsMessage()
    {
        String message = "Hello, Mr."+ this.UserName.getText().toString() + ",\n\n" +
                "Please check your order details \n" +
                "\n\nPizza size you selected :" + CurrentPizzaSize +
                "\nNumber of Pizzas :" + this.CurrentQuantity +

                "\n\nList of Toppings you have added to your pizza " +
                "\n\nCheese          :" + (this.Cheese.isChecked() ? "YES":"NO") +
                "\nPappers         :" + (this.Pappers.isChecked() ? "YES":"NO") +
                "\nExtra Cheese  :" + (this.ExtraCheese.isChecked() ? "YES" : "NO") +
                "\nBlack Olives  :" +(this.BlackOlives.isChecked() ? "YES" : "NO")+
                "\n\nThe Total prize of the pizza is : $" + calculatePrice() * CurrentQuantity;

        return message;
    }


    // on click orderDetails Button
    public void OrderDetailsSummary(View view){
        String UserNameInput = UserName.getText().toString();
        String UserEmailInput = UserEmailAddress.getText().toString();
        // if user name or email id is empty display the below Toast message
        if(UserNameInput.isEmpty() || UserEmailInput.isEmpty()){
            Toast.makeText(MainActivity.this,"User name or Email can't be empty", Toast.LENGTH_SHORT).show();

        }else {
            String message = createOrderDetailsMessage();
            Intent intent = new Intent(MainActivity.this, summary.class);
            intent.putExtra("toSender", this.UserEmailAddress.getText().toString());
            intent.putExtra("summaryMessage", message);
            startActivity(intent);
        }
    }

    // onclick increment button function
    public void decrementQunatity(View view){
        if (CurrentQuantity > 1) {
            CurrentQuantity--;
            displayQuantity(CurrentQuantity); // send pizza quantity value to displayQuantity Function
        }
        else {
            // if pizza quantity value is less than 1 display below text in Toast
            Toast.makeText(MainActivity.this,"You can't choose pizzas less than 1", Toast.LENGTH_SHORT).show();
        }
    }

    // onclick decrement button function
    public void incrementQuantity (View view){
        if (CurrentQuantity < 8){
            CurrentQuantity++;
            displayQuantity(CurrentQuantity); // send pizza quantity value to displayQuantity Function
        }
        else {
            // if pizza quantity is above 8 display below text in the Toast
            Toast.makeText(MainActivity.this, "You can order max of 8 pizzas at once", Toast.LENGTH_SHORT).show();
        }
    }

    // after increment or decrement display text value function
    public void displayQuantity(int count){
        numberOfPizzas = findViewById(R.id.numberOfPizzas_id);
        numberOfPizzas.setText("" + count);
    }

}