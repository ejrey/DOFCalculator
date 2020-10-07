package cmpt276.as2.ui;

/**
 * Add lens. Able to add a new lens to the list of already existing lens
 * User has option to enter values (As long it follows guidelines).
 * Edited a way to only allow numbers to display when user inputs (Strings = text, numbers = numbers only)
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DecimalFormat;

import cmpt276.as2.R;

public class AddLensActivity extends AppCompatActivity {
    private int finalFocal;
    private double finalAperture;
    private float tempAperture;
    private int cameraStringSize;

    private boolean checkFocal = false;
    private boolean checkAperture = false;
    private boolean checkCameraSize = false;

    public static Intent makeIntent(Context c) {
        return new Intent(c, AddLensActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lens);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpSaveButton();
        setUpCancelButton();
    }

    private void setUpSaveButton() {
        EditText userCameraMake = (EditText) findViewById(R.id.inputCamera);
        EditText userFocalLength = (EditText) findViewById(R.id.inputFocalLength);
        EditText userAperture = (EditText) findViewById(R.id.inputAperture);

        Button btn = (Button) findViewById(R.id.saveOption);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String givenCameraMake = userCameraMake.getText().toString();
                String givenFocalLength = userFocalLength.getText().toString();
                String givenAperture = userAperture.getText().toString();
                cameraStringSize = givenCameraMake.length();

                // Obtain information about try and catch in java https://www.w3schools.com/java/java_try_catch.asp
                try {
                    finalFocal = Integer.parseInt(givenFocalLength);
                } catch (NumberFormatException error) {
                    finalFocal = -1;
                }
                // Researched information about Decimal Formatting in java https://www.baeldung.com/java-decimalformat
                try {
                    tempAperture = Float.parseFloat(givenAperture);
                    String temp = new DecimalFormat("#.#").format(tempAperture);
                    finalAperture = Double.parseDouble(temp);
                } catch (NumberFormatException error) {
                      finalAperture = -1;
                }

                // If statement style taken from here https://www.w3schools.com/java/java_conditions.asp
                checkCameraSize = (cameraStringSize > 0) ? true : false;
                checkFocal = (finalFocal > 0) ? true : false;
                checkAperture = (finalAperture >= 1.4) ? true : false;

                if(!(checkCameraSize)) {
                    Toast.makeText(AddLensActivity.this, "Please enter a camera make", Toast.LENGTH_LONG).show();
                }else if(!(checkFocal)) {
                    Toast.makeText(AddLensActivity.this, "Focal Length MUST be greater than 0", Toast.LENGTH_LONG).show();
                }else if(!(checkAperture)) {
                    Toast.makeText(AddLensActivity.this, "Aperture CAN'T be less than 1.4", Toast.LENGTH_LONG).show();
                }

                if(checkFocal && checkAperture && checkCameraSize) {
                    Intent i = getIntent();
                    i.putExtra("camera", givenCameraMake);
                    i.putExtra("focal", finalFocal);
                    i.putExtra("aperture" , finalAperture);
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }
            }
        });
    }

    private void setUpCancelButton() {
        Button btn = (Button) findViewById(R.id.cancelOption);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
    }
}
