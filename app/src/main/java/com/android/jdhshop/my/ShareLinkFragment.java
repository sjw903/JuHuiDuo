package com.android.jdhshop.my;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.R;
import com.android.jdhshop.adapter.InviteLinkUrlAdapter;
import com.android.jdhshop.base.BaseFragment;
import com.android.jdhshop.common.SPUtils;
import com.android.jdhshop.common.T;
import com.android.jdhshop.config.Constants;
import com.android.jdhshop.https.HttpUtils;
import com.android.jdhshop.https.HttpUtils.TextHttpResponseHandler;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static android.content.Context.CLIPBOARD_SERVICE;

public class ShareLinkFragment extends BaseFragment {
    Context mContext;
    Activity mActivity;
    String TAG = getClass().getSimpleName();
    List<String> invite_link_list = new ArrayList<>();
    String link = "";
    InviteLinkUrlAdapter linkUrlAdapter;

    @BindView(R.id.invite_rc)
    RecyclerView invite_rc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_share_link, container, false);
        ButterKnife.bind(this,v);
        mContext = getContext();
        mActivity = getActivity();
        initUI();
        initData();
        return v;
    }

    private void initData(){
//        if(CaiNiaoApplication.getUserInfoBean()!=null && "Y".equals(CaiNiaoApplication.getUserInfoBean().user_msg.is_share_vip)){
//            link= SPUtils.getStringData( getActivity(),"share_url_vip","")+"/wap/UserAccount/register/referrer_id/"+ SPUtils.getStringData(getContext(),Constants.UID,"");
//        }else{

//        if ("".equals(SPUtils.getStringData(mActivity,"share_domain",""))){
//            link=Constants.APP_IP+"/wap/Index/down?inviteCode="+ SPUtils.getStringData(getContext(),"inviteCode","");
//        }
//        else{
//            link=SPUtils.getStringData(mActivity,"share_domain","") + "/wap/Index/down?inviteCode="+ SPUtils.getStringData(getContext(),"inviteCode","");
//        }

//        }
        link = CaiNiaoApplication.getUserInfoBean().user_msg.share_url;
        getLink();

    }
    private void initUI(){
        linkUrlAdapter = new InviteLinkUrlAdapter(mContext, invite_link_list, new InviteLinkUrlAdapter.OnCopyClickListen() {
            @Override
            public void onCopyClick(int position) {
                ClipboardManager myClipboard;
                myClipboard = (ClipboardManager) mActivity.getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip;
                myClip = ClipData.newPlainText("text", invite_link_list.get(position));
                myClipboard.setPrimaryClip(myClip);
                ToastUtils.showShortToast(mContext, "复制成功");
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        invite_rc.setLayoutManager(linearLayoutManager);
        invite_rc.setAdapter(linkUrlAdapter);
    }
    private void getLink(){
        RequestParams params = new RequestParams();
        params.put("token", SPUtils.getStringData(mContext,"token",""));
        HttpUtils.post(Constants.INVITE_LIST_URL, ShareLinkFragment.this,params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // Log.d(TAG, "onFailure: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // Log.d(TAG, "onSuccess: " + responseString);
                JSONObject response = JSONObject.parseObject(responseString);
                if (response.getIntValue("code") == 0){
                    //成功
                    for (int i=0;i<response.getJSONArray("list").size();i++){
                        JSONObject item = response.getJSONArray("list").getJSONObject(i);
                        invite_link_list.add(item.getString("start_content") + item.getString("url") + item.getString("end_content"));
                        linkUrlAdapter.notifyDataSetChanged();
                    }

                    // Log.d(TAG, "onSuccess: "  + invite_link_list.size());
                    // Log.d(TAG, "onSuccess: " + invite_link_list);
                }
                else{
                    //失败
                    T.showShortBottom(mContext,response.getString("msg"));
                }
            }
        });
    }
}