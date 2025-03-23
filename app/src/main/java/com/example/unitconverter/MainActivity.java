package com.example.unitconverter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerCategory, spinnerSource, spinnerDestination;
    private EditText inputValue;
    private Button convertButton;
    private TextView resultText;
    private String[] categories = {"Length", "Weight", "Temperature"};
    private String[] lengthUnits = {"Inch", "Foot", "Yard", "Mile", "Cm"};
    private String[] weightUnits = {"Kg", "Pound", "Ounce"};
    private String[] tempUnits = {"Celsius", "Fahrenheit", "Kelvin"};

    private String[] currentUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerSource = findViewById(R.id.spinner_source);
        spinnerDestination = findViewById(R.id.spinner_destination);
        inputValue = findViewById(R.id.input_value);
        convertButton = findViewById(R.id.convert_button);
        resultText = findViewById(R.id.result_text);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(categoryAdapter);

        currentUnits = lengthUnits;
        setUnitSpinners(currentUnits);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = categories[position];
                switch (category) {
                    case "Length":
                        currentUnits = lengthUnits;
                        break;
                    case "Weight":
                        currentUnits = weightUnits;
                        break;
                    case "Temperature":
                        currentUnits = tempUnits;
                        break;
                }
                setUnitSpinners(currentUnits);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sourceUnit = spinnerSource.getSelectedItem().toString();
                String destinationUnit = spinnerDestination.getSelectedItem().toString();
                String inputStr = inputValue.getText().toString();

                if (inputStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                    return;
                }

                double value;
                    value = Double.parseDouble(inputStr);

                double convertedValue = convertUnits(value, sourceUnit, destinationUnit);
                resultText.setText("Result: " + convertedValue);
            }
        });
    }

    private void setUnitSpinners(String[] units) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units);
        spinnerSource.setAdapter(adapter);
        spinnerDestination.setAdapter(adapter);
    }

    private double convertUnits(double value, String source, String destination) {
        if (source.equals(destination)) {
            return value;
        }

        String category = spinnerCategory.getSelectedItem().toString();
        switch (category) {
            case "Length":
                double sourceToCm = getLengthFactor(source);
                double destToCm = getLengthFactor(destination);
                return (value * sourceToCm) / destToCm;
            case "Weight":
                double sourceToKg = getWeightFactor(source);
                double destToKg = getWeightFactor(destination);
                return (value * sourceToKg) / destToKg;
            case "Temperature":
                return convertTemperature(value, source, destination);
            default:
                return value;
        }
    }

    private double getLengthFactor(String unit) {
        switch (unit) {
            case "Inch":
                return 2.54;
            case "Foot":
                return 30.48;
            case "Yard":
                return 91.44;
            case "Mile":
                return 1609.34;
            case "Cm":
                return 1.0;
            default:
                return 1.0;
        }
    }

    private double getWeightFactor(String unit) {
        switch (unit) {
            case "Pound":
                return 0.453592;
            case "Ounce":
                return 0.0283495;
            case "Kg":
                return 1.0;
            default:
                return 1.0;
        }
    }

    private double convertTemperature(double value, String source, String destination) {
        double celsius;
        if (source.equals("Celsius")) {
            celsius = value;
        } else if (source.equals("Fahrenheit")) {
            celsius = (value - 32) / 1.8;
        } else if (source.equals("Kelvin")) {
            celsius = value - 273.15;
        } else {
            celsius = value;
        }

        if (destination.equals("Celsius")) {
            return celsius;
        } else if (destination.equals("Fahrenheit")) {
            return (celsius * 1.8) + 32;
        } else if (destination.equals("Kelvin")) {
            return celsius + 273.15;
        } else {
            return celsius;
        }
    }
}
