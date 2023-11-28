import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;


/**
 * The UniversityCourseManagementSystem class represents a system for managing university courses.
 */
public class UniversityCourseManagementSystem {

    /**
     * Checks if a given character is a digit.
     *
     * @param c the character to check
     * @return true if the character is a digit, false otherwise
     */
    static boolean isDigit(char c) {
        String digits = "0123456789";
        for (int i = 0; i < digits.length(); i++) {
            if (c == digits.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a given character is an alphabetic character.
     *
     * @param c the character to check
     * @return true if the character is an alphabetic character, false otherwise
     */
    static boolean isAlphabetic(char c) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < alphabet.length(); i++) {
            if (c == alphabet.charAt(i)) {
                return true;
            }
        }
        return false;
    }
    public static int getNumOfMembers() {
        return humans.size();
    }
    /**
     * Validates if a given name consists only of alphabetic characters.
     *
     * @param name the name to validate
     * @return true if the name is valid, false otherwise
     */
    static boolean validateName(String name) {
        for (int i = 0; i < name.length(); i++) {
            if (!isAlphabetic(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates if a given course name consists only of alphabetic characters and underscores.
     *
     * @param name the course name to validate
     * @return true if the course name is valid, false otherwise
     */
    static boolean validateCourseName(String name) {
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!isAlphabetic(c) && c != '_') {
                return false;
            }
        }
        return true;
    }

    /**
     * Converts a string input to an integer if it contains only digits.
     *
     * @param s the Scanner input
     * @return the integer value, or -1 if the input is not a valid integer
     */
    static int inputInt(Scanner s) {
        if (s.hasNext()) {
            String buff = s.next();
            for (int i = 0; i < buff.length(); i++) {
                if (!isDigit(buff.charAt(i))) {
                    return -1;
                }
            }
            return Integer.parseInt(buff);
        }
        return -1;
    }

    /**
     * Converts a string input to lowercase if it exists; returns a default non-valid string otherwise.
     *
     * @param s the Scanner input
     * @return the lowercase string if it exists, or "******" as a non-valid string
     */
    static String inputString(Scanner s) {
        if (s.hasNext()) {
            return s.next().toLowerCase();
        }
        return "******"; // non-valid string in context of task
    }

    // List of valid commands
    /**
     * List of all possible commands.
     */
    private static final List<String> COMMANDS = Arrays.asList(
            "course",
            "student",
            "professor",
            "enroll",
            "drop",
            "teach",
            "exempt"
    );

    // Success messages for different operations
    /**
     * Success addition string.
     */
    private static final String ADD_SUCCESS = "Added successfully";
    /**
     * Success enrollment string.
     */
    private static final String ENROLL_SUCCESS = "Enrolled successfully";
    /**
     * Success drop string.
     */
    private static final String DROP_SUCCESS = "Dropped successfully";
    /**
     * Success exemption string.
     */
    private static final String EXEMPT_SUCCESS = "Professor is exempted";
    /**
     * Success teaching assignment string.
     */
    private static final String TEACH_SUCCESS = "Professor is successfully assigned to teach this course";


    // List of success messages
    /**
     * List of successful codes for commands.
     */
    private static final List<String> SUCCESS = Arrays.asList(
            ADD_SUCCESS, ENROLL_SUCCESS, DROP_SUCCESS, EXEMPT_SUCCESS, TEACH_SUCCESS
    );

    // Lists of humans, courses, and mapping of whether each member is a student or professor
    /**
     * List of humans in System.
     */
    private static List<UniversityMember> humans;
    /**
     * List of courses in System.
     */
    private static List<Course> courses;
    /**
     * Dict for status of humans in System.
     */
    private static Map<Integer, Boolean> isStudent;

    /**
     * The main method that executes the university course management system.
     * @param args Command-line arguments (not used in this case)
     */
    public static void main(String[] args) {
        humans = new ArrayList<>();
        courses = new ArrayList<>();
        isStudent = new HashMap<>();
        fillInitialData();
        Scanner s = new Scanner(System.in);
        while (s.hasNext()) {
            String output = execute(s.next().toLowerCase(), s);
            System.out.println(output);
            if (!SUCCESS.contains(output)) {
                break;
            }
        }
    }

    /**
     * Fills the initial data in the university system.
     */
    public static void fillInitialData() {
        humans.add(new Student("Alice"));
        humans.add(new Student("Bob"));
        humans.add(new Student("Alex"));
        humans.add(new Professor("Ali"));
        humans.add(new Professor("Ahmed"));
        humans.add(new Professor("Andrey"));

        courses.add(new Course("java_beginner", CourseLevel.BACHELOR));
        courses.add(new Course("java_intermediate", CourseLevel.BACHELOR));
        courses.add(new Course("python_basics", CourseLevel.BACHELOR));
        courses.add(new Course("algorithms", CourseLevel.MASTER));
        courses.add(new Course("advanced_programming", CourseLevel.MASTER));
        courses.add(new Course("mathematical_analysis", CourseLevel.MASTER));
        courses.add(new Course("computer_vision", CourseLevel.MASTER));
        final int aliceId = 0;
        final int bobId = 1;
        final int alexId = 2;
        final int aliId = 3;
        final int ahmedId = 4;
        final int andreyId = 5;
        final int javaBeginnerId = 0;
        final int javaIntermediateId = 1;
        final int pythonBasicsId = 2;
        final int algorithmsId = 3;
        final int advancedProgrammingId = 4;
        final int mathematicalAnalysisId = 5;

        ((Student) humans.get(aliceId)).enroll(courses.get(javaBeginnerId));
        ((Student) humans.get(aliceId)).enroll(courses.get(javaIntermediateId));
        ((Student) humans.get(aliceId)).enroll(courses.get(pythonBasicsId));

        ((Student) humans.get(bobId)).enroll(courses.get(javaBeginnerId));
        ((Student) humans.get(bobId)).enroll(courses.get(algorithmsId));

        ((Student) humans.get(alexId)).enroll(courses.get(advancedProgrammingId));

        ((Professor) humans.get(aliId)).teach(courses.get(javaBeginnerId));
        ((Professor) humans.get(aliId)).teach(courses.get(javaIntermediateId));

        ((Professor) humans.get(ahmedId)).teach(courses.get(pythonBasicsId));
        ((Professor) humans.get(ahmedId)).teach(courses.get(advancedProgrammingId));

        ((Professor) humans.get(andreyId)).teach(courses.get(mathematicalAnalysisId));

        isStudent.put(aliceId, true);
        isStudent.put(bobId, true);
        isStudent.put(alexId, true);
        isStudent.put(aliId, false);
        isStudent.put(ahmedId, false);
        isStudent.put(andreyId, false);
    }
    /**
     * Executes a command based on user input.
     *
     * @param command the command to execute
     * @param s       the Scanner for user input
     * @return the result of the command execution
     */
    static String execute(String command, Scanner s) {
        if (Objects.equals(command, "course")) {
            return course(s);
        }
        if (Objects.equals(command, "student")) {
            return student(s);
        }
        if (Objects.equals(command, "professor")) {
            return professor(s);
        }
        if (Objects.equals(command, "enroll")) {
            return enroll(s);
        }
        if (Objects.equals(command, "drop")) {
            return drop(s);
        }
        if (Objects.equals(command, "exempt")) {
            return exempt(s);
        }
        if (Objects.equals(command, "teach")) {
            return teach(s);
        }
        return "Wrong inputs";
    }

    /**
     * Retrieves a university member based on member ID.
     *
     * @param memberId the ID of the university member
     * @return the university member
     */
    static UniversityMember member(int memberId) {
        return humans.get(memberId);
    }

    /**
     * Retrieves a course based on course ID.
     *
     * @param courseId the ID of the course
     * @return the course
     */
    static Course course(int courseId) {
        return courses.get(courseId);
    }

    /**
     * Handles the 'course' command, adding a new course to the system.
     *
     * @param s the Scanner for user input
     * @return the result of the command execution
     */
    static String course(Scanner s) {
        String courseName = inputString(s);
        if (!validateCourseName(courseName) || COMMANDS.contains(courseName)) {
            return "Wrong inputs";
        }

        for (Course c : courses) {
            if (Objects.equals(c.getCourseName(), courseName)) {
                return "Course exists";
            }
        }

        String courseLevel = inputString(s);
        if (!(Objects.equals(courseLevel, "bachelor") || Objects.equals(courseLevel, "master"))) {
            return "Wrong inputs";
        }

        CourseLevel cl = Objects.equals(courseLevel, "bachelor") ? CourseLevel.BACHELOR : CourseLevel.MASTER;
        courses.add(new Course(courseName, cl));
        return ADD_SUCCESS;
    }

    /**
     * Handles the 'student' command, adding a new student to the system.
     *
     * @param s the Scanner for user input
     * @return the result of the command execution
     */
    static String student(Scanner s) {
        String name = inputString(s);
        if (!validateName(name) || COMMANDS.contains(name)) {
            return "Wrong inputs";
        }

        humans.add(
                new Student(name)
        );
        isStudent.put(humans.size() - 1, true);
        return ADD_SUCCESS;
    }

    /**
     * Handles the 'professor' command, adding a new professor to the system.
     *
     * @param s the Scanner for user input
     * @return the result of the command execution
     */
    static String professor(Scanner s) {
        String name = inputString(s);
        if (!validateName(name) || COMMANDS.contains(name)) {
            return "Wrong inputs";
        }

        humans.add(
                new Professor(name)
        );
        isStudent.put(humans.size() - 1, false);
        return ADD_SUCCESS;
    }

    /**
     * Handles the 'enroll' command, allowing a student to enroll in a course.
     *
     * @param s the Scanner for user input
     * @return the result of the command execution
     */
    static String enroll(Scanner s) {
        int memberId = inputInt(s) - 1;
        if (memberId > humans.size() - 1 || memberId < 0) {
            return "Wrong inputs";
        }
        int courseId = inputInt(s) - 1;
        if (courseId > courses.size() - 1 || courseId < 0) {
            return "Wrong inputs";
        }

        UniversityMember person = member(memberId);
        Course course = course(courseId);

        if (!isStudent.get(memberId)) {
            return "Wrong inputs";
        }
        if (course.isFull()) {
            return "Course if full";
        }
        if (course.enrolled((Student) person)) {
            return "Student is already enrolled in this course";
        }
        if (((Student) person).enrolledCourses() == ((Student) person).getMaxEnrollment()) {
            return "Maximum enrollment is reached for the student";
        }

        ((Student) person).enroll(course);
        return ENROLL_SUCCESS;
    }

    /**
     * Handles the 'drop' command, allowing a student to drop a course.
     *
     * @param s the Scanner for user input
     * @return the result of the command execution
     */
    static String drop(Scanner s) {
        int memberId = inputInt(s) - 1;
        if (memberId > humans.size() - 1 || memberId < 0) {
            return "Wrong inputs";
        }
        int courseId = inputInt(s) - 1;
        if (courseId > courses.size() - 1 || courseId < 0) {
            return "Wrong inputs";
        }

        UniversityMember person = member(memberId);
        Course course = course(courseId);

        if (!isStudent.get(memberId)) {
            return "Wrong inputs";
        }
        if (!course.enrolled((Student) person)) {
            return "Student is not enrolled in this course";
        }

        ((Student) person).drop(course);
        return DROP_SUCCESS;
    }

    /**
     * Handles the 'exempt' command, allowing a professor to exempt a course.
     *
     * @param s the Scanner for user input
     * @return the result of the command execution
     */
    static String exempt(Scanner s) {
        int memberId = inputInt(s) - 1;
        if (memberId > humans.size() - 1 || memberId < 0) {
            return "Wrong inputs";
        }
        int courseId = inputInt(s) - 1;
        if (courseId > courses.size() - 1 || courseId < 0) {
            return "Wrong inputs";
        }

        UniversityMember person = member(memberId);
        Course course = course(courseId);

        if (isStudent.get(memberId)) {
            return "Wrong inputs";
        }
        if (!((Professor) person).exempt(course)) {
            return "Professor is not teaching this course";
        }

        return EXEMPT_SUCCESS;
    }

    /**
     * Handles the 'teach' command, allowing a professor to teach a course.
     *
     * @param s the Scanner for user input
     * @return the result of the command execution
     */
    static String teach(Scanner s) {
        int memberId = inputInt(s) - 1;
        if (memberId > humans.size() - 1 || memberId < 0) {
            return "Wrong inputs";
        }
        int courseId = inputInt(s) - 1;
        if (courseId > courses.size() - 1 || courseId < 0) {
            return "Wrong inputs";
        }

        UniversityMember person = member(memberId);
        Course course = course(courseId);

        if (isStudent.get(memberId)) {
            return "Wrong inputs";
        }
        if (((Professor) person).assignedCourses() == 2) {
            return "Professor's load is complete";
        }
        if (((Professor) person).isTeaching(course)) {
            return "Professor is already teaching this course";
        }

        ((Professor) person).teach(course);
        UniversityMember.getNumberOfMembers();

        return TEACH_SUCCESS;
    }
}

/**
 * The Enroll-able interface represents entities that can be enrolled in courses.
 */
interface Enrollable {
    boolean drop(Course course);
    boolean enroll(Course course);
}

/**
 * The UniversityMember class represents a member of the university.
 */

abstract class UniversityMember {
    /**
     * Contains total number of Members.
     */
    private static int numberOfMembers = 0;
    /**
     * Member id of UniversityMember.
     */
    private int memberId;
    /**
     * Name of UniversityMember.
     */
    private String memberName;

    public static int getNumberOfMembers() {
        return numberOfMembers;
    }

    /**
     * Constructor for the UniversityMember class.
     * @param memId The member's ID
     * @param memName The member's name
     */
    public UniversityMember(int memId, String memName) {
        numberOfMembers++;
        memberId = memId;
        memberName = memName.toLowerCase();
    }
}

/**
 * The CourseLevel enum represents the level of a university course.
 */
enum CourseLevel {
    /**
    * Represents bachelor level of courses.
     */
    BACHELOR,
    /**
     * Represents master level of courses.
     */
    MASTER
}

/**
 * The Course class represents a university course.
 */
class Course {
    /**
     * Maximum members in course.
     */
    private static final int CAPACITY = 3;
    /**
     * Total number of courses.
     */
    private static int numberOfCourses = 0;
    /**
     * ID of course.
     */
    private final int courseId;
    /**
     * Name of course.
     */
    private String courseName;
    /**
     * List of students enrolled to this course.
     */
    private List<Student> enrolledStudents;
    /**
     * Level of course.
     */
    private CourseLevel courseLevel;

    /**
     * Constructor for the Course class.
     * @param cName The course name
     * @param cLevel The course level (BACHELOR or MASTER)
     */
     public Course(String cName, CourseLevel cLevel) {
        courseId = ++numberOfCourses;
        courseLevel = cLevel;
        courseName = cName;
        enrolledStudents = new ArrayList<>();
    }

    public String getCourseName() {
        return courseName;
    }

    public void remove(Student student) {
        enrolledStudents.remove(student);
    }
    public boolean enrolled(Student student) {
        return enrolledStudents.contains(student);
    }
    public void add(Student student) {
        enrolledStudents.add(student);
    }
    /**
     * Checks if the course is full.
     * @return true if the course is full, false otherwise
     */
    public boolean isFull() {
        return CAPACITY == enrolledStudents.size();
    }
}

/**
 * The Professor class represents a professor at the university.
 */
class Professor extends UniversityMember {
    /**
     * Maximum courses to teach.
     */
    private static final int MAX_LOAD = 2;
    /**
     * List of assigned courses.
     */
    private List<Course> assignedCourses;

    /**
     * Constructor for the Professor class.
     * @param memName The professor's name
     */
     public Professor(String memName) {
        super(UniversityCourseManagementSystem.getNumOfMembers() + 1, memName);
        assignedCourses = new ArrayList<>();
    }

    /**
     * Assigns a course to the professor for teaching.
     * @param course The course to teach
     * @return true if the assignment is successful, false otherwise
     */
    public boolean teach(Course course) {
        if (MAX_LOAD != assignedCourses.size() && !assignedCourses.contains(course)) {
            assignedCourses.add(course);
            return true;
        }
        return false;
    }

    public boolean isTeaching(Course course) {
        return assignedCourses.contains(course);
    }
    public int assignedCourses() {
        return assignedCourses.size();
    }

    /**
     * Exempts the professor from teaching a course.
     * @param course The course to exempt
     * @return true if the exemption is successful, false otherwise
     */
    public boolean exempt(Course course) {
        boolean exempted = false;
        if (assignedCourses.contains(course)) {
            assignedCourses.remove(course);
            exempted = true;
        }
        return exempted;
    }
}

/**
 * The Student class represents a student at the university.
 */
class Student extends UniversityMember implements Enrollable {
    /**
     * Maximum courses to enroll.
     */
    private static final int MAX_ENROLLMENT = 3;
    /**
     * List of enrolled courses.
     */
    private List<Course> enrolledCourses;

    /**
     * Constructor for the Student class.
     * @param memName The student's name
     */
    public Student(String memName) {
        super(UniversityCourseManagementSystem.getNumOfMembers() + 1, memName);
        enrolledCourses = new ArrayList<>();
    }

    @Override
    public boolean drop(Course course) {
        boolean dropped = false;
        if (enrolledCourses.contains(course)) {
            enrolledCourses.remove(course);
            course.remove(this);
            dropped = true;
        }
        return dropped;
    }

    @Override
    public boolean enroll(Course course) {
        boolean enrolled = false;
        if (enrolledCourses.size() != MAX_ENROLLMENT
                && !course.enrolled(this) && !this.enrolledCourses.contains(course)) {
            course.add(this);
            enrolledCourses.add(course);
            enrolled = true;
        }
        return enrolled;
    }
    public int enrolledCourses() {
        return enrolledCourses.size();
    }
    public int getMaxEnrollment() {
        return MAX_ENROLLMENT;
    }
}
