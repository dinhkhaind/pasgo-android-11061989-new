package com.onepas.android.pasgo.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.onepas.android.pasgo.Constants;
import com.onepas.android.pasgo.Pasgo;
import com.onepas.android.pasgo.Pasgo.PWErrorListener;
import com.onepas.android.pasgo.Pasgo.PWListener;
import com.onepas.android.pasgo.R;
import com.onepas.android.pasgo.models.NationCode;
import com.onepas.android.pasgo.ui.BaseAppCompatActivity;
import com.onepas.android.pasgo.ui.home.HomeActivity;
import com.onepas.android.pasgo.utils.NetworkUtils;
import com.onepas.android.pasgo.utils.ParserUtils;
import com.onepas.android.pasgo.utils.Utils;
import com.onepas.android.pasgo.utils.WebServiceUtils;
import com.onepas.android.pasgo.widgets.IndexableListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectNationActivity extends BaseAppCompatActivity {
	private LinearLayout mLayoutKoDuLieu;
	private IndexableListView mListView;
	private List<NationCode> mList = new ArrayList<NationCode>();
	private List<NationCode> mListSearch = new ArrayList<NationCode>();
	private String mKeySearch = "";
	private EditText mEditsearch;
	private boolean mIsSearch = false;
	private boolean mIsHideMenu = false;
	private TextView mKoCoDL;
	private Menu menu;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		//getMenuInflater().inflate(R.menu.menu_search, menu);
		/*mEditsearch = (EditText) menu.findItem(R.id.menu_search).getActionView();
		mEditsearch
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            String textSearch = mEditsearch.getText().toString();
                            searchItem(textSearch);
                            return true;
                        }
                        return false;
                    }
                });
		mEditsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Utils.Log("CharSequence", "" + s);
                String textSearch = s.toString();
                searchItem(textSearch);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
		MenuItem menuSearch = menu.findItem(R.id.menu_search);
		menuSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			// Menu Action Collapse
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				// Empty EditText to remove text filtering
				mKeySearch = "";
				mIsSearch = false;
				mListSearch.clear();
				setListAdapter(mList);
				mEditsearch.setText("");
				mEditsearch.clearFocus();
                View v = (View) findViewById(mEditsearch.getId());
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
			}

			// Menu Action Expand
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				mEditsearch.requestFocus();
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				return true;
			}
		});*/
		return true;
	}

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            /*if (mEditsearch.isFocused()) {
                View v = (View) findViewById(mEditsearch.getId());
                Rect outRect = new Rect();
                mEditsearch.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    mEditsearch.clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }*/
        }
        return super.dispatchTouchEvent(event);
    }

	private void searchItem(String textSearch) {
		textSearch = textSearch.trim();
            if(textSearch.length()>0) {
                mIsSearch = true;
                searchNation(textSearch);
            }
            else {
                setListAdapter(mList);
                mIsSearch = false;
            }
	}

	private void hideOption(int id) {
		/*if (menu != null) {
			MenuItem item = menu.findItem(R.id.menu_search);
			item.setVisible(false);
		}*/
	}

	private void showOption(int id) {
		/*if (menu != null) {
			MenuItem item = menu.findItem(R.id.menu_search);
			item.setVisible(true);
		}*/
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_list_nation);
		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		mToolbar.setTitle("");
		TextView toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
		toolbarTitle.setText(getString(R.string.select_nation_code));
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationIcon(R.drawable.ic_action_back);
		mToolbar.setNavigationOnClickListener(v -> finishToRightToLeft());
		mProgressToolbar =(ProgressBar)mToolbar.findViewById(R.id.toolbar_progress_bar);
		mListView = (IndexableListView) findViewById(R.id.listView);
        mListView.setFastScrollEnabled(true);
		mLayoutKoDuLieu = (LinearLayout) findViewById(R.id.lyKhongCoThongBao);
		mLnErrorConnectNetwork = (LinearLayout) findViewById(R.id.lnErrorConnectNetwork);
		mLnErrorConnectNetwork.setVisibility(View.GONE);
		mKoCoDL = (TextView) findViewById(R.id.tvKhongCoDuLieu);
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
            getListNationCode();
            mLnErrorConnectNetwork.setVisibility(View.GONE);
        } else {
            mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
        }
		mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (NetworkUtils.getInstance(getBaseContext())
                        .isNetworkAvailable()) {
                    lvClickItem(position);
                }
            }
        });

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event != null && keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			finishToRightToLeft();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(bundle);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finishToRightToLeft();
			return true;
		}
		return true;
	}

	private void lvClickItem(int position) {

        NationCode item = null;
		if (mIsSearch) {
			if (mListSearch.size() > 0) {
				item = mListSearch.get(position);
			}
		} else {
			if (mList.size() > 0) {
				item = mList.get(position);
			}
		}
		if (item != null) {
            if(item.getId().equals(Constants.KEY_NATION_CODE_ID))
                return;
            Intent intent = new Intent();
            intent.putExtra(Constants.BUNDLE_NATION_NAME_VIEW,item.getTenHienThi());
            intent.putExtra(Constants.BUNDLE_NATION_CODE,item.getMa());
            setResult(Activity.RESULT_OK, intent);
			finishToRightToLeft();
		}
	}

	private void setListAdapter(List<NationCode> listcode) {
		int sizeList = listcode.size();
		if (sizeList > 0) {
			mLayoutKoDuLieu.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
            SelectNationAdapter adapter = new SelectNationAdapter(mContext,
					R.layout.item_select_nation, listcode);
            mListView.setAdapter(adapter);
            showOption(R.menu.menu_search);
		} else {
			mLayoutKoDuLieu.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.MENU_TO_LIST_C1) {
			if (data == null) {
				return;
			}
			Bundle bundle = data.getExtras();
			int complete = 0;
			if (bundle != null) {
				complete = bundle.getInt(Constants.KEY_COMPLETE);
			}
			if (complete == Constants.KEY_INT_COMPLETE) {
				Intent intent = null;
				intent = new Intent(mActivity, HomeActivity.class);//ReserveActivity
				Bundle bundle1 = new Bundle();
				bundle1.putInt(Constants.KEY_COMPLETE,
						Constants.KEY_INT_COMPLETE);
				intent.putExtras(bundle1);
				setResult(Constants.MENU_TO_LIST_C1, intent);

				finishToRightToLeft();
			} else {
				finishToRightToLeft();
			}
		}
	}

	private void getListNationCode() {
		String url = WebServiceUtils
				.URL_GET_MA_QUOC_GIA_BY_NAME();
		JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("name", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
			mProgressToolbar.setVisibility(ProgressBar.VISIBLE);
			Pasgo.getInstance().addToRequestQueue(url, jsonParams,
					new PWListener() {

						@Override
						public void onResponse(JSONObject response) {
							Utils.Log("response ", "response"
									+ response);
                            mList = ParserUtils.getAllNationCodes(response);
                            setListAdapter(mList);

							mLnErrorConnectNetwork.setVisibility(View.GONE);
							mProgressToolbar.setVisibility(ProgressBar.GONE);
						}

						@Override
						public void onError(int maloi) {
						}

					}, new PWErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							mProgressToolbar.setVisibility(ProgressBar.GONE);

						}
					});
		} else {
			showKoDL();
		}
	}
    private void searchNation(String search)
    {
        mListSearch.clear();
        for(int i=0;i<mList.size();i++)
        {
            if(Utils.contains(mList.get(i).getTen(),search))
                mListSearch.add(mList.get(i));
        }
        setListAdapter(mListSearch);
    }


    private void showKoDL() {
		mLayoutKoDuLieu.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
		if (mIsSearch && mIsHideMenu == false) {
			mKoCoDL.setText(getString(R.string.khong_co_du_lieu));
		} else {
			mKoCoDL.setText(getString(R.string.khong_co_du_lieu));
		}
		mProgressToolbar.setVisibility(ProgressBar.GONE);
	}

	@Override
	public void onNetworkChanged() {
		if (mLnErrorConnectNetwork != null) {

			if (NetworkUtils.getInstance(getBaseContext()).isNetworkAvailable()) {
				mLnErrorConnectNetwork.setVisibility(View.GONE);
			} else {
				mLnErrorConnectNetwork.setVisibility(View.VISIBLE);
			}
		}
	}
    @Override
    public void onStartMoveScreen() {

    }
	@Override
	public void onUpdateMapAfterUserInterection() {
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}