package com.example.antonio.courtcounter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Vibrator;



public class MainActivity extends AppCompatActivity {

    // Variable to Save the State
    static final String STATE_SCORE_TEAM_A = "scoreTeamA";
    static final String STATE_SCORE_TEAM_B = "scoreTeamB";
    static final String STATE_SET_TEAM_A = "setTeamA";
    static final String STATE_SET_TEAM_B = "setTeamB";
    static final String STATE_SCORE_TO_WIN = "scoreToWin";
    static final String STATE_SERVICE_TEAM_A = "serviceTeamA";

    // Declare and initialize variables
    int scoreTeamA=0;
    int scoreTeamB=0;
    int setTeamA=0;
    int setTeamB=0;
    boolean serviceTeamA = true;
    int scoreToWin=25;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Save the state on portrait-landscape swap
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(STATE_SCORE_TEAM_A,scoreTeamA);
        savedInstanceState.putInt(STATE_SCORE_TEAM_B,scoreTeamB);
        savedInstanceState.putInt(STATE_SET_TEAM_A,setTeamA);
        savedInstanceState.putInt(STATE_SET_TEAM_B,setTeamB);
        savedInstanceState.putInt(STATE_SCORE_TO_WIN,scoreToWin);
        savedInstanceState.putBoolean(STATE_SERVICE_TEAM_A,serviceTeamA);

        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        scoreTeamA=savedInstanceState.getInt(STATE_SCORE_TEAM_A);
        scoreTeamB=savedInstanceState.getInt(STATE_SCORE_TEAM_B);
        setTeamA=savedInstanceState.getInt(STATE_SET_TEAM_A);
        setTeamB=savedInstanceState.getInt(STATE_SET_TEAM_B);
        scoreToWin=savedInstanceState.getInt(STATE_SCORE_TO_WIN);
        serviceTeamA=savedInstanceState.getBoolean(STATE_SERVICE_TEAM_A);

        serviceBallDisplay();
        displayTable();
    }

    //  Increment the score of Team A
    //  Check if the Team has the score to win the Set
    public void PointForA(View view){
        scoreTeamA +=1;
        if ((scoreTeamA>=scoreToWin) && ((scoreTeamA-scoreTeamB)>=2)){

            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);

            setTeamA += 1; // Upload the set won by Team A
            scoreTeamA = 0; // Reset the score for the new set
            scoreTeamB = 0;

            displayTable();
            SetUpload();

        } else {
            TextView scoreA = (TextView) findViewById(R.id.team_a_button);
            scoreA.setText(String.valueOf(scoreTeamA)); //scoreTeamA);

            serviceTeamA=true;
            serviceBallDisplay();

        }
    }


    public void PointForB(View view){
        scoreTeamB +=1;
        if ((scoreTeamB>=scoreToWin) && ((scoreTeamB-scoreTeamA)>=2)){

            // Vibrate for 500 milliseconds
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);

            setTeamB += 1;
            scoreTeamA = 0;
            scoreTeamB = 0;

            displayTable();
            SetUpload();

        } else {
            TextView scoreB = (TextView) findViewById(R.id.team_b_button);
            scoreB.setText(String.valueOf(scoreTeamB)); //scoreTeamA);

            serviceTeamA=false;
            serviceBallDisplay();

        }
    }


    // Visualize the ball in the correct side of the court
    // If serviceTeamA is true the ball is visible in court A and invisible in court B

    private void serviceBallDisplay(){
        if (serviceTeamA) {
            ImageView service = findViewById(R.id.service_a_image);
            service.setVisibility(View.VISIBLE);
            service = findViewById(R.id.service_b_image);
            service.setVisibility(View.INVISIBLE);
        }else{
            ImageView service = findViewById(R.id.service_a_image);
            service.setVisibility(View.INVISIBLE);
            service = findViewById(R.id.service_b_image);
            service.setVisibility(View.VISIBLE);
        }
    }

    /*
        Upload the visualization of score and set for team A and B
     */
    private void displayTable(){
        TextView tabUpload = (TextView) findViewById(R.id.team_a_button);
        tabUpload.setText(String.valueOf(scoreTeamA));

        tabUpload = (TextView) findViewById(R.id.team_b_button);
        tabUpload.setText(String.valueOf(scoreTeamB));

        tabUpload = (TextView) findViewById(R.id.set_a_text);
        tabUpload.setText(String.valueOf(setTeamA));

        tabUpload = (TextView) findViewById(R.id.set_b_text);
        tabUpload.setText(String.valueOf(setTeamB));
    }


    private void SetUpload () {

        if ((setTeamA+setTeamB)==4) scoreToWin=15; // the score to win is 15 if they are playing the 5th set

        if (setTeamA == 3) winnerTeam("Team A"); // check if team a has won

        if (setTeamB == 3) winnerTeam("Team B");  // check if team b has won


    }

    private void winnerTeam(String winner){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(winner + " wins!!!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        setTeamA = 0; // Reset the score-set-service for a new match
                        setTeamB = 0;
                        scoreTeamA = 0;
                        scoreTeamB = 0;
                        scoreToWin=25;
                        serviceTeamA=true;

                        serviceBallDisplay();
                        displayTable();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
