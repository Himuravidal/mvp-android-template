/*
    Copyright 2018 Gaurav Kumar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.github.mvpbasearchitecture.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mvpbasearchitecture.R;
import com.github.mvpbasearchitecture.base.BaseMVPFragment;
import com.github.mvpbasearchitecture.data.models.local.Item;
import com.github.mvpbasearchitecture.di.component.ActivityComponent;
import com.github.mvpbasearchitecture.ui.adapter.MainItemListAdapter;

import java.util.List;

import javax.inject.Inject;

/**
 * Main Fragment where most of the UI stuff happens
 * Extends functionality of {@link BaseMVPFragment}
 * Implements Screen specific ui tasks {@link MainContract.View}
 *
 * Created by gk
 */
public class MainFragment extends BaseMVPFragment<MainContract.Presenter> implements MainContract.View {

    private View inflatedView;

    @Inject
    MainContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;
    private TextView mEmptyListText;
    private Button mRefreshItemBtn;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public MainFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflatedView = inflater.inflate(R.layout.fragment_main, container, false);

        initViews();
        setupListeners();

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            mPresenter.onAttach(this);

            mPresenter.loadItems(false);
        }

        return inflatedView;
    }

    private void setupListeners() {
        mRefreshItemBtn.setOnClickListener(view -> {
            mPresenter.loadItems(true);
        });
    }

    @Override
    protected void initViews() {
        //initialize view here
        mRecyclerView = inflatedView.findViewById(R.id.item_recycler_view);
        mEmptyListText = inflatedView.findViewById(R.id.empty_list_text);
        mRefreshItemBtn = inflatedView.findViewById(R.id.refresh_items);
    }

    @Override
    public void refreshItemList(@NonNull List<Item> itemList) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyListText.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        MainItemListAdapter itemAdapter = new MainItemListAdapter(getContext(), itemList);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(itemAdapter);
    }

    @Override
    public void showEmptyListUI() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyListText.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}
