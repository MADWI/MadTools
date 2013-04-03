package pl.edu.zut.mad.tools.utils;

import pl.edu.zut.mad.tools.R;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockActivity;

public class TabCreator {

	private final SherlockActivity sa;
	private final TabListener tl;

	public TabCreator(SherlockActivity sa) {
		this.sa = sa;
		this.tl = (TabListener) sa;
	}

	public void createTab(int tabNumber) {

		String[] navi_items = sa.getResources().getStringArray(
				R.array.navi_items);

		for (String s : navi_items) {
			ActionBar.Tab tab = sa.getSupportActionBar().newTab();
			tab.setText(s);
			tab.setTabListener(tl);
			sa.getSupportActionBar().addTab(tab);

		}
		sa.getSupportActionBar().setSelectedNavigationItem(tabNumber);

	}
}
