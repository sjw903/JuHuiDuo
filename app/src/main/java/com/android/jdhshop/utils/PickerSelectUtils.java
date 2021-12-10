package com.android.jdhshop.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.android.jdhshop.bean.JsonBean;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 开发者：陈飞
 * 时间:2018/7/18 09:51
 * 说明：选择器工具类
 */
public class PickerSelectUtils {

    private static PickerSelectUtils pickerSelectUtils = null;

    private Context mContext;
    private TimePickerView timePickerView;

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private OptionsPickerView cityOptions;
    private OptionsPickerView singleTextOptions;

    public PickerSelectUtils() {

    }

    public static PickerSelectUtils getInstence() {
        if (pickerSelectUtils == null) {
            synchronized (PickerSelectUtils.class) {
                pickerSelectUtils = new PickerSelectUtils();
            }
        }
        return pickerSelectUtils;
    }

    public PickerSelectUtils from(Context context) {
        this.mContext = context;
        return this;
    }


    /**
     * @属性:时间选择器 格式 yyyy-MM-dd
     * @开发者:陈飞
     * @时间:2018/7/18 10:06
     */
    public PickerSelectUtils TimePicker(final View viewR, Callback callback) {

        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String content = DateUtils.format_yyyy_MM_dd(date);
                if (viewR instanceof TextView) {
                    ((TextView) viewR).setText(content);
                    callback.contentChanged(content);
                } else if (viewR instanceof EditText) {
                    ((EditText) viewR).setText(content);
                    callback.contentChanged(content);
                } else if (viewR instanceof Button) {
                    ((Button) viewR).setText(content);
                    callback.contentChanged(content);
                }
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true)
                .build();


        Dialog mDialog = timePickerView.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            timePickerView.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
        return this;
    }


    /**
     * @属性:时间选择器 格式 yyyy-MM
     * @开发者:陈飞
     * @时间:2018/7/18 10:06
     */
    public PickerSelectUtils TimeMonthPicker(final View viewR) {

        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (viewR instanceof TextView) {
                    ((TextView) viewR).setText(DateUtils.format_yyyy_MM(date));
                } else if (viewR instanceof EditText) {
                    ((EditText) viewR).setText(DateUtils.format_yyyy_MM(date));
                } else if (viewR instanceof Button) {
                    ((Button) viewR).setText(DateUtils.format_yyyy_MM(date));
                }
            }
        })
                .setType(new boolean[]{true, true, false, false, false, false})
                .isDialog(true)
                .build();


        Dialog mDialog = timePickerView.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            timePickerView.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
        return this;
    }

    /**
     * @属性:城市选择器 三级联动 省市区
     * @开发者:陈飞
     * @时间:2018/7/18 10:22
     */
    public PickerSelectUtils CityThreePicker(final View viewRe) {
        CityThreePicker(viewRe, null);
        return this;
    }


    /**
     * @属性:城市选择器 三级联动 省市区
     * @开发者:陈飞
     * @时间:2018/7/18 10:22
     */
    public PickerSelectUtils CityThreePicker(final View viewRe, final onPickerSelectListener listener) {
        //解析数据
        //开始解析数据
        initJsonData(new onJsonLisnener() {
            @Override
            public void onResult() {
                //设置选中项文字颜色
                cityOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        if (viewRe instanceof TextView) {
                            ((TextView) viewRe).setText(options1Items.get(options1).getPickerViewText() + options2Items.get(options1).get(options2) + options3Items.get(options1).get(options2).get(options3));
                        } else if (viewRe instanceof EditText) {
                            ((EditText) viewRe).setText(options1Items.get(options1).getPickerViewText() + options2Items.get(options1).get(options2) + options3Items.get(options1).get(options2).get(options3));
                        } else if (viewRe instanceof Button) {
                            ((Button) viewRe).setText(options1Items.get(options1).getPickerViewText() + options2Items.get(options1).get(options2) + options3Items.get(options1).get(options2).get(options3));
                        }
                        if (listener != null) {
                            listener.onSelectResult(options1);
                            listener.onSelectResult(options1, options1Items.get(options1).getPickerViewText(), options2Items.get(options1).get(options2), options3Items.get(options1).get(options2).get(options3));
                        }
                    }
                })
                        .setTitleText("城市选择")
                        .setDividerColor(Color.BLACK)
                        .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                        .setContentTextSize(20)
                        .build();

                cityOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
            }
        });
        return this;
    }


    /**
     * @属性:城市选择器 二级联动 省市
     * @开发者:陈飞
     * @时间:2018/7/18 10:43
     */
    public PickerSelectUtils CityTwoPicker(final View viewRe) {
        CityTwoPicker(viewRe, null);
        return this;
    }

    /**
     * @属性:城市选择器 二级联动 省市
     * @开发者:陈飞
     * @时间:2018/7/18 10:43
     */
    public PickerSelectUtils CityTwoPicker(final View viewRe, final onPickerSelectListener listener) {
        //解析数据
        //开始解析数据
        initJsonData(new onJsonLisnener() {
            @Override
            public void onResult() {
                //设置选中项文字颜色
                cityOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        if (viewRe instanceof TextView) {
                            ((TextView) viewRe).setText(options1Items.get(options1).getPickerViewText() + options2Items.get(options1).get(options2));
                        } else if (viewRe instanceof EditText) {
                            ((EditText) viewRe).setText(options1Items.get(options1).getPickerViewText() + options2Items.get(options1).get(options2));
                        } else if (viewRe instanceof Button) {
                            ((Button) viewRe).setText(options1Items.get(options1).getPickerViewText() + options2Items.get(options1).get(options2));
                        }
                        if (listener != null) {
                            listener.onSelectResult(options1);
                        }
                    }
                })
                        .setTitleText("城市选择")
                        .setDividerColor(Color.BLACK)
                        .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                        .setContentTextSize(20)
                        .build();

                cityOptions.setPicker(options1Items, options2Items);//三级选择器
            }
        });
        return this;
    }


    /**
     * @属性:城市选择器 一级联动 省
     * @开发者:陈飞
     * @时间:2018/7/18 10:43
     */
    public PickerSelectUtils CityOnePicker(final View viewRe) {
        CityOnePicker(viewRe, null);
        return this;
    }


    /**
     * @属性:城市选择器 一级联动 省
     * @开发者:陈飞
     * @时间:2018/7/18 10:43
     */
    public PickerSelectUtils CityOnePicker(final View viewRe, final onPickerSelectListener listener) {
        //解析数据
        //开始解析数据
        initJsonData(new onJsonLisnener() {
            @Override
            public void onResult() {
                //设置选中项文字颜色
                cityOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        if (viewRe instanceof TextView) {
                            ((TextView) viewRe).setText(options1Items.get(options1).getPickerViewText());
                        } else if (viewRe instanceof EditText) {
                            ((EditText) viewRe).setText(options1Items.get(options1).getPickerViewText());
                        } else if (viewRe instanceof Button) {
                            ((Button) viewRe).setText(options1Items.get(options1).getPickerViewText());
                        }
                        if (listener != null) {
                            listener.onSelectResult(options1);
                        }
                    }
                })
                        .setTitleText("城市选择")
                        .setDividerColor(Color.BLACK)
                        .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                        .setContentTextSize(20)
                        .build();

                cityOptions.setPicker(options1Items);//三级选择器
            }
        });
        return this;
    }

    /**
     * @属性:单列文字选择
     * @开发者:陈飞
     * @时间:2018/7/18 10:47
     */
    public PickerSelectUtils SingleTextPicker(String title, List<String> strings, View viewRe) {
        SingleTextPicker(title, strings, viewRe, null);
        return this;
    }

    /**
     * @属性:单列文字选择
     * @开发者:陈飞
     * @时间:2018/7/18 10:47
     */
    public PickerSelectUtils SingleTextPicker(String title, final List<String> strings, final View viewRe, final onPickerSelectListener listener) {
        //设置选中项文字颜色
        singleTextOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (viewRe instanceof TextView) {
                    ((TextView) viewRe).setText(strings.get(options1));
                } else if (viewRe instanceof EditText) {
                    ((EditText) viewRe).setText(strings.get(options1));
                } else if (viewRe instanceof Button) {
                    ((Button) viewRe).setText(strings.get(options1));
                }

                if (listener != null) {
                    listener.onSelectResult(options1);
                }
            }
        }).setTitleText(title)
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        if (singleTextOptions != null) {
            singleTextOptions.setPicker(strings);//三级选择器
        }

        return this;
    }


    /**
     * @属性:解析数据
     * @开发者:陈飞
     * @时间:2018/7/18 10:25
     */
    private void initJsonData(onJsonLisnener lisnener) {

        options1Items.clear();
        options2Items.clear();
        options3Items.clear();

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(mContext, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        if (lisnener != null) {
            lisnener.onResult();
        }

    }

    public interface onPickerSelectListener {
        void onSelectResult(int position);

        void onSelectResult(int position, String province, String city, String county);
    }


    public interface onJsonLisnener {
        void onResult();
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }


    public void show() {
        //时间选择器
        if (timePickerView != null) {
            timePickerView.show();
        } else if (cityOptions != null) {
            cityOptions.show();
        } else if (singleTextOptions != null) {
            singleTextOptions.show();
        }

    }

    public interface Callback {
        void contentChanged(String content);
    }
}
