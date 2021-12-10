package com.android.jdhshop.merchantactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jdhshop.R;
import com.android.jdhshop.activity.TaskBigImgActivity;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.merchantadapter.CaipinimglistssAdapter;
import com.android.jdhshop.merchantbean.Merchantimglist;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ImgoneFragment extends Fragment {

    private View mview;

    private RecyclerView recyclerView;
    private CaipinimglistssAdapter caipinimglistAdapter;
    private SmartRefreshLayout smartRefreshLayout;
    private int page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_imgone, container, false);
        initView();
        return mview;
    }

    public void initView() {
        recyclerView = mview.findViewById(R.id.imgone_recyclerview);
        smartRefreshLayout = mview.findViewById(R.id.refresh_layout);
        caipinimglistAdapter = new CaipinimglistssAdapter(getActivity(), caipinlist);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(caipinimglistAdapter);
        caipinimglistAdapter.setsubClickListener(new CaipinimglistssAdapter.SubClickListener() {
            @Override
            public void OntopicClickListener(View v, String detail, int posit) {
                ArrayList img_arr = new ArrayList();
                for (int i = 0; i < caipinlist.size(); i++) {
                    img_arr.add(caipinlist.get(i).img);
                }
                Intent intent = new Intent(getActivity(), TaskBigImgActivity.class);
                intent.putExtra("position", posit);
                intent.putExtra("title", "图片");
                intent.putStringArrayListExtra("paths", img_arr);
                startActivity(intent);
            }
        });
        getmerchantjiesao();


        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                caipinlist.clear();
                page = 1;
                getmerchantjiesao();

                //refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;

                getmerchantjiesao();
                //refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败

            }
        });
    }


    public List<Merchantimglist> caipinlist = new ArrayList<>();

    //获取菜品相册列表
    private void getmerchantjiesao() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("merchant_id", getArguments().getString("merid"));
        requestParams.put("type", getArguments().getString("pid"));
        requestParams.put("p", page);
        requestParams.put("per", "8");
        HttpUtils.post(Constants.getimgList, ImgoneFragment.this, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                }
                try {
                    Log.e("msg", responseString);
                    JSONObject object = new JSONObject(responseString);
                    if ("0".equals(object.getString("code"))) {
                        JSONArray array = object.getJSONObject("data").getJSONArray("list");
                        for (int i = 0; i < array.length(); i++) {
                            caipinlist.add(new Gson().fromJson(array.getJSONObject(i).toString(), Merchantimglist.class));
                        }
                        caipinimglistAdapter.notifyDataSetChanged();

                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
