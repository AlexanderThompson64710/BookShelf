package temple.edu.bookshelf;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BookSearch extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        EditText searchBox = findViewById(R.id.searchBox);
        Button searchButton = findViewById(R.id.commenceSearch);

        searchButton.setOnClickListener(v -> {
            String response = searchBox.getText().toString();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("response", response);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
    }
}
