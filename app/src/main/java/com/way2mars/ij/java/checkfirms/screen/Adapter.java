package com.way2mars.ij.java.checkfirms.screen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import androidx.recyclerview.widget.SortedList;
import com.google.android.material.snackbar.Snackbar;
import com.way2mars.ij.java.checkfirms.App;
import com.way2mars.ij.java.checkfirms.R;
import com.way2mars.ij.java.checkfirms.model.FirmData;
import com.way2mars.ij.java.checkfirms.model.FirmStorage;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.FirmViewHolder> {

    private SortedList<FirmStorage> sortedList;

    static class FirmViewHolder extends RecyclerView.ViewHolder {

        TextView textName;
        TextView textInn;
        View linearLiquidated;
        View linearAddress;
        TextView textDateEgrul;
        TextView textEgrul;
        TextView textDateCourt;
        TextView textCourt;

        FirmStorage firmStorage;

        // Идентификатор уведомления
        private static final int NOTIFY_ID = 101;
        // Идентификатор канала
        private static String CHANNEL_ID = "Cat_channel";

        @SuppressLint("MissingPermission")
        public FirmViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.item_name);
            textInn = itemView.findViewById(R.id.item_inn);
            linearLiquidated = itemView.findViewById(R.id.item_liquidated);
            linearAddress = itemView.findViewById(R.id.item_address_warning);
            textDateEgrul = itemView.findViewById(R.id.item_date_egrul);
            textEgrul = itemView.findViewById(R.id.item_text_egrul);
            textDateCourt = itemView.findViewById(R.id.item_date_court);
            textCourt = itemView.findViewById(R.id.item_text_court);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    Snackbar.make(itemView,"Нажатие на итем", Snackbar.LENGTH_LONG);
//                    AddFirmActivity.start((Activity) itemView.getContext(), firmStorage);
                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(App.getInstance(), CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentTitle("Напоминание")
                                    .setContentText("Пора покормить кота")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManager =
                            NotificationManagerCompat.from(App.getInstance());

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID,"check_firms", NotificationManager.IMPORTANCE_DEFAULT);
                        mChannel.setDescription("description");
                        mChannel.enableLights(true);
                        mChannel.enableVibration(true);
                        notificationManager.createNotificationChannel(mChannel);
                    }
                    notificationManager.notify(NOTIFY_ID, builder.build());
                }
            });
        }

        public void bind(FirmStorage firm)
        {
            this.firmStorage = firm;
            textName.setText(firm.shortName);
            textInn.setText("ИНН " + firm.inn);

            if (firm.addressWarning)  // недостоверность адреса
                linearAddress.setVisibility(View.VISIBLE);
            else
                linearAddress.setVisibility(View.GONE);

            if(firm.dateLiquidation.length()>0)  // если ликвидировано
                linearLiquidated.setVisibility(View.VISIBLE);
            else
                linearLiquidated.setVisibility(View.GONE);
            textDateEgrul.setText(firm.dateLastRecord);
            textEgrul.setText(firm.textLastRecord);

            textDateCourt.setText(firm.dateLastCourtAction);
            textCourt.setText(firm.numberLastCourtAction);
        }
    }

    public Adapter(){
        sortedList = new SortedList<>(FirmStorage.class, new SortedList.Callback<FirmStorage>() {
            @Override
            public int compare(FirmStorage o1, FirmStorage o2) {
                return -max(o1.dateLastRecord,o1.dateLastCourtAction).
                        compareTo(max(o2.dateLastRecord, o2.dateLastCourtAction));
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(FirmStorage oldItem, FirmStorage newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(FirmStorage item1, FirmStorage item2) {
                return item1.uid == item2.uid;
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            // each param can be a void String ""
            String max(@NonNull String str1,@NotNull String str2)
            {
                if(str1.length()==0) return str2;
                if(str2.length()==0) return str1;

                if(str1.compareTo(str2)>0) return str1;
                else return str2;
            }
        });

    }

    public void setItem(List<FirmStorage> firms){
        sortedList.replaceAll(firms);
    }

    @NonNull
    @NotNull
    @Override
    public FirmViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new FirmViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FirmViewHolder holder, int position) {
        holder.bind(sortedList.get(position));
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

}