package co.za.spanbi.fragmentsharedata.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.za.spanbi.fragmentsharedata.R;
import co.za.spanbi.fragmentsharedata.model.DogBreed;
import co.za.spanbi.fragmentsharedata.util.Util;


public class DogsListAdapter extends RecyclerView.Adapter<DogsListAdapter.DogViewHolder> {

    private ArrayList<DogBreed> dogBreedList;

    public DogsListAdapter(ArrayList<DogBreed> dogBreedList) {
        this.dogBreedList = dogBreedList;
    }

    public void updateDogsList(List<DogBreed> newDogsList){
        dogBreedList.clear();
        dogBreedList.addAll(newDogsList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dog,parent,false);
        DogViewHolder viewHolder = new DogViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {
        ImageView image = holder.itemView.findViewById(R.id.imageView);
        TextView name = holder.itemView.findViewById(R.id.name);
        TextView lifespan = holder.itemView.findViewById(R.id.lifespan);
        LinearLayout doglayout = holder.itemView.findViewById(R.id.dogLayout);
        name.setText(dogBreedList.get(position).dogBreed);
        lifespan.setText(dogBreedList.get(position).lifeSpan);

        Util.loadImage(image, dogBreedList.get(position).imageUrl, Util.getProgressDrawable(image.getContext()));

        doglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListFragmentDirections.ActionDetail actionDetail = ListFragmentDirections.actionDetail();
                actionDetail.setDogUuid(dogBreedList.get(position).uuid);
                Navigation.findNavController(doglayout).navigate(actionDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dogBreedList.size();
    }

    public class DogViewHolder extends RecyclerView.ViewHolder{
        public View itemView;
        public DogViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView=itemView;
        }
    }
}
