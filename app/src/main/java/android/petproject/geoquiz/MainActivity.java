package android.petproject.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.*;
import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mBackButton;
    private TextView mQuestionTextView;
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT =0;
    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, false),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, true),
            new Question(R.string.question_africa, true),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate(Bundle) called");
        setContentView(R.layout.activity_main);
        if (savedInstanceState!=null){
            mCurrentIndex= savedInstanceState.getInt(KEY_INDEX,0);
        }


        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        updateQuestion();


        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                v.setClickable(false);
                mFalseButton.setClickable(false);
            }
        });
        mCheatButton =(Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this,answerIsTrue);
                startActivityForResult(intent,REQUEST_CODE_CHEAT);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                v.setClickable(false);
                mTrueButton.setClickable(false);
            }
        });
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater =false;
                updateQuestion();
                mFalseButton.setClickable(true);
                mTrueButton.setClickable(true);
            }
        });
        updateQuestion();

        mBackButton = (ImageButton) findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        updateQuestion();
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(requestCode==REQUEST_CODE_CHEAT){
            if(data==null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG,"onStart() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,"onResume() called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"onPause() called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG,"onStop() called");
    }

    @Override
    public void onDestroy  (){
        super.onDestroy();
        Log.d(TAG,"onDestroy() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;
        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerTrue) {
                messageResId = R.string.correct_toast;

            } else {
                messageResId = R.string.incorrect_toast;

            }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                    .show();
        }
    }
}








