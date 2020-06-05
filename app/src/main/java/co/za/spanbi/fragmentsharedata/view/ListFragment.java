package co.za.spanbi.fragmentsharedata.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.za.spanbi.fragmentsharedata.R;
import co.za.spanbi.fragmentsharedata.model.DogBreed;
import co.za.spanbi.fragmentsharedata.viewmodel.ListViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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

    private ListViewModel viewModel;
    private DogsListAdapter dogsListAdapter  = new DogsListAdapter(new ArrayList<DogBreed>());
    @BindView(R.id.dogsList)
    RecyclerView dogsList;
    @BindView(R.id.listError)
    TextView listError;
    @BindView(R.id.loadingView)
    ProgressBar loadingView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail();
       // Navigation.findNavController(view).navigate(action);

        viewModel = new ViewModelProvider(this).get(ListViewModel.class);
        viewModel.refresh();

        dogsList.setLayoutManager(new LinearLayoutManager(getContext()));
        dogsList.setAdapter(dogsListAdapter);
        observeViewModel();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dogsList.setVisibility(View.GONE);
                listError.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
                viewModel.refreshBypass();
                refreshLayout.setRefreshing(false);
            }
        });

    }

    private void observeViewModel(){
        viewModel.dogs.observe(getViewLifecycleOwner(), dogs -> {
            if(dogs != null && dogs instanceof List){
                dogsList.setVisibility(View.VISIBLE);
                dogsListAdapter.updateDogsList(dogs);
            }
        });

        viewModel.dogLoadError.observe(getViewLifecycleOwner(), isError -> {
            if(isError !=null && isError instanceof Boolean){
                listError.setVisibility(isError ? View.VISIBLE : View.GONE);
            }
        });

        viewModel.loading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading instanceof Boolean) {
                loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    listError.setVisibility(View.GONE);
                    dogsList.setVisibility(View.GONE);
                }
            }
        });
    }
}