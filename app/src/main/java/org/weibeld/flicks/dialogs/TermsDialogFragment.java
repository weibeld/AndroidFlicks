package org.weibeld.flicks.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import org.greenrobot.eventbus.EventBus;
import org.weibeld.flicks.R;
import org.weibeld.flicks.databinding.DialogTermsBinding;
import org.weibeld.flicks.events.TermsAcceptanceEvent;
import org.weibeld.flicks.util.Util;

/**
 * Created by dw on 17/03/17.
 */

public class TermsDialogFragment extends DialogFragment {

    DialogTermsBinding b;
    AlertDialog mDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        b = DataBindingUtil.inflate(inflater, R.layout.dialog_terms, null, false);
        // Theme for the AlertDialog (with white accent colour)
        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(getActivity(), R.style.WhiteAccent);
        // Create the AlertDialog
        mDialog = new AlertDialog.Builder(themeWrapper).setTitle(R.string.terms_dialog_title)
                .setView(b.getRoot())
                .setPositiveButton(R.string.terms_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        acceptTerms(true);
                    }
                })
                .setNegativeButton(R.string.terms_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        acceptTerms(false);
                    }
                }).create();

        // Disable the "Continue" button until the "I accept..." checkbox is checked
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button posButton = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                posButton.setEnabled(false);
                b.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) posButton.setEnabled(true);
                        else posButton.setEnabled(false);
                    }
                });
            }
        });

        // Load the terms of use into the (hidden) WebView
        b.webView.loadUrl(getString(R.string.uri_terms));

        // Show the WebView on clicking the "Read Terms of Use" button
        b.btnReadTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.toggleVisibility(b.webView);
            }
        });

        return mDialog;
    }

    // Called when clicking outside the dialog or pressing the back button
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        acceptTerms(false);
    }

    private void acceptTerms(boolean accept) {
        EventBus.getDefault().post(new TermsAcceptanceEvent(accept));
    }
}
