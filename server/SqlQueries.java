package server;

/**
 * An interface containing all the SQL queries used in the project.
 * @author Martin
 * @version 1.0
 * @since April 11, 2018
 */
interface SqlQueries {
	/**
	 * Gets the user information for a pair of login credentials, if it exists.
	 */
	static final String LOGIN = "SELECT TYPE, FIRSTNAME, LASTNAME, EMAIL, EMAIL_PASSWORD FROM USER WHERE ID = ? AND PASSWORD = ?";
	/**
	 * Gets the courses taught by a professor.
	 */
	static final String GET_PROF_COURSE = "SELECT ID, NAME, ACTIVE FROM COURSE WHERE PROF_ID = ? ORDER BY ID";
	/**
	 * Gets the active courses a student is enrolled in.
	 */
	static final String GET_STUDENT_COURSE = "SELECT C.ID, C.NAME, U.LASTNAME FROM COURSE C JOIN STUDENT_ENROLLMENT SE ON SE.COURSE_ID = C.ID JOIN USER U ON U.ID = C.PROF_ID WHERE SE.STUDENT_ID = ? AND C.ACTIVE = 0b1 ORDER BY C.ID";
	/**
	 * Updates the active state of a course.
	 */
	static final String UPDATE_COURSE = "UPDATE COURSE SET ACTIVE = ? WHERE ID = ?";
	/**
	 * Creates a new inactive course.
	 */
	static final String CREATE_COURSE = "INSERT INTO COURSE (PROF_ID, NAME, ACTIVE) VALUES (?, ?, ?)";
	/**
	 * Gets a professor's assignments for a particular course.
	 */
	static final String GET_PROF_ASSIGNMENT = "SELECT ID, TITLE, ACTIVE, DUE_DATE FROM ASSIGNMENT WHERE COURSE_ID = ? ORDER BY DUE_DATE";
	/**
	 * Gets the active assignment for a course for a student user.
	 */
	static final String GET_STUDENT_ASSIGNMENT = "SELECT A.ID, A.TITLE, CASE WHEN S.STUDENT_ID IS NULL THEN 0 ELSE 1 END SUBMITTED, A.DUE_DATE, A.EXTENSION, S.GRADE FROM ASSIGNMENT A LEFT JOIN SUBMISSION S ON S.ASSIGN_ID = A.ID AND S.STUDENT_ID = ? WHERE A.COURSE_ID = ? AND A.ACTIVE = 0b1 ORDER BY A.DUE_DATE";
	/**
	 * Changes the active state of an assignment.
	 */
	static final String UPDATE_ASSIGNMENT = "UPDATE ASSIGNMENT SET ACTIVE = ? WHERE ID = ?";
	/**
	 * Creates a new inactive assignment. 
	 */
	static final String CREATE_ASSIGNMENT = "INSERT INTO ASSIGNMENT (COURSE_ID, TITLE, EXTENSION, ACTIVE, DUE_DATE) VALUES (?, ?, ?, ?, ?)";
	/**
	 * Gets all students with a particular name.
	 */
	static final String GET_ALL_NAME_STUDENT = "SELECT U.ID, U.FIRSTNAME, U.LASTNAME, CASE WHEN SE.STUDENT_ID IS NULL THEN 0 ELSE 1 END ENROLLED FROM USER U LEFT JOIN STUDENT_ENROLLMENT SE ON U.ID = SE.STUDENT_ID AND SE.COURSE_ID = ? WHERE U.TYPE = 'S' AND U.LASTNAME = ? ORDER BY U.ID";
	/**
	 * Gets all the students with a particular id.
	 */
	static final String GET_ALL_ID_STUDENT = "SELECT U.ID, U.FIRSTNAME, U.LASTNAME, CASE WHEN SE.STUDENT_ID IS NULL THEN 0 ELSE 1 END ENROLLED FROM USER U LEFT JOIN STUDENT_ENROLLMENT SE ON U.ID = SE.STUDENT_ID AND SE.COURSE_ID = ? WHERE U.TYPE = 'S' AND U.ID = ? ORDER BY U.ID";
	/**
	 * Gets all the students.
	 */
	static final String GET_ALL_STUDENT = "SELECT U.ID, U.FIRSTNAME, U.LASTNAME, CASE WHEN SE.STUDENT_ID IS NULL THEN 0 ELSE 1 END ENROLLED FROM USER U LEFT JOIN STUDENT_ENROLLMENT SE ON U.ID = SE.STUDENT_ID AND SE.COURSE_ID = ? WHERE U.TYPE = 'S' ORDER BY U.ID";
	/**
	 * Gets all the enrolled students with a particular name.
	 */
	static final String GET_ENROLLED_NAME_STUDENT = "SELECT U.ID, U.FIRSTNAME, U.LASTNAME FROM USER U JOIN STUDENT_ENROLLMENT E ON U.ID = E.STUDENT_ID WHERE E.COURSE_ID = ? AND U.LASTNAME = ? ORDER BY U.ID";
	/**
	 * Gets all the enrolled students with a particular id.
	 */
	static final String GET_ENROLLED_ID_STUDENT = "SELECT U.ID, U.FIRSTNAME, U.LASTNAME FROM USER U JOIN STUDENT_ENROLLMENT E ON U.ID = E.STUDENT_ID WHERE E.COURSE_ID = ? AND U.ID = ? ORDER BY U.ID";
	/**
	 * Gets all the enrolled students.
	 */
	static final String GET_ENROLLED_STUDENT = "SELECT U.ID, U.FIRSTNAME, U.LASTNAME FROM USER U JOIN STUDENT_ENROLLMENT E ON U.ID = E.STUDENT_ID WHERE E.COURSE_ID = ? ORDER BY U.ID";
	/**
	 * Enrolls a student in a course.
	 */
	static final String CREATE_ENROLLEMENT = "INSERT INTO STUDENT_ENROLLMENT (STUDENT_ID, COURSE_ID) SELECT ?, ? FROM STUDENT_ENROLLMENT WHERE NOT EXISTS (SELECT * FROM STUDENT_ENROLLMENT WHERE STUDENT_ID = ? AND COURSE_ID = ?) LIMIT 1";
	/**
	 * Unenrolls a student from a course.
	 */
	static final String DELETE_ENROLLEMENT = "DELETE FROM STUDENT_ENROLLMENT WHERE STUDENT_ID = ? AND COURSE_ID = ?";
	/**
	 * Creates a new submission for an assignment.
	 */
	static final String CREATE_SUBMISSION = "INSERT INTO SUBMISSION (ASSIGN_ID, STUDENT_ID, EXTENSION, NAME, TIMESTAMP, GRADE) VALUES (?, ?, ?, ?, ?, ?)";
	/**
	 * Gets all student submissions for an assignment.
	 */
	static final String GET_SUBMISSION = "SELECT S.ID, S.NAME, S.EXTENSION, S.TIMESTAMP, U.FIRSTNAME, U.LASTNAME, S.GRADE FROM SUBMISSION S JOIN USER U ON U.ID = S.STUDENT_ID WHERE S.ASSIGN_ID = ?";
	/**
	 * Updates the grade for an assignment submission.
	 */
	static final String UPDATE_SUBMISSION = "UPDATE SUBMISSION SET GRADE = ? WHERE ID = ?";
	/**
	 * Gets the last id auto inserted into the table.
	 */
	static final String GET_LAST_ID = "SELECT LAST_INSERT_ID()";
	/**
	 * Gets the authentication information for a users email.
	 */
	static final String EMAIL_LOGIN = "SELECT EMAIL, EMAIL_PASSWORD FROM USER WHERE ID = ?";
	/**
	 * Gets the email addresses of all students in a particular course.
	 */
	static final String PROF_RECIPIENTS = "SELECT U.EMAIL FROM USER U JOIN STUDENT_ENROLLMENT SE ON U.ID = SE.STUDENT_ID WHERE SE.COURSE_ID = ?";
	/**
	 * Gets the email address of the students professor.
	 */
	static final String STUDENT_RECIPIENT = "SELECT U.EMAIL FROM USER U JOIN COURSE C ON C.PROF_ID = U.ID WHERE C.ID = ?";
	/**
	 * Gets a chat message with a particular id.
	 */
	static final String CHAT_ID_MESSAGE = "SELECT U.FIRSTNAME, U.LASTNAME, C.CONTENT FROM USER U JOIN CHAT C ON U.ID = C.USER_ID WHERE C.ID = ?";
	/**
	 * Gets all the chat messages for a course, in order.
	 */
	static final String CHAT_ALL_MESSAGES = "SELECT U.FIRSTNAME, U.LASTNAME, C.CONTENT FROM USER U JOIN CHAT C ON U.ID = C.USER_ID WHERE C.COURSE_ID = ? ORDER BY C.ID";
	/**
	 * Adds a new message to a courses chat.
	 */
	static final String SUBMIT_MESSAGE = "INSERT INTO CHAT (USER_ID, COURSE_ID, CONTENT) VALUES (?, ?, ?)";
}
