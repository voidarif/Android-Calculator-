package com.nexthopetech.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {
    TextView resultTV, solutionTV;
    MaterialButton buttonC, buttonSqrt, buttonCloseBrack;
    MaterialButton buttonDivide, buttonMultiply, buttonPlus, buttonMinus, buttonEqual;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonAC, buttonDot;

    float number1,number2;
    double result;
    int intResult;
    char operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTV = findViewById(R.id.result_tv);
        solutionTV = findViewById(R.id.solution_tv);
        buttonEqual = findViewById(R.id.button_equal);

        assignID(buttonC, R.id.button_c);
        assignID(buttonSqrt, R.id.button_sqrt);
        assignID(buttonCloseBrack, R.id.button_close_bracket);
        assignID(buttonDivide, R.id.button_divide);
        assignID(buttonMultiply, R.id.button_multiply);
        assignID(buttonPlus, R.id.button_plus);
        assignID(buttonMinus, R.id.button_minus);
       // assignID(buttonEqual, R.id.button_equal);
        assignID(button0, R.id.button_0);
        assignID(button1, R.id.button_1);
        assignID(button2, R.id.button_2);
        assignID(button3, R.id.button_3);
        assignID(button4, R.id.button_4);
        assignID(button5, R.id.button_5);
        assignID(button6, R.id.button_6);
        assignID(button7, R.id.button_7);
        assignID(button8, R.id.button_8);
        assignID(button9, R.id.button_9);
        assignID(buttonAC, R.id.button_ac);
        assignID(buttonDot, R.id.button_dot);

    }
    void assignID(MaterialButton btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    //eval function

    public static double eval(final String str) {

        return new Object() {

            int pos = -1, ch;


            void nextChar() {

                ch = (++pos < str.length()) ? str.charAt(pos) : -1;

            }

            boolean eat(int charToEat) {

                while (ch == ' ') nextChar();

                if (ch == charToEat) {

                    nextChar();

                    return true;

                }

                return false;

            }

            double parse() {

                nextChar();

                double x = parseExpression();

                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);

                return x;

            }

            // Grammar:

            // expression = term | expression `+` term | expression `-` term

            // term = factor | term `*` factor | term `/` factor

            // factor = `+` factor | `-` factor | `(` expression `)`

            //        | number | functionName factor | factor `^` factor

            double parseExpression() {

                double x = parseTerm();

                for (;;) {

                    if      (eat('+')) x += parseTerm(); // addition

                    else if (eat('-')) x -= parseTerm(); // subtraction

                    else return x;

                }

            }

            double parseTerm() {

                double x = parseFactor();

                for (;;) {

                    if      (eat('*')) x *= parseFactor(); // multiplication

                    else if (eat('/')) x /= parseFactor(); // division

                    else return x;

                }

            }

            double parseFactor() {

                if (eat('+')) return parseFactor(); // unary plus

                if (eat('-')) return -parseFactor(); // unary minus

                double x;

                int startPos = this.pos;

                if (eat('(')) { // parentheses

                    x = parseExpression();

                    eat(')');

                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers

                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();

                    x = Double.parseDouble(str.substring(startPos, this.pos));

                } else if (ch >= 'a' && ch <= 'z') { // functions

                    while (ch >= 'a' && ch <= 'z') nextChar();

                    String func = str.substring(startPos, this.pos);

                    x = parseFactor();

                    if (func.equals("sqrt")) x = Math.sqrt(x);

                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));

                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));

                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));

                    else if (func.equals("log")) x = Math.log10(x);

                    else if (func.equals("ln")) x = Math.log(x);

                    else throw new RuntimeException("Unknown function: " + func);

                } else {

                    throw new RuntimeException("Unexpected: " + (char)ch);

                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;

            }

        }.parse();

    } //end of eval function

    @Override
    public void onClick(View view) {
    MaterialButton button = (MaterialButton) view;
    String buttonText = button.getText().toString();
    String dataToCalculate = solutionTV.getText().toString();


    if (buttonText.equals("AC")){
        solutionTV.setText("");
        resultTV.setText("0");
        return;
    }


    if (buttonText.equals("C")){
       dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
       if(dataToCalculate.length() == 0 || dataToCalculate == " ") {
           solutionTV.setText("0");
           return;
       }
    }else {
        dataToCalculate = dataToCalculate + buttonText;
    }

    solutionTV.setText(dataToCalculate);

        buttonEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = solutionTV.getText().toString();
                result = eval(str);
                intResult = (int)result;

                if(result != intResult) {
                    resultTV.setText(String.valueOf(result));
                }else {
                    resultTV.setText(String.valueOf(intResult));
                }
            }
        });

    }


}