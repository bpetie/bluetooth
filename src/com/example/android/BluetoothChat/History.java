package com.example.android.BluetoothChat;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link History.OnFragmentInteractionListener} interface to handle interaction
 * events. Use the {@link History#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class History extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	public static final String ARG_SECTION_NUMBER = "section_number";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private int mSectionNum;
	private String mParam2;

	private OnHistoryUpdateListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment History.
	 */
	// TODO: Rename and change types and number of parameters
	public static History newInstance(int param1, String param2) {
		History fragment = new History();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public History() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mSectionNum = getArguments().getInt(ARG_SECTION_NUMBER);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_history, container, false);
		ListView listView = (ListView) rootView.findViewById (R.id.listView1);
		BluetoothChat main = (BluetoothChat) getActivity();
		listView.setAdapter(main.mHistory);
		return rootView;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onHistoryUpdateListener(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnHistoryUpdateListener) activity;
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
	public interface OnHistoryUpdateListener {
		// TODO: Update argument type and name
		public void onHistoryUpdateListener(Uri uri);
	}

}
