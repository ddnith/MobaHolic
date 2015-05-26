package in.blogspot.alcory.mobaholic;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class HistoryFragment extends Fragment implements AbsListView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private SimpleAdapter mAdapter;
    ArrayList<HashMap<String,String>> userHistoryList;

    // TODO: Rename and change types of parameters
    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HistoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userHistoryList = getPresentableHistoryList();
        String[] from = { "date", "count","time_spent" };
        int[] to = { R.id.dateTextView, R.id.countTextView,R.id.timeTextView };
        mAdapter = new SimpleAdapter(getActivity(), userHistoryList,
                R.layout.list_items, from, to);
    }

    private ArrayList<HashMap<String,String>> getPresentableHistoryList(){
        DbTools dbTools = new DbTools(getActivity(),MyApp.DATABASE_NAME,null,1);
        ArrayList<HashMap<String,String>> userHistoryList = dbTools.getAllUserHistory();
        ArrayList<HashMap<String,String>> presentableHistoryList = new ArrayList<HashMap<String, String>>();

        for(HashMap<String,String> userHistory: userHistoryList){
            HashMap<String,String> presentableHistoryMap = new HashMap<String,String>();
            presentableHistoryMap.put("date",getPresentableDate(userHistory.get("date")));
            presentableHistoryMap.put("count",userHistory.get("count"));
            presentableHistoryMap.put("time_spent",getPresentableTimeSpent(userHistory.get("time_spent")));
            presentableHistoryList.add(presentableHistoryMap);
        }
        return presentableHistoryList;
    }

    private String getPresentableTimeSpent(String time_spent){
        long time  = Long.parseLong(time_spent);

        long diffSeconds = time / 1000 % 60;
        long diffMinutes = time / (60 * 1000) % 60;
        long diffHours = time / (60 * 60 * 1000) % 24;

        time_spent = diffHours +":"+diffMinutes+":"+diffSeconds;
        return time_spent;
    }

    private String getPresentableDate(String date){
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(Long.parseLong(date));
        int month = c.get(Calendar.MONTH)+1;
        date = "" +c.get(Calendar.YEAR)+"-"+month+"-"+c.get(Calendar.DAY_OF_MONTH);

        return date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }


    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
