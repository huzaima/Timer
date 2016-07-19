package co.magency.huzaima.timer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.magency.huzaima.timer.Interface.OnItemClickListener;
import co.magency.huzaima.timer.Model.Timer;
import co.magency.huzaima.timer.R;
import co.magency.huzaima.timer.Utilities.AppUtility;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Huzaima Khan on 7/19/2016.
 */
public class TimerRecyclerViewAdapter extends RealmRecyclerViewAdapter<Timer,
        TimerRecyclerViewAdapter.TimerViewHolder> {

    private Context context;
    private OnItemClickListener listener;

    public TimerRecyclerViewAdapter(Context context, OrderedRealmCollection<Timer> data) {
        super(context, data, true);
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public TimerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_timer_item, parent, false);
        return new TimerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimerViewHolder holder, int position) {
        Timer timer = getData().get(position);
        holder.timerName.setText(timer.getName());

        String temp = timer.getHour() + "h : " + timer.getMinute() + "m : " + timer.getSecond() + "s";
        holder.timerTime.setText(temp);

        holder.timerLapse.setText(context.getString(R.string.lapse_text, timer.getNoOfLapse()));

        if (timer.getAlertType().equals(AppUtility.DONT_NOTIFY))
            holder.timerNotification.setText("Don't notify me");
        else
            holder.timerNotification.setText(context.getString(R.string.notify_text, timer.getAlertType(), timer.getAlertFrequency()));

        holder.attachListener(timer, listener);
    }

    public class TimerViewHolder extends RecyclerView.ViewHolder {

        TextView timerName, timerTime, timerLapse, timerNotification;

        TimerViewHolder(View v) {
            super(v);
            timerName = (TextView) v.findViewById(R.id.timer_name);
            timerTime = (TextView) v.findViewById(R.id.timer_time);
            timerLapse = (TextView) v.findViewById(R.id.timer_lapse);
            timerNotification = (TextView) v.findViewById(R.id.timer_notification);
        }

        public void attachListener(final Timer timer, final OnItemClickListener listener1) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener1.onItemClick(timer);
                }
            });
        }
    }
}
