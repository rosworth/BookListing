package com.example.android.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final String GOOGLE_BOOKS_API = "https://www.googleapis.com/books/v1/volumes";
    private static final int BOOK_LOADER_ID = 1;
    @BindView(R.id.tv_empty)
    TextView mEmptyStateTextView;
    @BindView(R.id.search_term)
    EditText searchText;
    @BindView(R.id.button_search)
    ImageView searchButton;
    @BindView(R.id.list)
    ListView bookList;
    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mEmptyStateTextView.setText(getString(R.string.empty_list));
        bookList.setEmptyView(mEmptyStateTextView);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookList.setAdapter(mAdapter);

        final ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        final LoaderManager loaderManager = getLoaderManager();
        if (networkInfo != null && networkInfo.isConnected()) {
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //avoids NullPointerException
                NetworkInfo networkReTest = connMgr.getActiveNetworkInfo();
                if (!TextUtils.isEmpty(searchText.getText().toString().trim())) {
                    if (networkReTest != null && networkReTest.isConnected()) {
                        loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                    } else {
                        mAdapter.clear();
                        mEmptyStateTextView.setText(R.string.no_internet_connection);
                    }
                }
            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        String searchTerm = searchText.getText().toString().trim();
        //query only if text is not null
        if (!TextUtils.isEmpty(searchTerm)) {
            Uri baseUri = Uri.parse(GOOGLE_BOOKS_API);
            Uri.Builder builder = baseUri.buildUpon();
            builder.appendQueryParameter("q", searchTerm);
            builder.appendQueryParameter("maxResults", "10");
            return new BookLoader(this, builder.toString());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        if (!TextUtils.isEmpty(searchText.getText().toString().trim()))
            mEmptyStateTextView.setText(getString(R.string.no_result));
        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }
}
