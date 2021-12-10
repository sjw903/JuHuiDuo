package com.android.jdhshop.dialog;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdhshop.R;
import com.android.jdhshop.utils.WxUtil;
import com.android.jdhshop.utils.ZxingUtils;
import com.android.jdhshop.widget.CircleImageView;
import com.android.jdhshop.wmm.QQShareUtil;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 全屏名片分享Dialog
 */
public class FullScreenDialog extends DialogFragment {
    @BindView(R.id.iv_header)
    CircleImageView iv_header;

    @BindView(R.id.iv_qrcode)
    ImageView iv_qrcode;

    private LinearLayout cardLinearLayout;
    private String headUrl;
    private String name;
    private String sign;
    private String qrCode;
    private Bitmap bitmap;
    private List<Pair<String, Integer>> pairList = new ArrayList<>();

    {
//        pairList.add(new Pair<>("名片分享", R.mipmap.icon_card_share));
        pairList.add(new Pair<>("微信", R.mipmap.icon_weixin));
        pairList.add(new Pair<>("朋友圈", R.mipmap.icon_friend));
        pairList.add(new Pair<>("QQ", R.mipmap.icon_qq));
        pairList.add(new Pair<>("QQ空间", R.mipmap.icon_qqzone));
        pairList.add(new Pair<>("保存图片", R.mipmap.icon_save));
    }

    @Nullable
    @androidx.annotation.Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable @androidx.annotation.Nullable ViewGroup container, @Nullable @androidx.annotation.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_card, container, true);
        ButterKnife.bind(this, view);
        initView(view);
        cardLinearLayout = view.findViewById(R.id.ll_header);
        cardLinearLayout.setOnClickListener(v -> {
        });
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        BaseQuickAdapter<Pair<String, Integer>, BaseViewHolder> adapter = new BaseQuickAdapter<Pair<String, Integer>, BaseViewHolder>(R.layout.item_share_card, pairList) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, Pair<String, Integer> s) {
                RelativeLayout relativeLayout = baseViewHolder.getView(R.id.rl_share);
                CircleImageView img = baseViewHolder.getView(R.id.img);
                img.setImageResource(s.second);
                TextView tv_name = baseViewHolder.getView(R.id.tv_name);
                tv_name.setText(s.first);
                relativeLayout.setOnClickListener(v -> {
                    dismiss();
                    share(baseViewHolder.getLayoutPosition());
                });
            }
        };
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        Glide.with(this).load(headUrl).placeholder(R.mipmap.icon_defult_boy).dontAnimate().into(iv_header);
        iv_qrcode.setImageBitmap(bitmap);
        TextView tv_header = view.findViewById(R.id.tv_header);
        tv_header.setText(name);
        TextView tv_sign = view.findViewById(R.id.tv_sign);
        if (!TextUtils.isEmpty(sign)) {
            tv_sign.setText(sign);
        }
        LinearLayout bottom = view.findViewById(R.id.ll_bottom);
        bottom.setOnClickListener(v -> {
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFullScreen); //dialog全屏
        bitmap = ZxingUtils.createBitmap(qrCode);
    }

    private void initView(View view) {
        view.setOnClickListener(v -> {
            dismiss();
        });
        TextView cancel = view.findViewById(R.id.tv_cancel);
        if (null != cancel) {
            cancel.setOnClickListener(v -> dismiss());
        }
    }

    /**
     * @param header 头像
     * @param name   名字
     * @param qrcode 二维码
     * @param sign   签名
     */
    public void setContentView(String header, String name, String qrcode, String sign) {
        headUrl = header;
        this.name = name;
        qrCode = qrcode;
        this.sign = sign;
    }

    public LinearLayout getCardLinearLayout() {
        return cardLinearLayout;
    }

    private void share(int pos) {
        Bitmap bitmap = ImageUtils.view2Bitmap(cardLinearLayout);
        switch (pos) {
            case 0:
                WxUtil.sharePicByBitMap(bitmap, "py", SendMessageToWX.Req.WXSceneSession, getContext());
                break;
            case 1:
                WxUtil.sharePicByBitMap(bitmap, "pyq", SendMessageToWX.Req.WXSceneTimeline, getContext());
                break;
            case 2: {
                String fileName = System.currentTimeMillis() + ".png";
                File file = new File(getContext().getCacheDir(), fileName);
                FileUtils.createFileByDeleteOldFile(file.getPath());
                byte[] bytes = ImageUtils.bitmap2Bytes(bitmap);
                FileIOUtils.writeFileFromBytesByChannel(file, bytes, true);
                QQShareUtil.shareImgToQQ(file.getPath(), getActivity(), new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        FileUtils.delete(file);
                    }

                    @Override
                    public void onError(UiError uiError) {
                        FileUtils.delete(file);
                    }

                    @Override
                    public void onCancel() {
                        FileUtils.delete(file);
                    }
                });
                break;
            }
            case 3: {
                String fileName = System.currentTimeMillis() + ".png";
                File file = new File(getContext().getCacheDir(), fileName);
                FileUtils.createFileByDeleteOldFile(file.getPath());
                byte[] bytes = ImageUtils.bitmap2Bytes(bitmap);
                FileIOUtils.writeFileFromBytesByChannel(file, bytes, true);
                QQShareUtil.shareImgToQQZONE(file.getPath(), getActivity(), new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        FileUtils.delete(file);
                    }

                    @Override
                    public void onError(UiError uiError) {
                        FileUtils.delete(file);
                    }

                    @Override
                    public void onCancel() {
                        FileUtils.delete(file);
                    }
                });
                break;
            }
            case 4: {
                File file = ImageUtils.save2Album(bitmap, Bitmap.CompressFormat.PNG);
                if (null == file) {
                    Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "保存图片到相册成功", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
