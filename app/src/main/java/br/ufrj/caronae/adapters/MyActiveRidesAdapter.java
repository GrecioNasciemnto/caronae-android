package br.ufrj.caronae.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.ufrj.caronae.App;
import br.ufrj.caronae.R;
import br.ufrj.caronae.RoundedTransformation;
import br.ufrj.caronae.Util;
import br.ufrj.caronae.acts.ChatAct;
import br.ufrj.caronae.acts.MainAct;
import br.ufrj.caronae.acts.ProfileAct;
import br.ufrj.caronae.asyncs.UnsubGcmTopic;
import br.ufrj.caronae.models.Ride;
import br.ufrj.caronae.models.User;
import br.ufrj.caronae.models.modelsforjson.RideForJson;
import br.ufrj.caronae.models.modelsforjson.RideIdForJson;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyActiveRidesAdapter extends RecyclerView.Adapter<MyActiveRidesAdapter.ViewHolder> {

    private final List<RideForJson> ridesList;
    private final MainAct activity;

    public MyActiveRidesAdapter(List<RideForJson> ridesList, MainAct activity) {
        this.ridesList = ridesList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_myactiveride, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final RideForJson rideWithUsers = ridesList.get(position);

        final User driver = rideWithUsers.getDriver();

        final boolean isDriver = driver.getDbId() == App.getUser().getDbId();

        int color = 0, bgRes = 0;
        if (rideWithUsers.getZone().equals("Centro")) {
            color = ContextCompat.getColor(activity, R.color.zone_centro);
            bgRes = R.drawable.bg_bt_raise_zone_centro;
            holder.chat_bt.setBackgroundResource(bgRes);
        }
        if (rideWithUsers.getZone().equals("Zona Sul")) {
            color = ContextCompat.getColor(activity, R.color.zone_sul);
            bgRes = R.drawable.bg_bt_raise_zone_sul;
            holder.chat_bt.setBackgroundResource(bgRes);
        }
        if (rideWithUsers.getZone().equals("Zona Oeste")) {
            color = ContextCompat.getColor(activity, R.color.zone_oeste);
            bgRes = R.drawable.bg_bt_raise_zone_oeste;
            holder.chat_bt.setBackgroundResource(bgRes);
        }
        if (rideWithUsers.getZone().equals("Zona Norte")) {
            color = ContextCompat.getColor(activity, R.color.zone_norte);
            bgRes = R.drawable.bg_bt_raise_zone_norte;
            holder.chat_bt.setBackgroundResource(bgRes);
        }
        if (rideWithUsers.getZone().equals("Baixada")) {
            color = ContextCompat.getColor(activity, R.color.zone_baixada);
            bgRes = R.drawable.bg_bt_raise_zone_baixada;
            holder.chat_bt.setBackgroundResource(bgRes);
        }
        if (rideWithUsers.getZone().equals("Grande Niterói")) {
            color = ContextCompat.getColor(activity, R.color.zone_niteroi);
            bgRes = R.drawable.bg_bt_raise_zone_niteroi;
            holder.chat_bt.setBackgroundResource(bgRes);
        }
        holder.lay1.setBackgroundColor(color);

        rideWithUsers.setDbId(rideWithUsers.getId().intValue());

        final String location;
        if (rideWithUsers.isGoing())
            location = rideWithUsers.getNeighborhood() + " -> " + rideWithUsers.getHub();
        else
            location = rideWithUsers.getHub() + " -> " + rideWithUsers.getNeighborhood();

        String profilePicUrl = driver.getProfilePicUrl();
        if (profilePicUrl == null || profilePicUrl.isEmpty()) {
            Picasso.with(activity).load(R.drawable.user_pic)
                    .into(holder.user_pic);
        } else {
            Picasso.with(activity).load(profilePicUrl)
                    .placeholder(R.drawable.user_pic)
                    .error(R.drawable.user_pic)
                    .transform(new RoundedTransformation(0))
                    .into(holder.user_pic);
        }
        holder.user_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDriver) {//dont allow user to open own profile
                    Intent intent = new Intent(activity, ProfileAct.class);
                    intent.putExtra("user", new Gson().toJson(driver));
                    intent.putExtra("from", "activeRides");
                    activity.startActivity(intent);
                }
            }
        });
        holder.location_tv.setText(location);
        holder.name_tv.setText(driver.getName());
        holder.profile_tv.setText(driver.getProfile());
        holder.way_tv.setText(rideWithUsers.getRoute());
        holder.place_tv.setText(rideWithUsers.getPlace());
        holder.phoneNumber_tv.setText(driver.getPhoneNumber());
        holder.course_tv.setText(driver.getCourse());
        holder.time_tv.setText(activity.getString(R.string.arrivingAt, Util.formatTime(rideWithUsers.getTime())));
        holder.time_tv.setTextColor(color);
        holder.date_tv.setText(Util.formatBadDateWithoutYear(rideWithUsers.getDate()));
        holder.date_tv.setTextColor(color);
        holder.carModel_tv.setText(driver.getCarModel());
        holder.carColor_tv.setText(driver.getCarColor());
        holder.carPlate_tv.setText(driver.getCarPlate());
        holder.description_tv.setText(rideWithUsers.getDescription());
        holder.ridersList.setAdapter(new RidersAdapter(rideWithUsers.getRiders(), activity));
        holder.ridersList.setHasFixedSize(true);
        holder.ridersList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        final int finalColor = color, finalBgRes = bgRes;
        holder.chat_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ChatAct.class);
                intent.putExtra("rideId", rideWithUsers.getDbId() + "");
                intent.putExtra("location", location);
                intent.putExtra("color", finalColor);
                intent.putExtra("bgRes", finalBgRes);
                intent.putExtra("date", Util.formatBadDateWithoutYear(rideWithUsers.getDate()));
                intent.putExtra("time", Util.formatTime(rideWithUsers.getTime()));

                String riders = driver.getName().split(" ")[0] + ", ";
                for (User user : rideWithUsers.getRiders()) {
                    riders += user.getName().split(" ")[0] + ", ";
                }
                riders = riders.substring(0, riders.length() - 2);
                intent.putExtra("riders", riders);
                activity.startActivity(intent);
            }
        });

        final String rideId = rideWithUsers.getDbId() + "";

        if (isDriver) {
            holder.leave_bt.setText("CANCELAR");
        }
        holder.leave_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.getNetworkService().leaveRide(new RideIdForJson(rideWithUsers.getDbId()), new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        Util.toast(R.string.rideDeleted);
                        ridesList.remove(rideWithUsers);
                        notifyItemRemoved(holder.getAdapterPosition());

                        new UnsubGcmTopic(activity, rideId).execute();

                        List<Ride> rides = Ride.find(Ride.class, "db_id = ?", rideId);
                        if (rides != null && !rides.isEmpty())
                            rides.get(0).delete();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Util.toast(R.string.errorRideDeleted);

                        Log.e("leaveRide", error.getMessage());
                    }
                });
            }
        });

        if (!isDriver) {
            holder.finish_bt.setVisibility(View.GONE);
        }
        holder.finish_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.getNetworkService().finishRide(new RideIdForJson(rideWithUsers.getDbId()), new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        Util.toast(R.string.rideFinished);
                        ridesList.remove(rideWithUsers);
                        notifyItemRemoved(holder.getAdapterPosition());

                        new UnsubGcmTopic(activity, rideId).execute();

                        List<Ride> rides = Ride.find(Ride.class, "db_id = ?", rideId);
                        if (rides != null && !rides.isEmpty())
                            rides.get(0).delete();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Util.toast(R.string.errorFinishRide);

                        Log.e("finish_bt", error.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return ridesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView user_pic;
        public TextView location_tv;
        public TextView name_tv;
        public TextView profile_tv;
        public TextView course_tv;
        public TextView time_tv;
        public TextView date_tv;
        public TextView way_tv;
        public TextView place_tv;
        public TextView carModel_tv;
        public TextView carColor_tv;
        public TextView carPlate_tv;
        public TextView description_tv;
        public TextView phoneNumber_tv;
        public Button leave_bt;
        public Button chat_bt;
        public Button finish_bt;
        public RelativeLayout lay1;
        public RelativeLayout layout;
        public RecyclerView ridersList;

        public ViewHolder(View itemView) {
            super(itemView);

            user_pic = (ImageView) itemView.findViewById(R.id.user_pic);
            location_tv = (TextView) itemView.findViewById(R.id.location_tv);
            name_tv = (TextView) itemView.findViewById(R.id.name_tv);
            profile_tv = (TextView) itemView.findViewById(R.id.profile_tv);
            course_tv = (TextView) itemView.findViewById(R.id.course_tv);
            time_tv = (TextView) itemView.findViewById(R.id.time_tv);
            way_tv = (TextView) itemView.findViewById(R.id.way_tv);
            place_tv = (TextView) itemView.findViewById(R.id.place_tv);
            date_tv = (TextView) itemView.findViewById(R.id.date_tv);
            carModel_tv = (TextView) itemView.findViewById(R.id.carModel_tv);
            carColor_tv = (TextView) itemView.findViewById(R.id.carColor_tv);
            carPlate_tv = (TextView) itemView.findViewById(R.id.carPlate_tv);
            description_tv = (TextView) itemView.findViewById(R.id.description_tv);
            phoneNumber_tv = (TextView) itemView.findViewById(R.id.phoneNumber_tv);
            leave_bt = (Button) itemView.findViewById(R.id.leave_bt);
            chat_bt = (Button) itemView.findViewById(R.id.chat_bt);
            finish_bt = (Button) itemView.findViewById(R.id.finish_bt);
            lay1 = (RelativeLayout) itemView.findViewById(R.id.lay1);
            layout = (RelativeLayout) itemView.findViewById(R.id.layout);
            ridersList = (RecyclerView) itemView.findViewById(R.id.ridersList);
        }
    }
}
