package apps.gen.lib.controllers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

import apps.gen.lib.views.NavigationBar;

/**
 * Created by Gen on 2016/4/13.
 */
public class Controller extends Fragment {
    static public class ButtonItem {
        Drawable mIcon;

        public Drawable getIcon() {
            return mIcon;
        }

        public void setIcon(Drawable icon) {
            if (mIcon != icon) {
                mIcon = icon;
            }
        }

        View.OnClickListener mOnClickListener;
        public View.OnClickListener getOnClickListener() {
            return mOnClickListener;
        }

        public void setOnClickListener(View.OnClickListener clickListener) {
            mOnClickListener = clickListener;
        }

        public ButtonItem(Drawable icon, View.OnClickListener onClickListener) {
            mIcon = icon;
            mOnClickListener = onClickListener;
        }
    }

    public static<T extends Controller> T instantiate(Context context, Class<T> type) {
        T res = (T) T.instantiate(context, type.getName());
        res.initialize(context);
        return res;
    }
    Handler mHandler = new Handler();
    protected Handler getHandler() {
        return mHandler;
    }

    // Override this method to initialize some thing such as tabitems or navitems
    protected void initialize(Context context) {}

    boolean viewDidLoaded = false;
    ArrayList<Runnable> onViewPrepared = new ArrayList<>();
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setVisibility(View.INVISIBLE);
        final View bView = view;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bView.setVisibility(View.VISIBLE);
                viewDidLoaded = true;
                for (Runnable run : onViewPrepared) {
                    run.run();
                }
            }
        }, 0);
    }
    String mTitle;

    protected void onSubControllerChangeTitle(Controller sender, String title, NavigationBar.AnimationType type) {}
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        if (mTitle != title || (mTitle != null && !mTitle.equals(title))) {
            mTitle = title;
            if (mParent != null) {
                mParent.onSubControllerChangeTitle(this, mTitle, NavigationBar.AnimationType.FADE);
            }
        }
    }

    public class NavigationItem {
        ArrayList<ButtonItem> mItems = new ArrayList<>();

        void setItems(ButtonItem[] items) {
            mItems.clear();
            Collections.addAll(mItems, items);
        }
        ButtonItem[] getItems() {
            ButtonItem[] ret = new ButtonItem[mItems.size()];
            return mItems.toArray(ret);
        }
        void addItem(ButtonItem item) {
            mItems.add(item);
        }
        void removeItem(ButtonItem item) {mItems.remove(item);}

        public NavigationItem() {}
        public  NavigationItem(Drawable icon, View.OnClickListener clickListener) {
            addItem(new ButtonItem(icon, clickListener));
        }
        public  NavigationItem(Drawable icon) {
            addItem(new ButtonItem(icon, null));
        }
    }
    NavigationItem mLeftItems;
    NavigationItem mRightItems;

    public NavigationItem getLeftItems() {
        return mLeftItems;
    }
    public void setLeftItems(NavigationItem leftItems) {
        mLeftItems = leftItems;
    }

    public NavigationItem getRightItems() {
        return mRightItems;
    }
    public  void setRightItems(NavigationItem rightItems) {
        mRightItems = rightItems;
    }

    protected enum AnimationType {
        PUSH_POP,
        MOVE,
        FADE
    }
    AnimationType lastType;
    protected AnimationType getLastType(){return lastType;}
    float pushAnimatorValue;
    void setPushAnimatorValue(float value) {
        pushAnimatorValue = value;

        View view = getView();
        lastType = AnimationType.PUSH_POP;
        if (view != null) {
            updateAnimator(view, value);
        }
    }
    float getPushAnimatorValue() {return pushAnimatorValue;}

    float moveAnimatorValue;
    void setMoveAnimatorValue(float value) {
        moveAnimatorValue = value;
        View view = getView();
        lastType = AnimationType.MOVE;
        if (view != null) {
            updateAnimator(view, value);
        }
    }
    float getMoveAnimatorValue() {return moveAnimatorValue;}

    float changeAnimatorValue;
    void setChangeAnimatorValue(float value) {
        changeAnimatorValue = value;
        View view = getView();
        lastType = AnimationType.FADE;
        if (view != null) {
            updateAnimator(view, value);
        }
    }

    protected void updateAnimator(View view, float value) {
        if (lastType == AnimationType.FADE) {
            view.setAlpha(1-Math.abs(value));
        }else {
            float width = view.getWidth();
            view.setTranslationX(width*value);
            view.setAlpha(1 - Math.abs(value));
        }
    }

    protected void enterStart(Controller controller) {
        setParent(controller);
        if (viewDidLoaded) {
            onViewWillAppear();
        }else {
            onViewPrepared.add(new Runnable() {
                @Override
                public void run() {
                    onViewWillAppear();
                }
            });
        }
    }
    protected void enterEnd() {
        onViewDidAppear();
    }
    protected void exitStart() {
        onViewWillDisappear();
    }
    protected void exitEnd(Controller controller) {
        if (getParent() == controller)
            setParent(null);
        onViewDidDisappear();
    }

    protected void onViewWillAppear() {  }
    protected void onViewDidAppear() { }
    protected void onViewWillDisappear() {  }
    protected void onViewDidDisappear() {  }

    Controller mParent;
    protected void setParent(Controller parent) {
        mParent = parent;
    }
    public Controller getParent() {
        return mParent;
    }
}
