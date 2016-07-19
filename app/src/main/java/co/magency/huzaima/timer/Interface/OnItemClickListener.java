package co.magency.huzaima.timer.Interface;

import co.magency.huzaima.timer.Model.Timer;

/**
 * Created by Huzaima Khan on 7/19/2016.
 * <p/>
 * This interface is for RecyclerView to send item click events to listeners
 */
public interface OnItemClickListener {
    void onItemClick(Timer timer);
}
