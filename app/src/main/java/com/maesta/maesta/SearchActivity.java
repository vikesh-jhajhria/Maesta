package com.maesta.maesta;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.maesta.maesta.adapter.SearchAdapter;
import com.maesta.maesta.utils.AppPreferences;
import com.maesta.maesta.utils.Config;
import com.maesta.maesta.utils.HTTPUrlConnection;
import com.maesta.maesta.utils.Utils;
import com.maesta.maesta.vo.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends BaseActivity {

    private List<Collection> searchList;
    private SearchAdapter seachAdapter;
    RecyclerView searchRecycler;
    String search = "", categoryId = "";
    AppPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        categoryId = getIntent().getStringExtra("ID");
        mPrefs = AppPreferences.getAppPreferences(SearchActivity.this);

        findViewById(R.id.img_search).setOnClickListener(this);
        findViewById(R.id.txt_cancel).setOnClickListener(this);
        searchRecycler = (RecyclerView) findViewById(R.id.search_recycle_view);
        ((EditText) findViewById(R.id.txt_search)).addTextChangedListener(new MySearchValidation(((EditText) findViewById(R.id.txt_search))));
        searchList = new ArrayList<>();
        seachAdapter = new SearchAdapter(searchList, this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchRecycler.setLayoutManager(layoutManager);
        searchRecycler.setAdapter(seachAdapter);
        ((EditText) findViewById(R.id.txt_search)).setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    if (Utils.isNetworkConnected(getApplicationContext(), true)) {
                        new GetSearchProductsTask().execute();
                        return true;
                    }
                }
                return false;
            }

        });

    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_search), Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_cancel), Config.BOLD);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.img_search:
                search = ((EditText) findViewById(R.id.txt_search)).getText().toString().trim();
                if (!search.isEmpty()) {
                    ((EditText) findViewById(R.id.txt_search)).setText("");
                    searchList.clear();
                }
                break;
            case R.id.txt_cancel:
                finish();
                break;
        }
    }

    class GetSearchProductsTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgessDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            String apikey = mPrefs.getStringValue(AppPreferences.API_KEY);

            postDataParams.put("page", "1");
            postDataParams.put("search_text", search);
            postDataParams.put("api_key", apikey);
            postDataParams.put("category_id", categoryId);
            return HTTPUrlConnection.getInstance().load(Config.PRODUCT, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONArray data = object.getJSONArray("data");
                    searchList.clear();
                    for (int i = 0; i < data.length(); i++) {
                        Collection product = new Collection();
                        product.id = ((JSONObject) data.get(i)).getInt("id");
                        product.product_name = ((JSONObject) data.get(i)).getString("name");
                        product.price = ((JSONObject) data.get(i)).getString("price");
                        product.thumbURL = ((JSONObject) data.get(i)).getString("image");

                        searchList.add(product);
                    }
                    seachAdapter.notifyDataSetChanged();
                } else if (!object.isNull("apistatus") && object.getString("apistatus").equalsIgnoreCase("API rejection")) {
                    Utils.resetLogin(SearchActivity.this);

                } else {
                    Toast.makeText(SearchActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class MySearchValidation implements TextWatcher {
        private View view;

        private MySearchValidation(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.txt_search:
                    search = ((EditText) findViewById(R.id.txt_search)).getText().toString().trim();
                    if (search.isEmpty()) {
                        ((ImageView) findViewById(R.id.img_search)).setImageResource(R.drawable.search_home);
                        //searchRecycler.setVisibility(View.GONE);
                        searchList.clear();

                    } else {
                        ((ImageView) findViewById(R.id.img_search)).setImageResource(R.drawable.close_icon);
                        //searchRecycler.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }
}
