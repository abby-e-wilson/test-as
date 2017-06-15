package wilson.abby.test;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import static android.content.pm.PackageManager.MATCH_DEFAULT_ONLY;

public class DisplayMessageActivity extends AppCompatActivity {
    private static final int PHOTO_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.textViewOutput);
        textView.setText(message);
    }

    /* called when Google button pressed*/
    public void takePhotoIntent(View view){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Verify it resolves
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(camera, 0);
        boolean isIntentSafe = activities.size() > 0;

        // Start an activity if it's safe
        if (isIntentSafe) {
            startActivityForResult(camera, PHOTO_REQUEST_CODE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // Check which request we're responding to
        if (requestCode == PHOTO_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Uri returned = data.getData();
//                TextView text = (TextView) findViewById(R.id.textView2);
//                text.setText(returned.toString());
            }
        }

    }
}
