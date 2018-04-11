package server;

interface SqlQueries {
	static final String LOGIN = "SELECT TYPE, FIRSTNAME, LASTNAME, EMAIL, EMAIL_PASSWORD FROM USER WHERE ID = ? AND PASSWORD = ?";
	static final String GET_PROF_COURSE = "SELECT ID, NAME, ACTIVE FROM COURSE WHERE PROF_ID = ? ORDER BY ID";
	static final String GET_STUDENT_COURSE = "SELECT C.ID, C.NAME, U.LASTNAME FROM COURSE C JOIN STUDENT_ENROLLMENT SE ON SE.COURSE_ID = C.ID JOIN USER U ON U.ID = C.PROF_ID WHERE SE.STUDENT_ID = ? AND C.ACTIVE = 0b1 ORDER BY C.ID";
	static final String UPDATE_COURSE = "UPDATE COURSE SET ACTIVE = ? WHERE ID = ?";
	static final String CREATE_COURSE = "INSERT INTO COURSE (PROF_ID, NAME, ACTIVE) VALUES (?, ?, ?)";
	static final String GET_PROF_ASSIGNMENT = "SELECT ID, TITLE, ACTIVE, DUE_DATE FROM ASSIGNMENT WHERE COURSE_ID = ? ORDER BY DUE_DATE";
	static final String GET_STUDENT_ASSIGNMENT = "SELECT A.ID, A.TITLE, CASE WHEN S.STUDENT_ID IS NULL THEN 0b0 ELSE 0b1 END SUBMITTED, A.DUE_DATE, A.EXTENSION, S.GRADE FROM ASSIGNMENT A LEFT JOIN SUBMISSION S ON S.ASSIGN_ID = A.ID AND S.STUDENT_ID = ? WHERE A.COURSE_ID = ? AND A.ACTIVE = 0b1 ORDER BY A.DUE_DATE";
	static final String UPDATE_ASSIGNMENT = "UPDATE ASSIGNMENT SET ACTIVE = ? WHERE ID = ?";
	static final String CREATE_ASSIGNMENT = "INSERT INTO ASSIGNMENT (COURSE_ID, TITLE, EXTENSION, ACTIVE, DUE_DATE) VALUES (?, ?, ?, ?, ?)";
	static final String GET_ALL_NAME_STUDENT = "SELECT U.ID, U.FIRSTNAME, U.LASTNAME, CASE WHEN SE.STUDENT_ID IS NULL THEN 0b0 ELSE 0b1 END ENROLLED FROM USER U LEFT JOIN STUDENT_ENROLLMENT SE ON U.ID = SE.STUDENT_ID AND SE.COURSE_ID = ? WHERE U.TYPE = 'S' AND U.LASTNAME = ? ORDER BY U.ID";
	static final String GET_ALL_ID_STUDENT = "SELECT U.ID, U.FIRSTNAME, U.LASTNAME, CASE WHEN SE.STUDENT_ID IS NULL THEN 0b0 ELSE 0b1 END ENROLLED FROM USER U LEFT JOIN STUDENT_ENROLLMENT SE ON U.ID = SE.STUDENT_ID AND SE.COURSE_ID = ? WHERE U.TYPE = 'S' AND U.ID = ? ORDER BY U.ID";
	static final String GET_ALL_STUDENT = "SELECT U.ID, U.FIRSTNAME, U.LASTNAME, CASE WHEN SE.STUDENT_ID IS NULL THEN 0b0 ELSE 0b1 END ENROLLED FROM USER U LEFT JOIN STUDENT_ENROLLMENT SE ON U.ID = SE.STUDENT_ID AND SE.COURSE_ID = ? WHERE U.TYPE = 'S' ORDER BY U.ID";
	static final String GET_ENROLLED_NAME_STUDENT = "SELECT U.ID, U.FIRSTNAME, U.LASTNAME FROM USER U JOIN STUDENT_ENROLLMENT E ON U.ID = E.STUDENT_ID WHERE E.COURSE_ID = ? AND U.LASTNAME = ? ORDER BY U.ID";
	static final String GET_ENROLLED_ID_STUDENT = "SELECT U.ID, U.FIRSTNAME, U.LASTNAME FROM USER U JOIN STUDENT_ENROLLMENT E ON U.ID = E.STUDENT_ID WHERE E.COURSE_ID = ? AND U.ID = ? ORDER BY U.ID";
	static final String GET_ENROLLED_STUDENT = "SELECT U.ID, U.FIRSTNAME, U.LASTNAME FROM USER U JOIN STUDENT_ENROLLMENT E ON U.ID = E.STUDENT_ID WHERE E.COURSE_ID = ? ORDER BY U.ID";
	static final String CREATE_ENROLLEMENT = "INSERT INTO STUDENT_ENROLLMENT (STUDENT_ID, COURSE_ID) SELECT ?, ? FROM STUDENT_ENROLLMENT WHERE NOT EXISTS (SELECT * FROM STUDENT_ENROLLMENT WHERE STUDENT_ID = ? AND COURSE_ID = ?) LIMIT 1";
	static final String DELETE_ENROLLEMENT = "DELETE FROM STUDENT_ENROLLMENT WHERE STUDENT_ID = ? AND COURSE_ID = ?";
	static final String CREATE_SUBMISSION = "INSERT INTO SUBMISSION (ASSIGN_ID, STUDENT_ID, EXTENSION, NAME, TIMESTAMP, GRADE) VALUES (?, ?, ?, ?, ?, ?)";
	static final String GET_SUBMISSION = "SELECT S.ID, S.NAME, S.EXTENSION, S.TIMESTAMP, U.FIRSTNAME, U.LASTNAME, S.GRADE FROM SUBMISSION S JOIN USER U ON U.ID = S.STUDENT_ID WHERE S.ASSIGN_ID = ?";
	static final String UPDATE_SUBMISSION = "UPDATE SUBMISSION SET GRADE = ? WHERE ID = ?";
	static final String GET_LAST_ID = "SELECT LAST_INSERT_ID()";
	static final String EMAIL_LOGIN = "SELECT EMAIL, EMAIL_PASSWORD FROM USER WHERE ID = ?";
	static final String PROF_RECIPIENTS = "SELECT U.EMAIL FROM USER U JOIN STUDENT_ENROLLMENT SE ON U.ID = SE.STUDENT_ID WHERE SE.COURSE_ID = ?";
	static final String STUDENT_RECIPIENT = "SELECT U.EMAIL FROM USER U JOIN COURSE C ON C.PROF_ID = U.ID WHERE C.ID = ?";
	static final String CHAT_ID_MESSAGE = "SELECT U.FIRSTNAME, U.LASTNAME, C.CONTENT FROM USER U JOIN CHAT C ON U.ID = C.USER_ID WHERE C.ID = ?";
	static final String CHAT_ALL_MESSAGES = "SELECT U.FIRSTNAME, U.LASTNAME, C.CONTENT FROM USER U JOIN CHAT C ON U.ID = C.USER_ID WHERE C.COURSE_ID = ? ORDER BY C.ID";
	static final String SUBMIT_MESSAGE = "INSERT INTO CHAT (USER_ID, COURSE_ID, CONTENT) VALUES (?, ?, ?)";
}
