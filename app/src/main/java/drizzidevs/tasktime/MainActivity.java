package drizzidevs.tasktime;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements CursorRecyclerViewAdapter.OnTaskClickListener,
                                                                       AddEditActivityFragment.OnSaveClicked,
                                                                       AppDialog.DialogEvents {

    private static final String TAG = "MainActivity";

    private boolean mTwoPane = false;

   
    public static final int DIALOG_ID_DELETE = 1;
    public static final int DIALOG_ID_CANCEL_EDIT = 2;

    private AlertDialog mDialog = null;     // module scope because we need to dismiss it in onStop
                                            // e.g. when orientation changes to avoid memory leaks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.task_details_container) != null) {
            mTwoPane = true;
        }
    }

    @Override
    public void onSaveClicked() {
        Log.d(TAG, "onSaveClicked: starts");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.task_details_container);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.menumain_addTask:
                taskEditRequest(null);
                break;
            case R.id.menumain_showDurations:
                break;
            case R.id.menumain_showAbout:
                showAboutDialog();
                break;
            case R.id.menumain_generate:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    public void showAboutDialog() {
        @SuppressLint("InflateParams") View messageView = getLayoutInflater().inflate(R.layout.about, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);

        builder.setView(messageView);

        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(true);
//        builder.setTitle(R.string.app_name);
//        builder.setIcon(R.mipmap.ic_launcher);

        messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Entering messageView.onClick, showing = " + mDialog.isShowing());
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });

        TextView tv = messageView.findViewById(R.id.about_version);
        tv.setText("v" + BuildConfig.VERSION_NAME);

        mDialog.show();
    }

    @Override
    public void onEditClick(Task task) {
        taskEditRequest(task);
    }

    @Override
    public void onDeleteClick(Task task) {
        Log.d(TAG, "onDeleteClick: starts");

        AppDialog dialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, DIALOG_ID_DELETE);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.deldiag_message, task.getId(), task.getName()));
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, (R.string.deldiag_positive_caption));

        args.putLong("TaskId", task.getId());


        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), null);
    }

    private void taskEditRequest(Task task) {
        Log.d(TAG, "taskEditRequest: starts");
        if (mTwoPane) {
            Log.d(TAG, "taskEditRequest: in two-pane mode (tablet)");

            AddEditActivityFragment fragment = new AddEditActivityFragment();

            Bundle arguments = new Bundle();
            arguments.putSerializable(Task.class.getSimpleName(),task);
            fragment.setArguments(arguments);

//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.task_details_container, fragment);
//            fragmentTransaction.commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.task_details_container, fragment)
                    .commit();


        } else {
            Log.d(TAG, "taskEditRequest: in single-pane mode (phone)");
            // in single pane mode, start the detail activity for the selected item Id;
            Intent detailIntent = new Intent(this, AddEditActivity.class);
            if (task != null) {
                detailIntent.putExtra(Task.class.getSimpleName(), task);
                startActivity(detailIntent);

            } else {
                startActivity(detailIntent);
            }

        }
    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onPositiveDialogResult: called");
        switch (dialogId) {
            case DIALOG_ID_DELETE:
                Long taskId = args.getLong("TaskId");
                if (BuildConfig.DEBUG && taskId == 0) throw new AssertionError("Task ID is zero");
                getContentResolver().delete(TasksContract.buildTaskUri(taskId), null, null);
                break;
            case DIALOG_ID_CANCEL_EDIT:
                break;

        }


    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onNegativeDialogResult: called");
        switch (dialogId) {
            case DIALOG_ID_DELETE:
                break;
            case DIALOG_ID_CANCEL_EDIT:
                finish();
                break;
        }

    }

    @Override
    public void onDialogCancelled(int dialogId) {
        Log.d(TAG, "onDialogCancelled: called");

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: called");
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditActivityFragment fragment = (AddEditActivityFragment) fragmentManager.findFragmentById(R.id.task_details_container);
        if ((fragment == null) || fragment.canClose()) {
            super.onBackPressed();
        } else {
            // Blah blah confirm to quit text;
            AppDialog dialog = new AppDialog();
            Bundle args = new Bundle();
            args.putInt(AppDialog.DIALOG_ID, DIALOG_ID_CANCEL_EDIT);
            args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.cancelEditDiag_message));
            args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.cancelEditDiag_positive_caption);
            args.putInt(AppDialog.DIALOG_NEGATIVE_RID, R.string.cancelEditDiag_negative_caption);

            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), null);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();

        }
    }
}






























