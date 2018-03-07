# BaseAdapter

## Base

### SimpleBaseAdapter
```java
public class SimpleStringAdapter extends SimpleBaseAdapter<Object> {

    public SimpleStringAdapter(Context context, List<Object> items) {
        super(context, items);
    }

    @Override
    protected void convert(BaseViewHolder holder, Object item, int position) {
        // set data
    }

    @Override
    protected int getItemLayoutId() {
        return 0; // return layout res id
    }
}
```

### MultiBaseAdapter
```java
public class ImageAdapter extends MultiBaseAdapter<Object> {

    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_IMAGE = 2;

    public ImageAdapter(Context context, List<Object> items) {
        super(context, items);
    }

    @Override
    protected void convert(BaseViewHolder holder, Object item, int position, int viewType) {
        if (viewType == VIEW_TYPE_NORMAL) {
            
        } else {
            
        }
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        if (viewType == VIEW_TYPE_NORMAL) {
            return R.layout.item;
        } else {
            return R.layout.item_has_image;
        }
    }

    @Override
    protected int getMultiViewType(Object item, int position) {
        if (!item.isHasImage()) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_IMAGE;
        }
    }
}
```

## Helper

### EmptyHelperAdapter
```java
final EmptyHelperAdapter emptyHelperAdapter = new EmptyHelperAdapter(simpleBaseAdapter);
emptyHelperAdapter.setEmptyView(emptyView);
```

### HeaderFooterHelperAdapter 头部底部
```java
HeaderFooterHelperAdapter headerFooterHelperAdapter = new HeaderFooterHelperAdapter(simpleBaseAdapter);
headerFooterHelperAdapter.addHeader(headerView);
headerFooterHelperAdapter.addFooter(footerView);
```

### LoadMoreHelperAdapter 加载更多
```java
final LoadMoreHelperAdapter loadMoreHelperAdapter = new MyLoadMoreAdapter(simpleBaseAdapter);
loadMoreHelperAdapter.isLoadMoreEnable(true);
loadMoreHelperAdapter.setLoadMoreListener(new OnLoadMoreListener(){
    ...
});
// STATUS_LOADING
// STATUS_EMPTY
// STATUS_COMPLETED
// STATUS_ERROR
// STATUS_PREPARE
loadMoreHelperAdapter.setLoadStatus(LoadMoreHelperAdapter.STATUS_COMPLETED)
```

## 一些栗子

* [二级列表](https://github.com/Temoa/BaseAdapter/tree/master/app/src/main/java/me/temoa/baseadapter/expand)

* [简单的拖拽](https://github.com/Temoa/BaseAdapter/tree/master/app/src/main/java/me/temoa/baseadapter/item_touch_helper)

* [自定义分割线](https://github.com/Temoa/BaseAdapter/tree/master/app/src/main/java/me/temoa/baseadapter/item_decoration)

* [吸顶效果 | 两种实现方式一种是分割线完成的, 一种是监听 RecyclerView 滑动完成](https://github.com/Temoa/BaseAdapter/tree/master/app/src/main/java/me/temoa/baseadapter/item_decoration)