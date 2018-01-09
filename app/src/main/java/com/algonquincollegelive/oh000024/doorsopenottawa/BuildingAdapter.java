package com.algonquincollegelive.oh000024.doorsopenottawa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.algonquincollegelive.oh000024.doorsopenottawa.model.BuildingPOJO;
import com.algonquincollegelive.oh000024.doorsopenottawa.service.MyService;
import com.algonquincollegelive.oh000024.doorsopenottawa.utils.HttpMethod;
import com.algonquincollegelive.oh000024.doorsopenottawa.utils.RequestPackage;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static com.algonquincollegelive.oh000024.doorsopenottawa.MainActivity.DOO_URL;

/**
 * exposes a list of Building
 * *  @author Jake Oh (oh000024@algonquinlive.com)
 */


public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.ViewHolder> {

    private static final String DELETE_BASE_URL = "https://doors-open-ottawa.mybluemix.net/buildings/";
    private final static int DETAIL_REQUEST_CODE = 100;
    private final static int EDIT_REQUEST_CODE = 101;
    public static final String CLICKEDBUILDING = "CLICKEDBUILDING";
    public static final String ADDRESS = "ADDRESS";
    public static final String BUILDINGNAME = "BUILDINGNAME";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String OPENTIME = "OPENTIME";
    private static final String TAG = "BuildingAdapter";

    //    private Map<Integer, Bitmap> mBitmaps;
    private Context mContext;
    private ArrayList<BuildingPOJO> mBuildings;

    public BuildingAdapter(Context mainActivity) {
        this.mContext = mainActivity;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewType If your RecyclerView has more than one type of item (which ours doesn't) you
     *                 can use this viewType integer to provide a different layout. See
     *                 {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                 for more details.
     * @return A new BuildingAdapterViewHolder that holds the View for each list item
     */
    @Override
    public BuildingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View buildingView = inflater.inflate(R.layout.item_building, parent, false);
        ViewHolder viewHolder = new ViewHolder(buildingView);
        return viewHolder;
    }

    public final Context getmContext() {
        return mContext;
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the building
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the
     *                 contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final BuildingAdapter.ViewHolder holder, final int position) {
        final BuildingPOJO building = mBuildings.get(position);

        holder.tvName.setText(building.getNameEN());
        holder.tvAddress.setText(building.getAddressEN());
        String url = MainActivity.DOO_URL + "/" + building.getBuildingId() + "/image";
        Picasso.with(mContext)
                .load(url)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.drawable.noimagefound)
                .fit()
                .into(holder.imageView);
        holder.imgDelete.setVisibility(View.INVISIBLE);
        holder.imgEdit.setVisibility(View.INVISIBLE);

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if ((holder.imgDelete.getVisibility() == View.VISIBLE) && (holder.imgEdit.getVisibility() == View.VISIBLE)) {
                    holder.imgDelete.setVisibility(View.INVISIBLE);
                    holder.imgEdit.setVisibility(View.INVISIBLE);
                } else {
                    holder.imgDelete.setVisibility(View.VISIBLE);
                    holder.imgEdit.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(CLICKEDBUILDING, building);
                mContext.startActivity(intent);
            }
        });

//        holder.setListeners();

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                // TODO - externalize strings to strings.xml
                builder.setTitle("Confirm")
                        .setMessage("Delete " + building.getBuildingId() + " - " + building.getNameEN() + "?")

                        // Displays: OK
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete this course
//                                Toast.makeText(mContext, "Deleted Course: " + aCourse.getCode(), Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "Deleted Course: " + building.getBuildingId());
                                holder.imgDelete.setVisibility(View.INVISIBLE);
                                holder.imgEdit.setVisibility(View.INVISIBLE);


                                RequestPackage requestPackage = new RequestPackage();
                                requestPackage.setMethod(HttpMethod.DELETE);

                                requestPackage.setEndPoint(DOO_URL + "/" + Integer.toString(building.getBuildingId()));//"https://doors-open-ottawa.mybluemix.net/buildings");


                                Intent intent = new Intent(mContext, MyService.class);
                                intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
                                mContext.startService(intent);

                                mBuildings.remove(position);
//                                BuildingAdapter.this.notifyDataSetChanged();
//
                                dialog.dismiss();
                            }
                        })

                        // Displays: Cancel
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();

                                Toast.makeText(mContext, "CANCELLED: Deleted Course: " + building.getBuildingId(), Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "CANCELLED: Deleted Course: " + building.getBuildingId());
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Edit Course: " + building.getBuildingId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, EditBuildingActivity.class);
                intent.putExtra(CLICKEDBUILDING, building);
                mContext.startActivity(intent);
                holder.imgDelete.setVisibility(View.INVISIBLE);
                holder.imgEdit.setVisibility(View.INVISIBLE);
            }
        });
    }


    public void sortByNameAscending() {
        Collections.sort(mBuildings, new Comparator<BuildingPOJO>() {
            @Override
            public int compare(BuildingPOJO lhs, BuildingPOJO rhs) {
                return lhs.getNameEN().compareTo(rhs.getNameEN());
            }
        });

        notifyDataSetChanged();
    }

    public void sortByNameDescending() {
        Collections.sort(mBuildings, Collections.reverseOrder(new Comparator<BuildingPOJO>() {
            @Override
            public int compare(BuildingPOJO lhs, BuildingPOJO rhs) {
                return lhs.getNameEN().compareTo(rhs.getNameEN());
            }
        }));

        notifyDataSetChanged();
    }

    /**
     * This method simply returns the number of buildings to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of buildings
     */
    @Override
    public int getItemCount() {
        return mBuildings.size();
    }

    public void setBuildings(BuildingPOJO[] buildingsArray) {
        this.mBuildings = new ArrayList<>(buildingsArray.length);
        mBuildings.clear();
        mBuildings.addAll(new ArrayList<>(Arrays.asList(buildingsArray)));
        notifyDataSetChanged();
    }

    /**
     * Recycle the children views for a building list item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvAddress;
        public ImageView imageView;
        public ImageView imgDelete;
        public ImageView imgEdit;
        public View mView;
        int position;

        public ViewHolder(View buildingView) {
            super(buildingView);

            tvName = (TextView) buildingView.findViewById(R.id.buildNameText);
            tvAddress = (TextView) buildingView.findViewById(R.id.buildingAddressText);
            mView = buildingView;
            imageView = (ImageView) buildingView.findViewById(R.id.imageView);

            imgDelete = (ImageView) itemView.findViewById(R.id.img_row_delete);
            imgEdit = (ImageView) itemView.findViewById(R.id.img_row_edit);

            imgDelete.setVisibility(View.INVISIBLE);
            imgEdit.setVisibility(View.INVISIBLE);
        }

    }

}
