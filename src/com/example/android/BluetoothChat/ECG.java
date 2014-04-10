package com.example.android.BluetoothChat;

import java.util.StringTokenizer;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.Time;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link ECG.OnFragmentInteractionListener} interface to handle interaction
 * events. Use the {@link ECG#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class ECG extends Fragment implements ChartFragment.OnChartListener{
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	public static final String ARG_SECTION_NUMBER = "section_number";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private int section_num;
	private String mParam2;

	private OnECGUpdateListener mListener;
	public static TextView textview;
	
	View rootView;
	TextView texts;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment ECG.
	 */
	// TODO: Rename and change types and number of parameters
	public static ECG newInstance(int section_number, String param2) {
		ECG fragment = new ECG();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, section_number);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public ECG() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			section_num = getArguments().getInt(ARG_SECTION_NUMBER);
			mParam2 = getArguments().getString(ARG_PARAM2);
			
			
			android.support.v4.app.FragmentManager fragmentManager = getChildFragmentManager();
			android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			ChartFragment fragment = new ChartFragment();
			fragmentTransaction.add(R.id.chart, fragment);
			fragmentTransaction.commit();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		rootView = inflater.inflate(R.layout.fragment_ecg, container, false);
		textview = (TextView) rootView.findViewById (R.id.textView1);
		//textview.setText("100");
		BluetoothChat main = (BluetoothChat) getActivity();
		return rootView;
	}

	public void updateData(String data) {	
		android.support.v4.app.FragmentManager fragmentManager = getChildFragmentManager();
		ChartFragment fragment = (ChartFragment) fragmentManager.findFragmentById(R.id.chart);
		
		// convert incoming string to an int array
		StringTokenizer tokens = new StringTokenizer(data, ",");
		int[] array = new int[50];
		int index = 0;
		while (tokens.hasMoreElements())
		{
			array[index] = Integer.valueOf(tokens.nextToken());
			index++;
		}
		// add incoming packet into the queue
		if (array != null)
		{
			fragment.packetQueue.add(array);
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnECGUpdateListener) activity;
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
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnECGUpdateListener {
		// TODO: Update argument type and name
		public void onECGUpdateListener(String data);
	}
	@Override
	public void OnChartListener(int data) {
		// TODO Auto-generated method stub
		
	}

}
