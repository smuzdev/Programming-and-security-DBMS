package by.bstu.svs.db.lr10.database;

public final class DatabaseContract {

    public static final int     DATABASE_VERSION    = 7;
    public static final String  DATABASE_NAME       = "STUDENTS_DB";
    private static final String INT_TYPE            = " INTEGER";
    private static final String TEXT_TYPE           = " TEXT";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private DatabaseContract() {}

    public static abstract class StudentsTable {

        public static final String TABLE_NAME       = "students";

        public static final String COLUMN_ID        = "id";
        public static final String COLUMN_GROUP_ID  = "group_id";
        public static final String COLUMN_NAME      = "name";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME      + " (" +
                COLUMN_ID       + INT_TYPE  + " PRIMARY KEY AUTOINCREMENT," +
                COLUMN_GROUP_ID + INT_TYPE  + "," +
                COLUMN_NAME     + TEXT_TYPE + "," +
                "foreign key (" + COLUMN_GROUP_ID  + ") references " +
                GroupsTable.TABLE_NAME + "( " + GroupsTable.COLUMN_ID +  ") " +
                "ON UPDATE CASCADE ON DELETE CASCADE)";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    public static abstract class GroupsTable {

        public static final String TABLE_NAME       = "groups";

        public static final String COLUMN_ID        = "id";
        public static final String COLUMN_COURSE    = "course";
        public static final String COLUMN_FACULTY   = "faculty";
        public static final String COLUMN_NAME      = "name";
        public static final String COLUMN_HEAD_ID   = "head_id";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME      + " (" +
                COLUMN_ID       + INT_TYPE  + " PRIMARY KEY AUTOINCREMENT," +
                COLUMN_COURSE   + INT_TYPE  + "," +
                COLUMN_FACULTY  + TEXT_TYPE + "," +
                COLUMN_NAME     + TEXT_TYPE + "," +
                COLUMN_HEAD_ID  + INT_TYPE  + "," +
                "foreign key (" + COLUMN_HEAD_ID  + ") references " +
                StudentsTable.TABLE_NAME + "( " + StudentsTable.COLUMN_ID +  ") " +
                "ON UPDATE CASCADE ON DELETE SET NULL)";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}