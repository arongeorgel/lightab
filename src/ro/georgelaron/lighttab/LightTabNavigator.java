/**
 *    
 *  Copyright [2013] [Aron Georgel - aron.georgel@gmail.com]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.georgelaron.lighttab;

import java.util.*;

import ro.georgelaron.R;
import ro.georgelaron.lighttab.LightAnimation.Animation;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;

/**
 * 
 * @author Georgel Aron <a
 *         href="mailto:aron.georgel@gmail.com">aron.georgel@gmail.com</a> <br />
 * <br/>
 * 
 *         <p>
 *         Activity container for tabbed navigation which holds the logic of
 *         navigation and stack of fragments from each tab.
 *         </p>
 *         <p>
 *         More examples for implementing this library can be found on below
 *         links:
 *         <ul>
 *         <li><a href="#">Github</a></li>
 *         </ul>
 *         </p>
 */
public abstract class LightTabNavigator extends FragmentActivity {
    /**
     * Container for a tabbed window view
     */
    private TabHost mTabHost;
    /**
     * Navigation stacks for each tab gets created
     */
    private HashMap<String, Stack<LightTabFragment>> mStacks;
    /**
     * Holds the name of current tab
     */
    private String mCurrentTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tab_navigation_activity);
        mStacks = new HashMap<String, Stack<LightTabFragment>>();
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        int apiLevel = android.os.Build.VERSION.SDK_INT;

        if (apiLevel >= 15) {
            mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_unselected);
        }
    }

    /**
     * Add a new tab in container
     * 
     * @param tabName
     *            will be displayed on tab
     * @param fragmentClass
     *            fragment inside tab
     * @param drawableId
     *            resource id for add an icon for tab
     */
    private synchronized final void addTab(String tabName, LightTabFragment fragmentClass, int drawableId) {
        mStacks.put(tabName, new Stack<LightTabFragment>());
        TabHost.TabSpec spec = mTabHost.newTabSpec(tabName);

        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, mTabHost.getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);

        title.setText(tabName);
        icon.setImageResource(drawableId);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        spec.setIndicator(tabIndicator);

        mTabHost.addTab(spec);

    }

    /**
     * Switch tab programmatically, from inside any of the fragment.
     * 
     * @param tabNumber
     *            the number of the tab
     */
    public final void setCurrentTab(int tabNumber) {
        mTabHost.setCurrentTab(tabNumber);
    }

    /**
     * 
     * Add fragment to a tab.<br />
     * <br />
     * 
     * <p>
     * Push a new fragment in same tab:<br />
     * Example: <br />
     * <code>
     * getNavigator().pushFragment("ONE", new FragmentOneTabOneTwo(), true, true);
     * </code>
     * </p>
     * 
     * @param tagId
     *            Tab identifier fragment
     * @param fragment
     *            Fragment to show, in tab identified by tag
     * @param shouldAnimate
     *            should animate transaction false when we switch tabs, or
     *            adding first fragment to a tab true when when we are pushing
     *            more fragment into navigation stack
     * @param shouldAdd
     *            Should add to fragment navigation stack (mStacks.get(tag)).
     *            false when we are switching tabs (except for the first time)
     *            true in all other cases.
     */
    public synchronized final void pushFragment(String tagId, LightTabFragment fragment, boolean shouldAnimate, boolean shouldAdd, Bundle extras) {
        if (shouldAdd)
            mStacks.get(tagId).push(fragment);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (shouldAnimate)
            ft.setCustomAnimations(Animation.LEFT_IN.getAnimation(), LightAnimation.getComplementaryAnimation(Animation.LEFT_IN));
        ft.replace(R.id.realtabcontent, fragment);
        ft.commit();
    }

    /**
     * Add a new fragment in current tab
     * 
     * @param fragment
     * @param animation
     * @param shouldAdd
     *            TODO work in progress
     */
    @SuppressWarnings("unused")
    private synchronized final void pushFragment(LightTabFragment fragment, Animation animation, boolean shouldAdd) {
        if (shouldAdd)
            mStacks.get(mCurrentTab).push(fragment);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (animation.getAnimation() != 0)
            ft.setCustomAnimations(animation.getAnimation(), LightAnimation.getComplementaryAnimation(animation));
        ft.replace(R.id.realtabcontent, fragment);
        ft.commit();
    }

    /**
     * Remove a fragment from a tab
     * 
     */
    public synchronized final void popFragment() {
        LightTabFragment fragment = mStacks.get(mCurrentTab).elementAt(mStacks.get(mCurrentTab).size() - 2);
        mStacks.get(mCurrentTab).pop();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.animator.slide_right_in, R.animator.slide_right_out);
        ft.replace(R.id.realtabcontent, fragment);
        ft.commit();
    }

    /**
     * toggle tabs visibility (VISIBLE or GONE)
     */
    public void toggleTabsVisibility() {
        mTabHost.setVisibility(mTabHost.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    /**
     * 
     * @return true if tabs are visible, false otherwise
     */
    public boolean getTabsVisibility() {
        return mTabHost.getVisibility() == View.GONE ? false : true;
    }

    @Override
    public void onBackPressed() {
        if (mStacks.get(mCurrentTab).size() == 1) {
            finish();
            return;
        }
        popFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mStacks.get(mCurrentTab).size() == 0) {
            return;
        }
        mStacks.get(mCurrentTab).lastElement().onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 
     * @author Georgel Aron
     * 
     */
    private static class FragmentHolder {
        private String mKey;
        private LightTabFragment mValue;

        public FragmentHolder(String key, LightTabFragment value) {
            mKey = key;
            mValue = value;
        }

        /**
         * 
         * @return tab key
         */
        public String getKey() {
            return mKey;
        }

        /**
         * 
         * @return first fragment from a tab
         */
        public LightTabFragment getValue() {
            return mValue;
        }
    }

    /**
     * 
     * @author Georgel Aron
     * 
     */
    public static class NavigationBuilder {
        private LightTabNavigator mTabNavigation;
        private List<FragmentHolder> mFragmentHolderList;

        /**
         * Constructor using a context for this builder
         * 
         * @throws LightTabNavigator
         */
        public NavigationBuilder(FragmentActivity fragmentActivity) throws LightTabException {
            if (fragmentActivity instanceof LightTabNavigator) {
                mFragmentHolderList = new ArrayList<FragmentHolder>();
                mTabNavigation = (LightTabNavigator) fragmentActivity;
            } else {
                throw new LightTabException("Fragment activity must be '" + this.getClass().getName() + "' type.");
            }
        }

        /**
         * Add a new tab in container
         * 
         * @param tabName
         *            will be displayed on tab
         * @param fragmentClass
         *            fragment inside tab
         * @param drawableId
         *            resource id for add an icon for tab
         */
        public NavigationBuilder addTab(String tabName, LightTabFragment fragmentClass, int drawableId) {
            mFragmentHolderList.add(new FragmentHolder(tabName, fragmentClass));
            mTabNavigation.addTab(tabName, fragmentClass, drawableId);
            return this;
        }

        public void build() {
            Collections.reverse(mFragmentHolderList);
            for (int i = 0; i < mFragmentHolderList.size(); i++) {
                mTabNavigation.pushFragment(mFragmentHolderList.get(i).getKey(), mFragmentHolderList.get(i).getValue(), false, true, null);
                if (i == mFragmentHolderList.size() - 1) {
                    mTabNavigation.mCurrentTab = mFragmentHolderList.get(i).getKey();
                }
            }
            mTabNavigation.mTabHost.setOnTabChangedListener(mTabChangeListener);
            mFragmentHolderList = null;
        }

        private TabHost.OnTabChangeListener mTabChangeListener = new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                mTabNavigation.mCurrentTab = tabId;
                mTabNavigation.pushFragment(tabId, mTabNavigation.mStacks.get(tabId).lastElement(), false, false, null);
            }

        };
    }

}
