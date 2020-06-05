package co.za.spanbi.fragmentsharedata.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.za.spanbi.fragmentsharedata.R;
import co.za.spanbi.fragmentsharedata.model.DogBreed;
import co.za.spanbi.fragmentsharedata.util.Util;
import co.za.spanbi.fragmentsharedata.viewmodel.DetailViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private DetailViewModel viewModel;
    private int dogUuid;

    @BindView(R.id.dogImage)
    ImageView dogImage;
    @BindView(R.id.dogName)
    TextView dogName;
    @BindView(R.id.dogPurpose)
    TextView dogPurpose;
    @BindView(R.id.dogTemperament)
    TextView dogTemperament;
    @BindView(R.id.dogLifespan)
    TextView dogLifespan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null){
            dogUuid = DetailFragmentArgs.fromBundle(getArguments()).getDogUuid();
        }
        viewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        viewModel.fetch(dogUuid);
        observeViewModel();

    }
    private void observeViewModel(){
        viewModel.dogLiveData.observe(getViewLifecycleOwner(), dogBreed -> {
            if(dogBreed != null && getContext() != null){
                dogName.setText(dogBreed.dogBreed);
                dogPurpose.setText(dogBreed.bredFor);
                dogTemperament.setText(dogBreed.Temperament);
                dogLifespan.setText(dogBreed.lifeSpan);

                if(dogBreed.imageUrl!=null){
                    Util.loadImage(dogImage, dogBreed.imageUrl, Util.getProgressDrawable(getContext()));
                }
            }
        });
    }

}