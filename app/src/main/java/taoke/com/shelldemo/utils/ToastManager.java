package taoke.com.shelldemo.utils;

/**
 * Created by Administrator on 2017/8/8.
 */

import android.content.Context;
import android.widget.Toast;

/**
 * 功 能： Toast统一管理类
 * 时 间：2016/11/7 10:35 <br>
 */
public class ToastManager {
    private static Toast mToast;
//    private static TextView mContent;

    private ToastManager() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow) {
            if(mToast == null) {
                mToast = getInstance(context);
            }
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setText(message);
//            mContent.setText(message);
            mToast.show();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (isShow) {
            if(mToast == null) {
                mToast = getInstance(context);
            }
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setText(context.getString(message));
//            mContent.setText(context.getString(message));

            mToast.show();
        }
//        if (isShow) Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow) {
            if(mToast == null) {
                mToast = getInstance(context);
            }
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setText(message);
            mToast.show();
        }
//        if (isShow) Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (isShow) {
            if(mToast == null) {
                mToast = getInstance(context);
            }
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setText(context.getString(message));
            mToast.show();
        }
//        if (isShow) Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow) {
            if(mToast == null) {
                mToast = getInstance(context);
            }
            mToast.setDuration(duration);
            mToast.setText(message);
            mToast.show();
        }
//        if (isShow) Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (isShow) {
            if(mToast == null) {
                mToast = getInstance(context);
            }
            mToast.setDuration(duration);
            mToast.setText(context.getString(message));
            mToast.show();
        }
//        if (isShow) Toast.makeText(context, message, duration).show();
    }

    private static Toast getInstance(Context context){
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.layout_toast, null);
//        mContent = (TextView) view.findViewById(R.id.chapterName);
        Toast toast = Toast.makeText(context,"", Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 20);
//        toast.setView(view);
//        Toast toast = new Toast(context);

        return toast;
    }

}
