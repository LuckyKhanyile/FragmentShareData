package co.za.spanbi.fragmentsharedata.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import co.za.spanbi.fragmentsharedata.model.DogBreed;
import co.za.spanbi.fragmentsharedata.model.DogDao;
import co.za.spanbi.fragmentsharedata.model.DogDatabase;

public class DetailViewModel extends AndroidViewModel {
    public MutableLiveData<DogBreed> dogLiveData = new MutableLiveData<DogBreed>();
    public RetrieveDogAsyn retrieveDogAsyn;
    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetch(int uuid){
        //DogBreed dog1 = new DogBreed("1","corgi","15 years","","guarding house","Friendly to kids","");
        //dogLiveData.setValue(dog1);
        retrieveDogAsyn = new RetrieveDogAsyn();
        retrieveDogAsyn.execute(uuid);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(retrieveDogAsyn!=null){
            retrieveDogAsyn.cancel(true);
            retrieveDogAsyn = null;
        }

    }

    public class RetrieveDogAsyn extends AsyncTask<Integer, Void, DogBreed>{
        DogDao dogDao;
        DogBreed dogBreed;
        @Override
        protected DogBreed doInBackground(Integer... integers) {
            dogDao = DogDatabase.getInstance(getApplication()).dogDao();
            dogBreed = dogDao.getDog(integers[0]);
            return dogBreed;
        }

        @Override
        protected void onPostExecute(DogBreed dogBreed) {
            dogLiveData.setValue(dogBreed);
        }
    }
}
