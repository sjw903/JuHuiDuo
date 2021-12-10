# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5

# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames

# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses

# 这句话能够使我们的项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose

# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers

# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify

# 保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses

# 避免混淆泛型
-keepattributes Signature

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*


#############################################
#
# Android开发中一些需要保留的公共部分
#
#############################################

# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService


# 保留support下的所有类及其内部类
-keep class android.support.** {*;}
-ignorewarnings
# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

# 保留R下面的资源
-keep class **.R$* {*;}

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留在Activity中的方法参数是view的方法，
# 这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class com.android.jdhshop.bean.**{*;}
-keep class com.android.jdhshop.snbean.**{*;}
-keep class com.android.jdhshop.mallbean.**{*;}
-keep class com.android.jdhshop.merchantbean.**{*;}
# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}
-keep class com.google.gson.** { *; }
-dontwarn okio.**
-keep class okhttp3.** { *; }
# glide 的混淆代码
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }
#eventbus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#极光推送
-dontoptimize
-dontpreverify
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
#微信分享
-dontwarn com.tencent.mm.**
-keep class com.tencent.mm.**{*;}
#Butterkinfe
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
 -keepattributes Signature
     -keep class sun.misc.Unsafe { *; }
     -keep class com.taobao.** {*;}
     -keep class com.alibaba.** {*;}
     -keep class com.alipay.** {*;}
     -dontwarn com.taobao.**
     -dontwarn com.alibaba.**
     -dontwarn com.alipay.**
     -keep class com.ut.** {*;}
     -dontwarn com.ut.**
     -keep class com.ta.** {*;}
     -dontwarn com.ta.**
     -keep class org.json.** {*;}
     -keep class com.ali.auth.**{*;}
     -keep class com.alibaba.fastjson.**{*;}
     -keep class com.alipay.android.app.IAlixPay{*;}
     -keep class com.alipay.android.app.IAlixPay$Stub{*;}
     -keep class com.alipay.android.app.IRemoteServiceCallback{*;}
     -keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
     -keep class com.alipay.sdk.app.PayTask{ public *;}
     -keep class com.alipay.sdk.app.AuthTask{ public *;}
     -keep class com.alipay.sdk.app.H5PayCallback {
         <fields>;
         <methods>;
     }
     -keep class com.alipay.android.phone.mrpc.core.** { *; }
     -keep class com.alipay.apmobilesecuritysdk.** { *; }
     -keep class com.alipay.mobile.framework.service.annotation.** { *; }
     -keep class com.alipay.mobilesecuritysdk.face.** { *; }
     -keep class com.alipay.tscenter.biz.rpc.** { *; }
     -keep class org.json.alipay.** { *; }
     -keep class com.alipay.tscenter.** { *; }
     -keep class com.ta.utdid2.** { *;}
     -keep class com.ut.device.** { *;}
     -keep class com.kepler.jd.**{ public <fields>; public <methods>; public *; }
     -keep @com.facebook.common.internal.DoNotStrip class *
     -keepclassmembers class * {
     @com.facebook.common.internal.DoNotStrip *;
     }
     #基线包使用，生成mapping.txt
     -printmapping mapping.txt
     #生成的mapping.txt在app/build/outputs/mapping/release路径下，移动到/app路径下
     #修复后的项目使用，保证混淆结果一致
     #-applymapping mapping.txt
     #hotfix
     -keep class com.taobao.sophix.**{*;}
     -keep class com.ta.utdid2.device.**{*;}
     -dontwarn com.alibaba.sdk.android.utils.**
     #防止inline
     -dontoptimize
     -keepclassmembers class com.android.jdhshop.CaiNiaoApplication {
         public <init>();
     }
     #极光统计
      -keep public class cn.jiguang.analytics.android.api.** {
             *;
         }
#     -keep class org.greenrobot.greendao.**{*;}
#     -keep public interface org.greenrobot.greendao.**
#     -keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
#     public static java.lang.String TABLENAME;
#     }
#     -keep class **$Properties
#     -keep class net.sqlcipher.database.**{*;}
#     -keep public interface net.sqlcipher.database.**
#     -dontwarn net.sqlcipher.database.**
#     -dontwarn org.greenrobot.greendao.**
-keep class com.lljjcoder.**{
	*;
}

-dontwarn demo.**
-keep class demo.**{*;}
-dontwarn net.sourceforge.pinyin4j.**
-keep class net.sourceforge.pinyin4j.**{*;}
-keep class net.sourceforge.pinyin4j.format.**{*;}
-keep class net.sourceforge.pinyin4j.format.exception.**{*;}
-keepattributes Signature
-ignorewarnings
-keep class javax.ws.rs.** { *; }
-keep class com.alibaba.fastjson.** { *; }
-dontwarn com.alibaba.fastjson.**
-keep class sun.misc.Unsafe { *; }
-dontwarn sun.misc.**
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-keep class com.ut.** {*;}
-dontwarn com.ut.**
-keep class com.ta.** {*;}
-dontwarn com.ta.**
-keep class org.json.** {*;}
-dontwarn org.json.**
-keep class com.ali.auth.**{*;}
-dontwarn com.ali.auth.**
-keep class com.taobao.securityjni.** {*;}
-keep class com.taobao.wireless.security.** {*;}
-keep class com.taobao.dp.**{*;}
-keep class com.alibaba.wireless.security.**{*;}
-keep interface mtopsdk.mtop.global.init.IMtopInitTask {*;}
-keep class * implements mtopsdk.mtop.global.init.IMtopInitTask {*;}
-keep class org.greenrobot.greendao.**{*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep class net.sqlcipher.database.**{*;}
-keep public interface net.sqlcipher.database.**
-dontwarn net.sqlcipher.database.**
-dontwarn org.greenrobot.greendao.**

# 安全联盟混淆规则

 -keep, includedescriptorclasses class com.asus.msa.SupplementaryDID.** { *; }
 -keepclasseswithmembernames class com.asus.msa.SupplementaryDID.** { *; }
 -keep, includedescriptorclasses class com.asus.msa.sdid.** { *; }
 -keepclasseswithmembernames class com.asus.msa.sdid.** { *; }
 -keep public class com.netease.nis.sdkwrapper.Utils {public <methods>;}
 -keep class com.bun.miitmdid.**{*;}
 -keep class com.bun.lib.**{*;}
 -keep class com.samsung.android.deviceidservice.**{*;}
 -keep class a.**{*;}

 -keep class XI.CA.XI.**{*;}
 -keep class XI.K0.XI.**{*;}
 -keep class XI.XI.K0.**{*;}
 -keep class XI.xo.XI.XI.**{*;}
 -keep class com.asus.msa.SupplementaryDID.**{*;}
 -keep class com.asus.msa.sdid.**{*;}
 -keep class com.bun.lib.**{*;}
 -keep class com.bun.miitmdid.**{*;}
 -keep class com.huawei.hms.ads.identifier.**{*;}
 -keep class com.samsung.android.deviceidservice.**{*;}
 -keep class com.zui.opendeviceidlibrary.**{*;}
 -keep class org.json.**{*;}
 -keep public class com.netease.nis.sdkwrapper.Utils {public <methods>;}

 #三星 10加固奔溃处理方法
 -keep class com.bun.** {*;}
 -keep class com.asus.msa.** {*;}
 -keep class com.heytap.openid.** {*;}
 -keep class com.huawei.android.hms.pps.** {*;}
 -keep class com.meizu.flyme.openidsdk.** {*;}
 -keep class com.samsung.android.deviceidservice.** {*;}
 -keep class com.zui.** {*;}
 -keep class com.huawei.hms.ads.** {*; }
 -keep interface com.huawei.hms.ads.** {*; }

 -keepattributes *Annotation*
 #非androidX用户
 -keep @android.support.annotation.Keep class **{
     @android.support.annotation.Keep <fields>;
     @android.support.annotation.Keep <methods>;
 }
 -ignorewarnings
 -dontwarn com.baidu.mobads.sdk.api.**
 -keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
 }

 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }

 -keep class com.baidu.mobads.** { *; }
 -keep class com.style.widget.** {*;}
 -keep class com.component.** {*;}
 -keep class com.baidu.ad.magic.flute.** {*;}
 -keep class com.baidu.mobstat.forbes.** {*;}

 -keep class org.chromium.** {*;}
 -keep class org.chromium.** { *; }
 -keep class aegon.chrome.** { *; }
 -keep class com.kwai.**{ *; }
 -dontwarn com.kwai.**
 -dontwarn com.kwad.**
 -dontwarn com.ksad.**
 -dontwarn aegon.chrome.**

 # mimo SDK`
# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.examples.android.model.** { <fields>; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# AndroidX
-dontwarn com.google.android.material.**
-keep class com.google.android.material.** { *; }
-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

# Kotlin
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Analytics SDK
-keep class com.miui.analytics.** { *; }
-keep class com.xiaomi.analytics.* { public protected *; }

#Ad SDK
-keep class com.miui.zeus.mimo.sdk.** { *; }


# 安全联盟 sdk
-keep class com.bun.miitmdid.** { *; }
-keep interface com.bun.supplier.** { *; }

# asus
-keep class com.asus.msa.SupplementaryDID.** { *; }
-keep class com.asus.msa.sdid.** { *; }
# freeme
-keep class com.android.creator.** { *; }
-keep class com.android.msasdk.** { *; }
# huawei
-keep class com.huawei.hms.ads.** { *; }
-keep interface com.huawei.hms.ads.** {*; }
# lenovo
-keep class com.zui.deviceidservice.** { *; }
-keep class com.zui.opendeviceidlibrary.** { *; }
# meizu
-keep class com.meizu.flyme.openidsdk.** { *; }
# nubia
-keep class com.bun.miitmdid.provider.nubia.NubiaIdentityImpl { *; }
# oppo
-keep class com.heytap.openid.** { *; }
# samsung
-keep class com.samsung.android.deviceidservice.** { *; }
# vivo
-keep class com.vivo.identifier.** { *; }
# xiaomi
-keep class com.bun.miitmdid.provider.xiaomi.IdentifierManager { *; }
# zte
-keep class com.bun.lib.** { *; }
# coolpad
-keep class com.coolpad.deviceidsupport.** { *; }

# 小米推送
-dontwarn com.xiaomi.push.**
-keep class com.xiaomi.push.** { *; }