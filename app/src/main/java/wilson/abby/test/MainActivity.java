package wilson.abby.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements HeadlineFragment.OnHeadlineSelectedListener {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private File datafile;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            android.support.v4.app.Fragment firstFragment = new android.support.v4.app.Fragment();
            ArticleFragment af = new ArticleFragment();
            firstFragment.setArguments(getIntent().getExtras());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // Add the fragment to the 'fragment_container' FrameLayout
            ft.add(R.id.fragment_container, firstFragment);
            ft.replace(R.id.fragment_container, af);
            ft.commit();
        }

        // start with no preferences
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.clear().apply();

        //create the datafile
        filename = "entered_message.txt";
        datafile = new File(getFilesDir(), filename);
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        //save message to a file
        //collecting message
        EditText editText = (EditText) findViewById(R.id.TextInput);
        String message = editText.getText().toString();
        FileOutputStream fops = null;

        //writing to file
        try {
            fops = openFileOutput(filename, MODE_PRIVATE);
            fops.write(message.getBytes());
        } catch (Exception e){
            e.printStackTrace();
            Log.e("Test", "sendMessage: ", e);
        } finally {
            if (fops != null) {
                try {
                    fops.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //pass message onto next activity
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void onArticleSelected(int position) {
        // when the headline fragment is clicked, replace the fragment in the fragment container with a new
        // headline fragment, to show a change
            HeadlineFragment hf = new HeadlineFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, hf).commit();
    }

    @Override
    public void onResume(){
        super.onResume();

        //shared preferences check
        //display shared preference as button text
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        String message = pref.getString("Preference", "Send");
        Button button = (Button) findViewById(R.id.button);
        button.setText(message);

        //Read from file
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(datafile));
            String data = "Data from file: " + buffer.readLine();
            buffer.close();
            TextView text = (TextView) findViewById(R.id.textView);
            text.setText(data);
        } catch (Exception e){
            e.printStackTrace();
            System.out.print("\nFailed to read");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //save preferences (in this case, a simple string as there are no preference options)
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("Preference", "Saved preference").apply();
    }
}