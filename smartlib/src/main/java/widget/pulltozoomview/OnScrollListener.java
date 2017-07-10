package widget.pulltozoomview;

/**
 * ScrollView滚动监听
 * @author GISirFive
 * @since 2015-12-28 下午6:54:47
 */
public interface OnScrollListener {
	
	/**
	 * 滚动条滚动
	 * @param scrollY ScrollView滑动的Y方向距离
	 * @param ratio 此时HeaderView的可见比例  <b>(0-1.0f)</b></br>(ratio=HeaderView当前高度 / 原高度)
	 * @author GISirFive
	 */
	public void onScroll(int scrollY, float ratio);
	
}
