package com.android.jdhshop.ziyuan;
 
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.util.SimpleArrayMap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.reflect.Constructor;

/**
 * 拦截系统View实例化，加载外部分离资源
 */
public class ResourceFactory implements LayoutInflater.Factory2 {
 
    /**
     * 通过反射构造方法的参数列表类型
     */
    private static final Class<?>[] sConstructorSignature = new Class<?>[]{
            Context.class, AttributeSet.class};
 
    /**
     * 缓存之前已经构造过的View的构造方法
     */
    private static final SimpleArrayMap<String, Constructor<? extends View>> sConstructorMap =
            new SimpleArrayMap<>();
 
    /**
     * 系统View的包名前缀
     */
    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };
 
    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return null;
    }
 
    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view = null;
        // 如果没有包名，那么加上系统View的包名前缀遍历，看是否有能够找到的类，如果有则初始化并返回
        if (-1 == name.indexOf(".")) {
            for (int i = 0; i < sClassPrefixList.length; i++) {
                view = createViewByPrefix(context, name, sClassPrefixList[i], attrs);
                if (view != null) {
                    break;
                }
            }
        } else {
            view = createViewByPrefix(context, name, null, attrs);
        }
 
        // 设置自定义属性
        if (view != null) {
            setViewResource(view, attrs);
        }
 
        return view;
    }
 
    /**
     *
     * @param context
     * @param name
     * @param prefix
     * @param attrs
     * @return
     */
    private View createViewByPrefix(Context context, String name, String prefix, AttributeSet attrs) {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        try {
            // 如果缓存里没有，那么就根据具体View的类型(prefix? + name)找到对应的构造方法
            if (constructor == null) {
                Class<? extends View> clazz = Class.forName(
                        prefix != null ? (prefix + name) : name,
                        false,
                        context.getClass().getClassLoader()).asSubclass(View.class);
                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            // 设置构造方法访问权限
            constructor.setAccessible(true);
            // 通过反射实例化对应的View
            return constructor.newInstance(context, attrs);
        } catch (Exception e) {
            return null;
        }
    }
 
    /**
     * 设置自定义属性
     *
     * @param view 需要设置属性的view
     * @param attrs 具体的View属性信息
     */
    private void setViewResource(View view, AttributeSet attrs) {
        // 遍历这个属性的集合，找到自定义外部资源属性
        int attributeCount = attrs.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = attrs.getAttributeName(i);
 
            if (!attributeName.equals("resource")) {
                continue;
            }
 
            /** 获取自定义属性值。
             * 规则：[需要设置的view的属性名]/[资源类型]/[资源名]
             * 例如：background/drawable/a1，表示需要设置View的background属性，资源类型是drawable，资源名是a1
             * 例如：textColor/color/purePink，表示需要设置View的textColor属性，资源类型是color，资源名是purePink
             */
            String attributeValue = attrs.getAttributeValue(i);
            String[] split = attributeValue.split("/");
            if (split == null || split.length != 3) {
                continue;
            }

            String viewAttributeName = split[0];
            String resourceType = split[1];
            String resourceName = split[2];
 
            if (viewAttributeName.equals("background")) {
                Drawable drawable = ResourceManager.getInstance().getDrawable(resourceName, resourceType);
                if (drawable != null) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        view.setBackground(drawable);
//                    }
                } else {
                    throw new ResourceException("The resource: " + attributeValue + " not found");
                }
            } else if (viewAttributeName.equals("src")) {
                if (view instanceof ImageView) {
                    Drawable drawable = ResourceManager.getInstance().getDrawable(resourceName, resourceType);
                    if (drawable != null) {
                        ((ImageView) view).setImageDrawable(drawable);
                    } else {
                        throw new ResourceException("The resource: " + attributeValue + " not found");
                    }
                } else {
                    throw new ResourceException("The resource: " + attributeValue + " not found");
                }
            } else if (viewAttributeName.equals("textColor")) {
                int color = ResourceManager.getInstance().getColor(resourceName, resourceType);
                ((TextView) view).setTextColor(color);
            }
 
        }
    }
 
}