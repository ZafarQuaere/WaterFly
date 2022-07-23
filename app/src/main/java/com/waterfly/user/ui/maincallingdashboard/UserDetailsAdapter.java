package com.waterfly.user.ui.maincallingdashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.waterfly.user.R;
import com.waterfly.user.data.network.model.nearbyvendors.Datum;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetailsAdapter extends RecyclerView.Adapter<UserDetailsAdapter.UserDetailsViewHolder> {

    private List<Datum> items;
    private UserDetailsListener listener;

    public interface UserDetailsListener {
        void onUserDetailsClicked(Datum userDetails);
    }

    public UserDetailsAdapter(UserDetailsListener listener) {
        this.listener = listener;
        items = new ArrayList<>();
    }

    public void setItems(List<Datum> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setSelectedCard(Datum userDetails){

        for(int i =0;i<items.size();i++){
            if(userDetails.getVendorId() == items.get(i).getVendorId()){
                items.get(i).setSelectedCard(true);
            }else{
                items.get(i).setSelectedCard(false);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public UserDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_details, parent, false);
        return new UserDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserDetailsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(items == null){
            return 0;
        }
        return items.size();
    }

    private Datum getItem(int position) {
        return items.get(position);
    }

    public class UserDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image)
        AppCompatImageView image;

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.distance)
        TextView desc;

        @BindView(R.id.rSelectedCard)
        RelativeLayout rSelectedCard;

        @BindView(R.id.l_cardBg)
        LinearLayout l_cardBg;

        @BindView(R.id.status)
        TextView status;

        UserDetailsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {
            Datum userDetails = getItem(position);

            setClickListener(userDetails);
            setTitle(userDetails.getVendorName());
//            setImage(userDetails.getAvatar());
            Double distance = Double.parseDouble(userDetails.getDistance()) * 1000.0;
            setDescription(String.format("%.2f",distance)+" Meter");

            if(userDetails.isSelectedCard()){
                l_cardBg.setBackgroundResource(R.drawable.vender_card_selected_bg1);
            }else{
                l_cardBg.setBackgroundResource(R.drawable.vender_card_bg);
            }

            if(userDetails.isCalled()){
                l_cardBg.setBackgroundResource(R.drawable.vender_card_selected_bg);
                status.setVisibility(View.VISIBLE);
//                rSelectedCard.setVisibility(View.VISIBLE);
            }else{
                status.setVisibility(View.GONE);
            }

        }

        private void setTitle(String title) {
            this.title.setText(title);
        }

        private void setImage(String imageUrl) {
//            Glide.with(itemView.getContext()).load(imageUrl).into(image);
        }

        private void setDescription(String description) {
            desc.setText(description);
        }

        private void setClickListener(Datum userDetails) {
            itemView.setTag(userDetails);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onUserDetailsClicked((Datum) view.getTag());
        }
    }
}