package com.example.roomdatabase.myWidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.roomdatabase.myAdapter.MyAdapter;
import com.example.roomdatabase.myUtils.ScreenUtil;

public class SlidingMenu extends HorizontalScrollView {

    private static final float radio = 0.2f;
    private final int mScreenWidth;
    private final int mMenuWidth;

    private boolean once = true;
    private boolean isOpen;

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScreenWidth = ScreenUtil.getScreenWidth(context);
        mMenuWidth = (int) (mScreenWidth * radio);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setHorizontalScrollBarEnabled(false);
    }

    /*关闭菜单 */
    public void closeMenu(){
        this.smoothScrollTo(0, 0);
        isOpen = false;
    }

    /*菜单是否打开*/
    public boolean isOpen(){
        return  isOpen;
    }

    /*获取Adapter */
    private MyAdapter getAdapter(){
        View view = this;
        do {
            view = (View) view.getParent();
        } while (!(view instanceof RecyclerView));
        return (MyAdapter) ((RecyclerView) view).getAdapter();
    }

    /*打开菜单时记录此view，方便下次关闭*/
    private void onOpenMenu(){
        getAdapter().holdOpenMenu(this);
        isOpen = true;
    }

    /*当触摸此 item 时，关闭上一个打开的 item*/
    private void closeOpenMenu(){
        if( !isOpen ){
            getAdapter().closeOpenMenu();
        }
    }

    /* 获取正在滑动的 item */
    public SlidingMenu getScrollingMenu(){
        return getAdapter().getScrollingMenu();
    }

    /* 设置本 item 为正在滑动 item */
    public void setScrollingMenu(SlidingMenu scrollingMenu){
        getAdapter().setScrollingMenu(scrollingMenu);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(once){
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            wrapper.getChildAt(0).getLayoutParams().width = mScreenWidth;
            wrapper.getChildAt(1).getLayoutParams().width = mMenuWidth;
            once = false;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d("tag1", "IMIN");
        if(getScrollingMenu() != null && getScrollingMenu() != this){
            return false;
        }
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                closeOpenMenu();  //!!!!!!!!!!!!!注意，要与closeMenu区分，不然没有效果；
                setScrollingMenu(this);
                break;
            case MotionEvent.ACTION_UP:
                setScrollingMenu(null);
                int scrollX = getScrollX();
                if(System.currentTimeMillis() - downTime <= 100 && scrollX == 0){
                    if(mCustomOnClickListener != null)
                        mCustomOnClickListener.onClick();
                    return false;
                }
                if(Math.abs(scrollX) > mMenuWidth / 2){
                    this.smoothScrollTo(mMenuWidth, 0);
                    onOpenMenu();
                }else {
                    this.smoothScrollTo(0, 0);
                }
                return false;
        }
        return super.onTouchEvent(ev);
    }
    long downTime = 0;

    public interface CustomOnClickListener{
        void onClick();
    }

    private CustomOnClickListener mCustomOnClickListener;

    public void setCustomOnClickListener(CustomOnClickListener listener){
        this.mCustomOnClickListener = listener;
    }


}
