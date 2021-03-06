package cn.hnist.pany.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private TextView mTipField;
    private ImageView mAddImage;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities
     */
    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        // 第三个参数告知布局生成器是否将生成的视图添加给父视图。
        // 这里传入了false参数，因为我们将以activity代码的方式添加视图

        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        // 指定RecyclerView的布局方式
        mCrimeRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));

        // 没有crime时的提示
        mTipField = view.findViewById(R.id.no_crimes_here);
        mAddImage = view.findViewById(R.id.add_crime_image);

        mAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
            }
        });

        int crimeCount = CrimeLab.get(getActivity()).getCrimes().size();
        if (crimeCount == 0) {
            mCrimeRecyclerView.setVisibility(View.INVISIBLE);
            mTipField.setVisibility(View.VISIBLE);
            mAddImage.setVisibility(View.VISIBLE);
        } else {
            mCrimeRecyclerView.setVisibility(View.VISIBLE);
            mTipField.setVisibility(View.INVISIBLE);
            mAddImage.setVisibility(View.INVISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
//                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
//                startActivity(intent);
                updateUI();
                mCallbacks.onCrimeSelected(crime);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    public void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            //先实例化Callback
            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
            //用Callback构造ItemtouchHelper
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            //调用ItemTouchHelper的attachToRecyclerView方法建立联系
            touchHelper.attachToRecyclerView(mCrimeRecyclerView);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;
        private Crime mCrime;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            // 在CrimeHolder的构造方法里，首先实例化list_item_crime布局，然后传给super()方法，
            // 也就是ViewHolder的构造方法。基类ViewHolder因而实际引用这个视图。
            // 如果需要，可以在ViewHolder的itemView变量里找到它。
            itemView.setOnClickListener(this);

            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);
        }

        @Override
        public void onClick(View view) {
//            Toast.makeText(getActivity(), mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
//            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
//            startActivity(intent);
            mCallbacks.onCrimeSelected(mCrime);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(DateFormat.format("EEEE, MMM dd, yyyy  kk:mm", mCrime.getDate()));
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
            //mCrime.getDate().toString()
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> implements ItemTouchHelperAdapter{

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            CrimeHolder crimeHolder = new CrimeHolder(layoutInflater, viewGroup);
            return crimeHolder;
        }

        // CrimeAdapter必须覆盖onBindViewHolder()方法
        @Override
        public void onBindViewHolder(@NonNull CrimeHolder crimeHolder, int position) {
            Crime crime = mCrimes.get(position);
//            crimeHolder.mTitleTextView.setText(crime.getTitle());
            crimeHolder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public void onItemMove(int fromPosition, int toPosition) {
            //交换位置
            Collections.swap(mCrimes,fromPosition,toPosition);
            notifyItemMoved(fromPosition,toPosition);
        }

        @Override
        public void onItemDissmiss(int position) {
            //移除数据
            CrimeLab.get(getActivity()).deleteCrime(mCrimes.get(position));
            mCrimes.remove(position);
            notifyItemRemoved(position);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public interface ItemTouchHelperAdapter {
        void onItemMove(int fromPosition, int toPosition);

        //数据删除
        void onItemDissmiss(int position);
    }
}
