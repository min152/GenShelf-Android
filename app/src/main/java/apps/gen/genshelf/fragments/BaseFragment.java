package apps.gen.genshelf.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import apps.gen.genshelf.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BaseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BaseFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String mTitle;
    public void setTitle(String val) {
        mTitle = val;
        if (mListener != null) mListener.titleChange(this, val);
    }
    public String getTitle() {
        return mTitle;
    }

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void pushFragment(BaseFragment other) {
        if (mListener != null)
            mListener.onPushFragment(this, other);
    }

    public BaseFragment popFragment() {
        if (mListener != null)
            return mListener.onPopFragment(this);
        return null;
    }

    public void setText(String text) {}

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onPushFragment(BaseFragment sender, BaseFragment other);
        BaseFragment onPopFragment(BaseFragment sender);
        void titleChange(BaseFragment sender, String title);
    }
}
