package in.blogspot.alcory.mobaholic;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SummaryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SummaryFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView seekBarTextView;
    private SeekBar seekBar;
    private CheckBox seekBarCheckBox;
//    private CheckBox dailyNotificationCheckBox;
//    private TimePicker timePicker;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SummaryFragment.
     */
    public static SummaryFragment newInstance() {
        SummaryFragment fragment;
        fragment = new SummaryFragment();
        return fragment;
    }
    public SummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        seekBarTextView = (TextView)getView().findViewById(R.id.seekBarTextView);
        seekBar = (SeekBar)getView().findViewById(R.id.seekBar);
        SharedPreferences sharedPref = getActivity().getSharedPreferences(MyApp.MY_SHARED_PREF,getActivity().MODE_PRIVATE);
        float progress = sharedPref.getFloat(MyApp.MAX_USE_NOTIFICATION_TIME,0)*2;

        seekBar.setProgress((int)progress);
        seekBarCheckBox = (CheckBox)getView().findViewById(R.id.seekBarCheckBox);
        seekBarCheckBox.setChecked(sharedPref.getBoolean(MyApp.MAX_USE_NOTIFICATION_SETTING,false));
        seekBarTextView.setText("Notify me if uses exceed by "+progress/2+" hour");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int iProgress, boolean fromUser) {
                progress = (float) (iProgress*1.0);
                seekBarTextView.setText("Notify me if uses exceed by "+progress/2+" hour");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                 SharedPreferences sharedPref = getActivity().getSharedPreferences(MyApp.MY_SHARED_PREF,getActivity().MODE_PRIVATE);
                 SharedPreferences.Editor editor = sharedPref.edit();
                 editor.putFloat(MyApp.MAX_USE_NOTIFICATION_TIME, progress / 2);
                 editor.putBoolean(MyApp.NEED_TO_NOTIFY_USER_FOR_MAX_LIMIT, true);
                 editor.apply();
                if(seekBarCheckBox.isChecked())
                  Toast.makeText(getActivity(),"Notification Set for max use of time "+ progress /2,Toast.LENGTH_SHORT).show();
            }
        });
        seekBarCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    SharedPreferences sharedPref = getActivity().getSharedPreferences(MyApp.MY_SHARED_PREF, getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(MyApp.MAX_USE_NOTIFICATION_SETTING,isChecked);
                editor.putBoolean(MyApp.NEED_TO_NOTIFY_USER_FOR_MAX_LIMIT, true);
                editor.apply();
                String notification_setting = isChecked?" Enabled ":" Disabled";
                Toast.makeText(getActivity(),"Notification"+notification_setting ,Toast.LENGTH_SHORT).show();

            }
        });

//        dailyNotificationCheckBox = (CheckBox)getView().findViewById(R.id.dailyNotificationCheckBox);
//        timePicker = (TimePicker)getView().findViewById(R.id.timePicker);
//        timePicker.setIs24HourView(true);
//        int hour = timePicker.getCurrentHour();
//        int min = timePicker.getCurrentMinute();

//        dailyNotificationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                Toast.makeText(getActivity(),"DEEPAK setChecked",Toast.LENGTH_LONG).show();
//                int hour = timePicker.getCurrentHour();
//                int min = timePicker.getCurrentMinute();
//          setDailyNotification(isChecked,hour,min);
//
//            }
//        });

    }

//    private void setDailyNotification(boolean isChecked, int hour, int min) {
//        if(isChecked){
//            Toast.makeText(getActivity(),"DEEPAK setDailyNotification"+hour+" "+min,Toast.LENGTH_LONG).show();
//
//            Intent myIntent = new Intent(getActivity(),AlarmReceiver.class);
//            AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(getActivity().ALARM_SERVICE);
//            PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0, myIntent, 0);
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.MINUTE, min);
//            calendar.set(Calendar.HOUR, hour);
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*60*24 , pendingIntent);
//        }
//
//    }


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
