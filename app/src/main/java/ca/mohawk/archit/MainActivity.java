//#I, Archit, student number 000882919, certify that this material is my original work.
//No other person's work has been used without due acknowledgment, and I have not made my work available to anyone else.

package ca.mohawk.archit;

import android.annotation.SuppressLint;
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
            textView.setText(orientation == Configuration.ORIENTATION_PORTRAIT
                    ? getString(R.string.locked_message)
                    : getString(R.string.rotate_to_portrait));
        }
        textView.setTextSize(24);
    }

    private class UnlockButtonListener implements View.OnClickListener {
        private final EditText passwordField;
        private final Snackbar snackbar;

        public UnlockButtonListener(EditText passwordField, Snackbar snackbar) {
            this.passwordField = passwordField;
            this.snackbar = snackbar;
        }

        @Override
        public void onClick(View v) {
            String input = passwordField.getText().toString();
            if (input.equals(getString(R.string.password))) {
                isUnlocked = true;
                updateTextView();
                snackbar.dismiss();
            } else {
                snackbar.dismiss();
                showAlertDialog();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void showSnackbar() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "", Snackbar.LENGTH_INDEFINITE);
        View customView = getLayoutInflater().inflate(R.layout.snackbar_unlock, null);

        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setPadding(1, 1, 1, 1);
        layout.addView(customView, 0);

        EditText editText = customView.findViewById(R.id.editTextPassword);
        Button unlockButton = customView.findViewById(R.id.buttonUnlock);
        unlockButton.setOnClickListener(new UnlockButtonListener(editText, snackbar));

        snackbar.show();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Incorrect Password")
                .setMessage("The entered password is not correct.")
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}