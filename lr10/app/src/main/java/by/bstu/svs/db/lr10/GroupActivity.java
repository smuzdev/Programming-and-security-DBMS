package by.bstu.svs.db.lr10;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import by.bstu.svs.db.lr10.database.GroupDatabaseManager;
import by.bstu.svs.db.lr10.database.StudentDatabaseManager;
import by.bstu.svs.db.lr10.database.exception.SQLiteDatabaseException;
import by.bstu.svs.db.lr10.model.Group;
import by.bstu.svs.db.lr10.model.Student;

public class GroupActivity extends AppCompatActivity {

    private TextView tv_course;
    private TextView tv_faculty;
    private TextView tv_name;
    private TextView tv_head;

    private StudentDatabaseManager studentDatabaseManager;
    private Group group;
    private ListView studentsListView;
    private ArrayAdapter<Student> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        group = (Group) getIntent().getSerializableExtra("group");
        studentDatabaseManager = new StudentDatabaseManager(this);
        showGroup(group);
    }

    private void showGroup(Group group) {

        tv_course = findViewById(R.id.tv_course);
        tv_faculty = findViewById(R.id.tv_faculty);
        tv_name = findViewById(R.id.tv_name);
        tv_head = findViewById(R.id.tv_head);

        studentsListView = findViewById(R.id.list_view);

        tv_course.setText(group.getCourse().toString());
        tv_faculty.setText(group.getFaculty());
        tv_name.setText(group.getName());

        studentDatabaseManager.getById(group.getHeadId()).ifPresent(head -> tv_head.setText(head.getName()));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentDatabaseManager.getAllByGroupId(group.getGroupId()));
        studentsListView.setAdapter(adapter);

        studentsListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Student student = adapter.getItem(i);
            PopupMenu popupMenu = new PopupMenu(GroupActivity.this, view, Gravity.END);
            popupMenu.inflate(R.menu.task_popup_menu);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.edit_item:
                        create(student);
                        break;
                    case R.id.delete_item:
                        if (student.getStudentId().equals(group.getHeadId())) {
                            tv_head.setText("");
                        }
                        studentDatabaseManager.deleteById(student.getStudentId());
                        adapter.remove(student);
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

    public void create(Student studentToEdit) {

        LayoutInflater inflater = getLayoutInflater();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("ADD NEW STUDENT")
                .setView(inflater.inflate(R.layout.dialog_create_student, null))
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .show();

        Student student = new Student();

        EditText et_name = dialog.findViewById(R.id.et_name);

        if (studentToEdit != null) {
            et_name.setText(studentToEdit.getName());
        }

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(view1 -> {

            if (et_name.length() == 0) {
                et_name.setError("Required");
                return;
            }

            student.setName(et_name.getText().toString());
            student.setGroupId(group.getGroupId());

            try {

                if (studentToEdit == null) {
                    studentDatabaseManager.add(student);
                } else {
                    adapter.remove(studentToEdit);
                    student.setStudentId(studentToEdit.getStudentId());
                    studentDatabaseManager.update(student);
                }

                student.setStudentId(studentDatabaseManager.getStudentId(student));

                adapter.add(student);
                Toast.makeText(GroupActivity.this, "success", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } catch (SQLiteDatabaseException e) {
                Toast.makeText(GroupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        });

        dialog.show();
    }
}