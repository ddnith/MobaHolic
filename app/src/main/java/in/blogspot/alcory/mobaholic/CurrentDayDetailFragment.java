package in.blogspot.alcory.mobaholic;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrentDayDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrentDayDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CurrentDayDetailFragment extends Fragment {
    private final static String TAG = "CurrentDayDetailFragment_DEEPAK";
    private TextView currentCountTextView;
    private TextView hourTextView;
    private TextView minuteTextView;
    private TextView secondTextView;
    private final int TIME_FOR_ANIMATION = 1500;
    DbTools dbTools;
//    ArrayList<HashMap<String,String>> allUserRecord;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CurrentDayDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentDayDetailFragment newInstance() {
        CurrentDayDetailFragment fragment = new CurrentDayDetailFragment();
        return fragment;
    }
    public CurrentDayDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbTools = new DbTools(this.getActivity(),MyApp.DATABASE_NAME,null,1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_current_day_detail, container, false);
        return inflater.inflate(R.layout.fragment_main_card_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();


        fillTodayDetails();
        fillYesterdayDetails();
        fillAverageDetail();
    }

    private void fillAverageDetail() {
        String averageTimeSpent = getAverageTimeSpent();
        TextView averageTextView = (TextView)getView().findViewById(R.id.averageTextView);
        averageTextView.setText(averageTimeSpent);
    }

    private String getAverageTimeSpent() {
        DbTools dbTools = new DbTools(getActivity(),MyApp.DATABASE_NAME,null,1);
        ArrayList<HashMap<String,String>> userHistoryList = dbTools.getAllUserHistory();
        long totalTimeSpent = 0;
        int count = 0;
        for(HashMap<String,String> userHistoryMap : userHistoryList){
            count++;
            totalTimeSpent += Long.parseLong(userHistoryMap.get("time_spent"));
            Log.d("CHANDAN"," CHANDAN time "+userHistoryMap.get("time_spent"));
            Log.d("CHANDAN"," CHANDAN id "+userHistoryMap.get("day_index"));

        }

        long averageTimeSpent = 0;
        if(count!=0){
            averageTimeSpent = totalTimeSpent/count;
        }

        long Seconds = averageTimeSpent / 1000 % 60;
        long Minutes = averageTimeSpent / (60 * 1000) % 60;
        long Hours = averageTimeSpent / (60 * 60 * 1000) % 24;

        return Hours+":"+Minutes+":"+Seconds ;
    }

    private void fillTodayDetails() {
        Calendar c = Calendar.getInstance();
        int thisDay = c.get(Calendar.DAY_OF_YEAR);
        int currentCount = getTodayCount(thisDay);
        increaseValueAnimatedly(currentCountTextView,currentCount);

        long totalTimeSpentToday = MyApp.getTotalTimeSpentToday(getActivity(),thisDay);
        displayTimeSpent(totalTimeSpentToday,hourTextView,minuteTextView,secondTextView);
    }

    private void fillYesterdayDetails() {
        Calendar c = Calendar.getInstance();
        int thisDay = c.get(Calendar.DAY_OF_YEAR);
        int currentCount = getTodayCount(thisDay-1);
        if(currentCount == 0){
             getView().findViewById(R.id.y_card_view).setVisibility(View.GONE);
            return;
        }
        TextView tv = (TextView)getView().findViewById(R.id.y_currentCountTextView);
        increaseValueAnimatedly(tv,currentCount);

        long totalTimeSpentThisDay = MyApp.getTotalTimeSpentToday(getActivity(),thisDay-1);
        TextView hTv = (TextView)getView().findViewById(R.id.y_hourTextView);
        TextView mTv = (TextView)getView().findViewById(R.id.y_minuteTextView);
        TextView sTv = (TextView)getView().findViewById(R.id.y_secondTextView);

        displayTimeSpent(totalTimeSpentThisDay,hTv,mTv,sTv);
    }

    public void init(){
//        allUserRecord = dbTools.getAllUserRecord();
        currentCountTextView = (TextView)getView().findViewById(R.id.currentCountTextView);
        hourTextView = (TextView)getView().findViewById(R.id.hourTextView);
        minuteTextView = (TextView)getView().findViewById(R.id.minuteTextView);
        secondTextView = (TextView)getView().findViewById(R.id.secondTextView);
    }

    private void increaseValueAnimatedly(final View view,int count){
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, count);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                ((TextView)view).setText(String.valueOf(animation.getAnimatedValue()));
            }
        });
        animator.setEvaluator(new TypeEvaluator<Integer>() {
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                return Math.round((endValue - startValue) * fraction);
            }
        });
        animator.setDuration(TIME_FOR_ANIMATION);
        animator.start();
    }

//    private int  getTodayCount(){
//
//        int  currentCount = 0;
//        Date toady = new Date();
//        Calendar cal1 = new GregorianCalendar();
//        cal1.setTime(toady);
//
//        for(HashMap<String,String> map : allUserRecord){
//            String startTime = map.get("start_time");
//            DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
//            try{
//                Date onlyDate = df.parse(startTime);
//                Calendar cal2 = new GregorianCalendar();
//                cal2.setTime(onlyDate);
//                if(cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE))
//                    currentCount++;
//            }
//            catch (ParseException ex){
//                Log.e(TAG, "ParseException" + ex);
//            }
//        }
//        return currentCount;
//    }

    private int  getTodayCount(int thisDay){
        String count = "0";
        HashMap<String,String> currentDayUserHistory = dbTools.getCurrentDayUserHistory(thisDay);
        count = currentDayUserHistory.get("count");
        Log.d("DEEPAK","get TodayCount count "+count);
        Log.d("DEEPAK","get DayId "+currentDayUserHistory.get("day_index"));
        if(count==null)count = "0";
        return Integer.parseInt(count);

    }

//    private long getTotalTimeSpentTodayOld(){
//        long totalTimeSpent = 0;
//        Date toady = new Date();
//        Calendar calToday = new GregorianCalendar();
//        calToday.setTime(toady);
//
//        for(HashMap<String,String> map : allUserRecord){
//            String startTime = map.get("start_time");
//            DateFormat df1 = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
//
//            String stopTime = map.get("stop_time");
//            DateFormat df2 = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
//            try{
//                Date start = df1.parse(startTime);
//                Calendar calStart = new GregorianCalendar();
//                calStart.setTime(start);
//
//                Date stop = df1.parse(stopTime);
//                Calendar calStop = new GregorianCalendar();
//                calStop.setTime(stop);
//                if(calToday.get(Calendar.DATE) == calStart.get(Calendar.DATE)){
//
//                    long diff = stop.getTime() - start.getTime();
//                    totalTimeSpent+=diff;
//
//                }
//
//            }
//            catch (ParseException ex){
//                Log.e(TAG,"ParseException"+ex);
//            }
//        }
//
//        return totalTimeSpent;
//    }

    private void displayTimeSpent(long time,TextView hourTextView,TextView minuteTextView,TextView secondTextView){
        Log.d(TAG, "displayTimeSpent time "+time);

        long diffSeconds = time / 1000 % 60;
        long diffMinutes = time / (60 * 1000) % 60;
        long diffHours = time / (60 * 60 * 1000) % 24;
        //long diffDays = time / (24 * 60 * 60 * 1000);

        increaseValueAnimatedly(hourTextView, (int) diffHours);
        increaseValueAnimatedly(minuteTextView, (int) diffMinutes);
        increaseValueAnimatedly(secondTextView, (int) diffSeconds);
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
        public void onFragmentInteraction(Uri uri);
    }

}
