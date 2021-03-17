## banner

一个支持图片无限轮播的控件，内部采用`ViewPager`进行实现，现暂时支持以下切换动画效果

- 淡入淡出：`Transformers.DEFAULT`

- 先快后慢：`Transformers.DAMPING`

- 窗口切换效果：`Transformers.FLIP`

- FLIPOG`ransformers.ROTATION_3D`

- 垂直方向：`Transformers.DAMPING_VERTICAL`

  

**扩展**：可以通过实现`AbsTransformer`和 `ViewPager.PageTransformer`接口实现自定义切换动画



<br/>

### 依赖配置

`VarietyTabLayout` 的使用依赖 `ViewPager` ，所以也需要加上 `ViewPager` 的依赖

```groovy
implementation 'com.ytempest:banner:1.0'
```



<br/>

### 简单使用

##### 第一步

继承 `AbsBannerBinder` 实现`Banner`的内容`View`，绑定数据，同时可以选择创建一个`Banner`的标题栏，可以不使用标题栏

```java
public class BannerBinder extends AbsBannerBinder<BannerBean> {

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, BannerBean data, int position) {
        return inflater.inflate(R.layout.item_banner_content, container, false);
    }

    @Nullable
    @Override
    protected View onCreateTitleView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.item_banner_title, container, false);
    }

    @Override
    protected void onUpdateTitleView(@Nullable View view, BannerBean data, int position, int count) {
    }
}
```

##### 第二步

配置`banner`

```java
mBanner = findViewById(R.id.view_main_banner);
// 设置数据绑定器
mBanner.setBannerBinder(new BannerBinder());
// 设置banner展示时间
mBanner.setPlayDuration(2000);
// 设置banner切换动画时间
mBanner.setScrollDuration(1000);
// 设置标题栏在banner的位置
mBanner.setBannerTitleGravity(Gravity.BOTTOM);
// 设置切换动画
mBanner.setScrollAnimation(Transformers.FLIP);
// 展示数据
List<BannerBean> list = new ArrayList<>();
// TODO list.add()
mBanner.display(list);


// 开启轮播
mBanner.startAutoPlay();
// 关闭轮播
mBanner.stopAutoPlay();
```

<br/>

<br/>