package org.weibeld.flicks.events;

/**
 * Created by dw on 17/03/17.
 */

public class TermsAcceptanceEvent {

    private boolean mIsAccepted;

    public TermsAcceptanceEvent(boolean isAccepted) {
        mIsAccepted = isAccepted;
    }

    public boolean isAccepted() {
        return mIsAccepted;
    }
}

