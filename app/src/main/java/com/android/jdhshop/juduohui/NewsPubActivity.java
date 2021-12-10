package com.android.jdhshop.juduohui;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.R;
import com.android.jdhshop.activity.NewsActivity;
import com.android.jdhshop.base.BaseActivity;
import com.android.jdhshop.common.LogUtils;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.android.jdhshop.juduohui.response.HttpJsonResponse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import me.panpf.sketch.SketchImageView;

public class NewsPubActivity extends BaseActivity {


    @BindView(R.id.clear_input)
    TextView clear_input;
    @BindView(R.id.pub_button)
    Button pub_button;
    @BindView(R.id.edit_url)
    EditText edit_url;
    @BindView(R.id.checkbox_zzxy)
    CheckBox checkbox_zzxy;
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.open_xieyi)
    TextView open_xieyi;
    @BindView(R.id.news_cat)
    Spinner news_cat;
    @BindView(R.id.news_pub_art_list)
    RecyclerView news_pub_art_list;
    Context mContext;

    String ip = "";
    String area = "";
    List<String> span_ss = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    int select_position = 0;
    String select_string = "请选择";
    JSONArray type_arr = new JSONArray();

    PubArtAdapter pubArtAdapter;
    JSONArray pub_art_list = new JSONArray();

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_news_pub);
        ButterKnife.bind(this);

        mContext = this;

        tv_left.setVisibility(View.VISIBLE);
        tv_title.setText("转载文章");
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.spinner_titlte, span_ss);
        news_cat.setAdapter(arrayAdapter);


    }

    @Override
    protected void initData() {
        HttpUtils.get_third("http://pv.sohu.com/cityjson?ie=utf-8", NewsPubActivity.this, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // Log.d(TAG, "awfeawefawef: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // Log.d(TAG, "awfeawefawef: " + responseString);
                try {
                    String tmp = responseString.replace("var returnCitySN = ", "").replace(";", "");
                    JSONObject jo = JSONObject.parseObject(tmp);
                    // Log.d(TAG, "awfeawefawef: " + jo.getString("cname"));
                    // Log.d(TAG, "awfeawefawef: " + jo.getString("cip"));
                    ip = jo.getString("cip");
                    area = jo.getString("cname");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        checkbox_zzxy.setChecked(SPUtils.getBoolean(mContext, "zzxy_checked", false));
        RequestParams req = new RequestParams();
        HttpUtils.post(Constants.GET_XH_MEDIA_LIB_TYPE, NewsPubActivity.this, req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                LogUtils.logd(response.toJSONString());
                if (response.getIntValue("code") == 0) {
                    type_arr = response.getJSONArray("list");

                    for (int i = 0; i < type_arr.size(); i++) {
                        if (type_arr.getJSONObject(i).getString("type_name").equals("推荐")) {
                            span_ss.add("请选择");
                        } else {
                            span_ss.add(type_arr.getJSONObject(i).getString("type_name"));
                        }
                    }

                    arrayAdapter.notifyDataSetChanged();

                } else {
                    news_cat.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
                // Log.d(TAG, "onFailure: " + responseString);
            }
        });

        RequestParams art_req = new RequestParams();
        art_req.put("cat_id", "31");
        HttpUtils.post(Constants.MESSAGE_ARTICLE_GETARTICLELIST_URL, NewsPubActivity.this, art_req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                // Log.d(TAG, "onSuccess: " + response.toJSONString());
                if (response.getIntValue("code") == 0) {
                    // Log.d(TAG, "onSuccess: " + response.getJSONObject("data").getJSONArray("list"));
                    pub_art_list.addAll(response.getJSONObject("data").getJSONArray("list"));
                    // Log.d(TAG, "onSuccess: pub_art_list = " + pub_art_list);
                    pubArtAdapter = new PubArtAdapter(NewsPubActivity.this, pub_art_list);
                    news_pub_art_list.setAdapter(pubArtAdapter);
                    LinearLayoutManager lm = new LinearLayoutManager(mContext);
                    lm.setOrientation(LinearLayoutManager.VERTICAL);
                    news_pub_art_list.setLayoutManager(lm);

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
            }
        });
    }


    String opt_url = "";

    @Override
    protected void initListener() {
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        news_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Log.d(TAG, "onItemSelected: " + position + "," + id + span_ss.get(position));
                // Log.d(TAG, "onItemSelected: " + view.getClass().getName());
                select_string = span_ss.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Log.d(TAG, "onNothingSelected: ");
            }
        });
        checkbox_zzxy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.saveBoolean(mContext, "zzxy_checked", isChecked);
            }
        });

        edit_url.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                ClipData data = cm.getPrimaryClip();

                try {
                    // Log.d(TAG, "onLongClick: " + data);
                    // Log.d(TAG, "onLongClick: " + data.getItemAt(0) + "," + data.getItemCount());
                    String txt = getClipboard();
                    // Log.d(TAG, "onLongClick: " + txt);
                    if (txt != null) edit_url.setText(txt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        open_xieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsActivity.actionStart(mContext, "79", "转载发布协议");
            }
        });

        edit_url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_url.getText().length() > 0) {
                    clear_input.setTextColor(Color.parseColor("#FF5722"));
                } else {
                    clear_input.setTextColor(Color.parseColor("#CCCCCC"));
                }
            }
        });

        clear_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_url.setText("");
            }
        });


        pub_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (!checkbox_zzxy.isChecked()) {
                    showToast("请先同意并勾选”转载发布协议“后再继续转载发布。");
                    return;
                }

                if (edit_url.getText().length() < 1) {
                    showToast("请粘贴或者输入需要转载的网站。");
                    return;
                }


                if (!StringUtils.contains(edit_url.getText(), "http")) {
                    showToast("转载的地址必须以http://或https://开头。");
                    return;
                }

                opt_url = "http" + StringUtils.substringAfter(edit_url.getText().toString(), "http");
                // Log.d(TAG, "onClick: " + opt_url);

                if (select_string.contains("请选择")) {
                    showToast("请选择转载分类");
                    news_cat.requestFocus();
                    return;
                }


                showLoadingDialog();
                // 今日头条
                if (opt_url.contains("toutiaocdn") || opt_url.contains("toutiao")) {
                    RequestParams req = new RequestParams();
                    req.put("short_url", opt_url);
                    AsyncHttpClient client = HttpUtils.getClient();
                    client.setEnableRedirects(false);
                    client.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.3 Mobile/15E148 Safari/604.1");
                    client.get(opt_url, new com.loopj.android.http.TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            for (Header header : headers) {
                                // Log.d(TAG, "onSuccess: " + header.getName() + " = " + header.getValue());
                            }

                            // Log.d(TAG, "onSuccess: " + responseString);
                            getTransUrl(opt_url);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                            closeLoadingDialog();
                            // Log.d(TAG, "onFailure: " + statusCode +"," + responseString);
                            if (statusCode == 302) {
                                // responseString <a href="https://m.toutiaocdn.com/i1714279079034892/?app=news_article&amp;timestamp=1634896593&amp;use_new_style=1&amp;share_token=122f1ade-22bc-4c35-9e0c-d5643fdc5a6b&amp;tt_from=copy_link&amp;utm_source=copy_link&amp;utm_medium=toutiao_android&amp;utm_campaign=client_share">Found</a>.
                                String ss = "<a href=\"https://m.toutiaocdn.com/i1714279079034892/?app=news_article&amp;timestamp=1634896593&amp;use_new_style=1&amp;share_token=122f1ade-22bc-4c35-9e0c-d5643fdc5a6b&amp;tt_from=copy_link&amp;utm_source=copy_link&amp;utm_medium=toutiao_android&amp;utm_campaign=client_share\">Found</a>.";
                                runOnUiThread(() -> {
                                    getTransUrl("http" + StringUtils.substringBetween(responseString, "http", "\""));
                                });

                            } else {
//                                showToast("发生了意想不到的问题，请稍后重试或联系客服");
                                getTransUrl(opt_url);
                            }
                        }
                    });
                } else if (opt_url.contains("mparticle.uc.cn") || opt_url.contains("partners.sina.cn")) {
                    getTransUrl(opt_url);
                } else {
                    HttpUtils.get_third(opt_url, NewsPubActivity.this, new TextHttpResponseHandler() {
                        @Override
                        protected void onSuccess(int statusCode, Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            submitJuduohui(responseString);
                        }

                        @Override
                        protected void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            showToast("文章转载失败，如有问题请联系客服!");
                        }
                    });
                }


            }
        });

    }

    // 第一步，转换网址
    private void getTransUrl(String url) {
        RequestParams thr = new RequestParams();
        thr.put("short_url", url);
        HttpUtils.post(Constants.MEDIA_LIB_REPRINT_TRANS_URL, NewsPubActivity.this, thr, new HttpJsonResponse() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                closeLoadingDialog();
                if (throwable != null) throwable.printStackTrace();
                showToast("文章转载失败，如有问题请联系客服!");
            }

            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                runOnUiThread(() -> {
                    getRealUrl(response.getString("longUrl"));
                });
            }
        });
    }

    // 第二步，取转换后的网址内容
    private void getRealUrl(String url) {
        // Log.d(TAG, "getRealUrl: " + url);
        HttpUtils.get_third(url, NewsPubActivity.this, new TextHttpResponseHandler() {
            @Override
            protected void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
                // Log.d(TAG, "onSuccess: " + responseString);
                submitJuduohui(responseString);
            }

            @Override
            protected void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                // Log.d(TAG, "onFailure: " + responseString);
                closeLoadingDialog();
                showToast("取内容失败");
            }
        });
    }

    // 第三步，向服务器提交HTML内容
    private void submitJuduohui(String html) {
        RequestParams req = new RequestParams();
        req.put("html", html);
        req.put("url", opt_url);
        req.put("area", area);
        req.put("media_type", getNewsTypeId(select_string));
        // Log.d(TAG, "onSuccess: " + req.toString());

        HttpUtils.post(Constants.MEDIA_LIB_REPRINT_ADD, NewsPubActivity.this, req, new HttpJsonResponse() {
            @Override
            protected void onSuccess(JSONObject response) {
                super.onSuccess(response);
                if (response.getIntValue("code") == 0) {
                    showToast(response.getString("msg"));
                    setResult(RESULT_OK);
                    finish();
                } else {
                    closeLoadingDialog();
                    showToast(response.getString("msg"));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
                // Log.d(TAG, "MEDIA_LIB_REPRINT_ADD onSuccess: " + responseString);

            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }


    private String getNewsTypeId(String news_type_name) {
        for (int i = 0; i < type_arr.size(); i++) {
            if (news_type_name.equals(type_arr.getJSONObject(i).getString("type_name"))) {
                return type_arr.getJSONObject(i).getString("id");
            }
        }

        return "";
    }

    private static class PubArtAdapter extends RecyclerView.Adapter<PubArtAdapter.PTViewHolder> {
        Activity mActivity;
        JSONArray items = new JSONArray();

        public PubArtAdapter(Activity activity, @NotNull JSONArray arrays) {
            mActivity = activity;
            items.addAll(arrays);
        }

        @Override
        public PTViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mActivity).inflate(R.layout.item_pub_art_list, parent, false);
            return new PTViewHolder(v);
        }

        @Override
        public void onBindViewHolder(PTViewHolder holder, int position) {
            // Log.d(TAG, "onBindViewHolder: " + items.getJSONObject(position).getString("title"));
            holder.art_icon.getOptions().setErrorImage(R.drawable.app_icon).setLoadingImage(R.drawable.app_icon);
            holder.art_icon.displayContentImage(items.getJSONObject(position).getString("img"));
            final String text_title = items.getJSONObject(position).getString("title");
            holder.art_text.setText(text_title);
            final String text_id = items.getJSONObject(position).getString("article_id");
            holder.art_list_item.setOnClickListener(v -> NewsActivity.actionStart(mActivity, text_id, text_title));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private static class PTViewHolder extends RecyclerView.ViewHolder {
            SketchImageView art_icon;
            TextView art_text;
            LinearLayout art_list_item;

            public PTViewHolder(View itemView) {
                super(itemView);
                art_icon = itemView.findViewById(R.id.art_list_icon);
                art_text = itemView.findViewById(R.id.art_list_text);
                art_list_item = itemView.findViewById(R.id.art_list_item);
            }
        }
    }
}