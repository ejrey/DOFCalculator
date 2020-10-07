package cmpt276.as2.ui;

/**
 * Calculate the chosen lens from user in this Activity, able to display results
 * NOTE: If user inputs aperture greater or equal to 1.4 BUT less than the lens Max Aperture then INVALID APERTURE
 * will be shown.
 * Edited a way to only allow numbers to display when user inputs (Strings = text, numbers = numbers only)
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import cmpt276.as2.R;
import cmpt276.as2.model.DoFCalc;
import cmpt276.as2.model.Lens;
import cmpt276.as2.model.LensManager;

public class CalculateActivity extends AppCompatActivity {

    private LensManager lensManager;
    private Lens lens;
    private static String INVALID_MSG = "Invalid aperture";
    private static int UNIT_CONVERTER = 1000;
    private double COC;
    private double aperture;
    private double distance;
    private boolean checkCOC;
    private boolean checkDistance;
    private boolean checkAperture;

    public static Intent makeLaunchIntent(Context c, int position) {
        Intent intent = new Intent(c, CalculateActivity.class);
        intent.putExtra("Position", position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lensManager = LensManager.getInstance();

        Intent intent = getIntent();
        int positionsLens = intent.getIntExtra("Position", 0);
        lens = lensManager.get(positionsLens);

        TextView view = (TextView) findViewById(R.id.userChosen);
        view.setText(lens.getCameraMake() + " " + lens.getFocalLength() + "mm F" + lens.getMaxAperture());

        setUpCalculate();
    }

    private void setUpCalculate() {
        EditText inputCOC = (EditText) findViewById(R.id.inputCOC);
        EditText inputDistance = (EditText) findViewById(R.id.inputDistance);
        EditText inputAperture = (EditText) findViewById(R.id.inputAperture);

        Button btn = (Button) findViewById(R.id.calculateBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String givenCOC = inputCOC.getText().toString();
                String givenDistance = inputDistance.getText().toString();
                String givenAperture = inputAperture.getText().toString();

                // Try and catch information used (Link in AddLens Activity)
                try {
                    COC = Double.parseDouble(givenCOC);
                } catch (NumberFormatException error) {
                    COC = -1;
                }

                try {
                    aperture = Double.parseDouble(givenAperture);
                } catch (NumberFormatException error) {
                    aperture = -1;
                }

                try {
                    distance = Double.parseDouble(givenDistance);
                    distance *= UNIT_CONVERTER;
                } catch (NumberFormatException error) {
                   distance = -1;
                }

                // If statement style taken from here https://www.w3schools.com/java/java_conditions.asp
                checkCOC = (COC > 0) ? true : false;
                checkDistance = (distance > 0) ? true : false;
                checkAperture = (aperture >= 1.4) ? true : false;

                if(!(checkCOC)) {
                    Toast.makeText(CalculateActivity.this, "Circle of Confusion MUST be greater than 0", Toast.LENGTH_LONG).show();
                }else if(!(checkDistance)) {
                    Toast.makeText(CalculateActivity.this, "Distance MUST be greater than 0", Toast.LENGTH_LONG).show();
                }else if(!(checkAperture)) {
                    Toast.makeText(CalculateActivity.this, "Selected Aperture MUST be greater or equal to 1.4", Toast.LENGTH_LONG).show();
                }

                if(checkCOC && checkAperture && checkDistance) {
                    if(aperture < lens.getMaxAperture()) {
                        TextView viewNear = (TextView) findViewById(R.id.resultNearFocal);
                        TextView viewFar = (TextView) findViewById(R.id.resultFarFocal);
                        TextView viewDOf = (TextView) findViewById(R.id.resultDoF);
                        TextView viewHyper = (TextView) findViewById(R.id.resultHyper);

                        viewNear.setText(INVALID_MSG);
                        viewFar.setText(INVALID_MSG);
                        viewDOf.setText(INVALID_MSG);
                        viewHyper.setText(INVALID_MSG);
                        Toast.makeText(CalculateActivity.this, "Aperture inputted OVER Max Aperture (Check Max Aperture above)", Toast.LENGTH_LONG).show();
                    }else {

                        DoFCalc dof = new DoFCalc(lens, aperture, distance, COC);
                        TextView viewNear = (TextView) findViewById(R.id.resultNearFocal);
                        TextView viewFar = (TextView) findViewById(R.id.resultFarFocal);
                        TextView viewDOf = (TextView) findViewById(R.id.resultDoF);
                        TextView viewHyper = (TextView) findViewById(R.id.resultHyper);

                        double nearFocal = dof.nearFocalPoint() / UNIT_CONVERTER;
                        double farFocal = dof.farFocalPoint() / UNIT_CONVERTER;
                        double depthOfField = dof.depthOfField() / UNIT_CONVERTER;
                        double hyperFocal = dof.hyperFocalDis() / UNIT_CONVERTER;

                        viewNear.setText("" + formatM(nearFocal) + "m");
                        viewFar.setText("" + formatM(farFocal) + "m");
                        viewDOf.setText("" + formatM(depthOfField) + "m");
                        viewHyper.setText("" + formatM(hyperFocal) + "m");
                    }
                }
            }
        });
    }

    // Got from assignment 1 formatting.
    private String formatM(double distanceInM) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(distanceInM);
    }

}
