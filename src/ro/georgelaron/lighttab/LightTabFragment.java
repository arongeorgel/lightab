package ro.georgelaron.lighttab;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class LightTabFragment extends Fragment {
    private Bundle mExtras;

    public LightTabNavigator getNavigator() {
        return (LightTabNavigator) this.getActivity();
    }

    /**
     * @return the mExtras
     */
    public Bundle getmExtras() {
        return mExtras;
    }

    /**
     * @param mExtras the mExtras to set
     */
    public void setmExtras(Bundle mExtras) {
        this.mExtras = mExtras;
    }

}
