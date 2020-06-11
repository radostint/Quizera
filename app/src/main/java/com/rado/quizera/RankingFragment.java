package com.rado.quizera;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rado.quizera.Common.Common;
import com.rado.quizera.Interface.ItemClickListener;
import com.rado.quizera.Interface.RankingCallback;
import com.rado.quizera.Model.Question;
import com.rado.quizera.Model.QuestionScore;
import com.rado.quizera.Model.Ranking;
import com.rado.quizera.ViewHolder.RankingViewHolder;


public class RankingFragment extends Fragment {
    View fragment;

    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;

    FirebaseDatabase db;
    DatabaseReference questionScore, rankingTable;
    int sum = 0;

    public static RankingFragment newInstance() {
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance();
        questionScore = db.getReference("Question_Score");
        rankingTable = db.getReference("Ranking");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_ranking, container, false);
        rankingList = fragment.findViewById(R.id.rankingList);
        layoutManager = new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);

        //orderByChild метода подрежда във възходящ ред,затова обръщаме layout-а
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);

        updateScore(Common.currentUser.getUsername(), new RankingCallback<Ranking>() {
            @Override
            public void callback(Ranking ranking) {
                rankingTable.child(ranking.getUsername()).setValue(ranking);

            }
        });
        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(Ranking.class, R.layout.ranking_layout, RankingViewHolder.class, rankingTable.orderByChild("score")) {
            @Override
            protected void populateViewHolder(RankingViewHolder viewHolder, final Ranking model, int i) {
                viewHolder.rankingName.setText(model.getUsername());
                viewHolder.rankingScore.setText(String.valueOf(model.getScore()));
                if (model.getUsername().equals(Common.currentUser.getUsername())) {
                    viewHolder.itemView.setBackgroundResource(R.color.currentUserInRanking);
                    viewHolder.rankingName.setTextColor(Color.BLACK);
                    viewHolder.rankingScore.setTextColor(Color.BLACK);

                }
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent scoreDetails = new Intent(getActivity(), ScoreDetailsActivity.class);
                        scoreDetails.putExtra("viewUser", model.getUsername());
                        startActivity(scoreDetails);

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);
        return fragment;
    }


    private void updateScore(final String username, final RankingCallback<Ranking> callback) {
        questionScore.orderByChild("user").equalTo(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    QuestionScore ques = data.getValue(QuestionScore.class);
                    sum += Integer.parseInt(ques.getScore());

                    Ranking ranking = new Ranking(username, sum);
                    callback.callback(ranking);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
