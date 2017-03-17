package org.weibeld.flicks.activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.weibeld.flicks.R;
import org.weibeld.flicks.databinding.ActivityTermsBinding;

public class TermsActivity extends AppCompatActivity {

    ActivityTermsBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_terms);
        setSupportActionBar(b.toolbar);
        getSupportActionBar().setTitle(R.string.title_terms_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        b.webView.loadUrl(getString(R.string.uri_terms));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true; }
}
