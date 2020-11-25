package com.myor.android.camera2raw.ui.fragments;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.navigation.Navigation;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.coremedia.iso.boxes.TrackBox;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Mp4TrackImpl;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.myor.android.camera2raw.R;
import com.myor.android.camera2raw.camera.AutoFitTextureView;
import com.myor.android.camera2raw.camera.CameraVideoFragment;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Use the {@link Camera2VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Camera2VideoFragment extends CameraVideoFragment {

    private static final String TAG = "CameraFragment";
    private static final String VIDEO_DIRECTORY_NAME = "AndroidWave";
    @BindView(R.id.mTextureView)
    AutoFitTextureView mTextureView;
    @BindView(R.id.mRecordVideo)
    ImageView mRecordVideo;
    @BindView(R.id.mVideoView)
    VideoView mVideoView;
    @BindView(R.id.mPlayVideo)
    ImageView mPlayVideo;
    Unbinder unbinder;
    private String mOutputFilePath;

    private String mPatientNumber;
    private String mPatientAge;
    private String mPatientGender;
    private String mPatientDetailsJpegPath;
    private String mPatientDetailsRawPath1;
    private String mPatientDetailsRawPath2;

    private static ProgressBar progressBar;

    private View btnView;

    public Camera2VideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */


    public static Camera2VideoFragment newInstance() {
        Camera2VideoFragment fragment = new Camera2VideoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        unbinder = ButterKnife.bind(this, view);

        mPatientNumber = Camera2RawFragmentArgs.fromBundle(getArguments()).getPatientNumber();
        mPatientAge = Camera2RawFragmentArgs.fromBundle(getArguments()).getPatientAge();
        mPatientGender = Camera2RawFragmentArgs.fromBundle(getArguments()).getPatientGender();
        mPatientDetailsJpegPath = Camera2RawFragmentArgs.fromBundle(getArguments()).getPatientDetailsJpeg();
        mPatientDetailsRawPath1 = Camera2RawFragmentArgs.fromBundle(getArguments()).getPatientDetailsRaw1();
        mPatientDetailsRawPath2 = Camera2RawFragmentArgs.fromBundle(getArguments()).getPatientDetailsRaw2();

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_cyclic);

        return view;
    }

    @Override
    public int getTextureResource() {
        return R.id.mTextureView;
    }

    @Override
    protected void setUp(View view) {

    }

    private void moveToNextScreen(View view) {
        // Move to the next screen
        Camera2VideoFragmentDirections.ActionVideoFragmentToEndFragment action = Camera2VideoFragmentDirections.actionVideoFragmentToEndFragment();

        action.setPatientNumber(mPatientNumber);
        action.setPatientAge(mPatientAge);
        action.setPatientGender(mPatientGender);
        action.setPatientDetailsJpeg(mPatientDetailsJpegPath);
        action.setPatientDetailsRaw1(mPatientDetailsRawPath1);
        action.setPatientDetailsRaw2(mPatientDetailsRawPath2);
        action.setPatientDetailsVid(mOutputFilePath);

        Navigation.findNavController(view).navigate(action);
    }

    @OnClick({R.id.mRecordVideo, R.id.mPlayVideo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mRecordVideo:
                /**
                 * If media is not recoding then start recording else stop recording
                 */

                /*
                if (mIsRecordingVideo) {
                    try {
                        stopRecordingVideo();
                       // prepareViews(); // This starts the video playing views

                       moveToNextScreen(view);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                 */

                    // Used later when navigation out of this screen
                    btnView = view;

                    progressBar.setVisibility(View.VISIBLE);

                    startRecordingVideo();

                    mRecordVideo.setVisibility(View.INVISIBLE);
                  //  mRecordVideo.setImageResource(R.drawable.ic_stop);
                    //Receive out put file here
                    mOutputFilePath = getCurrentFile().getAbsolutePath();
               // }
                break;
            case R.id.mPlayVideo:
                mVideoView.start();
                mPlayVideo.setVisibility(View.GONE);
                break;
        }
    }

    private void prepareViews() {
        if (mVideoView.getVisibility() == View.GONE) {
            mVideoView.setVisibility(View.VISIBLE);
            mPlayVideo.setVisibility(View.VISIBLE);
            mTextureView.setVisibility(View.GONE);
            try {
                setMediaForRecordVideo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setMediaForRecordVideo() throws IOException {
        mOutputFilePath = parseVideo(mOutputFilePath);
        // Set media controller
        mVideoView.setMediaController(new MediaController(getActivity()));
        mVideoView.requestFocus();
        mVideoView.setVideoPath(mOutputFilePath);
        mVideoView.seekTo(100);
        mVideoView.setOnCompletionListener(mp -> {
            // Reset player
            mVideoView.setVisibility(View.GONE);
            mTextureView.setVisibility(View.VISIBLE);
            mPlayVideo.setVisibility(View.GONE);

            mRecordVideo.setVisibility(View.VISIBLE);
           // mRecordVideo.setImageResource(R.drawable.ic_record);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private String parseVideo(String mFilePath) throws IOException {
        DataSource channel = new FileDataSourceImpl(mFilePath);
        IsoFile isoFile = new IsoFile(channel);
        List<TrackBox> trackBoxes = isoFile.getMovieBox().getBoxes(TrackBox.class);
        boolean isError = false;
        for (TrackBox trackBox : trackBoxes) {
            TimeToSampleBox.Entry firstEntry = trackBox.getMediaBox().getMediaInformationBox().getSampleTableBox().getTimeToSampleBox().getEntries().get(0);
            // Detect if first sample is a problem and fix it in isoFile
            // This is a hack. The audio deltas are 1024 for my files, and video deltas about 3000
            // 10000 seems sufficient since for 30 fps the normal delta is about 3000
            if (firstEntry.getDelta() > 10000) {
                isError = true;
                firstEntry.setDelta(3000);
            }
        }
        File file = getOutputMediaFile();
        String filePath = file.getAbsolutePath();
        if (isError) {
            Movie movie = new Movie();
            for (TrackBox trackBox : trackBoxes) {
                movie.addTrack(new Mp4TrackImpl(channel.toString() + "[" + trackBox.getTrackHeaderBox().getTrackId() + "]", trackBox));
            }
            movie.setMatrix(isoFile.getMovieBox().getMovieHeaderBox().getMatrix());
            Container out = new DefaultMp4Builder().build(movie);

            //delete file first!
            FileChannel fc = new RandomAccessFile(filePath, "rw").getChannel();
            out.writeContainer(fc);
            fc.close();
            Log.d(TAG, "Finished correcting raw video");
            return filePath;
        }
        return mFilePath;
    }

    /**
     * Create directory and return file
     * returning video file
     */
    private File getOutputMediaFile() {
        // External sdcard file location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),
                VIDEO_DIRECTORY_NAME);
        // Create storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + VIDEO_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "VID_" + timeStamp + ".mp4");
        return mediaFile;
    }

    // This is called when the X duration seconds were recorded
    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
            Log.v("VIDEOCAPTURE","Maximum Duration Reached");
           // mr.stop();

            try {
                stopRecordingVideo();
                // prepareViews(); // This starts the video playing views
                progressBar.setVisibility(View.INVISIBLE);

                moveToNextScreen(btnView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}