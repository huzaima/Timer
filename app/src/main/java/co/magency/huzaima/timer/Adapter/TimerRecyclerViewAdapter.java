package co.magency.huzaima.timer.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.magency.huzaima.timer.Activity.ListTimerActivity;
import co.magency.huzaima.timer.Interface.OnItemClickListener;
import co.magency.huzaima.timer.Model.Timer;
import co.magency.huzaima.timer.R;
import co.magency.huzaima.timer.Utilities.AppUtility;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
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

        PopupMenu popupMenu = new PopupMenu(context, holder.itemView.findViewById(R.id.list_item_card));
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu, popupMenu.getMenu());

        holder.attachListener(timer, listener);
    }

    public class TimerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.timer_name)
        TextView timerName;
        @BindView(R.id.timer_time)
        TextView timerTime;
        @BindView(R.id.timer_lapse)
        TextView timerLapse;
        @BindView(R.id.timer_notification)
        TextView timerNotification;

        TimerViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public void attachListener(final Timer timer, final OnItemClickListener listener1) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener1.onItemClick(timer);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(ListTimerActivity.instance)
                            .setTitle("Delete")
                            .setMessage("Are you sure want to delete this timer?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Realm realm = Realm.getDefaultInstance();
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            realm.where(Timer.class)
                                                    .equalTo(AppUtility.TIMER_COLUMN_NAME, timer.getName())
                                                    .findFirst()
                                                    .deleteFromRealm();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("No", null)
                            .setCancelable(true)
                            .create()
                            .show();
                    return true;
                }
            });
        }
    }
}
