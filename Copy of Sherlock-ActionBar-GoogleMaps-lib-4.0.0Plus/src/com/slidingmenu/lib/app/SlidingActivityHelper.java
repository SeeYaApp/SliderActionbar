package com.slidingmenu.lib.app;

import android.app.Activity;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.actionbarsherlock.R;
import com.slidingmenu.lib.SlidingMenu;

public class SlidingActivityHelper {

	static final int VISIBLE=0;
	static final int INVISIBLE=4;
	private Activity mActivity;

	private SlidingMenu mSlidingMenu;
	private View mViewAbove;
	private View mViewBehind;
	private boolean mBroadcasting = false;

	private boolean mOnPostCreateCalled = false;
	private boolean mEnableSlide = true;

	public SlidingActivityHelper(Activity activity) {
		mActivity = activity;
	}

	public void onCreate(Bundle savedInstanceState) {
		mSlidingMenu = (SlidingMenu) LayoutInflater.from(mActivity).inflate(R.layout.slidingmenumain, null);
	}

	public void onPostCreate(Bundle savedInstanceState) {
		if (mViewBehind == null || mViewAbove == null) {
			throw new IllegalStateException("Both setBehindContentView must be called " +
					"in onCreate in addition to setContentView.");
		}

		mOnPostCreateCalled = true;

		// get the window background
		TypedArray a = mActivity.getTheme().obtainStyledAttributes(new int[] {android.R.attr.windowBackground});
		int background = a.getResourceId(0, 0);

		if (mEnableSlide) {
			// move everything into the SlidingMenu
			ViewGroup decor = (ViewGroup) mActivity.getWindow().getDecorView();
			ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
			// save ActionBar themes that have transparent assets
			decorChild.setBackgroundResource(background);
			decor.removeView(decorChild);
			mSlidingMenu.setContent(decorChild);
			decor.addView(mSlidingMenu);
		} else {
			// take the above view out of
			ViewGroup parent = (ViewGroup) mViewAbove.getParent();
			if (parent != null) {
				parent.removeView(mViewAbove);
			}
			// save people from having transparent backgrounds
			if (mViewAbove.getBackground() == null) {
				mViewAbove.setBackgroundResource(background);
			}
			mSlidingMenu.setContent(mViewAbove);
			parent.addView(mSlidingMenu, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
	}

	public void setSlidingActionBarEnabled(boolean b) {
		if (mOnPostCreateCalled)
			throw new IllegalStateException("enableSlidingActionBar must be called in onCreate.");
		mEnableSlide = b;
	}

	public View findViewById(int id) {
		View v;
		if (mSlidingMenu != null) {
			v = mSlidingMenu.findViewById(id);
			if (v != null)
				return v;
		}
		return null;
	}

	public void registerAboveContentView(View v, LayoutParams params) {
		if (!mBroadcasting)
			mViewAbove = v;
	}

	public void setContentView(View v) {
		mBroadcasting = true;
		mActivity.setContentView(v);
	}

	public void setBehindContentView(View v, LayoutParams params) {
		mViewBehind = v;
		mSlidingMenu.setMenu(mViewBehind);
	}

	public SlidingMenu getSlidingMenu() {
		return mSlidingMenu;
	}

	public void toggle() {
		if (mSlidingMenu.isBehindShowing()) {
			showAbove();
		} else {
			showBehind();
		}
	}

	public void showAbove() {
		mSlidingMenu.showAbove();
	}

	public void showBehind() {
		mSlidingMenu.showBehind();
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mSlidingMenu.isBehindShowing()) {
			LinearLayout menuLayout=(LinearLayout)((RelativeLayout)mViewBehind).getChildAt(2);//get slide menu layout from menu_frame.xml
			ListView usersList=(ListView)((LinearLayout)((RelativeLayout)mViewBehind).getChildAt(1)).getChildAt(0);
			if(menuLayout.getVisibility()==INVISIBLE)
			{						
				mSlidingMenu.setBehindOffsetRes(R.dimen.actionbar_home_width);	
				mSlidingMenu.requestLayout();
				menuLayout.setVisibility(VISIBLE);	
				//usersList.removeAllViewsInLayout();				
				//usersList.removeViews(0, usersList.getCount());
				
				CustomListAdapter emptyAdapter=new CustomListAdapter() ;
				usersList.setAdapter(emptyAdapter);
				int num=0;
				num++;
			}
			else
			{				
				showAbove();
			}
			return true;
		}
		return false;
	}


	public class CustomListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}
	 
		
	   

	}

}
