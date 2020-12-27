package by.bstu.svs.db.lr10;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import by.bstu.svs.db.lr10.database.GroupDatabaseManager;
import by.bstu.svs.db.lr10.database.StudentDatabaseManager;
import by.bstu.svs.db.lr10.database.exception.SQLiteDatabaseException;
import by.bstu.svs.db.lr10.model.Group;
import by.bstu.svs.db.lr10.model.Student;

public class MainActivity extends AppCompatActivity {

    private GroupDatabaseManager databaseManager;
    private ListView groupsListView;
    private ArrayAdapter<Group> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseManager = new GroupDatabaseManager(this);

        groupsListView = findViewById(R.id.groups);
        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, databaseManager.getAll());
        groupsListView.setAdapter(adapter);
        groupsListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Group group = adapter.getItem(i);
            Intent showGroupIntent = new Intent(MainActivity.this, GroupActivity.class);
            showGroupIntent.putExtra("group", group);
            startActivity(showGroupIntent);
        });
        groupsListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Group group = adapter.getItem(i);
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, view, Gravity.END);
            popupMenu.inflate(R.menu.task_popup_menu);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.edit_item:
                        create(group);
                        break;
                    case R.id.delete_item:
                        databaseManager.deleteById(group.getGroupId());
                        adapter.remove(group);
                        break;
                }
                return true;
            });
            popupMenu.show();
            return true;
        });

    }

    public void onClickAdd(View view) {
        create(null);
    }

    public void create(Group groupToEdit) {

        LayoutInflater inflater = getLayoutInflater();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("CREATE NEW GROUP")
                .setView(inflater.inflate(R.layout.dialog_create_group, null))
                .setPositiveButton("Create", null)
                .setNegativeButton("Cancel", null)
                .show();

        Group group = new Group();

        EditText et_name = dialog.findViewById(R.id.et_name);
        EditText et_course = dialog.findViewById(R.id.et_course);
        EditText et_faculty = dialog.findViewById(R.id.et_faculty);
        Spinner sp_head = dialog.findViewById(R.id.sp_head);
        List<Student> heads = new ArrayList<>();
        int position = 0;

        if (groupToEdit != null) {
            et_name.setText(groupToEdit.getName());
            et_faculty.setText(groupToEdit.getFaculty());
            et_course.setText(groupToEdit.getCourse().toString());
            group.setGroupId(groupToEdit.getGroupId());
            heads = new StudentDatabaseManager(this).getAllByGroupId(group.getGroupId());
            position = heads.indexOf(heads.stream().filter(student -> student.getStudentId().equals(groupToEdit.getHeadId())).findFirst().orElse(new Student()));
        }


        ArrayAdapter<Student> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, heads);
        sp_head.setAdapter(spinnerAdapter);
        sp_head.setSelection(position);

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(view1 -> {

            List<EditText> required = new ArrayList<>();
            required.add(et_course);
            required.add(et_faculty);
            required.add(et_name);

            for (EditText editText: required) {
                if (editText.length() == 0) editText.setError("Required");
            }

            if (required.stream().anyMatch(editText -> editText.length() == 0)) {
                return;
            }


            group.setName(et_name.getText().toString());
            group.setCourse(Integer.valueOf(et_course.getText().toString()));
            group.setFaculty(et_faculty.getText().toString());
            group.setHeadId(sp_head.getSelectedItem() == null ? null : ((Student)sp_head.getSelectedItem()).getStudentId());

            try {

                if (groupToEdit == null) {
                    databaseManager.add(group);
                    group.setGroupId(new GroupDatabaseManager(this).getGroupId(group));
                } else {
                    adapter.remove(groupToEdit);
                    databaseManager.update(group);
                }

                adapter.add(group);
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } catch (SQLiteDatabaseException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        });

        dialog.show();
    }
}