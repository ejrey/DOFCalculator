package cmpt276.as2.ui;

/**
 * Lens Activity (Starting point of App). Displays available option of Lens and a feature to add a new one.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cmpt276.as2.R;
import cmpt276.as2.model.Lens;
import cmpt276.as2.model.LensManager;

public class MainActivity extends AppCompatActivity {
    private LensManager lensmanager;
    private ArrayAdapter<String> adapter;
    private int GIVEN_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lensmanager = LensManager.getInstance();

        preAddedLens();
        populateListView();
        registerCallBack();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = AddLensActivity.makeIntent(MainActivity.this);
            startActivityForResult(intent, GIVEN_REQUEST_CODE);
        });
    }

    //Gets called when activity we started finished.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        //Add lens to list
        if(requestCode == GIVEN_REQUEST_CODE) {
            String cameraMake = data.getStringExtra("camera");
            int focalLength = data.getIntExtra("focal", 0);
            double aperture = data.getDoubleExtra("aperture", 0.0);

            lensmanager.add(new Lens("" + cameraMake, aperture, focalLength));
            updateUI();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void preAddedLens() {
        lensmanager.add(new Lens("Canon", 1.8, 50));
        lensmanager.add(new Lens("Tamron", 2.8, 90));
        lensmanager.add(new Lens("Sigma", 2.8, 200));
        lensmanager.add(new Lens("Nikon", 4, 200));
    }

    // List out lenses in side ArrayList in lensmanager
    private void populateListView() {
        String myItems[] = new String[lensmanager.listSize()];
        for(int i = 0; i <= lensmanager.listSize() - 1; i++) {
            Lens lens = lensmanager.get(i);
            myItems[i] = lens.toString();
        }

        adapter = new ArrayAdapter<String>(this, R.layout.activity_list, myItems);
        ListView list = (ListView) findViewById(R.id.listMain);
        list.setAdapter(adapter);
    }

    //Get's the selection of what user pressed
    private void registerCallBack() {
        ListView list = (ListView) findViewById(R.id.listMain);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = CalculateActivity.makeLaunchIntent(MainActivity.this, position);
                startActivity(i);
            }
        });
    }

    // To update the list after added a new lens
    private void updateUI() {
        String myItems[] = new String[lensmanager.listSize()];
        for(int i = 0; i <= lensmanager.listSize() - 1; i++) {
            Lens lens = lensmanager.get(i);
            myItems[i] = lens.toString();
        }

        adapter = new ArrayAdapter<String>(this, R.layout.activity_list, myItems);
        ListView list = (ListView) findViewById(R.id.listMain);
        list.setAdapter(adapter);
    }

}

