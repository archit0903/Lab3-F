package ca.mohawk.archit;
//I, Archit Archit 000882919 certify that this material is my original work.
//No other persons work has been used without due acknowledgement.

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private boolean isUnlocked = false;
    private static final String KEY_UNLOCKED = "isUnlocked";
    private static final String KEY_PREV_ORIENTATION = "prevOrientation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        if (savedInstanceState != null) {
            isUnlocked = savedInstanceState.getBoolean(KEY_UNLOCKED, false);
            int prevOrientation = savedInstanceState.getInt(KEY_PREV_ORIENTATION, Configuration.ORIENTATION_UNDEFINED);
            int currentOrientation = getResources().getConfiguration().orientation;

            if (prevOrientation == Configuration.ORIENTATION_LANDSCAPE
                    && currentOrientation == Configuration.ORIENTATION_PORTRAIT
                    && !isUnlocked) {
                showSnackbar();
            }
        }

        updateTextView();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_UNLOCKED, isUnlocked);
        outState.putInt(KEY_PREV_ORIENTATION, getResources().getConfiguration().orientation);
    }

    private void updateTextView() {
        if (isUnlocked) {
            textView.setText(getString(R.string.unlocked_data));
        } else {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                textView.setText(getString(R.string.locked_message));
            } else {
                textView.setText(getString(R.string.rotate_to_portrait));
            }
        }
        textView.setTextSize(22);
    }

    private void showSnackbar() {
        // Create regular Snackbar
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "", Snackbar.LENGTH_INDEFINITE);

        // Inflate custom view
        View customView = getLayoutInflater().inflate(R.layout.snackbar_unlock, null);

        // Configure view parameters
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setPadding(1, 1, 1, 1); // Remove default padding

        // Add our custom view
        layout.addView(customView, 0);

        // Get references to UI components
        final EditText editText = customView.findViewById(R.id.editTextPassword);
        Button unlockButton = customView.findViewById(R.id.buttonUnlock);

        // Replaced lambda with anonymous inner class
        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                String correctPassword = getString(R.string.password);

                if (input.equals(correctPassword)) {
                    isUnlocked = true;
                    updateTextView();
                    snackbar.dismiss();
                } else {
                    snackbar.dismiss();
                    showAlertDialog();
                }
            }
        });

        snackbar.show();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Incorrect Password");
        builder.setMessage("The entered password is not correct.");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}