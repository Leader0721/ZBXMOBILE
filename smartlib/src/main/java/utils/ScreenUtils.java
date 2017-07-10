package utils;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

public class ScreenUtils {
	private static float density;
	private static int deviceWidth;

	public static int dipToPx(Context context, int dip) {
		if (density <= 0.0F)
			density = context.getResources().getDisplayMetrics().density;
		return (int) (dip * density + 0.5F);
	}

	public static int pxToDip(Context context, int px) {
		if (density <= 0.0F)
			density = context.getResources().getDisplayMetrics().density;
		return (int) (px / density + 0.5F);
	}

	public static int designToDevice(Context context, int designScreenWidth,
			int designPx) {
		if (deviceWidth == 0) {
			int[] scrSize = getScreenSize(context);
			deviceWidth = (scrSize[0] < scrSize[1]) ? scrSize[0] : scrSize[1];
		}
		return (int) (designPx * deviceWidth / designScreenWidth + 0.5F);
	}

	public static int designToDevice(Context context,
			float designScreenDensity, int designPx) {
		if (density <= 0.0F)
			density = context.getResources().getDisplayMetrics().density;
		return (int) (designPx * density / designScreenDensity + 0.5F);
	}

	public static int[] getScreenSize(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		Point outSize = new Point();
		wm.getDefaultDisplay().getSize(outSize);
		return new int[] { outSize.x, outSize.y };
	}

	public static int getScreenWidth(Context context) {
		return getScreenSize(context)[0];
	}

	public static int getScreenHeight(Context context) {
		return getScreenSize(context)[1];
	}

}