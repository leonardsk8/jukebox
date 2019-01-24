package ilioncorp.com.jukebox.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;
import ilioncorp.com.jukebox.R;
import ilioncorp.com.jukebox.model.dao.CommentsDAO;
import ilioncorp.com.jukebox.model.dto.CommentsVO;
import ilioncorp.com.jukebox.utils.constantes.Constantes;
import ilioncorp.com.jukebox.view.adapter.CommentListAdapter;
import ilioncorp.com.jukebox.view.generic.GenericActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RatingActivity extends GenericActivity implements Handler.Callback, View.OnClickListener {

    private Handler bridge;
    private String idBar;
    private CommentsDAO commentsDAO;
    private ArrayList<CommentsVO> listComments;
    private android.support.v7.widget.RecyclerView listRecyclerComments;
    private CommentListAdapter adapter;
    private android.widget.RatingBar rbFiveStarts;
    private android.widget.RatingBar rbFourStarts;
    private android.widget.RatingBar rbThreeStarts;
    private android.widget.RatingBar rbTwoStarts;
    private android.widget.RatingBar rbOneStarts;
    private android.widget.ProgressBar pb5Start;
    private android.widget.ProgressBar pb4Start;
    private android.widget.ProgressBar pb3Start;
    private android.widget.ProgressBar pb2Start;
    private android.widget.ProgressBar pb1Start;
    private android.widget.TextView tvAverage;
    private RatingBar rbAverage;
    private TextView tvTotalRating;
    private TextView tvTotalFive;
    private TextView tvTotalFour;
    private TextView tvTotalThree;
    private TextView tvTotalTwo;
    private TextView tvTotalOne;
    private TextView tvTitleComments;
    private boolean editComment;
    private TextView tvNamePersonComment;
    private TextView tvDateComment;
    private android.widget.EditText etLeaveComment;
    private RatingBar rbLeaveStars;
    private android.widget.ImageView btnAcceptComment;
    private Date date;
    private DateFormat dateFormat ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        date = new Date();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        this.btnAcceptComment =  findViewById(R.id.btnAcceptComment);
        this.rbLeaveStars = findViewById(R.id.rbLeaveStars);
        this.etLeaveComment = findViewById(R.id.etLeaveComment);
        this.tvNamePersonComment = findViewById(R.id.tvNamePersonComment);
        this.tvDateComment = findViewById(R.id.tvDateComment);
        editComment = false;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.tvTitleComments =  findViewById(R.id.tvTitleComments);
        this.tvTotalOne = findViewById(R.id.tvTotalOne);
        this.tvTotalTwo = findViewById(R.id.tvTotalTwo);
        this.tvTotalThree = findViewById(R.id.tvTotalThree);
        this.tvTotalFour = findViewById(R.id.tvTotalFour);
        this.tvTotalFive = findViewById(R.id.tvTotalFive);
        this.tvTotalRating = findViewById(R.id.tvTotalRating);
        this.rbAverage = findViewById(R.id.rbAverage);
        this.pb1Start = findViewById(R.id.pb1Start);
        this.pb2Start = findViewById(R.id.pb2Starat);
        this.pb3Start = findViewById(R.id.pb3Start);
        this.pb4Start = findViewById(R.id.pb4Start);
        this.pb5Start = findViewById(R.id.pb5Start);
        this.rbOneStarts = findViewById(R.id.rbOneStarts);
        this.rbTwoStarts = findViewById(R.id.rbTwoStarts);
        this.rbThreeStarts = findViewById(R.id.rbThreeStarts);
        this.rbFourStarts = findViewById(R.id.rbFourStarts);
        this.rbFiveStarts = findViewById(R.id.rbFiveStarts);
        this.tvAverage = findViewById(R.id.tvAverage);
        this.listRecyclerComments = findViewById(R.id.listRecyclerComments);
        this.listRecyclerComments.setHasFixedSize(true);
        this.listRecyclerComments.setLayoutManager(new LinearLayoutManager(this));
        rbOneStarts.setRating(1);
        rbTwoStarts.setRating(2);
        rbThreeStarts.setRating(3);
        rbFourStarts.setRating(4);
        rbFiveStarts.setRating(5);
        idBar = getIntent().getStringExtra("idBar");
        bridge = new Handler(this::handleMessage);
        commentsDAO = new CommentsDAO(bridge, idBar);
        commentsDAO.verifyComment(Constantes.userActive.getUserUID(), new Handler(message -> {
            CommentsVO commentOwn = (CommentsVO) message.obj;
            editComment = true;
            this.rbLeaveStars.setRating(commentOwn.getRating());
            this.etLeaveComment.setText(commentOwn.getComment());
            return false;
        }));
        commentsDAO.getComments();
        this.tvNamePersonComment.setText(Constantes.userActive.getUserName());
        this.btnAcceptComment.setOnClickListener(this::onClick);
        this.tvDateComment.setText(dateFormat.format(date));


    }

    @Override
    public boolean handleMessage(Message message) {
        listComments = (ArrayList<CommentsVO>) message.obj;
        if (listComments.size() == 0)
            tvTitleComments.setText("AUN NO HAY COMENTARIOS\nSE EL PRIMERO");
        else
            tvTitleComments.setText("COMENTARIOS");
        adapter = new CommentListAdapter(listComments, this);
        calculateRatingAndPutValues();
        classifyRating();
        listRecyclerComments.setAdapter(adapter);
        return false;
    }

    private void classifyRating() {
        int one, two, three, four, five;
        one = two = three = four = five = 0;
        float value;
        for (CommentsVO vo : listComments) {
            value = vo.getRating();
            switch ((int) value) {
                case 1:
                    one++;
                    break;
                case 2:
                    two++;
                    break;
                case 3:
                    three++;
                    break;
                case 4:
                    four++;
                    break;
                case 5:
                    five++;
                    break;
            }
        }
        pb1Start.setMax(listComments.size() + 1);
        pb2Start.setMax(listComments.size() + 1);
        pb3Start.setMax(listComments.size() + 1);
        pb4Start.setMax(listComments.size() + 1);
        pb5Start.setMax(listComments.size() + 1);
        pb1Start.setProgress(one);
        pb2Start.setProgress(two);
        pb3Start.setProgress(three);
        pb4Start.setProgress(four);
        pb5Start.setProgress(five);
        tvTotalOne.setText(one + "");
        tvTotalTwo.setText(two + "");
        tvTotalThree.setText(three + "");
        tvTotalFour.setText(four + "");
        tvTotalFive.setText(five + "");

    }

    private void calculateRatingAndPutValues() {
        float averageStars = 0;
        float sumStars = 0;
        for (CommentsVO vo : listComments) {
            sumStars += vo.getRating();
        }
        float divide = listComments.size();
        if (divide == 0)
            divide = 1;
        averageStars = sumStars / divide;
        String aux = String.valueOf(averageStars);
        if(aux.length()>2)
            aux = aux.substring(0,3);
        tvAverage.setText(aux);
        rbAverage.setRating(averageStars);
        tvTotalRating.setText(listComments.size() + " comments");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btnAcceptComment:
                saveComment();
                if(editComment)
                    dialog();
                break;

        }
    }

    private void saveComment() {
        if(!validateFieldsComment()) {
            messageToast("Debe llenar los campos");
            editComment=false;
            return;
        }
        else {
            CommentsVO commentsVO = new CommentsVO();
            commentsVO.setComment(etLeaveComment.getText().toString());
            commentsVO.setRating(rbLeaveStars.getRating());
            commentsVO.setDate(dateFormat.format(date));
            commentsVO.setIdBar(this.idBar);
            commentsVO.setNameUser(Constantes.userActive.getUserName());
            commentsVO.setIdUser(Constantes.userActive.getUserUID());
            commentsDAO.saveComment(commentsVO, Constantes.userActive.getUserUID());
            editComment = true;
            etLeaveComment.setText("");
            rbLeaveStars.setRating(0);
        }
    }

    private boolean validateFieldsComment() {

            if(etLeaveComment.getText().toString().isEmpty() || rbLeaveStars.getRating()==0.0)
                return false;
        return true;
    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tu comentario se ha editado correctamente");
        builder.create();
        builder.show();
    }

}
