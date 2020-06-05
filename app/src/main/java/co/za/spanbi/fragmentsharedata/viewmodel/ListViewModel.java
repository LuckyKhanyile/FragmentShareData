package co.za.spanbi.fragmentsharedata.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import co.za.spanbi.fragmentsharedata.model.DogBreed;
import co.za.spanbi.fragmentsharedata.model.DogDao;
import co.za.spanbi.fragmentsharedata.model.DogDatabase;
import co.za.spanbi.fragmentsharedata.model.DogsApiService;
import co.za.spanbi.fragmentsharedata.util.SharedPreferenceHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends AndroidViewModel {
    public MutableLiveData<List<DogBreed>> dogs = new MutableLiveData<List<DogBreed>>();
    public MutableLiveData<Boolean> dogLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();

    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    private DogsApiService dogsApiService = new DogsApiService();
    private CompositeDisposable disposable = new CompositeDisposable();
    private AsyncTask<List<DogBreed>, Void, List<DogBreed>> insertAsyncTask;
    private AsyncTask<Void, Void, List<DogBreed>> retrieveAsyncTask;

    SharedPreferenceHelper prefHelper = SharedPreferenceHelper.getmInstance(getApplication());
    private long refreshTime = 5*60*1000*1000*1000L;
    public void refresh() {
        /*DogBreed dog1 = new DogBreed("1", "corgi", "15 years", "", "", "", "");
        DogBreed dog2 = new DogBreed("2", "rotwaillor", "10 years", "", "", "", "");
        DogBreed dog3 = new DogBreed("3", "labrodor", "13 years", "", "", "", "");
        DogBreed dog4 = new DogBreed("4", "pitbull", "9 years", "", "", "", "");

        ArrayList<DogBreed> dogList = new ArrayList<DogBreed>();
        dogList.add(dog1);
        dogList.add(dog2);
        dogList.add(dog3);
        dogList.add(dog4);

        dogs.setValue(dogList);
        dogLoadError.setValue(false);
        loading.setValue(false);*/
        //fetchFromRemote();
        //fetchFromLocal();

        long updateTime = prefHelper.getUpdatedTime();
        long currentTime = System.nanoTime();
        if(updateTime != 0 && currentTime-updateTime < refreshTime){
            fetchFromLocal();
        }else {
            fetchFromRemote();
        }
    }

    private void fetchFromLocal(){
        loading.setValue(true);
        retrieveAsyncTask = new RetrieveDogsAsyncTask();
        retrieveAsyncTask.execute();
    }

    public void refreshBypass(){
        fetchFromRemote();
    }

    private void fetchFromRemote() {
        loading.setValue(true);
        disposable.add(
                dogsApiService.getDogs()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                            @Override
                            public void onSuccess(List<DogBreed> dogBreeds) {
                                insertAsyncTask = new InsertDogsAsyncTask();
                                insertAsyncTask.execute(dogBreeds);
                                prefHelper.saveUpdatedTime(System.nanoTime());
                                dogsRetrieved(dogBreeds);
                            }

                            @Override
                            public void onError(Throwable e) {
                                dogLoadError.setValue(true);
                                loading.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );

    }

    private void dogsRetrieved(List<DogBreed> dogList){
        dogs.setValue(dogList);
        dogLoadError.setValue(false);
        loading.setValue(false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();

        if(insertAsyncTask != null){
            insertAsyncTask.cancel(true);
            insertAsyncTask = null;
        }
        if(retrieveAsyncTask!=null){
            retrieveAsyncTask.cancel(true);
            retrieveAsyncTask=null;
        }
    }


    private class RetrieveDogsAsyncTask extends AsyncTask<Void, Void, List<DogBreed>>{
        List<DogBreed> dogBreeds;
        @Override
        protected List<DogBreed> doInBackground(Void... voids) {
            DogDao dogDao = DogDatabase.getInstance(getApplication()).dogDao();
            this.dogBreeds = dogDao.getAllDogs();
            return dogBreeds;
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            dogsRetrieved(dogBreeds);
        }
    }

    private class InsertDogsAsyncTask extends AsyncTask<List<DogBreed>, Void, List<DogBreed>>{

        @Override
        protected List<DogBreed> doInBackground(List<DogBreed>... lists) {
            List<DogBreed> mlist = lists[0];
            DogDao dogDao = DogDatabase.getInstance(getApplication()).dogDao();
            dogDao.deleteAllDogs();
            ArrayList<DogBreed> newList = new ArrayList<DogBreed>(mlist);
            List<Long> result = dogDao.insertAll(newList.toArray(new DogBreed[0]));
            for(int i =0; i<result.size(); i++){
                mlist.get(i).uuid=result.get(i).intValue();
            }
            return mlist;
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            dogsRetrieved(dogBreeds);
        }
    }
}
