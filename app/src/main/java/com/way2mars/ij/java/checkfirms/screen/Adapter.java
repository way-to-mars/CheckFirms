package com.way2mars.ij.java.checkfirms.screen;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import androidx.recyclerview.widget.SortedList;
import com.way2mars.ij.java.checkfirms.App;
import com.way2mars.ij.java.checkfirms.R;
import com.way2mars.ij.java.checkfirms.model.FirmStorage;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.FirmViewHolder> {

    final private SortedList<FirmStorage> sortedList;

    static class FirmViewHolder extends RecyclerView.ViewHolder {

        TextView textName;
        TextView textInn;
        View linearLiquidated;
        View linearAddress;
        TextView textDateEgrul;
        TextView textEgrul;


        FirmStorage firmStorage;

        public FirmViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.item_name);
            textInn = itemView.findViewById(R.id.item_inn);
            linearLiquidated = itemView.findViewById(R.id.item_liquidated);
            linearAddress = itemView.findViewById(R.id.item_address_warning);
            textDateEgrul = itemView.findViewById(R.id.item_date_egrul);
            textEgrul = itemView.findViewById(R.id.item_text_egrul);

            itemView.setOnClickListener(v -> AddFirmActivity.start((Activity) itemView.getContext(), firmStorage));
        }

        public void bind(FirmStorage firm)
        {
            this.firmStorage = firm;
            textName.setText(firm.shortName);
            Resources res = App.getInstance().getResources();
            textInn.setText(res.getString(R.string.item_inn_placeholder,firm.inn));
            //textInn.setText("ИНН ".concat(firm.inn));

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

        }
    }

    public Adapter(){
        sortedList = new SortedList<>(FirmStorage.class, new SortedList.Callback<FirmStorage>() {
            @Override
            public int compare(FirmStorage o1, FirmStorage o2) {
//                return -max(o1.dateLastRecord,o1.dateLastCourtAction).
//                        compareTo(max(o2.dateLastRecord, o2.dateLastCourtAction));
                return o1.dateLastRecord.compareTo(o2.dateLastRecord);
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
//            String max(@NonNull String str1,@NotNull String str2)
//            {
//                if(str1.length()==0) return str2;
//                if(str2.length()==0) return str1;
//
//                if(str1.compareTo(str2)>0) return str1;
//                else return str2;
//            }
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